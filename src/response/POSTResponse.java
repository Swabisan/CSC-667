
package src.response;

import src.resource.*;
import src.request.*;
import src.configuration.*;
import java.util.Date;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

public class POSTResponse extends Response {

  private static String reasonPhrase;
  private static String absolutePath;
  private static FileReader fileReader;

  public POSTResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);
    this.body = this.request.getBody();
    this.bodyBytes = this.body.getBytes();
    this.statusCode = 200;
    this.reasonPhrase = "OK";

    if(this.validFile()) {
      this.fileReader = new FileReader(absolutePath);
    }
  }

  public void send(OutputStream out) throws IOException {

    if(this.validFile() && this.body.equals("")) {
      out.write(this.getResponseHeaders());
      out.write(this.getResource());
      out.write(this.bodyBytes);
      out.flush();
      out.close();
    } else if(this.validFile()) {
      out.write(this.get201ResponseHeaders());
      out.write(this.getResource());
      out.flush();
      out.close();
    } else {
      out.write(this.get404ResponseHeaders());
      out.flush();
      out.close();
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
    headers.append("Content-Length: " + this.bodyBytes.length + this.getResource().length);
    headers.append("\n");
    headers.append("\n");
    headers.append(this.body);
    headers.append("\n");

    byte[] headerBytes = headers.toString().getBytes();

    return headerBytes;
  }
}
