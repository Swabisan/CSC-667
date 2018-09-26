import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.configuration.*;
import src.worker.*;

public class WebServer {
  // Dictionary accessFiles;
  private static ConfigurationReader configReader = new ConfigurationReader();
  private static Config HTTPD_CONF, MIME_TYPE;
  private static final int THREAD_COUNT = 4;
  private static int port;

  public static void main(String[] args) throws IOException {
    loadConfiguration();
    startServer();
  }

  private static void startServer() throws IOException {
    ServerWorker server = new ServerWorker(port, THREAD_COUNT);
    new Thread(server).start();
  }

  private static void loadConfiguration() {
    HTTPD_CONF = configReader.getConfig("HTTPD_CONF");
    MIME_TYPE = configReader.getConfig("MIME_TYPE");

    port = Integer.parseInt(HTTPD_CONF.lookUp("Listen", "HTTPD_CONF"));
  }

}
