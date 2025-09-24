package project.annotations;

public class UserSubResponse {
  private String subId; // unique id, null on failure
  private SubmissionStatus status; // status of user submission

  public UserSubResponse(String subId, SubmissionStatus status) {
    this.subId = subId;
    this.status = status;
  }

  public String getSubId() {
    return subId;
  }

  public SubmissionStatus getStatus() {
    return status;
  }

}
