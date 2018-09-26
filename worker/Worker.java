
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

  private ResponseFactory responseFactory = new ResponseFactory();
  private Request request;
  private Resource resource;
  private Response response;
  private String username;

  public Worker(Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.logger = new Logger();
  }

  public void run() {
    clearFields();

    try {
      talkToClient();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void talkToClient() throws IOException {
    request = new Request(clientSocket);
    resource = new Resource(request);

    if (request.isBadRequest()) {
      response = new BadRequestResponse(resource);
      this.sendResponse(response);
      return;
    }

    if (request.isPopulated()) {

      if (resource.isProtected()) {
        String authInfo = request.getHeader("Authorization");

        if (authInfo != "KEY_NOT_FOUND") {
          String accessPath = resource.getHtaccessPath();
          AccessCheck accessCheck = new AccessCheck(accessPath);

          username = accessCheck.getUsername(authInfo);

          if (!accessCheck.isAuthorized(authInfo)) {
            response = new ForbiddenResponse(resource);
            this.sendResponse(response);
            return;
          }

        } else {
          response = new UnauthorizedResponse(resource);
          this.sendResponse(response);
          return;
        }
      }

      response = responseFactory.getResponse(resource);
      this.sendResponse(response);
    }
  }

  private void clearFields() {
    this.request  = null;
    this.resource = null;
    this.response = null;
    this.username = "UNKNOWN_USER";
  }

  private void sendResponse(Response response) throws IOException {
    response.send(clientSocket.getOutputStream());
    logger.log(request, response, username);
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
