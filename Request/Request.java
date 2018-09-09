package Request;

import java.util.*;
import java.net.*;
import java.io.*;

public class Request {

  String method;
  String identifier;
  String version;
  // Hashmap headers;
  // - body : ?

  public Request(Socket client) throws IOException {
    parseHttpRequest(client);
  }

  private void parseHttpRequest(Socket client) throws IOException {
    int lineNo = 0;
    String line;

    BufferedReader reader = new BufferedReader(
      new InputStreamReader(client.getInputStream())
    );

    while(true) {
      lineNo++;
      line = reader.readLine();
      System.out.println( "> " + line );

      // determines when to end
      if(line == null) {
        break;
      }

      if (lineNo == 1) {
        setDataFields(line);
      }
    }
    printRequestVariables();
  }

  private void setDataFields(String firstLine) {
    String[] tokens = firstLine.split(" ");
    this.method = tokens[0];
    this.identifier = tokens[1];
    this.version = tokens[2];
  }

  private void printRequestVariables() {
    System.out.println("Method: " + this.method);
    System.out.println("Identifier: " + this.identifier);
    System.out.println("Version: " + this.version);
  }
}
