import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import project.usercompute.grpc.UserComputeGrpcService;
import project.rpc.usercompute.UserComputeAPIGrpc;

public class EndToEndGrpcTest {

  @Test
  public void testMemoryInputEndToEnd() throws Exception {
    
    // Creating servers on temporary ports(pot = 0).
    Server dataStoreServer = null;
    Server computeServer = null;
    ManagedChannel dsChannel = null;
    ManagedChannel computeChannel = null;
    
    try {
      // Start datastore server over gRPC.
      DataStoreComputeAPI storeImpl = new DataStoreComputeAPIImplementation();
      // pick any available port
      dataStoreServer = ServerBuilder.forPort(0).addService(new DataStoreGrpcService(storeImpl)).build().start();
      
      int dataStorePort = dataStoreServer.getPort();
      System.out.println("Datastore test server started on port " + dataStorePort);
      
      // Create gRPC client adapter for dataqstore to be used by compute server.
      dsChannel = ManagedChannelBuilder.forAddress("localhost", dataStorePort).usePlaintext().build();
      
      DataStoreComputeAPI storeClient = new GrpcDataStoreComputeAPI(dsChannel);
      
      // Wire compute server with compute and datastore over gRPC.
      ComputeControllerAPI core = new ComputeControllerAPIImplementation();
      UserComputeAPI userApi = new UserComputeAPIImplementation(storeClient, core);
      
      computeServer = ServerBuilder.forPort(0).addService(new UserComputeGrpcService(userApi)).build().start();
      
      int computePort = computeServer.getPort();
      System.out.println("Compute test server started on port " + computePort);

      // Create gRPC client stub to compute server.
      computeChannel = ManagedChannelBuilder.forAddress("localhost", computePort).usePlaintext().build();    
      
      UserComputeAPIGrpc.UserComputeAPIBlockingStub stub =
          UserComputeAPIGrpc.newBlockingStub(computeChannel);
      
      // Build UserSubmission using same fields as client.
      project.rpc.usercompute.UserSubmission request = project.rpc.usercompute.UserSubmission.newBuilder()
          .setInput(
              project.rpc.usercompute.InputSource.newBuilder()
                  .setInputType("memory")
                  .setLocation("16")
                  .build())
          .setOutput(
              project.rpc.usercompute.OutputSource.newBuilder()
                  .setOutputType("stdout")
                  .setLocation("") 
                  .build())
          .setDelimiters(
              project.rpc.usercompute.Delimiter.newBuilder()
                  .setComboDelimiter(",")
                  .setResultDelimiter(":")
                  .build())
          .build();
      
      // Send request over gRPC and get response
      project.rpc.usercompute.UserSubResponse response = stub.submit(request);
      
      System.out.println("EndToEndGrpcTest -> Status: " + response.getStatus()+ ", subId: " + response.getSubId());
      
      // Assertions to make sure pipeline succeeds.
      assertEquals(project.rpc.usercompute.SubmissionStatus.SUB_STATUS_SUCCESS, response.getStatus(), "End-to-end gRPC submission succeeds.");
      
      assertNotNull(response.getSubId(), "Submission ID shouldn't be null.");
      assertFalse(response.getSubId().isBlank(), "Submission ID shouldn't be blank.");
      
      // Verify the result is stored by datastore.
      String stored = storeImpl.loadResult(response.getSubId());
      assertNotNull(stored, "Stored result shouldn't be null");
      
      assertEquals("2,3,5,7,11,13", stored);
        
    } finally {
      // Shutdown channels and servers.
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
