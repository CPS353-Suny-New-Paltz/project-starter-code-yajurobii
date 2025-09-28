import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.Delimiter;
import project.annotations.InputSource;
import project.annotations.OutputSource;
import project.annotations.SubmissionStatus;
import project.annotations.UserComputeAPI;
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

}
