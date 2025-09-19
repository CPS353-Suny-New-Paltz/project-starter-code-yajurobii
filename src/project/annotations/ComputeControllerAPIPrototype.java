package project.annotations;

public class ComputeControllerAPIPrototype {
  @ConceptualAPIPrototype
  public void prototype(ComputeControllerAPI n) {
    // Make a request that computes primes up to 16.
    ComputeRequest req = new ComputeRequest(16);

    // Ask the core to compute the result.
    ComputeResponse resp = n.compute(req);

    // If the computation worked, the result will be retrieved.
    if (resp != null) {
      String result = resp.getResult();
      System.out.println("Computation result: " + result);
      // If not this will be printed.
    } else {
      System.out.println("Computation failed!");
    }

  }

}
