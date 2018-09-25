
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
    Request httpRequest = new Request(clientSocket);

    if (httpRequest.isPopulated()) {
      Resource resource = new Resource(httpRequest);
      logger.log(httpRequest, resource);
      if (resource.isProtected()) {
        String authToken = httpRequest.getHeader("Authorization");

        if (authToken != "KEY_NOT_FOUND") {
          AccessCheck accessCheck = new AccessCheck();

          if (!accessCheck.isAuthorized(authToken)) {
            // 403
          }
        } else {
          // 401
        }
      }
      //This will change to responsefactory
      ResponseFactory responseFactory = new ResponseFactory();
      Response response = responseFactory.getResponse(resource);
      
      response.send(clientSocket.getOutputStream());
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
