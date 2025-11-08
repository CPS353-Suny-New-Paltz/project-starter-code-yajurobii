package project.annotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataStoreComputeAPIImplementation implements DataStoreComputeAPI {

  @Override
  public List<Integer> read(InputSource src) {
    List<Integer> nums = new ArrayList<>();
    if (src == null) {
      return nums;
    }

    String type = safe(src.getInputType());
    String loc = safe(src.getLocation());
    if (loc.isEmpty()) {
      return nums;
    }

    try {
      if ("memory".equalsIgnoreCase(type) || loc.startsWith("input-")) {
        int n = loadData(loc);
        if (n > 0) {
          nums.add(n);
        }
        return nums;
      }

      if (!Files.exists(Paths.get(loc))) {
        return nums;
      }

      String raw = Files.readString(Paths.get(loc)).trim();
      if (raw.isEmpty()) {
        return nums;
      }

      for (String token : raw.split("[\\s,]+")) {
        try {
          nums.add(Integer.parseInt(token));
        } catch (NumberFormatException ignore) {
          System.out.println("[warn] Skipped malformed token: " + token);
        }
      }
      return nums;
    } catch (Exception e) {
      return nums;
    }
  }

  private static String safe(String s) {
    return s == null ? "" : s;
  }

  @Override
  public StorageResponse insertRequest(int input) {// Converts integer to string then writes string to file imput.txt
    try {
      Files.writeString(Paths.get("manualTestInput.txt"), String.valueOf(input));
      return new StorageResponse("manualTestInput.txt", StoreStatus.SUCCESS);
    } catch (Exception e) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }
  }

  @Override
  public int loadData(String id) {// method takes file name as id, reads into string, trims and converts to
                                  // integer.
    if (id == null || id.isBlank()) {
      return 0;
    }
    try {
      if (!Files.exists(Paths.get(id))) {
        return 0;
      }
      String content = Files.readString(Paths.get(id)).trim();
      return Integer.parseInt(content);
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public StorageResponse insertResult(String result) {// Computed result string written to result.txt.
    if (result == null) {
      result = "";
    }
    try {
      Files.writeString(Paths.get("manualTestOutput.txt"), result == null ? "" : result);
      return new StorageResponse("manualTestOutput.txt", StoreStatus.SUCCESS);
    } catch (Exception e) {
      return new StorageResponse(null, StoreStatus.FAILURE_WRITE_ERROR);
    }
  }

  @Override
  public String loadResult(String id) { // Reads file named by id, returns as trimmed string.
    if (id == null || id.isBlank()) {
      return null;
    }
    try {
      return Files.readString(Paths.get(id)).trim();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Integer> loadInputs(String inputPath) {
    List<Integer> nums = new ArrayList<>();
    if (inputPath == null || inputPath.isBlank()) {
      return nums;
    }
    try {
      String raw = Files.readString(Paths.get(inputPath)).trim();
      if (raw.isEmpty()) {
        return nums;
      }
      for (String token : raw.split("\\s+")) {
        try {
          nums.add(Integer.parseInt(token));
        } catch (NumberFormatException ignore) {
          System.out.print("Warning: malformed token.");
        }
      }
      return nums;
    } catch (Exception e) {
      return nums;
    }
  }

  @Override
  public StoreStatus writeResult(String outputPath, String content) {
    if (outputPath == null || outputPath.isBlank()) {
      return StoreStatus.FAILURE_WRITE_ERROR;
    }
    try {
      Files.writeString(Paths.get(outputPath), content == null ? "" : content);
      return StoreStatus.SUCCESS;
    } catch (Exception e) {
      return StoreStatus.FAILURE_WRITE_ERROR;
    }
  }
} // no validation on int range for insert request because all ints are ,
  // coordinator decides what's accepted.
