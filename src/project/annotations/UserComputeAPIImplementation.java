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

    try { // unified path
      List<Integer> nums = dataStore.read(submission.getInput());
      if (nums == null || nums.isEmpty()) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      // Select the Combo delimiter (e.g., "," or ";")
      String combo = ComputeCoordinatorUtil.chooseComboDelimiter(submission.getDelimiters());

      // Compute each integer sequentially using API.
      String join = ComputeCoordinatorUtil.sequentialCompute(nums, combo, computeEngine);
      if (join.isEmpty()) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      // Write result using storage and package the response.
      var res = ComputeCoordinatorUtil.writeOutput(dataStore, submission.getOutput(), join);
      return new UserSubResponse(res.subId, res.status);

    } catch (Exception e) { // No exception crosses the boundary.
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }
  }
}
