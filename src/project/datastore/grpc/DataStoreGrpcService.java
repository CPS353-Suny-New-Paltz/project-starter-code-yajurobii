package project.datastore.grpc;

import java.util.ArrayList;
import java.util.List;

import io.grpc.stub.StreamObserver;
import project.annotations.DataStoreComputeAPI;
import project.annotations.InputSource;
import project.annotations.StorageResponse;
import project.annotations.StoreStatus;
import project.rpc.datastore.DataStoreAPIGrpc;
import project.rpc.datastore.InsertRequest;
import project.rpc.datastore.InsertResponse;
import project.rpc.datastore.InsertResultRequest;
import project.rpc.datastore.InsertResultResponse;
import project.rpc.datastore.LoadDataRequest;
import project.rpc.datastore.LoadDataResponse;
import project.rpc.datastore.LoadInputsRequest;
import project.rpc.datastore.LoadInputsResponse;
import project.rpc.datastore.LoadResultRequest;
import project.rpc.datastore.LoadResultResponse;
import project.rpc.datastore.ReadRequest;
import project.rpc.datastore.ReadResponse;
import project.rpc.datastore.WriteResultRequest;
import project.rpc.datastore.WriteResultResponse;

public class DataStoreGrpcService extends DataStoreAPIGrpc.DataStoreAPIImplBase {

  private final DataStoreComputeAPI store; // datastore api implementation.

  public DataStoreGrpcService(DataStoreComputeAPI store) {
    this.store = store;
  }

  // Read RPC: convert proto inputSource, store.read(), package result into
  // ReadResponse.
  @Override
  public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
    // Proto version of InputSource
    project.rpc.datastore.InputSource srcProto = request.getSrc();
    // Existing version of InputSource in annotations.
    InputSource src = new InputSource(srcProto.getInputType(), srcProto.getLocation());

    List<Integer> nums = store.read(src);
    if (nums == null) {
      nums = new ArrayList<>();
    }

    ReadResponse resp = ReadResponse.newBuilder().addAllNums(nums).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  // Insert RPC: stores single int input.
  @Override
  public void insert(InsertRequest request, StreamObserver<InsertResponse> responseObserver) {

    StorageResponse res = store.insertRequest(request.getInput());
    boolean ok = res != null && res.getStatus() == StoreStatus.SUCCESS;

    InsertResponse resp = InsertResponse.newBuilder().setId(ok ? res.getId() : "").setOk(ok).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  // LoadData RPC: look up stored int using id.
  @Override
  public void loadData(LoadDataRequest request, StreamObserver<LoadDataResponse> responseObserver) {

    int value = store.loadData(request.getId());

    LoadDataResponse resp = LoadDataResponse.newBuilder().setValue(value).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  // Handle InsertResult RPC: stores result string.
  @Override
  public void insertResult(InsertResultRequest request, StreamObserver<InsertResultResponse> responseObserver) {

    StorageResponse res = store.insertResult(request.getResult());
    boolean ok = res != null && res.getStatus() == StoreStatus.SUCCESS;

    InsertResultResponse resp = InsertResultResponse.newBuilder().setId(ok ? res.getId() : "").setOk(ok).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  // LoadResult RPC: retrieve stored result string using id.
  @Override
  public void loadResult(LoadResultRequest request, StreamObserver<LoadResultResponse> responseObserver) {

    String result = store.loadResult(request.getId());

    LoadResultResponse resp = LoadResultResponse.newBuilder().setResult(result == null ? "" : result).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  // LoadInputs RPC: load list of integers from path.
  @Override
  public void loadInputs(LoadInputsRequest request, StreamObserver<LoadInputsResponse> responseObserver) {

    List<Integer> nums = store.loadInputs(request.getPath());
    if (nums == null) {
      nums = new ArrayList<>();
    }

    LoadInputsResponse resp = LoadInputsResponse.newBuilder().addAllNums(nums).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  // WriteResult RPC: write result string to path.
  @Override
  public void writeResult(WriteResultRequest request, StreamObserver<WriteResultResponse> responseObserver) {

    StoreStatus status = store.writeResult(request.getPath(), request.getContent());

    WriteResultResponse resp = WriteResultResponse.newBuilder().setOk(status == StoreStatus.SUCCESS).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }
}
