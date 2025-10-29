package project.annotations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UserComputeAPIImplementation implements UserComputeAPI {

  private final DataStoreComputeAPI dataStore;
  private final ComputeControllerAPI computeEngine;

  public UserComputeAPIImplementation(DataStoreComputeAPI dataStore, ComputeControllerAPI computeEngine) {
    this.dataStore = dataStore;
    this.computeEngine = computeEngine;

  }

  @Override
  public UserSubResponse submit(UserSubmission submission) { // validation
    if (submission == null || submission.getInput() == null || submission.getInput().getLocation() == null
        || submission.getInput().getLocation().isBlank() || submission.getOutput() == null
        || submission.getOutput().getLocation() == null || submission.getOutput().getLocation().isBlank()) {

      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR); // Shows invalid parameters as error
                                                                               // result
    }

    try { // load input from location.
      List<Integer> inputs = dataStore.loadInputs(submission.getInput().getLocation());
      if (inputs.isEmpty()) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      StringBuilder results = new StringBuilder(); // Compute each and build combined line.
      for (Integer n : inputs) {
        if (n == null || n <= 0) {
          continue; // ignoring negatives
        }
        ComputeResponse result = computeEngine.compute(new ComputeRequest(n));
        if (results.length() > 0) {
          results.append(",");
        }
        results.append(result.getResult());
      }
      if (results.length() == 0) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      StorageResponse storeResp = dataStore.insertResult(results.toString());
      if (storeResp.getStatus() != StoreStatus.SUCCESS || storeResp.getId() == null) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      // Return the storage-assigned result ID so tests can retrieve it
      return new UserSubResponse(storeResp.getId(), SubmissionStatus.SUCCESS);// store
                                                                              // result
                                                                              // string to
                                                                              // user
                                                                              // specified
                                                                              // path.
      // return id as output
      // file path so tests
      // can load if
      // necessary.

    } catch (Exception e) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR); // prevent exception from boundary
                                                                               // crossing.
    }
  }
} // Delimiter isn't validated because the engine does not rely on a specific
  // delimiter format.
