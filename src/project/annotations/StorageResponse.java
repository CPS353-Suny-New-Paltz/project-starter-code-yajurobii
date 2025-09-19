package project.annotations;

public class StorageResponse {
  private String id; // key for retrieving stored data later.

  public StorageResponse(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

}
