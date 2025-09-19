package project.annotations;

public class UserSubmission {
  private InputSource input; // Where input data is.
  private OutputSource output; // Where output should go.
  private Delimiter delimiters; // Formatting.

  public UserSubmission(InputSource input, OutputSource output, Delimiter delimiters) {
    this.input = input;
    this.output = output;
    this.delimiters = delimiters;
  }

  // Getter methods for compute engine detail access.
  public InputSource getInput() {
    return input;
  }

  public OutputSource getOutput() {
    return output;
  }

  public Delimiter getDelimiters() {
    return delimiters;
  }

}
