import java.util.ArrayList;
import java.util.List;

import project.annotations.DataStoreComputeAPI;
import project.annotations.StorageRequest;
import project.annotations.StorageResponse;
import project.annotations.StoreStatus;

public class DataStoreInMemory implements DataStoreComputeAPI {
  private List<byte[]> userInputs = new ArrayList<>(); // Stores raw input data in memory.
  private List<byte[]> results = new ArrayList<>(); // Stores computed results in memory.

  // If request is null failure response returned, otherwise .size() gives index
  // where item will go, data added to list, id created e.g. "input-0",
  // "input-1",etc. return with id and SUCCESS status.
  @Override
  public StorageResponse insertRequest(StorageRequest request) {
    if (request == null || request.getData() == null) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }

    int index = userInputs.size();
    userInputs.add(request.getData());

    return new StorageResponse("input-" + index, StoreStatus.SUCCESS);
  }

  // Remove input prefix from id, convert the number left over to index,
  // .getIndex() to return stored data, if something goes wrong null will be
  // returned.
  @Override
  public byte[] loadData(String id) {
    try {
      int index = Integer.parseInt(id.replace("input-", ""));
      return userInputs.get(index);
    } catch (Exception e) {
      return null;
    }
  }

  // Same as insert request, but stores results in results list.
  @Override
  public StorageResponse insertResult(StorageRequest request) {
    if (request == null || request.getData() == null) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }

    int index = results.size();
    results.add(request.getData());

    return new StorageResponse("result-" + index, StoreStatus.SUCCESS);
  }

  // Works like loadData, but pulls from result.
  @Override
  public byte[] loadResult(String id) {
    try {
      int index = Integer.parseInt(id.replace("result-", ""));
      return results.get(index);
    } catch (Exception e) {
      return null;
    }
  }

}
