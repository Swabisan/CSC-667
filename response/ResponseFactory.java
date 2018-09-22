
package response;

import resource.*;
import request.*;
import java.util.*;
import java.io.IOException;

public class ResponseFactory {

  public void getResponse(Resource resource) throws IOException {
    String method = resource.getRequest().getMethod();

    switch(method) {
      case "GET":
        return new GETResponse(resource);
      case "POST":
        return new POSTResponse(resource);
      case "PUT":
        return new PUTResponse(resource);
      case "DELETE":
        return new DELETEResponse(resource);
      case "HEAD":
        return new HEADResponse(resource);
    }
  }

  public static void main(String[] args) {
    System.out.println("james");
  }
}
