
package response;

import resource.*;
import request.*;
import configuration.*;
import java.util.Date;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

public class PUTResponse extends Response {

  private static String reasonPhrase = "CREATED";
  private static int statusCode = 201;

  private static String absolutePath;
  private static FileReader fileReader;
  //Put creates a file

  public PUTResponse(Resource resource) {

  }

  public void send(OutputStream out) {

  }

  public static void main(String[] args) {
    ConfigurationReader test = new ConfigurationReader();
    Config tester = test.getConfig("MIME_TYPE");


    System.out.println(tester.lookUp("json", "MIME_TYPE"));
  }
}
