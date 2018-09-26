
package src.response;

import src.request.*;
import src.resource.*;
import src.configuration.*;
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
  public static int statusCode;
  public static String reasonPhrase;

  public abstract void send(OutputStream out) throws IOException;

  public Boolean validFile() {
    if ((this.request.getIdentifier().equals("/ab/"))
    || (this.request.getIdentifier().equals("/~traciely/"))) {
      return true;
    }
    return file.exists() && !file.isDirectory();
  }

  public byte[] getResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode);
    headers.append(" ");
    headers.append(this.reasonPhrase = "OK");
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

    byte[] headersBytes = headers.toString().getBytes();

    return headersBytes;
  }
  public byte[] get304ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 304);
    headers.append(" ");
    headers.append(this.reasonPhrase = "OK");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 304 Not Modified");
    headers.append("\n");
    headers.append("Content-Location: " + this.resource.uri);
    headers.append("\n");
    headers.append("\n");

    byte[] headersBytes = headers.toString().getBytes();

    return headersBytes;
  }


  public byte[] get404ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 404);
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

    byte[] headersBytes = headers.toString().getBytes();

    return headersBytes;
  }


  public byte[] get403ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 403);
    headers.append(" ");
    headers.append("Not Found");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 403 Forbidden");
    headers.append("\n");
    headers.append("\n");

    byte[] headersBytes = headers.toString().getBytes();

    return headersBytes;
  }
  public byte[] get400ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 400);
    headers.append(" ");
    headers.append("BAD REQUEST");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 400 BAD REQUEST");
    headers.append("\n");
    headers.append("\n");

    byte[] headersBytes = headers.toString().getBytes();

    return headersBytes;
  }
  public byte[] get401ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();
    System.out.println(this.request.getVersion());
    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 401);
    headers.append(" ");
    headers.append("Unauthorized");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 401 Unauthorized");
    headers.append("\n");
    headers.append("WWW-Authenticate: Basic realm=Need Auth");
    headers.append("\n");
    headers.append("\n");

    byte[] headersBytes = headers.toString().getBytes();

    return headersBytes;
  }

  public byte[] get201ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 201);
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
    headers.append("Content-Location: ");
    headers.append(this.resource.uri);
    headers.append("\n");
    headers.append("Content-Length: " + this.bodyBytes.length + this.getResource().length);
    headers.append("\n");
    headers.append("\n");

    byte[] headerBytes = headers.toString().getBytes();

    return headerBytes;
  }

  public byte[] get204ResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();

    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode = 204);
    headers.append(" ");
    headers.append("NO CONTENT");
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: FireSquad/1.0");
    headers.append("\n");
    headers.append("Status: 204 NO CONTENT");
    headers.append("\n");
    headers.append("Content-Location: ");
    headers.append(this.resource.uri);
    headers.append("\n");
    headers.append("Content-Length: " + this.getResource().length);
    headers.append("\n");
    headers.append("\n");

    byte[] headerBytes = headers.toString().getBytes();

    return headerBytes;
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

  public int getByteLength() {
    if (this.bodyBytes == null ) {
      return 0;
    }

    return this.bodyBytes.length;
  }

  public int getStatusCode() {
    return this.statusCode;
  }
}
