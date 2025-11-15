package project.datastore.grpc;

import java.util.ArrayList;
import java.util.List;

import io.grpc.ManagedChannel;
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

public class GrpcDataStoreComputeAPI implements DataStoreComputeAPI {

  private final DataStoreAPIGrpc.DataStoreAPIBlockingStub stub;

  public GrpcDataStoreComputeAPI(ManagedChannel channel) {
    this.stub = DataStoreAPIGrpc.newBlockingStub(channel);
  }

  @Override
  public List<Integer> read(InputSource src) { // calls Read RPC, build proto InputSource from actual InputSource.
    project.rpc.datastore.InputSource srcProto = project.rpc.datastore.InputSource.newBuilder()
        .setInputType(src.getInputType() == null ? "" : src.getInputType())
        .setLocation(src.getLocation() == null ? "" : src.getLocation()).build();

    ReadRequest req = ReadRequest.newBuilder().setSrc(srcProto).build();

    ReadResponse resp = stub.read(req);
    return new ArrayList<>(resp.getNumsList());
  }

  @Override
  public StorageResponse insertRequest(int input) { // calls Insert RPC.
    InsertRequest req = InsertRequest.newBuilder().setInput(input).build();

    InsertResponse resp = stub.insert(req);

    StoreStatus st = resp.getOk() ? StoreStatus.SUCCESS : StoreStatus.FAILURE_WRITE_ERROR;

    return new StorageResponse(resp.getId(), st);
  }

  @Override
  public int loadData(String id) {
    LoadDataRequest req = LoadDataRequest.newBuilder().setId(id == null ? "" : id).build();

    LoadDataResponse resp = stub.loadData(req);
    return resp.getValue();
  }

  @Override
  public StorageResponse insertResult(String result) { // Calls LoadData RPC.
    InsertResultRequest req = InsertResultRequest.newBuilder().setResult(result == null ? "" : result).build();

    InsertResultResponse resp = stub.insertResult(req);

    StoreStatus st = resp.getOk() ? StoreStatus.SUCCESS : StoreStatus.FAILURE_WRITE_ERROR;

    return new StorageResponse(resp.getId(), st);
  }

  @Override
  public String loadResult(String id) { // Calls LoadResult RPC.
    LoadResultRequest req = LoadResultRequest.newBuilder().setId(id == null ? "" : id).build();

    LoadResultResponse resp = stub.loadResult(req);
    String result = resp.getResult();
    return result.isBlank() ? null : result;
  }

  @Override
  public List<Integer> loadInputs(String path) { // Calls LoadInputs RPC.
    LoadInputsRequest req = LoadInputsRequest.newBuilder().setPath(path == null ? "" : path).build();

    LoadInputsResponse resp = stub.loadInputs(req);
    return new ArrayList<>(resp.getNumsList());
  }

  @Override
  public StoreStatus writeResult(String outputPath, String content) { // Calls WriteResult RPC.
    WriteResultRequest req = WriteResultRequest.newBuilder().setPath(outputPath == null ? "" : outputPath)
        .setContent(content == null ? "" : content).build();

    WriteResultResponse resp = stub.writeResult(req);
    boolean ok = resp.getOk();

    return ok ? StoreStatus.SUCCESS : StoreStatus.FAILURE_WRITE_ERROR;
  }
}
