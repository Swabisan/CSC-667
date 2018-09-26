
package src.worker;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWorker implements Runnable {

  private ServerSocket serverSocket;
  private Socket clientSocket;
  private ExecutorService threadPool;
  private Thread runningThread;
  private int connectionCount;
  private int threadCount;
  private int port;

  public ServerWorker(int port, int threadCount) {
    this.port = port;
    this.threadCount = threadCount;
  }

  public void run() {
    threadPool = Executors.newFixedThreadPool(threadCount);

    try {
      bindServerSocket();
      listenForClient();

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      threadPool.shutdown();
    }
  }

  private void bindServerSocket() throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Listening on Port: " + serverSocket.getLocalPort());
  }

  private void listenForClient() throws IOException {
    connectionCount = 0;
    setRunningThread();

    while(true) {
      clientSocket = serverSocket.accept();
      // printConnectionEstablished(clientSocket.getInetAddress().toString());

      threadPool.execute(new Worker(clientSocket));
    }
  }

  private synchronized void setRunningThread() {
    runningThread = Thread.currentThread();
  }

  private void printConnectionEstablished(String inetAddress) {
    final String HR = "-----------------";
    System.out.printf("%17s%20s%5s%17s\n", HR, "Creating Connection", "(" + connectionCount + ")", HR);
    System.out.println("(" + connectionCount + ") Request received from " + inetAddress);
    connectionCount++;
  }

}
