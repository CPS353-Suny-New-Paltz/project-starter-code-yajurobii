import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeRequest;
import project.annotations.ComputeResponse;
import project.annotations.ComputeControllerAPIImplementation;

public class TestComputeContollerAPI {
  @Test
  public void testComputePrimes() {
    ComputeControllerAPI mockcomp = Mockito.mock(ComputeControllerAPI.class);
    Mockito.when(mockcomp.compute(new ComputeRequest(16))).thenReturn(new ComputeResponse("2,3,5,7,11,13"));

    ComputeResponse resp = mockcomp.compute(new ComputeRequest(16));
    assertEquals("2,3,5,7,11,13", resp.getResult());

  }

  @Test
  public void testRealImplReturnsNull() {
    ComputeControllerAPI real = new ComputeControllerAPIImplementation();

    ComputeResponse resp = real.compute(new ComputeRequest(16));

    assertEquals("2,3,5,7,11,13", resp.getResult());
  }

}
