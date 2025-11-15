package project.usercompute.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.DataStoreComputeAPI;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.datastore.grpc.GrpcDataStoreComputeAPI;

public class ComputeServer {

  public static void main(String[] args) throws Exception {

    // gRPC channel to the datastore server process.
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();

    DataStoreComputeAPI store = new GrpcDataStoreComputeAPI(channel);

    // compute engine (prime numbers up to N).
    ComputeControllerAPI core = new ComputeControllerAPIImplementation();

    UserComputeAPI userApi = new UserComputeAPIImplementation(store, core);

    // UserComputeAPI over gRPC on 50051.
    Server server = ServerBuilder.forPort(50051).addService(new UserComputeGrpcService(userApi)).build().start();

    System.out.println("Compute server started on port 50051");

    // Keep running until process is killed.
    server.awaitTermination();

    // Close datastore channel when shutdown.
    channel.shutdownNow();
  }
}
