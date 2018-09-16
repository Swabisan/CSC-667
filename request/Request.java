package request;

import java.util.*;
import java.net.*;
import java.io.*;

public class Request {

  String method;
  String identifier;
  String version;
  HashMap<String, String> headers = new HashMap<String, String>();
  // - body : ?

  public Request(Socket client) throws IOException {
    parseHttpRequest(client);
  }

  private void parseHttpRequest(Socket client) throws IOException {
    String currentLine = null;
    int lineNo = 0;

    BufferedReader reader = new BufferedReader(
      new InputStreamReader(client.getInputStream())
    );

    while(true) {
      currentLine = reader.readLine();
      System.out.println( "> " + currentLine );

      if (isStringEmpty(currentLine)) {
        break;
      }

      lineNo++;

      if (lineNo == 1) {
        if (!addFirstLine(currentLine)){
          returnBadRequest();
          break;
        }
      }
      if (lineNo > 1) {
        if (!addHeaders(currentLine)){
          System.out.println("Successfully parsed request!");
          break;
        }
      }
    }
    printDataFields();
  }

  private boolean addHeaders(String header) {
    String[] tokens = header.split(": ");
    if (tokens.length < 2) {
      return false;
    }
    this.headers.put(tokens[0], tokens[1]);
    return true;
  }

  private boolean addFirstLine(String firstLine) {
    String[] tokens = firstLine.split(" ");
    if (tokens.length < 3) {
      return false;
    }
    this.method     = tokens[0];
    this.identifier = tokens[1];
    this.version    = tokens[2];
    return true;
  }

  private boolean isStringEmpty(String str) {
    if (str == null) {
      return true;
    }

    if (str == "") {
      return true;
    }

    return false;
  }

  private void printDataFields() {
    System.out.println("Method: "     + this.method);
    System.out.println("Identifier: " + this.identifier);
    System.out.println("Version: "    + this.version);
    System.out.println("Headers");

    for(Map.Entry<String, String> entry : headers.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
  }

  private void returnBadRequest() {
    System.out.println("400 Bad request");
  }

  public String getMethod() {
    return this.method;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getVersion() {
    return this.version;
  }
}
