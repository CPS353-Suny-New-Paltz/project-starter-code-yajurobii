package testharness;

import java.io.File;
import java.nio.file.Files;

import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;

public class TestUser {

  // TODO 3: change the type of this variable to the name you're using for your
  // @NetworkAPI interface; also update the parameter passed to the constructor
  private final UserComputeAPI coordinator;

  public TestUser(UserComputeAPI coordinator) {
    this.coordinator = coordinator;
  }

  public void run(String outputPath) {
    char delimiter = ';';
    String inputPath = "test" + File.separatorChar + "testInputFile.test";

    // TODO 4: Call the appropriate method(s) on the coordinator to get it to
    // run the compute job specified by inputPath, outputPath, and delimiter
    UserSubmission sub = new UserSubmission(new InputSource("file", inputPath), new OutputSource("file", outputPath),
        new Delimiter(String.valueOf(delimiter), ":"));

    UserSubResponse resp = coordinator.submit(sub);
    if (resp != null && resp.getStatus() == SubmissionStatus.SUCCESS) {
      try {
        String out = Files.readString(new File(outputPath).toPath());
        System.out.println("Thread output: " + out);
      } catch (Exception e) {
        throw new RuntimeException("Could not read output file: " + outputPath, e);
      }
    } else {
      throw new RuntimeException("Submission failed for output: " + outputPath);
    }
  }

}
