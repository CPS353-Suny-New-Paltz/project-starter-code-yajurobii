package project.annotations;

public class StorageResponse {
  private String id; // key for retrieving stored data later.
  private StoreStatus status; // status of operation.

  public StorageResponse(String id, StoreStatus status) {
    this.id = id;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public StoreStatus getStatus() {
    return status;
  }

}
