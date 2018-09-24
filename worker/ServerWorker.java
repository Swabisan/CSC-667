
package worker;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWorker implements Runnable {

  private ExecutorService threadPool;
  private int threadCount;
  private Thread runningThread;
  private int connectionCount = 0;
  private ServerSocket serverSocket;
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
    Socket clientSocket = null;
    setRunningThread();

    while(true) {
      clientSocket = serverSocket.accept();
      printConnectionEstablished();
      threadPool.execute(new Worker(clientSocket));
    }
  }

  private synchronized void setRunningThread() {
    runningThread = Thread.currentThread();
  }

  private void printConnectionEstablished() {
    final String HR = "-----------------";
    System.out.printf("%17s%20s%5s%17s\n", HR, "Creating Connection", "(" + connectionCount + ")", HR);
    connectionCount++;
  }

}
