
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

public class DELETEResponse extends Response {
  //202, 204, 200
  private static int statusCode = 204;

  private static String reasonPhrase;
  private static String absolutePath;
  private static FileReader fileReader;
  //Deletes file

  public DELETEResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);
    this.statusCode = 204;
    this.reasonPhrase = "NO CONTENT";

    if(this.validFile()) {
      this.fileReader = new FileReader(absolutePath);
    }
  }

  public void send(OutputStream out) throws IOException {
    if(this.validFile()) {
      out.write(this.get204ResponseHeaders());
      this.file.delete();
      out.flush();
      out.close();
    } else {
      out.write(this.get404ResponseHeaders());
      out.flush();
      out.close();
    }
  }

  public static void main(String[] args) {
    System.out.println("james");
  }
}
