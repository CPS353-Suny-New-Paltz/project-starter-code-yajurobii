package project.annotations;

@NetworkAPI
public interface UserComputeAPI {
  //User submits a request to the system. The user designates where input comes from, where output goes, and delimiters  with both. 
  UserSubResponse submit(UserSubmission submission);
}
