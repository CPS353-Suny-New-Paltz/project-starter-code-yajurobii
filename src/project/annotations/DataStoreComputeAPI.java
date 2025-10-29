package project.annotations;

import java.util.List;

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

  // Loads all integers from user specified input.
  List<Integer> loadInputs(String inputPath);

  // Writes final output string to user specified output.
  StoreStatus writeResult(String outputPath, String content);

}
