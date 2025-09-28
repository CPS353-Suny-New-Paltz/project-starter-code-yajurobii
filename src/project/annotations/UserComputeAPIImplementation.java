package project.annotations;

public class UserComputeAPIImplementation implements UserComputeAPI {

  @Override
  public UserSubResponse submit(UserSubmission submission) {
    return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);

  }

}
