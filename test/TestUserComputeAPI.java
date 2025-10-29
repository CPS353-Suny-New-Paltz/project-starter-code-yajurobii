import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.DataStoreComputeAPIImplementation;
import project.annotations.DataStoreComputeAPI;
import project.annotations.ComputeControllerAPI;
import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.StorageResponse;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;

public class TestUserComputeAPI {
  @Test
  public void testSubmissionSuccess() {
    UserComputeAPI mockSub = Mockito.mock(UserComputeAPI.class);
    UserSubmission sub = new UserSubmission(new InputSource("file", "input-0"), new OutputSource("stdout"),
        new Delimiter(";", ":"));

    Mockito.when(mockSub.submit(sub)).thenReturn(new UserSubResponse("sub-1", SubmissionStatus.SUCCESS));
    UserSubResponse resp = mockSub.submit(sub);
    assertEquals(SubmissionStatus.SUCCESS, resp.getStatus());
    assertEquals("sub-1", resp.getSubId());

  }

  @Test
  public void testSubmissionSuccessImpl() {

    DataStoreComputeAPI dataStore = new DataStoreComputeAPIImplementation();
    ComputeControllerAPI computeEngine = new ComputeControllerAPIImplementation();
    UserComputeAPI userApi = new UserComputeAPIImplementation(dataStore, computeEngine);

    // Store input 16.
    StorageResponse inputResp = dataStore.insertRequest(16);
    String inputId = inputResp.getId();
    

    // Submitting the computation with coordination layer.
    UserSubmission sub = new UserSubmission(new InputSource("file", inputId), new OutputSource("stdout"),
        new Delimiter(";", ":"));

    UserSubResponse resp = userApi.submit(sub);
    System.out.println("DEBUG → UserComputeAPI: subId = " + resp.getSubId());

    // Verifying success.
    assertNotNull(resp.getSubId());
    assertEquals(SubmissionStatus.SUCCESS, resp.getStatus());

    // Verify correct and stored result.
    String result = dataStore.loadResult(resp.getSubId());
    System.out.println("DEBUG → UserComputeAPI: loaded result = " + result);
    assertEquals("2,3,5,7,11,13", result);

  }

}
