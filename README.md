# Software Engineering Project Starter Code

This repo will start you off with an initial configuration that you'll modify as part of Checkpoint 1. As part of the modifications, you'll eventually delete the contents of this README and replace it with documentation for your project.

Computation description:
"The system computes all prime numbers up to the user's input number, for example, an input of '16' would output 2,3,5,7,11,13.'

System Diagram:
![System Diagram](SystemdiagramConverted.png)

# Multi Threaded Network API

There's a multi-thread implementation of the Network API with the upper bound of 8. Using "Executors.newFixedThreadPool(Math.max(1, threadCap))" to limit the threads.
