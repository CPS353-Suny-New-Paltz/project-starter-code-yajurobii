package project.annotations;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserComputeAPIParallelImplementation implements UserComputeAPI, AutoCloseable {

  private final DataStoreComputeAPI dataStore;
  private final ComputeControllerAPI computeEngine;
  private final ExecutorService pool;

  public static int defaultThreadCap() {
    int cores = Math.max(1, Runtime.getRuntime().availableProcessors());
    return Math.min(cores, 8); // upper bound: 8, prevents oversubscription.
  }

  public UserComputeAPIParallelImplementation(DataStoreComputeAPI dataStore, ComputeControllerAPI computeEngine) {
    this(dataStore, computeEngine, defaultThreadCap());
  }

  public UserComputeAPIParallelImplementation(DataStoreComputeAPI dataStore, ComputeControllerAPI computeEngine,
      int threadCap) {
    this.dataStore = Objects.requireNonNull(dataStore);
    this.computeEngine = Objects.requireNonNull(computeEngine);
    this.pool = Executors.newFixedThreadPool(Math.max(1, threadCap));
  }

  @Override
  public UserSubResponse submit(UserSubmission submission) {
    // Basic validation
    if (submission == null || submission.getInput() == null || submission.getOutput() == null) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }

    try { // unified path
      List<Integer> nums = dataStore.read(submission.getInput());
      if (nums == null || nums.isEmpty()) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      String combo = ComputeCoordinatorUtil.chooseComboDelimiter(submission.getDelimiters());

      // Computations are performed in parallel using thread pool.
      String join = ComputeCoordinatorUtil.computeParallel(nums, combo, computeEngine, pool);
      if (join.isEmpty()) {
        return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
      }

      var res = ComputeCoordinatorUtil.writeOutput(dataStore, submission.getOutput(), join);
      return new UserSubResponse(res.subId, res.status);

    } catch (InterruptedException ie) {
      // Restore the interrupt flag if the thread was interrupted.
      Thread.currentThread().interrupt();
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    } catch (Exception e) {
      return new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }
  }

  // Clean thread pool shutdown.
  @Override
  public void close() {
    pool.shutdown();
  }

}
