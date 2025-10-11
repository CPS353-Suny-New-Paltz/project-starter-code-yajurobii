package project.annotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataStoreComputeAPIImplementation implements DataStoreComputeAPI {

  private static final String INPUT_FILE = "input.txt";
  private static final String RESULT_FILE = "result.txt";

  @Override
  public StorageResponse insertRequest(int input) {
    try {
      Files.writeString(Paths.get(INPUT_FILE), String.valueOf(input));
      return new StorageResponse(INPUT_FILE, StoreStatus.SUCCESS);
    } catch (IOException e) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }
  }

  @Override
  public int loadData(String id) {
    try {
      String content = Files.readString(Paths.get(id)).trim();
      return Integer.parseInt(content);
    } catch (IOException | NumberFormatException e) {
      return 0;
    }
  }

  @Override
  public StorageResponse insertResult(String result) {
    try {
      Files.writeString(Paths.get(RESULT_FILE), result);
      return new StorageResponse(RESULT_FILE, StoreStatus.SUCCESS);
    } catch (IOException e) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }
  }

  @Override
  public String loadResult(String id) {
    try {
      return Files.readString(Paths.get(id)).trim();
    } catch (IOException e) {
      return null;
    }
  }
}
