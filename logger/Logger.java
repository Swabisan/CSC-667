
package logger;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import configuration.*;
import resource.*;
import request.*;

public class Logger {

  private ConfigurationReader configFactory = new ConfigurationReader();
  private Config httpdConf;
  private String logFile;

  public Logger() {
    this.httpdConf = this.configFactory.getConfig("HTTPD_CONF");
    this.logFile = this.httpdConf.lookUp("LogFile", "HTTPD_CONF");
  }

  public Logger(int test) {
    this.httpdConf = this.configFactory.getConfig("HTTPD_CONF");
    this.logFile = this.httpdConf.lookUp("LogFile", "HTTPD_CONF");

    try {
      this.log();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

  public void log() throws IOException {
    // TODO write in common log format
    // https://en.wikipedia.org/wiki/Common_Log_Format

    FileWriter fileWriter = new FileWriter(this.logFile, true);
    PrintWriter printWriter = new PrintWriter(fileWriter);

    Date currentDate = new Date();

    // Common_Log_Format: IP identifier user [date:time tz] verb uri version status size
    printWriter.printf("%s %s %s [%s/%s/%s:%s:%s:%s %s] %s %s %s %s %s\n",
      "testIP", "testIdentifier", "testUser",
      "testDay", "testMonth", "testYear", "testHour",
      "testMin", "testSecond", "testTZ",
      "testVerb", "testURI", "testVersion",
      "testStatus", "testSize");
    printWriter.close();
  }

  public static void main(String[] args) {
    Logger logger = new Logger(-1);
  }
}
