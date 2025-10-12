package project.annotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataStoreComputeAPIImplementation implements DataStoreComputeAPI {
  // Two files where data is stored and received.
  private static final String INPUT_FILE = "manualTestInput.txt";
  private static final String RESULT_FILE = "manualTestOutput.txt";

  @Override
  public StorageResponse insertRequest(int input) {// Converts integer to string then writes string to file imput.txt
    try {
      Files.writeString(Paths.get(INPUT_FILE), String.valueOf(input));
      return new StorageResponse(INPUT_FILE, StoreStatus.SUCCESS);
    } catch (IOException e) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }
  }

  @Override
  public int loadData(String id) {// method takes file name as id, reads into string, trims and converts to
                                  // integer.
    try {
      String content = Files.readString(Paths.get(id)).trim();
      return Integer.parseInt(content);
    } catch (IOException | NumberFormatException e) {
      return 0;
    }
  }

  @Override
  public StorageResponse insertResult(String result) {// Computed result string written to result.txt.
    try {
      Files.writeString(Paths.get(RESULT_FILE), result);
      return new StorageResponse(RESULT_FILE, StoreStatus.SUCCESS);
    } catch (IOException e) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }
  }

  @Override
  public String loadResult(String id) { // Reads file named by id, returns as trimmed string.
    try {
      return Files.readString(Paths.get(id)).trim();
    } catch (IOException e) {
      return null;
    }
  }
}
