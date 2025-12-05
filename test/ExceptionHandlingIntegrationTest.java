import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeRequest;
import project.annotations.ComputeResponse;
import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.StorageResponse;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;
import project.annotations.UserComputeAPI;

public class ExceptionHandlingIntegrationTest {

  static class SelfDestructingCompute implements ComputeControllerAPI {
    @Override
    public ComputeResponse compute(ComputeRequest request) {
      throw new RuntimeException("Ka-blam!");
    }
  }

  @Test
  public void coordCatchesComputeException() {
    DataStoreInMemory store = new DataStoreInMemory();
    UserComputeAPI user = new UserComputeAPIImplementation(store, new SelfDestructingCompute());

    StorageResponse inputResp = store.insertRequest(10);
    UserSubmission sub = new UserSubmission(new InputSource("file", inputResp.getId()),
        new OutputSource("manualTestOutput.txt"), new Delimiter(".", ":"));

    UserSubResponse resp = user.submit(sub);

    assertEquals(SubmissionStatus.FAILURE_SYSTEM_ERROR, resp.getStatus());
    assertNull(resp.getSubId());
  }

}
