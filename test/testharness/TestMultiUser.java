package testharness;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.DataStoreComputeAPI;
import project.annotations.DataStoreComputeAPIImplementation;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIParallelImplementation;

public class TestMultiUser {

  // TODO 1: change the type of this variable to the name you're using for your
  // @NetworkAPI
  // interface
  private UserComputeAPI coordinator;

  @BeforeEach
  public void initializeComputeEngine() {
    // Initialize components.
    DataStoreComputeAPI store = new DataStoreComputeAPIImplementation();
    ComputeControllerAPI engine = new ComputeControllerAPIImplementation();
    // TODO 2: create an instance of the implementation of your @NetworkAPI; this is
    // the component
    // that the user will make requests to
    // Store it in the 'coordinator' instance variable

    coordinator = new UserComputeAPIParallelImplementation(store, engine);

  }

  @Test
  public void compareMultiAndSingleThreaded() throws Exception {
    int numThreads = 4;
    List<TestUser> testUsers = new ArrayList<>();
    for (int i = 0; i < numThreads; i++) {
      testUsers.add(new TestUser(coordinator));
    }

    // Run single threaded
    String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
    for (int i = 0; i < numThreads; i++) {
      File singleThreadedOut = new File(singleThreadFilePrefix + i);
      singleThreadedOut.deleteOnExit();
      testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
    }

    // Run multi threaded
    ExecutorService threadPool = Executors.newCachedThreadPool();
    List<Future<?>> results = new ArrayList<>();
    String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
    for (int i = 0; i < numThreads; i++) {
      File multiThreadedOut = new File(multiThreadFilePrefix + i);
      multiThreadedOut.deleteOnExit();
      String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
      TestUser testUser = testUsers.get(i);
      results.add(threadPool.submit(() -> testUser.run(multiThreadOutputPath)));
    }

    results.forEach(future -> {
      try {
        future.get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Check that the output is the same for multi-threaded and single-threaded
    List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, numThreads);
    List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, numThreads);
    Assert.assertEquals(singleThreaded, multiThreaded);
  }

  private List<String> loadAllOutput(String prefix, int nThreads) throws IOException {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < nThreads; i++) {
      File multiThreadedOut = new File(prefix + i);
      result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
    }
    return result;
  }
}
