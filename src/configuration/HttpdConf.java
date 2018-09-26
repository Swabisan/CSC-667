
package src.configuration;

import java.util.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

class HttpdConf extends Config {

  private String config;
  private HashMap<String,String> httpdMap;
  private HashMap<String,String> scriptAliasMap;
  private HashMap<String,String> aliasMap;
  private BufferedReader bufferReader;
  private FileReader fileReader;
  private String currentLine;
  private StringTokenizer token;
  private String key;
  private String value;

  public HttpdConf(String filePath) {
    this.config = filePath;
    this.httpdMap = new HashMap<String,String>();
    this.aliasMap = new HashMap<String,String>();
    this.scriptAliasMap = new HashMap<String,String>();
    this.load();
  }

  public void load() {
    try {
      this.fileReader   = new FileReader(this.config);
      this.bufferReader = new BufferedReader(this.fileReader);

      while((currentLine = this.bufferReader.readLine()) != null) {

        this.token = new StringTokenizer(currentLine);
        String currentToken = this.token.nextToken();

        if(currentToken.equals("ScriptAlias")) {
          this.key = this.token.nextToken();

          while(this.token.hasMoreTokens()) {
            this.value = this.token.nextToken().replaceAll("^\"|\"$", "");
            scriptAliasMap.put(this.key, this.value);
          }

        } else if(currentToken.equals("Alias")) {
          this.key = this.token.nextToken();

          while(this.token.hasMoreTokens()) {
            this.value = this.token.nextToken().replaceAll("^\"|\"$", "");
            aliasMap.put(this.key, this.value);
          }

        } else {
          this.key = currentToken;
          this.value = this.token.nextToken().replaceAll("^\"|\"$", "");
          httpdMap.put(this.key, this.value);
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public String lookUp(String key, String configType) {
    //Possibly change to switch
    String httpdValue       = this.httpdMap.get(key);
    String scriptAliasValue = this.scriptAliasMap.get(key);
    String aliasValue       = this.aliasMap.get(key);

    if(configType == null) {
      return null;
    }
    if(configType.equalsIgnoreCase("SCRIPT_ALIAS")) {
      return scriptAliasValue;
    }
    if(configType.equalsIgnoreCase("ALIAS")) {
      return aliasValue;
    }
    if(configType.equalsIgnoreCase("HTTPD_CONF")) {
      return httpdValue;
    }

    return null;
  }
}
