package project.annotations;

public class OutputSource {
  private String outputtype; // e.g., "standard output" , "file" , "database"
  private String location; // e.g., "console"

  public OutputSource(String outputtype) {
    this(outputtype, "");
  }

  public OutputSource(String outputtype, String location) {
    this.outputtype = outputtype;
    this.location = location;
  }

  public String getOutputType() {
    return outputtype;
  }

  public String getLocation() {
    return location;
  }

}
