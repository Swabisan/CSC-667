
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
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.*;

public class GETResponse extends Response {

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

  public void send(OutputStream out) throws IOException {
    if(this.validFile()) {
      out.write(this.getResponseHeaders());
      out.write(this.getResource());
      out.flush();
      out.close();
    } else {
      out.write(this.get404ResponseHeaders());
      out.flush();
      out.close();
    }
  }
  public String getContenType() {
    String[] identifiers = file.getName().split("\\.");
    String lastElement = identifiers[identifiers.length - 1];
    String mimeType = mimeTypes.lookUp(lastElement, "MIME_TYPE");

    return mimeType;
  }
}
