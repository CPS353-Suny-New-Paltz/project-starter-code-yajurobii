# Software Engineering Project Starter Code

This repo will start you off with an initial configuration that you'll modify as part of Checkpoint 1. As part of the modifications, you'll eventually delete the contents of this README and replace it with documentation for your project.

Computation description:
"The system computes all prime numbers up to the user's input number, for example, an input of '16' would output 2,3,5,7,11,13.'

System Diagram:
![System Diagram](SystemdiagramConverted.png)

# Multi Threaded Network API

There's a multi-thread implementation of the Network API with the upper bound of 8. Using "Executors.newFixedThreadPool(Math.max(1, threadCap))" to limit the threads.

## Performance Tuning

**Bottleneck**: After running intial measurement test while using Jconsole, showing high CPU usage jump due to the recomputing of integers that have already been seen by original ComputeController.

**Fix**: Created ComputeControllerAPIFast, a version of ComputeControllerAPI that caches results of previously computed integers and retrievs them from the map instead of recomputing again even though the number has already been seen/computed previously.

### Benchmark Numbers

| Coordinator and Engine            | Time (ms) |
|---------------------------------- |-----------|
| ComputeControllerAPI (slow)       | 1.781541  |
| ComputeControllerAPIFast (fast)   | 0.006917  |

Hardware: m2 macbook air

Performance Improvement: 99.6% reduction in compute time.

Performance Test [test/ComputeEngineFastTest.java](test/ComputeEngineFastTest.java) to test for results.
