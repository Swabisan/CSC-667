
package response;

import request.*;
import resource.*;
import configuration.*;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.nio.file.Files;
import java.io.OutputStream;

public abstract class Response {

  ConfigurationReader configFactory = new ConfigurationReader();
  Config mimeTypes = configFactory.getConfig("MIME_TYPE");

  Resource resource;
  Request request;
  File file;
  public static String body;
  public static byte[] bodyBytes;

  public abstract void send(OutputStream out) throws IOException;

  public Boolean validFile() {
    if ((this.request.getIdentifier().equals("/ab/"))
    || (this.request.getIdentifier().equals("/~traciely/"))) {
      return true;
    }
    return file.exists() && !file.isDirectory();
  }

  public byte[] get404ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(404);
    headers.append(" ");
    headers.append("Not Found");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 404 Not Found");
    headers.append("\n");
    headers.append("Content-Type: " + this.getContenType());
    headers.append("\n");
    headers.append("\n");

    byte[] string = headers.toString().getBytes();

    return string;
  }

  public byte[] get201ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(201);
    headers.append(" ");
    headers.append("CREATED");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 201 CREATED");
    headers.append("\n");
    headers.append("Content-Type: " + this.bodyBytes.length + this.getResource().length);
    headers.append("\n");
    headers.append("\n");

    byte[] string = headers.toString().getBytes();

    return string;
  }

  public String getContenType() {
    String[] identifiers = file.getName().split("\\.");
    String lastElement = identifiers[identifiers.length - 1];
    String mimeType = mimeTypes.lookUp(lastElement, "MIME_TYPE");

    return mimeType;
  }

  public byte[] getResource() throws IOException {
    byte[] fileContent = Files.readAllBytes(this.file.toPath());

    return fileContent;
  }
}
