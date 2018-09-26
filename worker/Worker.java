
package worker;

import java.net.Socket;
import java.io.IOException;

import request.*;
import resource.*;
import response.*;
import accesscheck.*;
import logger.*;

public class Worker implements Runnable {

  private Socket clientSocket;
  private Logger logger;

  public Worker(Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.logger = new Logger();
  }

  public void run() {
    try {
      talkToClient();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void talkToClient() throws IOException {
    ResponseFactory responseFactory = new ResponseFactory();
    Request httpRequest = new Request(clientSocket);

    if (httpRequest.isBadRequest()) {
      Resource resource = new Resource(httpRequest);
      Response response = responseFactory.getResponse(resource);

      response.send(clientSocket.getOutputStream());
    }

    if (httpRequest.isPopulated()) {
      Resource resource = new Resource(httpRequest);
      String username = "PUBLIC_USER";

      if (resource.isProtected()) {
        String authToken = httpRequest.getHeader("Authorization");

        if (authToken != "KEY_NOT_FOUND") {
          AccessCheck accessCheck = new AccessCheck();
          username = accessCheck.getUsername(authToken);

          if (!accessCheck.isAuthorized(authToken)) {
            // 403
          }

        } else {
          // 401
        }
      }

      Response response = responseFactory.getResponse(resource);
      response.send(clientSocket.getOutputStream());

      logger.log(httpRequest, response, username);
    }

    closeConnection();
  }

  private void closeConnection() throws IOException {
    clientSocket.close();
    // printConnectionClosed();
  }

  private void printConnectionClosed() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "    Connection Closed    ", HR);
  }

}
