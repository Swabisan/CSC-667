import java.util.*;
import java.util.Properties;

class ConfigurationReader {
  private Properties configFile;
  private ClassLoader classLoader = getClass().getClassLoader();

  public ConfigurationReader(String config) {
	  this.configFile = new Properties();

    try {
  	  this.configFile.load(classLoader.getResourceAsStream(config));
  	}catch(Exception e){
  	  e.printStackTrace();
    }
  }

  public String getProperty(String key) {
  	String value = this.configFile.getProperty(key);
  	return value;
  }

  public Set<String> getProps() {
    return this.configFile.stringPropertyNames();
  }

  public static void main(String[] args) {
    //Do configuration reader stuff
    ConfigurationReader test = new ConfigurationReader("conf/mime.types");
    System.out.println(test.getProperty("application/andrew-inset"));
    System.out.println(test.getProps());
  }
}
