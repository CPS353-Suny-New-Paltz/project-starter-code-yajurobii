package project.annotations;

@ProcessAPI
public interface DataStoreComputeAPI {
  // Stores data(positive integer(s)) as bytes, then returns them as a response
  // with an ID.
  StorageResponse insertRequest(int input);

  // Loads the data back using it's ID.
  int loadData(String id);

  // Separate method for loading computation result.
  StorageResponse insertResult(String result);

  // Loads result back using ID.
  String loadResult(String id);

}
