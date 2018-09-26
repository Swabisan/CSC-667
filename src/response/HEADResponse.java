
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

public class HEADResponse extends Response {

  private static String reasonPhrase;
  private static String absolutePath;
  private static FileReader fileReader;

  public HEADResponse(Resource resource) throws IOException {
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

  public void send(OutputStream out) throws IOException {
    if(this.validFile()) {
      out.write(this.getResponseHeaders());
      out.flush();
      out.close();
    } else {
      out.write(this.get404ResponseHeaders());
      out.flush();
      out.close();
    }
  }
}
