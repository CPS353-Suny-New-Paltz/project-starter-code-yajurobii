package project.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

final class ComputeCoordinatorUtil {
  private ComputeCoordinatorUtil() {

  }

  static String chooseComboDelimiter(Delimiter d) { // Returns safe delimiter String(comma default).
    return (d != null && d.getComboDelimiter() != null && !d.getComboDelimiter().isEmpty()) ? d.getComboDelimiter()
        : ",";
  }

  static String sequentialCompute(List<Integer> nums, String comboSep, ComputeControllerAPI engine) { // Computes each
                                                                                                      // integer's
                                                                                                      // result in an
                                                                                                      // order.
    StringBuilder sb = new StringBuilder();
    for (Integer n : nums) {
      if (n == null || n <= 0) {
        continue;
      }
      ComputeResponse r = engine.compute(new ComputeRequest(n));
      String s = (r == null || r.getResult() == null) ? "" : r.getResult();
      if (sb.length() > 0) {
        sb.append(comboSep);
      }
      sb.append(s);
    }
    return sb.toString();
  }

  static String computeParallel(List<Integer> nums, String comboSep, ComputeControllerAPI engine, ExecutorService pool)
      throws InterruptedException { // runs compute jobs on multiple threads at the same time.
    List<Future<String>> futures = new ArrayList<>(nums.size());
    for (Integer n : nums) {
      futures.add(pool.submit(() -> {
        if (n == null || n <= 0) {
          return "";
        }
        ComputeResponse r = engine.compute(new ComputeRequest(n));
        return (r == null || r.getResult() == null) ? "" : r.getResult();
      }));
    }
    StringBuilder sb = new StringBuilder();
    for (Future<String> f : futures) {
      try {
        String s = f.get();
        if (sb.length() > 0) {
          sb.append(comboSep);
        }
        sb.append(s);
      } catch (ExecutionException ignore) {
        if (sb.length() > 0) {
          sb.append(comboSep);
        }
      }
    }
    return sb.toString();
  }

  static ComputeCoordinatorResult writeOutput(DataStoreComputeAPI store, OutputSource out, String content) {
    // Writes final output to file or stdout.
    String outType = out == null ? "" : out.getOutputType();
    String outLoc = out == null ? "" : out.getLocation();
    try {
      if ("file".equalsIgnoreCase(outType)) {
        if (outLoc == null || outLoc.isBlank()) {
          return new ComputeCoordinatorResult(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
        }

        StoreStatus st = store.writeResult(outLoc, (content == null) ? "" : content);
        return (st == StoreStatus.SUCCESS) ? new ComputeCoordinatorResult(outLoc, SubmissionStatus.SUCCESS)
            : new ComputeCoordinatorResult(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      } else {
        StorageResponse res = store.insertResult(content == null ? "" : content);
        return (res != null && res.getStatus() == StoreStatus.SUCCESS && res.getId() != null)
            ? new ComputeCoordinatorResult(res.getId(), SubmissionStatus.SUCCESS)
            : new ComputeCoordinatorResult(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }
    } catch (Exception e) {
      return new ComputeCoordinatorResult(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }
  }

  static final class ComputeCoordinatorResult { // Small class that wraps subId and status.
    final String subId;
    final SubmissionStatus status;

    ComputeCoordinatorResult(String subId, SubmissionStatus status) {
      this.subId = subId;
      this.status = status;
    }
  }

}
