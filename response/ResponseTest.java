
package response;

import java.awt.image.BufferedImage;
import configuration.*;
import request.*;
import resource.*;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.io.FilterOutputStream;

/************************************************************
This is just a proof of concept template that will be deleted.
A general response that sends headers and some resources
*************************************************************/

public class ResponseTest {
  ConfigurationReader configFactory = new ConfigurationReader();
  Config mimeTypes = configFactory.getConfig("MIME_TYPE");

  private static int statusCode = 200;
  private String reasonPhrase = "OK";

  private Resource resource;
  private FileReader fileReader;
  private String currentLine;
  private int filesize;

  File file;
  Socket client;
  Request request;
  String absolutePath;

 public ResponseTest(Resource resource) throws IOException {
   this.resource = resource;
   this.request = resource.getRequest();
   this.absolutePath = resource.absolutePath();
   this.file = new File(absolutePath);

   if(this.validFile()) {
     this.fileReader = new FileReader(absolutePath);
   }

 }

 public void send(Socket client) throws IOException {
   if(this.validFile()) {
     FilterOutputStream out = new FilterOutputStream(client.getOutputStream());

     byte[] headers = this.getResponseHeaders();

     out.write(headers);

     out.write(this.sendResource());
     System.out.println(this.getMimeType());

     out.flush();
     out.close();
   }
 }

 public byte[] getResponseHeaders() throws IOException {
   StringBuilder headers = new StringBuilder();
   Date localDate = new Date();

   headers.append(this.request.getVersion());
   headers.append(" ");
   headers.append(this.statusCode);
   headers.append(" ");
   headers.append(this.reasonPhrase);
   headers.append("\n");
   headers.append("Date: ");
   headers.append(localDate);
   headers.append("\n");
   headers.append("Server: FireSquad/1.0");
   headers.append("\n");
   headers.append("Status: 200 OK");
   headers.append("\n");
   headers.append("Content-Type: " + this.getMimeType());
   headers.append("\n");
   headers.append("Content-Length: " + getSize());
   headers.append("\n");
   headers.append("Connection:");
   headers.append("\n");
   headers.append("\n");

   byte[] string = headers.toString().getBytes();

   return string;
 }

 public int getSize() throws IOException {
   byte[] fileContent = Files.readAllBytes(this.file.toPath());

   return fileContent.length;
 }

 public byte[] sendResource() throws IOException {
   byte[] fileContent = Files.readAllBytes(this.file.toPath());

   return fileContent;
 }


 public Boolean validFile() {
   if ((this.request.getIdentifier().equals("/ab/"))
   || (this.request.getIdentifier().equals("/~traciely/"))) {
     return true;
   }
   return file.exists() && !file.isDirectory();
 }

 public String getMimeType() {
   String[] arrayOfString = file.getName().split("\\.");
   String str1 = arrayOfString[arrayOfString.length - 1];
   String str2 = mimeTypes.lookUp(str1, "MIME_TYPE");

   return str2;
 }

 public static void main(String[] args) throws IOException {
   ServerSocket localServerSocket = new ServerSocket(3300);
   Socket localSocket = null;
   for (;;) {
     localSocket = localServerSocket.accept();
     Request localRequest = new Request(localSocket);
     Resource localResource = new Resource(localRequest);
     ResponseTest localResponseTest = new ResponseTest(localResource);

     localResponseTest.send(localSocket);

     localSocket.close();
    }
  }
}
