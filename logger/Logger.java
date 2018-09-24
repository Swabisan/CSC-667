
package logger;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.lang.StringBuffer;



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
      FileWriter fileWriter = new FileWriter(this.logFile, true);
      PrintWriter printWriter = new PrintWriter(fileWriter);

      // Common_Log_Format: IP identifier user [date:time tz] verb uri version status size
      printWriter.printf("%s %s %s [%s] %s %s %s %s %s\n",
        "testIP", "testIdentifier", "testUser",
        this.getDateTime(ZonedDateTime.now()),
        request.getMethod(), request.getIdentifier(), request.getVersion(),
        "testStatus", "testSize");
      printWriter.close();
  }

  public void log() throws IOException {
    FileWriter fileWriter = new FileWriter(this.logFile, true);
    PrintWriter printWriter = new PrintWriter(fileWriter);

    // Common_Log_Format: IP identifier user [date:time tz] verb uri version status size
    printWriter.printf("%s %s %s [%s] %s %s %s %s %s\n",
      "testIP", "testIdentifier", "testUser",
      this.getDateTime(ZonedDateTime.now()),
      "testVerb", "testURI", "testVersion",
      "testStatus", "testSize");
    printWriter.close();
  }

  private String getDateTime(ZonedDateTime timeStamp) {
    return String.format("%02d/%s/%04d:%02d:%02d:%02d %s",
    timeStamp.getDayOfMonth(),
    timeStamp.getMonth().getDisplayName(TextStyle.SHORT,Locale.ENGLISH),
    timeStamp.getYear(), timeStamp.getHour(), timeStamp.getMinute(),
    timeStamp.getSecond(), timeStamp.getOffset());
  }

  public static void main(String[] args) {
    Logger logger = new Logger(-1);
  }
}
