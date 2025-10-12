import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.StorageResponse;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;
import project.annotations.UserComputeAPI;

public class TestEdgeCase {

  @Test
  public void testInvalidInputSub() {
    DataStoreInMemory store = new DataStoreInMemory();
    ComputeControllerAPI compute = new ComputeControllerAPIImplementation();
    UserComputeAPI userAPI = new UserComputeAPIImplementation(store, compute);

    // Inserting invalid(negative) int.
    StorageResponse inputResp = store.insertRequest(-5);

    // Create id submission.
    UserSubmission submission = new UserSubmission(new InputSource("file", inputResp.getId()),
        new OutputSource("stdout"), new Delimiter(",", ":"));

    // Submits comp.
    UserSubResponse response = userAPI.submit(submission);

    // Verify input was handled safely and give error status.
    assertNotNull(response);
    assertEquals(SubmissionStatus.FAILURE_SYSTEM_ERROR, response.getStatus());
    assertNull(response.getSubId());

  }

}
