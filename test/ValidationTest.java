import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;

public class ValidationTest {

  @Test
  public void RejectBlankInput() {
    UserComputeAPI user = new UserComputeAPIImplementation(new DataStoreInMemory(),
        new ComputeControllerAPIImplementation());
    // invalid InputSouce
    UserSubmission bad = new UserSubmission(new InputSource("file", "  "), new OutputSource("manualTestOutput.txt"),
        new Delimiter(",", ":"));

    UserSubResponse resp = user.submit(bad);
    assertEquals(SubmissionStatus.FAILURE_SYSTEM_ERROR, resp.getStatus());
    assertNull(resp.getSubId());

  }

}
