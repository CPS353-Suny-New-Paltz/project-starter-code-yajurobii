package project.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserComputeAPIImplementation implements UserComputeAPI {

  private final DataStoreComputeAPI dataStore;
  private final ComputeControllerAPI computeEngine;

  public UserComputeAPIImplementation(DataStoreComputeAPI dataStore, ComputeControllerAPI computeEngine) {
    this.dataStore = Objects.requireNonNull(dataStore);
    this.computeEngine = Objects.requireNonNull(computeEngine);
  }

  @Override
  public UserSubResponse submit(UserSubmission submission) {
    // Basic validation
    if (submission == null || submission.getInput() == null || submission.getOutput() == null) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }

    final String inType = safe(submission.getInput().getInputType());
    final String inLoc = safe(submission.getInput().getLocation());
    final String outType = safe(submission.getOutput().getOutputType());
    final String outLoc = safe(submission.getOutput().getLocation());

    // Input needs a location
    if (inLoc.isEmpty()) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }

    try {
      // Load inputs
      List<Integer> nums = new ArrayList<>();
      if ("memory".equalsIgnoreCase(inType)) {
        // Single integer by id (e.g., "input-0")
        int n = dataStore.loadData(inLoc);
        if (n > 0) {
          nums.add(n);
        }
      } else {
        // File/path can give multiple ints
        nums = dataStore.loadInputs(inLoc);
      }

      if (nums == null || nums.isEmpty()) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      // Compute per n and join with combo delimiter
      String combo = ",";
      if (submission.getDelimiters() != null && submission.getDelimiters().getComboDelimiter() != null
          && !submission.getDelimiters().getComboDelimiter().isEmpty()) {
        combo = submission.getDelimiters().getComboDelimiter();
      }

      StringBuilder results = new StringBuilder();
      for (Integer n : nums) {
        if (n == null || n <= 0) {
          continue;
        }
        ComputeResponse r = computeEngine.compute(new ComputeRequest(n));
        String s = (r == null || r.getResult() == null) ? "" : r.getResult();
        if (results.length() > 0)
          results.append(combo);
        results.append(s);
      }

      if (results.length() == 0) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      // Store output according to output type
      if ("stdout".equalsIgnoreCase(outType) || outType.isEmpty()) {
        // Put into result and return that id so tests can loadResult(id)
        StorageResponse res = dataStore.insertResult(results.toString());
        if (res == null || res.getStatus() != StoreStatus.SUCCESS || res.getId() == null) {
          return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
        }
        return new UserSubResponse(res.getId(), SubmissionStatus.SUCCESS);
      } else if ("file".equalsIgnoreCase(outType)) {
        if (outLoc.isEmpty()) {
          return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
        }
        StoreStatus st = dataStore.writeResult(outLoc, results.toString());
        if (st != StoreStatus.SUCCESS) {
          return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
        }
        // Use output path as the subId
        return new UserSubResponse(outLoc, SubmissionStatus.SUCCESS);
      } else {
        // Fallback
        StorageResponse res = dataStore.insertResult(results.toString());
        if (res == null || res.getStatus() != StoreStatus.SUCCESS || res.getId() == null) {
          return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
        }
        return new UserSubResponse(res.getId(), SubmissionStatus.SUCCESS);
      }

    } catch (Exception e) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }
  }

  private static String safe(String s) {
    return s == null ? "" : s;
  }
}
