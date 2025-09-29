import static org.junit.jupiter.api.Assertions.assertNull;
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

    ComputeRequest req = new ComputeRequest(16);
    ComputeResponse resp = mockcomp.compute(req);
    assertNull(resp);

  }
  
  @Test
  public void testRealImplReturnsNull() {
    ComputeControllerAPI real = new ComputeControllerAPIImplementation();
    ComputeRequest req = new ComputeRequest(16);
    
    ComputeResponse resp = real.compute(req);
    
    assertNull(resp);
  }

}
