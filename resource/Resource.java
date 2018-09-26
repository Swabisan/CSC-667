
package resource;

import java.io.File;

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
  private static String SCRIPTAlias  = "/cgi-bin/";
  private static String sAlias       = "SCRIPT_ALIAS";
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

  public Process test() throws IOException {
    if(this.uri.equals(SCRIPTAlias)) {
      Process test = Runtime.getRuntime().exec(this.httpdConfig.lookUp(this.uri, sAlias).concat("perl_env"));
      return test;
    }
    return null;
  }

  private Boolean isScripted() {
    return this.uri.equals(abSCRIPTED) || this.uri.equals(TRACIELY) || this.uri.equals(SCRIPTAlias);
  }

  public Boolean isProtected() {
    String htaccessPath = this.getHtaccessPath();
    File accessFile = new File(htaccessPath);

    if (accessFile.exists()) {
      return true;
    }
    
    return false;
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

  public String getHtaccessPath() {
    String path = this.absolutePath();
    String[] tokens = path.split("/");

    if (tokens.length > 0) {
      String lastToken = tokens[tokens.length - 1];
      return path.replace(lastToken, ".htaccess");
    }

    return path;

  }

}
