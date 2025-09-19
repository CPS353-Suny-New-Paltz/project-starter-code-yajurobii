package project.annotations;

public class InputSource {
  private String inputtype; // e.g., "file", "database", "standard input"
  private String location;

  public InputSource(String inputtype, String location) {
    this.inputtype = inputtype;
    this.location = location;
  }

  public String getInputType() {
    return inputtype;
  }

  public String getLocation() {
    return location;
  }
}
