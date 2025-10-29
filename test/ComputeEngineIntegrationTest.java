import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.StorageResponse;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;

public class ComputeEngineIntegrationTest {
  @Test
  public void testInMemoryDataStore() {

    ComputeControllerAPI computeApi = new ComputeControllerAPIImplementation();
    DataStoreInMemory store = new DataStoreInMemory();
    UserComputeAPI userApi = new UserComputeAPIImplementation(store, computeApi);

    // Act as if user entered "16"
    StorageResponse inResp = store.insertRequest(16);
    assertNotNull(inResp, "insertRequest returned null response");
    assertNotNull(inResp.getId(), "insertRequest returned null id");
    String inputId = inResp.getId();

    UserSubmission submission = new UserSubmission(new InputSource("memory", inputId), new OutputSource("stdout"),
        new Delimiter(",", ":"));

    UserSubResponse response = userApi.submit(submission);
    System.out.println("DEBUG → IntegrationTest: subId = " + response.getSubId());

    assertEquals(SubmissionStatus.SUCCESS, response.getStatus(), "submit did not return SUCCESS");
    assertNotNull(response.getSubId(), "submit returned null subId");

    String storedResult = store.loadResult(response.getSubId());
    System.out.println("DEBUG → IntegrationTest: loaded result = " + storedResult);
    assertEquals("2,3,5,7,11,13", storedResult);

  }

}
