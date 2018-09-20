
package logger;

import java.io.FileWriter;
import java.io.RrintWriter;

import configuration.*;
import resource.*;
import request.*;

public class Logger {

  private ConfigurationReader configFactory = new ConfigurationReader();
  private Config httpdConf;
  private String logFile;

  public Logger(String httpdConf) {
    this.httpdConf = this.configFactory.getConfig(httpdConf);
    this.logFile = this.httpdConf.lookUp("LogFile", "HTTPD_CONF");
  }

  public void log(Request request, Resource resource)
    throws IOException {
    // TODO write in common log format
    // https://en.wikipedia.org/wiki/Common_Log_Format

    FileWriter fileWriter = new FileWriter(this.logFile);
    PrintWriter printWriter = new PrintWriter(fileWriter);

    printWriter.println("If you can read this then it works!");
    printWriter.close();
  }

}
