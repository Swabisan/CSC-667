package configuration;

public interface Config {
  public void load();
  public String lookUp(String a, String b);
}
