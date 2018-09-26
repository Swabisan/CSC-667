
package src.configuration;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Htaccess extends Config {

  private String config;
  private HashMap<String, String> htaccess;
  private BufferedReader bufferReader;
  private FileReader fileReader;
  private String currentLine;

  public Htaccess(String filePath) {
    this.config = filePath;
    this.htaccess = new HashMap<String, String>();
    this.load();
  }

  public void load() {
    try {
      this.fileReader   = new FileReader(this.config);
      this.bufferReader = new BufferedReader(this.fileReader);

      while((currentLine = this.bufferReader.readLine()) != null) {
        parseLine(currentLine);
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public String lookUp(String key, String configType) {
    String value = this.htaccess.get(key);

    if(configType == null) {
      return null;
    }
    if(configType.equalsIgnoreCase("HTACCESS")) {
      return value;
    }

    return null;
  };

  private void parseLine(String line) {
    String[] tokens = line.split(" ");

    if (tokens.length == 2) {
      htaccess.put(tokens[ 0 ], tokens[ 1 ].replace("\"", "").trim());
    }
  }
}
