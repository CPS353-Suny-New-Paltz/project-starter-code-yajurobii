import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.ComputeControllerAPIFast;
import project.annotations.ComputeRequest;

public class ComputeEngineFastTest {

  @Test
  public void isFastFaster() {
    ComputeControllerAPI slow = new ComputeControllerAPIImplementation();
    ComputeControllerAPIFast fast = new ComputeControllerAPIFast();

    int[] inputs = { 50000, 40000, 30000, 50000, 45000, 50000, 35000, 40000, 50000, 32000, 50000, 40000, 28000, 50000,
        45000 };

    // Measure slow
    long slowStart = System.nanoTime();
    for (int n : inputs) {
      slow.compute(new ComputeRequest(n));
    }
    long slowTime = System.nanoTime() - slowStart;

    // Measure fast
    long fastStart = System.nanoTime();
    for (int n : inputs) {
      fast.compute(new ComputeRequest(n));
    }
    long fastTime = System.nanoTime() - fastStart;

    System.out.println("Uncached time (ns): " + slowTime);
    System.out.println("Uncached time (ms): " + (slowTime / 1_000_000.0));
    System.out.println("Cached time   (ns): " + fastTime);
    System.out.println("Cached time   (ms): " + (fastTime / 1_000_000.0));

    // Assertion to confirm that fast is at least 10% faster.
    assertTrue(fastTime * 11 < slowTime * 10, "Fast version must be at least 10% faster than slow version");
  }
}
