package project.annotations;

public class DataStoreComputeAPIPrototype {
  @ProcessAPIPrototype
  public void prototype(DataStoreComputeAPI store) {

    // Store integer input
    StorageResponse inputKey = store.insertRequest(16);

    if (inputKey.getStatus() == StoreStatus.SUCCESS) {
      int input = store.loadData(inputKey.getId());
      System.out.println("Retrieved input data: " + input);
    }

    // Store computed result
    StorageResponse resultKey = store.insertResult("2,3,5,7,11,13");

    if (resultKey.getStatus() == StoreStatus.SUCCESS) {
      String result = store.loadResult(resultKey.getId());
      System.out.println("Retrieved result: " + result);
    }
  }
}
