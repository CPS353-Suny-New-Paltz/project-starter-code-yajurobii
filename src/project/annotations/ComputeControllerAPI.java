package project.annotations;

@ConceptualAPI
public interface ComputeControllerAPI {
  // Compute a result based on the request (single integer input).
  ComputeResponse compute(ComputeRequest request);
}
