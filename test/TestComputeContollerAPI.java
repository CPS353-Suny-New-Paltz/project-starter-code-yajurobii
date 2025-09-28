import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeRequest;
import project.annotations.ComputeResponse;

public class TestComputeContollerAPI {
  @Test
  public void testComputePrimes() {
    ComputeControllerAPI mockcomp = Mockito.mock(ComputeControllerAPI.class);
    ComputeRequest req = new ComputeRequest(16);
    Mockito.when(mockcomp.compute(req)).thenReturn(new ComputeResponse("2,3,5,7,11,13"));
    ComputeResponse resp = mockcomp.compute(req);
    assertEquals("2,3,5,7,11,13", resp.getResult());

  }

}
