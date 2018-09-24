
package response;

import resource.*;
import request.*;
import configuration.*;
import java.util.Date;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.FilterOutputStream;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

public class GETResponse extends Response {

  private static int statusCode;
  private static String reasonPhrase;
  private static String absolutePath;
  private static FileReader fileReader;

  public GETResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);
    this.statusCode = 200;
    this.reasonPhrase = "OK";

    if(this.validFile()) {
      this.fileReader = new FileReader(absolutePath);
    }
  }
  //Get sends file content

  public void send(Socket client) throws IOException {
    try {

      FilterOutputStream out = new FilterOutputStream(client.getOutputStream());

      out.write(this.getResponseHeaders());
      out.write(this.getResource());
      out.flush();
      out.close();

    } catch(Exception e) {
      FilterOutputStream out = new FilterOutputStream(client.getOutputStream());
      try {
        out.write(this.get404ResponseHeaders());
        out.flush();
        out.close();
      } catch(Exception er) {
        er.printStackTrace();
      }
    }
  }

  public byte[] getResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode);
    headers.append(" ");
    headers.append(this.reasonPhrase);
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 200 OK");
    headers.append("\n");
    headers.append("Content-Type: " + this.getContenType());
    headers.append("\n");
    headers.append("Content-Length: " + this.getResource().length);
    headers.append("\n");
    headers.append("\n");

    byte[] string = headers.toString().getBytes();

    return string;
  }

  // public String getContenType() {
  //   String[] identifiers = file.getName().split("\\.");
  //   String lastElement = identifiers[identifiers.length - 1];
  //   String mimeType = mimeTypes.lookUp(lastElement, "MIME_TYPE");
  //
  //   return mimeType;
  // }
  //
  // public byte[] getResource() throws IOException {
  //   byte[] fileContent = Files.readAllBytes(this.file.toPath());
  //
  //   return fileContent;
  // }

  public static void main(String[] args) throws IOException {
    System.out.println("james");
    ServerSocket localServerSocket = new ServerSocket(3100);
    Socket localSocket = null;

    for (;;) {
      localSocket = localServerSocket.accept();
      Request localRequest = new Request(localSocket);
      Resource localResource = new Resource(localRequest);
      GETResponse localGETResponse = new GETResponse(localResource);

      localGETResponse.send(localSocket);
      System.out.println(localGETResponse.absolutePath);

      localSocket.close();
     }
  }
}
