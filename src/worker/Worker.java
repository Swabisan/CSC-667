
package src.worker;

import java.net.Socket;
import java.io.IOException;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.ChronoField;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;

import src.request.*;
import src.resource.*;
import src.response.*;
import src.accesscheck.*;
import src.logger.*;

public class Worker implements Runnable {

  private Socket clientSocket;
  private Logger logger;

  private ResponseFactory responseFactory = new ResponseFactory();
  private Request request;
  private Resource resource;
  private Response response;
  private String username;

  public Worker(Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.logger = new Logger();
  }

  public void run() {
    clearFields();

    try {
      talkToClient();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void talkToClient() throws IOException {
    request = new Request(clientSocket);
    resource = new Resource(request);

    if (request.isBadRequest()) {
      response = new BadRequestResponse(resource);
      this.sendResponse(response);
      return;
    }

    if (request.isPopulated()) {

      if (resource.isProtected()) {
        String authInfo = request.getHeader("Authorization");

        if (authInfo != "KEY_NOT_FOUND") {
          String accessPath = resource.getHtaccessPath();
          AccessCheck accessCheck = new AccessCheck(accessPath);

          username = accessCheck.getUsername(authInfo);

          if (!accessCheck.isAuthorized(authInfo)) {
            response = new ForbiddenResponse(resource);
            this.sendResponse(response);
            return;
          }

        } else {
          response = new UnauthorizedResponse(resource);
          this.sendResponse(response);
          return;
        }
      }

      String ims = request.getHeader("If-Modified-Since");
      if (ims != "KEY_NOT_FOUND") {
        LocalDateTime lastModified = resource.getLastModified().toLocalDateTime();
        LocalDateTime imsDateTime = this.parseIMS(ims).toLocalDateTime();
        if (imsDateTime.isAfter(lastModified)) {
          response = new NOTMODresponse(resource);
          this.sendResponse(response);
          return;
        }
      }

      response = responseFactory.getResponse(resource);
      this.sendResponse(response);
    }
  }

  private ZonedDateTime parseIMS(String ims) {
    String tokens[] = ims.split(" ");
    if (tokens.length != 6) {
      System.out.println("tokens " + tokens.length);
      return ZonedDateTime.now();
    }

    tokens[0] = tokens[0].replace(",", "").trim();
    String hourMinSec[] = tokens[4].split(":");
    if (hourMinSec.length != 3) {
      System.out.println("hms " + hourMinSec.length);
      return ZonedDateTime.now();
    }

    int month = this.switchMonth(tokens[2].toUpperCase());
    return ZonedDateTime.of(Integer.parseInt(tokens[3]), month,
     Integer.parseInt(tokens[1]), Integer.parseInt(hourMinSec[0]),
     Integer.parseInt(hourMinSec[1]), Integer.parseInt(hourMinSec[2]),
     0, ZoneId.of(tokens[5]));
  }

  private void clearFields() {
    this.request  = null;
    this.resource = null;
    this.response = null;
    this.username = "UNKNOWN_USER";
  }

  private void sendResponse(Response response) throws IOException {
    response.send(clientSocket.getOutputStream());
    logger.log(request, response, username);
    closeConnection();
  }

  private void closeConnection() throws IOException {
    clientSocket.close();
    // printConnectionClosed();
  }

  private void printConnectionClosed() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "    Connection Closed    ", HR);
  }

  private int switchMonth(String monthShort) {
    int monthInt;

    switch (monthShort) {
        case "JAN":  monthInt = 1;
                 break;
        case "FEB":  monthInt = 2;
                 break;
        case "MAR":  monthInt = 3;
                 break;
        case "APR":  monthInt = 4;
                 break;
        case "MAY":  monthInt = 5;
                 break;
        case "JUN":  monthInt = 6;
                 break;
        case "JUL":  monthInt = 7;
                 break;
        case "AUG":  monthInt = 8;
                 break;
        case "SEP":  monthInt = 9;
                 break;
        case "OCT": monthInt = 10;
                 break;
        case "NOV": monthInt = 11;
                 break;
        case "DEC": monthInt = 12;
                 break;
        default: monthInt = 0;
                 break;
    }

    return monthInt;
  }

}
