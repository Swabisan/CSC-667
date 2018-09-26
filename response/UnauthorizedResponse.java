
package response;

import resource.*;
import request.*;
import configuration.*;
import java.util.Date;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

public class UnauthorizedResponse extends Response {

  public void send(OutputStream out) throws IOException {
    System.out.println(this.get401ResponseHeaders());
    out.write(this.get401ResponseHeaders());
    out.flush();
    out.close();
  }
}
