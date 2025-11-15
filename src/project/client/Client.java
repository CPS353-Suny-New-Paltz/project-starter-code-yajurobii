package project.client;

import java.util.Scanner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import project.rpc.usercompute.UserComputeAPIGrpc;
import project.rpc.usercompute.UserSubmission;
import project.rpc.usercompute.UserSubResponse;

public class Client {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in); // Scanner reads input from user and sends job to server.

    System.out.println(" USER CLIENT ");
    System.out.print("Enter input type (file/memory): ");
    String inputType = sc.nextLine();

    System.out.print("Enter input location (e.g., input.txt or input-0): ");
    String inputLoc = sc.nextLine();

    System.out.print("Enter output type (file/stdout): ");
    String outputType = sc.nextLine();

    System.out.print("Enter output location (e.g., result.txt): ");
    String outputLoc = sc.nextLine();

    System.out.print("Enter combo delimiter (e.g., ','): ");
    String comboDel = sc.nextLine();

    System.out.print("Enter result delimiter (e.g., ':'): ");
    String resultDel = sc.nextLine();

    // Connects to the compute server.
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

    UserComputeAPIGrpc.UserComputeAPIBlockingStub stub = UserComputeAPIGrpc.newBlockingStub(channel);

    // Protobuf message for UserSubmission.
    UserSubmission request = UserSubmission.newBuilder()
        .setInput(
            project.rpc.usercompute.InputSource.newBuilder().setInputType(inputType).setLocation(inputLoc).build())
        .setOutput(
            project.rpc.usercompute.OutputSource.newBuilder().setOutputType(outputType).setLocation(outputLoc).build())
        .setDelimiters(project.rpc.usercompute.Delimiter.newBuilder().setComboDelimiter(comboDel)
            .setResultDelimiter(resultDel).build())
        .build();

    // Job sent to server.
    UserSubResponse response = stub.submit(request);

    // outcome
    System.out.println(" SERVER RESPONSE ");
    System.out.println("Status: " + response.getStatus());
    System.out.println("Submission ID: " + response.getSubId());

    // Closing connection
    channel.shutdownNow();
    sc.close();

    System.out.println("Client finished.");

  }

}
