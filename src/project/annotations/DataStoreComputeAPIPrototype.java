package project.annotations;

public class DataStoreComputeAPIPrototype {
  @ProcessAPIPrototype
  public void prototype(DataStoreComputeAPI store) {

    // Store the number "16" for example as raw bytes
    StorageResponse inputkey = store.insertRequest(new StorageRequest("16".getBytes()));

    // If storing succeeded (inputkey.getStatus() == StoreStatus.SUCCESS), the data
    // is loaded back.
    if (inputkey.getStatus() == StoreStatus.SUCCESS) {

      String input = new String(store.loadData(inputkey.getId()));
      System.out.println("Retrieved input data: " + input);
    }

    // Pretend core made calculation already, Store the string as the result.
    StorageResponse resultkey = store.insertResult(new StorageRequest("2,3,5,7,11,13".getBytes()));

    // If successful, load the result back to the returned id.
    if (resultkey.getStatus() == StoreStatus.SUCCESS) {
      String result = new String(store.loadResult(resultkey.getId()));
      System.out.println("Retrieved result: " + result);
    }
  }

}
