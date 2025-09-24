package project.annotations;

public class UserComputeAPIPrototype {
  @NetworkAPIPrototype
  public void prototype(UserComputeAPI choice) {
    // Sample user request.
    UserSubmission sub = new UserSubmission(new InputSource("file", "input.txt"), new OutputSource("stdout"),
        new Delimiter(";", ":"));

    // Submits user request to the compute engine.
    UserSubResponse resp = choice.submit(sub);

    // Gives confirmation of successful submission or in the event of failure
    // returns status.
    if (resp != null && resp.getStatus() == SubmissionStatus.SUCCESS) {
      System.out.println("Job submitted: " + resp.getSubId());
    } else {
      System.out.println("Job failed: " + resp.getStatus());
    }
  }
}
