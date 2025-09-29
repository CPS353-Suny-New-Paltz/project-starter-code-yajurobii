import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;
import project.annotations.UserSubResponse;
import project.annotations.UserSubmission;

public class TestUserComputeAPI {
  @Test
  public void testSubmissionSuccess() {
    UserComputeAPI mocksub = Mockito.mock(UserComputeAPI.class);
    UserSubmission sub = new UserSubmission(new InputSource("file", "input.txt"), new OutputSource("stdout"),
        new Delimiter(";", ":"));

    Mockito.when(mocksub.submit(sub)).thenReturn(new UserSubResponse("sub-1", SubmissionStatus.SUCCESS));
    UserSubResponse resp = mocksub.submit(sub);
    assertEquals(SubmissionStatus.SUCCESS, resp.getStatus());
    assertEquals("sub-1", resp.getSubId());

  }
  
  @Test
  public void testSubmissionSuccessImpl() {
    UserComputeAPI realSub = new UserComputeAPIImplementation();
    
    UserSubmission sub = new UserSubmission(new InputSource("file", "input.txt"), new OutputSource("stdout"),
        new Delimiter(";", ":"));
    
    UserSubResponse resp = realSub.submit(sub);
    
    assertEquals(SubmissionStatus.FAILURE_SYSTEM_ERROR, resp.getStatus());
    assertNull(resp.getSubId());
    
  }

}
