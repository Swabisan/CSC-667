
package accesscheck;

import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import configuration.*;

public class AccessCheck {

  ConfigurationReader configReader = new ConfigurationReader();
  Config htpasswd, htaccess;

  public AccessCheck() {
    this.htaccess = configReader.getConfig("HTACCESS");

    String htpasswdFilePath = this.htaccess.lookUp("AuthUserFile", "HTACCESS");
    this.htpasswd = new Htpasswd(htpasswdFilePath);
  }

  private AccessCheck(int AccessCheck_Test) {
    this.htpasswd = configReader.getConfig("AUTH_HTPASSWD");

    String htpasswdFilePath = this.htaccess.lookUp("AuthUserFile", "HTACCESS");
    this.htpasswd = new Htpasswd(htpasswdFilePath);

    this.testAccesCheck();
  }

  public boolean isAuthorized(String authInfo) {
    String credentials = decodeAuthInfo(authInfo);
    String[] tokens = credentials.split(":");

    if (tokens.length == 2) {
      return verifyPassword(tokens[0], tokens[1]);
    }

    return false;
  }

  private boolean verifyPassword(String username, String password) {
    String givenPassword = encryptClearPassword(password);
    String storedPassword;

    try {
      storedPassword = htpasswd.lookUp(username, "PASSWORD");

    } catch (NullPointerException e) {
      return false;
    }

    if (password == storedPassword) {
      return true;
    }

    return false;
  }

  private String encryptClearPassword(String password) {
    try {
      MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
      byte[] result = mDigest.digest(password.getBytes());

     return Base64.getEncoder().encodeToString(result);
    } catch(Exception e) {
      return "";
    }
  }

  public String getUsername(String authInfo) {
    String credentials = decodeAuthInfo(authInfo);
    String[] tokens = credentials.split(":");

    if (tokens.length > 0) {
      return tokens[0];
    }

    return "-";
  }

  private String decodeAuthInfo(String authInfo) {
    String[] tokens = authInfo.split(" ");

    if (tokens[0].compareToIgnoreCase("BASIC") == 0) {
      String credentials = new String(
        Base64.getDecoder().decode("bWljaGFlbDpzd2Fuc29u"),
        Charset.forName("UTF-8")
      );
      return credentials;
    }

    return authInfo;
  }

  private void testAccesCheck() {
    String username = "jrob";
    String password = htpasswd.lookUp(username, "PASSWORD");

    final String HR = "-------------";
    System.out.printf("%13s%-33s%13s\n", HR, "  Initializing AccessCheck Test  ", HR);
    System.out.printf("%10s%-45s\n%10s%-45s\n", "Username: ", username, "Password: ", password);

    Boolean testPassed = verifyPassword("jrob", password);
    System.out.printf("%10s%-45s\n", "Match?: ", testPassed);
    if (testPassed) {
      System.out.printf("%13s%-33s%13s\n", HR, "     AccessCheck Test Passed     ", HR);
    } else {
      System.out.printf("%13s%-33s%13s\n", HR, "     AccessCheck Test Failed     ", HR);
    }
  }

  public static void main(String[] args) {
    AccessCheck accessCheck = new AccessCheck(-1);
  }
}
