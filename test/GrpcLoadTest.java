import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.DataStoreComputeAPI;
import project.annotations.DataStoreComputeAPIImplementation;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.datastore.grpc.DataStoreGrpcService;
import project.datastore.grpc.GrpcDataStoreComputeAPI;
import project.rpc.usercompute.Delimiter;
import project.rpc.usercompute.InputSource;
import project.rpc.usercompute.OutputSource;
import project.rpc.usercompute.SubmissionStatus;
import project.rpc.usercompute.UserComputeAPIGrpc;
import project.rpc.usercompute.UserSubResponse;
import project.rpc.usercompute.UserSubmission;
import project.usercompute.grpc.UserComputeGrpcService;

public class GrpcLoadTest {

  @Test
  public void sustainedLoadTest() throws Exception {

    Server dataStoreServer = null;
    Server computeServer = null;
    ManagedChannel dsChannel = null;
    ManagedChannel computeChannel = null;
    ExecutorService pool = null;

    try {

      DataStoreComputeAPI storeImpl = new DataStoreComputeAPIImplementation();

      dataStoreServer = ServerBuilder.forPort(0).addService(new DataStoreGrpcService(storeImpl)).build().start();

      int dataStorePort = dataStoreServer.getPort();
      System.out.println("GrpcLoadTest -> Datastore server on port: " + dataStorePort);

      dsChannel = ManagedChannelBuilder.forAddress("localhost", dataStorePort).usePlaintext().build();

      DataStoreComputeAPI storeClient = new GrpcDataStoreComputeAPI(dsChannel);

      ComputeControllerAPI core = new ComputeControllerAPIImplementation();
      UserComputeAPI userApi = new UserComputeAPIImplementation(storeClient, core);

      computeServer = ServerBuilder.forPort(0).addService(new UserComputeGrpcService(userApi)).build().start();

      int computePort = computeServer.getPort();
      System.out.println("GrpcLoadTest -> Compute server on port: " + computePort);

      computeChannel = ManagedChannelBuilder.forAddress("localhost", computePort).usePlaintext().build();

      UserComputeAPIGrpc.UserComputeAPIBlockingStub stub = UserComputeAPIGrpc.newBlockingStub(computeChannel);

      // Pool of parallel tasks to stress the system for testing.
      int numRequests = 200;
      int parallelism = 8; // number of clients going at the same time.
      pool = Executors.newFixedThreadPool(parallelism);
      List<Callable<Void>> tasks = new ArrayList<>();

      for (int i = 0; i < numRequests; i++) {
        final int index = i;
        tasks.add(() -> {
          // Varies the input to test the cache and the performance of the compute.
          int n = 16 + (index % 5) * 1000; // 16, 1016, 2016, etc.

          UserSubmission request = UserSubmission.newBuilder()
              .setInput(InputSource.newBuilder().setInputType("memory").setLocation(Integer.toString(n)).build())
              .setOutput(OutputSource.newBuilder().setOutputType("stdout").setLocation("").build())
              .setDelimiters(Delimiter.newBuilder().setComboDelimiter(",").setResultDelimiter(":").build()).build();

          UserSubResponse response = stub.submit(request);

          assertEquals(SubmissionStatus.SUB_STATUS_SUCCESS, response.getStatus(), "Submission succeeds for n = " + n);
          assertNotNull(response.getSubId(), "Submission ID shouldn't be null.");
          assertFalse(response.getSubId().isBlank(), "Submission ID shouldn't be blank.");
          return null;
        });
      }

      long start = System.nanoTime();
      pool.invokeAll(tasks); // runs all the client calls.
      long durationNs = System.nanoTime() - start;
      double durationMs = durationNs / 1_000_000.0;

      System.out.println("GrpcLoadTest -> Completed: " + numRequests + " gRPC submissions in: " + durationMs + " ms");

    } finally {
      // Shut down thread pool, channels and servers.
      if (pool != null) {
        pool.shutdownNow();
      }
      if (computeChannel != null) {
        computeChannel.shutdownNow();
      }
      if (dsChannel != null) {
        dsChannel.shutdownNow();
      }
      if (computeServer != null) {
        computeServer.shutdownNow();
      }
      if (dataStoreServer != null) {
        dataStoreServer.shutdownNow();
      }
    }
  }
}
