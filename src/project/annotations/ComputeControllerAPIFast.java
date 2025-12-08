package project.annotations;

import java.util.LinkedHashMap;
import java.util.Map;

/*
   In order to identify the bottleneck, i made an initial end-to-end test InitialMeasurement test
   and ran it to get a baseline of what performance speed was.
   On my m2 macbook, the reading i got was 1.781541 ms. I also ran the test multiple times while using Jconsole
   and saw a CPU usage spike ranging from 3.5% to 9.2%.
   When observing my code and thinking about what could be changed to bring this spike down, I thought what could be changed is the actual computation.
   This is because the process of the computation computes primes up to n from scratch every single time, even if input repeats.
   This is causing it to repeat expensive work, which is definitely not helping performance time.
   So a way i thought to fix this repeating problem is to add a cache.
   This will save a result if its already been computed up to n once. If n is asked for another time saved result is returned instantly.
   Once I ran my new engine test that compared old API to new API with cache, the benchmark improved tremendously. It was 2.93ms compared to 0.0069ms.
   This is about a 99.8% percent improvement and a 99.6% percent improvement from initial measure.
   This may not be practical though as it needs input to be cached(repeated) for a significant speed up to occur.
 */

public class ComputeControllerAPIFast {

  // number of distinct n values that can be stored at once.
  private static final int MAX_CACHE_ENTRIES = 100;

  private final ComputeControllerAPI original = new ComputeControllerAPIImplementation();

  // Map from input n -> "2,3,5,7,..."
  private final Map<Integer, String> cache = new LinkedHashMap<Integer, String>(16, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, String> oldest) {
      // Evict oldest when the max size is exceeded.
      return size() > MAX_CACHE_ENTRIES;
    }
  };

  public ComputeResponse compute(ComputeRequest request) {
    if (request == null) {
      return new ComputeResponse("");
    }

    int n = request.getInput();
    if (n < 2) {
      return new ComputeResponse("");
    }

    // If cached, return it immediately.
    String cached = cache.get(n);
    if (cached != null) {
      return new ComputeResponse(cached);
    }

    // Compute using existing implementation.
    ComputeResponse resp = original.compute(request);

    if (resp != null && resp.getResult() != null) {
      cache.put(n, resp.getResult());
    }

    return resp;
  }
}
