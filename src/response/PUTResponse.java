
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

public class PUTResponse extends Response {

  private static String reasonPhrase;
  private static String absolutePath;
  private static FileReader fileReader;
  //Put creates a file

  public PUTResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);
    this.body = this.request.getBody();
    this.bodyBytes = this.body.getBytes();
    this.statusCode = 201;
    this.reasonPhrase = "OK";

    if(this.validFile()) {
      this.fileReader = new FileReader(absolutePath);
    }
  }

  public void send(OutputStream out) throws IOException {
    if(this.validFile() && !this.body.equals("")) {
      out.write(this.get201ResponseHeaders());
      out.write(this.getResource());
      out.write(this.bodyBytes);
      out.flush();
      out.close();
    } else if(this.validFile()) {
      out.write(this.get204ResponseHeaders());
      out.write(this.getResource());
      out.flush();
      out.close();
    } else {
      out.write(this.get404ResponseHeaders());
      out.flush();
      out.close();
    }
  }
}
