package project.annotations;

public class ComputeRequest {
  private int input; // The number that is going to be processed (like 16 in this case).

  public ComputeRequest(int input) {
    this.input = input;
  }

  public int getInput() {
    return input;
  }

}
