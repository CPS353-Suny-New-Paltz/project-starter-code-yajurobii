package project.annotations;

public class DataStoreComputeAPIImplementation implements DataStoreComputeAPI {
  @Override
  public StorageResponse insertRequest(StorageRequest request) {
    return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
  }

  @Override
  public byte[] loadData(String id) {
    return null;
  }

  @Override
  public StorageResponse insertResult(StorageRequest request) {
    return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
  }

  @Override
  public byte[] loadResult(String id) {
    return null;
  }

}
