
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

public class ForbiddenResponse extends Response {

  public ForbiddenResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 403;
    this.reasonPhrase = "Forbidden";
  }

  public void send(OutputStream out) throws IOException {
    out.write(this.get403ResponseHeaders());
    out.flush();
    out.close();
  }
}
