
package response;
import resource.*;

import configuration.*;
import java.net.Socket;

public class PUTResponse extends Response {

  private static int statusCode = 201;
  //Put creates a file

  public PUTResponse(Resource resource) {

  }

  public void send(Socket client) {
    
  }

  public static void main(String[] args) {
    ConfigurationReader test = new ConfigurationReader();
    Config tester = test.getConfig("MIME_TYPE");


    System.out.println(tester.lookUp("json", "MIME_TYPE"));
  }
}
