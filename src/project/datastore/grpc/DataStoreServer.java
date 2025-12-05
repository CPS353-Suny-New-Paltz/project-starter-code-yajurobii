package project.datastore.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import project.annotations.DataStoreComputeAPI;
import project.annotations.DataStoreComputeAPIImplementation;


public class DataStoreServer {

  public static void main(String[] args) throws Exception {

    DataStoreComputeAPI store = new DataStoreComputeAPIImplementation();


    Server server = ServerBuilder.forPort(50052).addService(new DataStoreGrpcService(store)).build().start();

    System.out.println("Datastore server started on port 50052");
    server.awaitTermination();
  }
}