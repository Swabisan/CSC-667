
package resource;

import java.net.*;
import java.io.*;
import request.*;
import configuration.*;

public class Resource {

  ConfigurationReader configFactory = new ConfigurationReader();
  Config httpdConfig = configFactory.getConfig(HTTPD_CONF);
  public static String uri;

  //Might have to read from config file directly.  Might also say.
  private static String HTTPD_CONF   = "HTTPD_CONF";
  private static String ALIAS        = "ALIAS";
  private static String DOCUMENTROOT = "DocumentRoot";
  private static String PROTECTED    = ".htaccess";
  private static String abSCRIPTED   = "/ab/";
  private static String TRACIELY     = "/~traciely/";
  private static Request request;

  public Resource(Request request) throws IOException {
    this.request = request;
    this.uri = this.request.getIdentifier();
  }

  public String absolutePath() {
    if(this.isScripted()) {
      if(this.uri.equals(abSCRIPTED)) {
        return this.httpdConfig.lookUp(this.uri, ALIAS).concat("index.html");
      }
      //handle TRACIELY scripted
    }

    if(this.uri.equals("/")) {
      return this.httpdConfig.lookUp(DOCUMENTROOT, HTTPD_CONF)
        .concat(this.trimedUri()).concat("index.html");
    }

    return this.httpdConfig.lookUp(DOCUMENTROOT, HTTPD_CONF)
      .concat(this.trimedUri());
  }

  private Boolean isScripted() {
    return this.uri.equals(abSCRIPTED) || this.uri.equals(TRACIELY);
  }

  public Boolean isProtected() {
    //Still need to access file working.
    return this.uri == PROTECTED;
  }

  public String getUri() {
    return this.request.getIdentifier();
  }

  public String trimedUri() {
    return this.uri.substring(1);
  }

  public Request getRequest() throws IOException {
    return this.request;
  }
}
