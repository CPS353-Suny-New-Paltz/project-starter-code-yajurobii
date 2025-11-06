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

  public static void main(String[] args) throws Exception {
    // Wire layers
    DataStoreComputeAPI dataStore = new DataStoreComputeAPIImplementation();
    ComputeControllerAPI computeEngine = new ComputeControllerAPIImplementation();
    UserComputeAPI coordinator = new UserComputeAPIImplementation(dataStore, computeEngine);

    UserSubmission sub = new UserSubmission(new InputSource("file", INPUT), new OutputSource("file", OUTPUT),
        new Delimiter(";", ":"));

    UserSubResponse resp = coordinator.submit(sub);
    System.out.println("Status: " + (resp == null ? "null" : resp.getStatus()));
    System.out.println("SubId : " + (resp == null ? "null" : resp.getSubId()));

  }
}
