package project.annotations;

@ProcessAPI
public interface DataStoreComputeAPI {
  // Stores data(positive integer(s)) as bytes, then returns them as a response
  // with an ID.
  StorageResponse insertRequest(StorageRequest request);

  // Loads the data back using it's ID.
  byte[] loadData(String id);

  // Separate method for loading computation result.
  StorageResponse insertResult(StorageRequest request);

  // Loads result back using ID.
  byte[] loadResult(String id);
}
