package project.annotations;

@ProcessAPI
public interface DataStoreComputeAPI {
  // Stores data(positive integer(s)) as bytes, then returns them as a response
  // with an ID.
  StorageResponse insertRequest(StorageRequest request);

  // Loads the data back using it's ID.
  byte[] loadData(String id);
}
