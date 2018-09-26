
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

public class NOTMODresponse extends Response {

  public NOTMODresponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 304;
    this.reasonPhrase = "Not Modified";
  }

  public void send(OutputStream out) throws IOException {
    out.write(this.get304ResponseHeaders());
    out.flush();
    out.close();
  }
}
