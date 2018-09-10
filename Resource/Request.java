package Resource;

import java.util.*;
import java.net.*;
import java.io.*;

public class Request {

  String method;
  String identifier;
  String version;
  HashMap<String, String> headers;
  // - body : ?

  public Request(Socket client) throws IOException {
    parseHttpRequest(client);
  }

  // TODO make less shitty
  private void parseHttpRequest(Socket client) throws IOException {
    headers = new HashMap();
    String line;
    int lineNo = 0;

    BufferedReader reader = new BufferedReader(
      new InputStreamReader(client.getInputStream())
    );

    while(true) {
      line = reader.readLine();
      System.out.println( "> " + line );

      // determines when to end
      if (line == null || line.isEmpty()) {
        break;
      }

      lineNo++;

      if (lineNo == 1) {
        setFirstLine(line);
      }
      if (lineNo > 1) {
        // determines when to end
        if (!line.isEmpty()) {
          addHeaders(line);
        }
      }
    }
    printDataFields();
  }

  private void addHeaders(String header) {
    String[] tokens = header.split(": ");
    this.headers.put(tokens[0], tokens[1]);
  }

  private void setFirstLine(String firstLine) {
    String[] tokens = firstLine.split(" ");
    this.method     = tokens[0];
    this.identifier = tokens[1];
    this.version    = tokens[2];
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
