package project.annotations;

public class ComputeResponse {
  private String result; // Computed result ( e.g. 2,3,5,7,11,13)

  public ComputeResponse(String result) {
    this.result = result;
  }

  public String getResult() {
    return result;
  }

}
