package project.annotations;

public class UserComputeAPIImplementation implements UserComputeAPI {

  private final DataStoreComputeAPI dataStore;
  private final ComputeControllerAPI computeEngine;

  public UserComputeAPIImplementation(DataStoreComputeAPI dataStore, ComputeControllerAPI computeEngine) {
    this.dataStore = dataStore;
    this.computeEngine = computeEngine;

  }

  @Override
  public UserSubResponse submit(UserSubmission submission) {
    try {
      int n = dataStore.loadData(submission.getInput().getLocation());
      ComputeResponse result = computeEngine.compute(new ComputeRequest(n));
      StorageResponse resResp = dataStore.insertResult(result.getResult());
      return new UserSubResponse(resResp.getId(), SubmissionStatus.SUCCESS);
    } catch (Exception e) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }
  }
}
