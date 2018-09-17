
package response;

import configuration.*;

public class Put extends Response {

  private static int statusCode = 201;
  //Put creates a file
  public Put() {

  }

  public Response getResponse(){
    return null;
  }

  public static void main(String[] args) {
    ConfigurationReader test = new ConfigurationReader();
    Config tester = test.getConfig("MIME_TYPE");


    System.out.println(tester.lookUp("json", "MIME_TYPE"));
  }
}
