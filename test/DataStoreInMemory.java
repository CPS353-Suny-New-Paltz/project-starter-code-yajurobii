import java.util.ArrayList;
import java.util.List;

import project.annotations.DataStoreComputeAPI;
import project.annotations.StorageResponse;
import project.annotations.StoreStatus;

public class DataStoreInMemory implements DataStoreComputeAPI {
  private List<Integer> inputs = new ArrayList<>(); // Stores raw input data in memory.
  private List<String> results = new ArrayList<>(); // Stores computed results in memory.

  // If request is null failure response returned, otherwise .size() gives index
  // where item will go, data added to list, id created e.g. "input-0",
  // "input-1",etc. return with id and SUCCESS status.
  @Override
  public StorageResponse insertRequest(int input) {
    inputs.add(input);
    String id = "input-" + (inputs.size() - 1);
    return new StorageResponse(id, StoreStatus.SUCCESS);
    }

    
  // Remove input prefix from id, convert the number left over to index,
  // .getIndex() to return stored data, if something goes wrong null will be
  // returned.
  @Override
  public int loadData(String id) {
    try {
      int index = Integer.parseInt(id.replace("input-", ""));
      return inputs.get(index);
    } catch (Exception e) {
      return 0;
    }
  }

  // Same as insert request, but stores results in results list.
  @Override
  public StorageResponse insertResult(String result) {
    results.add(result);
    String id = "result-" + (results.size() - 1);
    return new StorageResponse(id, StoreStatus.SUCCESS);
    }

    

  // Works like loadData, but pulls from result.
  @Override
  public String loadResult(String id) {
    try {
      int index = Integer.parseInt(id.replace("result-", ""));
      return results.get(index);
    } catch (Exception e) {
      return null;
    }
  }

}
