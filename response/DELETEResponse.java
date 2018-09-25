
package response;
import resource.*;
import java.io.OutputStream;
import java.net.Socket;

public class DELETEResponse extends Response {

  private static int statusCode = 204;
  //Deletes file

  public DELETEResponse(Resource resource) {

  }

  public void send(OutputStream out) {

  }

  public static void main(String[] args) {
    System.out.println("james");
  }
}
