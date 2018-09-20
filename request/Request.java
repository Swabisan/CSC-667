
package request;

import java.util.*;
import java.net.*;
import java.io.*;

public class Request {

  String method = "no method received";
  String identifier = "no method received";
  String version = "no method received";
  Socket client;

  public HashMap<String, String> headers = new HashMap<String,String>();
  // - body : ?

  public Request(Socket client) throws IOException {
    this.client = client;
    parseHttpRequest(client);
  }

  private void parseHttpRequest(Socket client) throws IOException {
    //this.headers = new HashMap<String,String>();
    String currentLine;
    int lineNo = 0;

    BufferedReader reader = new BufferedReader(
      new InputStreamReader(client.getInputStream())
    );

    // while(true) {
    while((currentLine = reader.readLine()) != null) {
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
          // System.out.println(headers);
          break;
        }
      }
    }
  // }
    //printDataFields();
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
    //This can be a 1-liner
    if (str == null) {
      return true;
    }

    if (str == "") {
      return true;
    }

    return false;
  }

  private void printDataFields() {
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
