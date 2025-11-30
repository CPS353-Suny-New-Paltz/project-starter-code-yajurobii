import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.ComputeRequest;

public class InitialMeasurementTest {

  @Test
  public void measureInitialPerformance() {
    ComputeControllerAPI engine = new ComputeControllerAPIImplementation();

    int input = 50000;

    engine.compute(new ComputeRequest(input));

    long start = System.nanoTime();
    engine.compute(new ComputeRequest(input));
    long end = System.nanoTime();

    long duration = end - start;
    System.out.println("Initial slow compute time (ns): " + duration);
    System.out.println("Initial slow compute time (ms): " + (duration / 1_000_000.0));
  }
}
