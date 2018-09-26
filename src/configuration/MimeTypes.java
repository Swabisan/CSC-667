
package src.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MimeTypes extends Config {

  private String config;
  private HashMap<String,String> mimeTypeMap;
  private BufferedReader bufferReader;
  private FileReader fileReader;
  private StringTokenizer token;
  private String currentLine;
  private String key;
  private String value;

  public MimeTypes(String filePath) {
    this.config = filePath;
    this.mimeTypeMap = new HashMap<String, String>();
    this.load();
  }

  public void load() {
    try {
      this.fileReader   = new FileReader(config);
      this.bufferReader = new BufferedReader(fileReader);

      while((currentLine = this.bufferReader.readLine()) != null) {
        this.token = new StringTokenizer(currentLine);

        if(this.token.countTokens() > 1) {
          this.value = this.token.nextToken();

          while(this.token.hasMoreTokens()) {
            this.key = this.token.nextToken();
            mimeTypeMap.put(this.key,this.value);
          }
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public String lookUp(String mimeType, String type) {
    String defaultMimeType = "text/text";

    if(type != "MIME_TYPE") {
      throw new IllegalArgumentException("Lookup type parameter must be 'MIME_TYPE'");
    }

    if(mimeTypeMap.get(mimeType) == null) {
      return defaultMimeType;
    }

    return mimeTypeMap.get(mimeType);
  }
}
