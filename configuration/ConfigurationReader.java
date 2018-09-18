
package configuration;

import java.util.*;

public class ConfigurationReader {

  public static Config getConfig(String config) {
    //It works.  Will test more.
    //Need to add rest of configuration files
    switch(config) {
      case "HTTPD_CONF":
        return new HttpdConf("conf/httpd.conf");
      case "MIME_TYPE":
        return new MimeTypes("conf/mime.types");
      case "AUTH_HTPASSWD":
        return new AuthHtpasswd("conf/auth.htpasswd");
      default:
        return null;
    }
  }
}
