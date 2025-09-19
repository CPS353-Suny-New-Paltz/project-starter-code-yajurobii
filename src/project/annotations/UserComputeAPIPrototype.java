package project.annotations;

public class UserComputeAPIPrototype {
  @NetworkAPIPrototype
  public void prototype(UserComputeAPI choice) {
    //Sample user request.
    UserSubmission sub = new UserSubmission(new InputSource("file", "input.txt"), new OutputSource("stdout"),
        new Delimiter(";", ":"));
    
    // Submits user request to the compute engine.
    choice.submit(sub);
  }
}
