package response;

public class Get extends Response {

  //URIs are “simply formatted strings which identify -
  //via name, location, or any other characteristic - a resource.”

  //A resource is the thing living on the other side of a URI and a URI
  //only points to one resource. That sounds rather abstract, so let’s look at the following example.

  //Example of a response:

  //HTTP/1.1 200 OK
// Date: Mon, 27 Jul 2009 12:28:53 GMT
// Server: Apache/2.2.14 (Win32)
// Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT
// Content-Length: 88
// Content-Type: text/html
// Connection: Closed

  private static int statusCode = 200;
  //Get sends file content
  public Get() {

  }

  public Response getResponse(){
    return null;
  }

  public static void main(String[] args) {
    System.out.println("james");
  }
}
