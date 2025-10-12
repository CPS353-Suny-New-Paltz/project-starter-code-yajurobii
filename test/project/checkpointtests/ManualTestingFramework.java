package project.checkpointtests;

import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.DataStoreComputeAPI;
import project.annotations.DataStoreComputeAPIImplementation;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.Delimiter;
import project.annotations.UserSubmission;
import project.annotations.UserSubResponse;

public class ManualTestingFramework {

  public static final String INPUT = "manualTestInput.txt";
  public static final String OUTPUT = "manualTestOutput.txt";

  public static void main(String[] args) {
    DataStoreComputeAPI dataStore = new DataStoreComputeAPIImplementation();
    ComputeControllerAPI computeEngine = new ComputeControllerAPIImplementation();
    UserComputeAPI coordinator = new UserComputeAPIImplementation(dataStore, computeEngine);

    UserSubmission submission = new UserSubmission(new InputSource("file", INPUT), new OutputSource(OUTPUT),
        new Delimiter(",", ":"));

    UserSubResponse result = coordinator.submit(submission);

    System.out.println("Computation has finished.");
    System.out.println("Status: " + result.getStatus());
    System.out.println("Result Id: " + result.getSubId());

  }
}
