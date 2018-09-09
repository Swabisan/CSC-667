import java.net.*;
import java.io.*;

import Request.*;
// import responseFactory.*;

class WebServer {

  static ServerSocket serverSocket;
  // Dictionary accessFiles;
  // MimeTypes mimeTypes;
  // HttpdConf configuration;

  // TODO get port from httpd.conf
  static final private int DEFAULT_PORT = 8096;

  public static void main(String[] args) throws IOException {
    start();
  }

  protected static void start() throws IOException {
    bindServerSocket(DEFAULT_PORT);
    listenForClient();
  }

  protected static void bindServerSocket(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Listening on Port: " + serverSocket.getLocalPort());
  }

  protected static void listenForClient() throws IOException {
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

  private static void printConnectionEstablished() {
    System.out.println("-----------------Connection Established...-----------------");
  }

  private static void printConnectionClosed() {
    System.out.println("-----------------    Connection Closed    -----------------");
  }
}
