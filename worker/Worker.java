
package worker;

import java.net.Socket;
import java.io.IOException;

import request.*;
import resource.*;
import response.*;
import accesscheck.*;

public class Worker implements Runnable {

  private Socket clientSocket;

  public Worker(Socket clientSocket) {
    this.clientSocket = clientSocket;
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
      Resource resource;
      resource = new Resource(httpRequest);

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
      // send Response

    }

    closeConnection();
  }

  private void closeConnection() throws IOException {
    clientSocket.close();
    printConnectionClosed();
  }

  private void printConnectionClosed() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "    Connection Closed    ", HR);
  }

}
