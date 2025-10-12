package project.checkpointtests;

import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;

import java.nio.file.Files;
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
import project.annotations.Delimiter;
import project.annotations.UserSubmission;

public class ManualTestingFramework {

  public static final String INPUT = "manualTestInput.txt";
  public static final String OUTPUT = "manualTestOutput.txt";

  public static void main(String[] args) {
    DataStoreComputeAPI dataStore = new DataStoreComputeAPIImplementation();
    ComputeControllerAPI computeEngine = new ComputeControllerAPIImplementation();
    UserComputeAPI coordinator = new UserComputeAPIImplementation(dataStore, computeEngine);

    try {
      List<String> inputs = Files.readAllLines(Paths.get(INPUT));
      StringBuilder allResults = new StringBuilder();

      for (String line : inputs) {
        int n = Integer.parseInt(line.trim());

        UserSubmission sub = new UserSubmission(new InputSource("file", INPUT), new OutputSource(OUTPUT),
            new Delimiter(",", ":"));

        dataStore.insertRequest(n);

        ComputeResponse result = computeEngine.compute(new ComputeRequest(n));
        allResults.append(result.getResult()).append(",");
      }

      if (allResults.length() > 0) {
        allResults.setLength(allResults.length() - 1);

        Files.writeString(Paths.get(OUTPUT), allResults.toString());
        System.out.println("Computation done, results written to " + OUTPUT);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
