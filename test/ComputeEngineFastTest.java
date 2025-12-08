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

    int input = 50000;

    // Measure slow
    long slowStart = System.nanoTime();
    slow.compute(new ComputeRequest(input));
    long slowTime = System.nanoTime() - slowStart;

    // First call fills the cache, so it can be called.
    fast.compute(new ComputeRequest(input));

    // Second call hits the cache, returns immediately.
    long fastStart = System.nanoTime();
    fast.compute(new ComputeRequest(input));
    long fastTime = System.nanoTime() - fastStart;

    System.out.println("Uncached time (ns): " + slowTime);
    System.out.println("Uncached time (ms): " + (slowTime / 1_000_000.0));
    System.out.println("Cached time   (ns): " + fastTime);
    System.out.println("Cached time   (ms): " + (fastTime / 1_000_000.0));

    // Assertion to confirm that fast is at least 10% faster.
    assertTrue(fastTime * 11 < slowTime * 10,
        "Fast version must be at least 10% faster than slow version");
  }
}


