package project.annotations;

public class DataStoreComputeAPIImplementation implements DataStoreComputeAPI {
  @Override
  public StorageResponse insertRequest(int input) {
    return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
  }

  @Override
  public int loadData(String id) {
    return 0;
  }

  @Override
  public StorageResponse insertResult(String result) {
    return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
  }

  @Override
  public String loadResult(String id) {
    return null;
  }

}
