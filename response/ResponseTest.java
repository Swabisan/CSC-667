
package response;

import configuration.*;
import request.*;
import resource.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;


/******************************************
This is just a proof of concept template that will be deleted.
A general response that sends headers and some resources
*******************************************/

public class ResponseTest {
  ConfigurationReader configFactory = new ConfigurationReader();
  Config mimeTypes = configFactory.getConfig("MIME_TYPE");

  private static int statusCode = 200;
  private String reasonPhrase = "OK";

  private Resource resource;
  private BufferedReader bufferReader;
  private FileReader fileReader;
  private String currentLine;

  File file;
  Socket client;
  Request request;
  String absolutePath;
  StringBuilder anotatedText;

 public ResponseTest(Resource resource) throws IOException {
   this.resource = resource;
   this.request = resource.getRequest();
   this.absolutePath = resource.absolutePath();
   this.file = new File(absolutePath);

   if(this.validFile()) {
     this.fileReader = new FileReader(absolutePath);
   }

   System.out.println(this.request.getMethod());
 }

 public void send(Socket client) throws IOException {
   if(this.validFile()) {
     OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());

     String headers = this.getResponseHeaders();
     out.write(headers.toCharArray());
     out.write(this.sendResource());

     out.flush();
     out.close();
   }
 }

 public String getResponseHeaders() {
   StringBuilder headers = new StringBuilder();
   Date localDate = new Date();

   headers.append(this.request.getVersion());
   headers.append(" ");
   headers.append(statusCode);
   headers.append(" ");
   headers.append(reasonPhrase);
   headers.append("\n");
   headers.append("Date: ");
   headers.append(localDate);
   headers.append("\n");
   headers.append("Server: FireSquad/1.0");
   headers.append("\n");
   headers.append("Status: 200 OK");
   headers.append("\n");
   headers.append("Content-Type: " + getMimeType());
   headers.append("\n");
   headers.append("Connection:");
   headers.append("\n");
   headers.append("\n");

   return headers.toString();
 }

 public long getFileSize() {
   try {
     File localFile = new File(absolutePath);
     return localFile.length();
   }
   catch (Exception e) {
     e.printStackTrace();
   }
   return 0L;
 }

 public String sendResource() throws IOException {
   String str1 = null;
   String str2 = "";

   FileReader file = fileReader;
   BufferedReader bufferReader = new BufferedReader(file);

   while ((str1 = bufferReader.readLine()) != null) {
     str2 = str2.concat(str1);
   }

   return str2;
 }

 public Boolean validFile() {
   if ((this.request.getIdentifier().equals("/ab/")) || (this.request.getIdentifier().equals("/~traciely/"))) {
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
   java.net.ServerSocket localServerSocket = new java.net.ServerSocket(3300);
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
