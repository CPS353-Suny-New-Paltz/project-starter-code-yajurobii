package project.usercompute.grpc;

import io.grpc.stub.StreamObserver;
import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;
import project.rpc.usercompute.UserComputeAPIGrpc;

public class UserComputeGrpcService extends UserComputeAPIGrpc.UserComputeAPIImplBase {

  // Compute implementation.
  private final UserComputeAPI userApi;

  public UserComputeGrpcService(UserComputeAPI userApi) {
    this.userApi = userApi;
  }

  // Map actual enums to proto enums.
  private static project.rpc.usercompute.SubmissionStatus toProto(SubmissionStatus s) {
    switch (s) {
    case SUCCESS:
      return project.rpc.usercompute.SubmissionStatus.SUB_STATUS_SUCCESS;
    case FAILURE_INVALID_INPUT:
      return project.rpc.usercompute.SubmissionStatus.SUB_STATUS_INVALID_INPUT;
    case FAILURE_SYSTEM_ERROR:
    default:
      return project.rpc.usercompute.SubmissionStatus.SUB_STATUS_SYSTEM_ERROR;
    }
  }
  // Handles submit RPC, convert protobuf submission to acttual submission,
  // delagate to actual submit, convert actual subresponse to protobuf
  // subresponse.
  @Override
  public void submit(project.rpc.usercompute.UserSubmission request,
      StreamObserver<project.rpc.usercompute.UserSubResponse> responseObserver) {

    // Convert input source.
    InputSource in = new InputSource(request.getInput().getInputType(), request.getInput().getLocation());

    // Convert output source.
    OutputSource out = new OutputSource(request.getOutput().getOutputType(), request.getOutput().getLocation());

    // Convert delimiters.
    Delimiter del = new Delimiter(request.getDelimiters().getComboDelimiter(),
        request.getDelimiters().getResultDelimiter());

    // Build submission object.
    UserSubmission sub = new UserSubmission(in, out, del);

    // Calls actual compute API.
    UserSubResponse resp = userApi.submit(sub);
    if (resp == null) {
      resp = new UserSubResponse(null, SubmissionStatus.FAILURE_SYSTEM_ERROR);
    }

    // Convert actual response to proto response
    project.rpc.usercompute.UserSubResponse protoResp = project.rpc.usercompute.UserSubResponse.newBuilder()
        .setSubId(resp.getSubId() == null ? "" : resp.getSubId()).setStatus(toProto(resp.getStatus())).build();

    responseObserver.onNext(protoResp);
    responseObserver.onCompleted();
  }
}
