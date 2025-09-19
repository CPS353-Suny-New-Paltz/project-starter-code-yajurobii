package project.annotations;

public class DataStoreComputeAPIPrototype {
  @ProcessAPIPrototype
  public void prototype(DataStoreComputeAPI store) {

    // Store the number "16" for example as raw bytes
    StorageResponse key = store.insertRequest(new StorageRequest("16".getBytes()));

    // If storing succeeded (key != null), the data is loaded back.
    if (key != null) {

      String result = new String(store.loadData(key.getId()));
      System.out.println("Retrieved: " + result);

      // If storing does not succeed, this is returned.
    } else {
      System.out.println("Insertion failed!");
    }
  }

}
