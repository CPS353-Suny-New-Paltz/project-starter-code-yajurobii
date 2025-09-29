import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import project.annotations.ComputeControllerAPI;
import project.annotations.ComputeControllerAPIImplementation;
import project.annotations.StorageRequest;
import project.annotations.StorageResponse;
import project.annotations.StoreStatus;
import project.annotations.UserComputeAPI;
import project.annotations.UserComputeAPIImplementation;

public class ComputeEngineIntegrationTest {
  @Test
  public void testInMemoryDataStore() {
    UserComputeAPI userApi = new UserComputeAPIImplementation();
    ComputeControllerAPI computeApi = new ComputeControllerAPIImplementation();
    DataStoreInMemory store = new DataStoreInMemory();

    // Act as if user entered "16"
    StorageResponse inResp = store.insertRequest(new StorageRequest("16".getBytes()));

    // Assert that the storage worked.
    assertEquals(StoreStatus.SUCCESS, inResp.getStatus());
    assertNotNull(inResp.getId());

    // Fetch same value back using earlier assigned ID.
    String loadedInput = new String(store.loadData(inResp.getId()));

    // Check what's gotten back is what was stored.
    assertEquals("16", loadedInput);
    
    assertNotNull(computeApi);

    // Act as if engine found primes <= 16.
    StorageResponse outResp = store.insertResult(new StorageRequest("2,3,5,7,11,13".getBytes()));

    // Assert result storage worked.
    assertEquals(StoreStatus.SUCCESS, outResp.getStatus());
    assertNotNull(outResp.getId());

    // Same as loadedInput but for result.
    String loadedResult = new String(store.loadResult(outResp.getId()));

    // Verify that expected result is gotten.
    assertEquals("2,3,5,7,11,13", loadedResult);
    
    assertNotNull(userApi);

  }

}
