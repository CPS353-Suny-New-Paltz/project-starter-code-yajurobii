package project.annotations;

public class ComputeControllerAPIImplementation implements ComputeControllerAPI {
  @Override
  public ComputeResponse compute(ComputeRequest request) {
    if (request == null) {
      return new ComputeResponse("");
    }
    int n = request.getInput(); // getting upper bound(input number e.g. 16)
    if (n < 2) {
      return new ComputeResponse("");
    }

    // looping from 2 up to n because 2 is first prime.
    try {
      StringBuilder result = new StringBuilder();
      for (int i = 2; i <= n; i++) {
        if (isPrime(i)) { // checking if i is prime.
          if (result.length() > 0) { // if its not he first prime a comma is added.
            result.append(",");
          }
          result.append(i); // add the prime number to the string.
        }

      }
      return new ComputeResponse(result.toString()); // returns all primes as CSV.

    } catch (RuntimeException ex) {
      return new ComputeResponse("");
    }
  }

  private boolean isPrime(int num) {
    if (num < 2) {
      return false;
    } // 0 and 1 are not prime numbers so auto-false.
    for (int i = 2; i * i <= num; i++) { // testing divisors up to sqrt of num(e.g. i = 2 → 2*2 4 ≤ 7 → test)
      if (num % i == 0) { // e.g. num = 7; remainder is 1, loop continues up to i = 3, 3*3=9, 9 ≤ 7 is
        // false so loop stops, no divisors so it is prime.
        return false;
      }
    }
    return true;
  }

}
