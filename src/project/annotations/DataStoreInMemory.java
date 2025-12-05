package project.annotations;
import java.util.ArrayList;
import java.util.List;

public class DataStoreInMemory implements DataStoreComputeAPI {
  private List<Integer> inputs = new ArrayList<>(); // Stores raw input data in memory.
  private List<String> results = new ArrayList<>(); // Stores computed results in memory.

  @Override
  public List<Integer> read(InputSource src) {
    List<Integer> list = new ArrayList<>();
    if (src == null) {
      return list;
    }

    String type = src.getInputType() == null ? "" : src.getInputType();
    String loc = src.getLocation() == null ? "" : src.getLocation();
    if (loc.isEmpty()) {
      return list;
    }

    if ("memory".equalsIgnoreCase(type) || loc.startsWith("input-")) {
      int value = loadData(loc);
      if (value > 0) {
        list.add(value);
      }
      return list;
    }
    if (loc.startsWith("mem:")) {
      String raw = loc.substring("mem:".length()).trim().replace(",", " ");
      for (String token : raw.split("\\s+")) {
        try {
          list.add(Integer.parseInt(token));
        } catch (NumberFormatException ignore) {
          System.out.print("Warning!");
        }
      }
      return list;
    }
    return list;
  }

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

  @Override
  public List<Integer> loadInputs(String inputPath) {
    // Move to unified read.
    return read(new InputSource("memory", inputPath));

  }

  @Override
  public StoreStatus writeResult(String outputPath, String content) {
    if (outputPath == null || outputPath.isBlank()) {
      return StoreStatus.FAILURE_WRITE_ERROR;
    }
    if (content == null) {
      content = "";
    }

    try {
      results.add(content);
      return StoreStatus.SUCCESS;
    } catch (Exception e) {
      return StoreStatus.FAILURE_WRITE_ERROR;
    }
  }
}
