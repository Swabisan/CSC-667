
package request;

import java.util.*;
import java.net.*;
import java.io.*;

public class Request {

  private String method     = "no method received";
  private String identifier = "no identifier received";
  private String version    = "no version received";
  private HashMap<String, String> headers;
  private String body;

  private BufferedReader bufferReader;

  public Request(Socket client) throws IOException {
    headers = new HashMap<String, String>();
    parseHttpRequest(client);
  }

  private void parseHttpRequest(Socket client) throws IOException {
    String currentLine = null;
    int lineNo = 0;

    this.bufferReader = new BufferedReader(
      new InputStreamReader(client.getInputStream())
    );

    while((currentLine = this.bufferReader.readLine()) != null) {
      System.out.println( "> " + currentLine );
      lineNo++;

      if (lineNo == 1) {
        if (!addFirstLine(currentLine)){
          returnBadRequest();
          break;
        }
      }
      if (lineNo > 1) {
        if (!addHeaders(currentLine)){
          System.out.println("Successfully parsed request...");
          break;
        }
      }
    }
    printDataFields();
  }

  private boolean addHeaders(String header) {
    String[] tokens = header.split(": ");

    if (isNoMoreHeaders(header)) {
      return false;
    }
    if (tokens.length < 2) {
      returnBadRequest();
      return false;
    }
    
    this.headers.put(tokens[0], tokens[1]);
    return true;
  }

  private boolean isNoMoreHeaders(String header) {
    return header.isEmpty();
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

  private void printDataFields() {
    final String CB = ": ";
    System.out.printf("%-20s%2s%S\n", "Method", CB, this.method);
    System.out.printf("%-20s%2s%S\n", "Identifier", CB, this.identifier);
    System.out.printf("%-20s%2s%S\n", "Version", CB, this.version);

    if (headers.isEmpty()) {
      System.out.printf("%-20s%2s%S\n", "Headers", ": ", "no headers received");
    } else {
      final String HR = "- - - - - - - - - - - - -";
      System.out.printf("%-25s%9s%25s\n", HR, " Headers ", HR);
    }

    for(Map.Entry<String, String> entry : headers.entrySet()) {
      System.out.printf("%-20s%2s%S\n", entry.getKey(), CB, entry.getValue());
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

  public String getHeader(String key) {
    String value;
    value = this.headers.getOrDefault(key, "KEY_NOT_FOUND");
    return value;
  }
}
