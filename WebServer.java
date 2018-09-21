import java.net.*;
import java.io.*;

import configuration.*;
import request.*;
import resource.*;
import response.*;
import accesscheck.*;
// import worker.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WebServer {
  // Dictionary accessFiles;
  private static ConfigurationReader configReader = new ConfigurationReader();
  private static Config HTTPD_CONF, MIME_TYPE;

  private static ExecutorService pool;
  private static final int THREAD_COUNT = 4;

  private static ServerSocket serverSocket;
  private static int port;

  public static void main(String[] args) throws IOException {
    loadConfiguration();
    start();
  }

  private static void start() throws IOException {
    bindServerSocket();
    listenForClient();



    // pool = Executors.newFixedThreadPool(THREAD_COUNT);
    //
    // for (int i = 0; i < THREAD_COUNT; i++) {
    //   Runnable worker = new Worker(port);
    //   pool.execute(worker);
    // }
    // stop();
  }

  private static void stop() {
    pool.shutdown();
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
