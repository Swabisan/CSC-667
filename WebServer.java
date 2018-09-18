import java.net.*;
import java.io.*;

import configuration.*;
import request.*;
import resource.*;
import response.*;
import accesscheck.*;

class WebServer {

  private static ServerSocket serverSocket;
  private static Config HTTPD_CONF, MIME_TYPE;
  // Dictionary accessFiles;
  private static int port;

  private static ConfigurationReader configReader = new ConfigurationReader();

  public static void main(String[] args) throws IOException {
    loadConfiguration();
    start();
  }

  private static void start() throws IOException {
    bindServerSocket();
    listenForClient();
  }

  private static void loadConfiguration() {
    HTTPD_CONF = configReader.getConfig("HTTPD_CONF");
    MIME_TYPE = configReader.getConfig("MIME_TYPE");

    port = Integer.parseInt(HTTPD_CONF.lookUp("Listen", "HTTPD_CONF"));
  }

  private static void bindServerSocket() throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Listening on Port: " + serverSocket.getLocalPort());
  }

  private static void listenForClient() throws IOException {
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
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "Connection Established...", HR);
  }

  private static void printConnectionClosed() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "    Connection Closed    ", HR);
  }
}
