
package resource;

import configuration.*;

public class Resource {
  ConfigurationReader configFactory = new ConfigurationReader();
  Config httpdConfig;
  String uri;

  private static String HTTPD_CONF   = "HTTPD_CONF";
  private static String ALIAS        = "ALIAS";
  private static String DOCUMENTROOT = "DocumentRoot";
  private static String PROTECTED    = ".htaccess";
  private static String abSCRIPTED   = "/ab/";
  private static String TRACIELY     = "/~traciely/";

  public Resource(String uri, String HttpdConfig) {
    this.httpdConfig = configFactory.getConfig(HttpdConfig);
    this.uri = uri;
  }

  private String absolutePath() {
    if(this.isScripted()) {
      return this.httpdConfig.lookUp(this.uri, ALIAS);
    }

    return this.httpdConfig.lookUp(DOCUMENTROOT, HTTPD_CONF)
      .replaceAll("^\"|\"$", "")
      .concat(uri);
  }

  private Boolean isScripted() {
    return this.uri == abSCRIPTED || this.uri == TRACIELY;
  }

  private Boolean isProtected() {
    //Still need to access file working.
    return this.uri == PROTECTED;
  }

  public static void main(String[] args) {
    Resource HTTP = new Resource("js/image-test.js", "HTTPD_CONF");
    System.out.println(HTTP.absolutePath());
    System.out.println(HTTP.isScripted());
  }
}
