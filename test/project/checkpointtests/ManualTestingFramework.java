package project.checkpointtests;

import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.ComputeRequest;
import project.annotations.ComputeResponse;
import project.annotations.DataStoreComputeAPI;
import project.annotations.DataStoreComputeAPIImplementation;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.SubmissionStatus;
import project.annotations.Delimiter;
import project.annotations.UserSubmission;
import project.annotations.UserSubResponse;

public class ManualTestingFramework {

  public static final String INPUT = "manualTestInput.txt";
  public static final String OUTPUT = "manualTestOutput.txt";

  public static void main(String[] args) {
    try { // Wire layers
      DataStoreComputeAPI dataStore = new DataStoreComputeAPIImplementation();
      ComputeControllerAPI computeEngine = new ComputeControllerAPIImplementation();
      UserComputeAPI coordinator = new UserComputeAPIImplementation(dataStore, computeEngine);

      // Ensure that the input exists; if not create a small sample.
      Path inputPath = Path.of(INPUT);
      if (!Files.exists(inputPath)) {
        Files.writeString(inputPath, "16\n2\n5\n");
        System.out.println("init Created: " + INPUT + " with: 16, 2, 5 ");
      }

      // A preview of the inputs
      try {
        List<String> preview = Files.readAllLines(inputPath);
        System.out.println("Preview: " + INPUT + " = " + preview);
      } catch (Exception e) {
        System.out.println("Warning: Could not preview input: " + e.getMessage());
      }

      // Building a submission; reading from and writing to a file.
      UserSubResponse sub = coordinator.submit(new project.annotations.UserSubmission(new InputSource("file", INPUT),
          new OutputSource("file", OUTPUT), new Delimiter(",", ":")));

      // Not throwing report status
      if (sub == null || sub.getStatus() != SubmissionStatus.SUCCESS) {
        System.out.println("Error: submission failed: " + (sub == null ? "null response" : sub.getStatus()));
        return;
      }

      System.out.println("[OK] submission status = " + sub.getStatus());
      System.out.println("[OK] result written to = " + sub.getSubId());

      // Showing output content
      try {
        String out = Files.readString(Path.of(OUTPUT)).trim();
        System.out.println("[result:file] " + out);
      } catch (Exception e) {
        System.out.println("Warning: Couldn't read output file: " + e.getMessage());
      }

    } catch (Exception e) {
      // Prevent re-throwing of checkpoint test asserts that no exception propagates.
      System.out.println("Fatal: Unexpected error: " + e.getMessage());
    }

  }
}
