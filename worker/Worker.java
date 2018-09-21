
package worker;

import java.net.*;
import java.io.*;

import request.*;
import resource.*;
import response.*;
import accesscheck.*;

public class Worker implements Runnable {

  private ServerSocket serverSocket;
  private int port;

  public Worker(int port) {
    this.port = port;
  }

  public void run() {
    try {
      bindServerSocket();
      listenForClient();
    } catch(IOException e) {
        e.printStackTrace();
    }
  }

  private void bindServerSocket() throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Listening on Port: " + serverSocket.getLocalPort());
  }

  private void listenForClient() throws IOException {
    Socket clientSocket = null;

    while(true) {
      clientSocket = serverSocket.accept();
      printConnectionEstablished();

      Request httpRequest = new Request(clientSocket);

      // TODO send response

      clientSocket.close();
      printConnectionClosed();
    }
  }

  private void printConnectionEstablished() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "Connection Established...", HR);
  }

  private void printConnectionClosed() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "    Connection Closed    ", HR);
  }

}
