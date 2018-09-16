package configuration;

public abstract class Config {
  public abstract void load();
  public abstract String lookUp(String a, String b);
}
