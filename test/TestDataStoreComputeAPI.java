import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.DataStoreComputeAPI;
import project.annotations.DataStoreComputeAPIImplementation;
import project.annotations.StorageResponse;
import project.annotations.StoreStatus;

public class TestDataStoreComputeAPI {
  @Test
  public void testInsertRequest() {
    DataStoreComputeAPI mockstore = Mockito.mock(DataStoreComputeAPI.class);
    Mockito.when(mockstore.insertRequest(10)).thenReturn(new StorageResponse("input-0", StoreStatus.SUCCESS));
    StorageResponse resp = mockstore.insertRequest(10);
    assertEquals(StoreStatus.SUCCESS, resp.getStatus());
    assertEquals("input-0", resp.getId());
  }

  @Test
  public void testInsertRequestImpl() {
    DataStoreComputeAPI realStore = new DataStoreComputeAPIImplementation();
    // Sim of int input save.
    StorageResponse inputResp = realStore.insertRequest(16);
    assertEquals(StoreStatus.SUCCESS, inputResp.getStatus());
    assertNotNull(inputResp.getId());
    // Making sure int is retrieved.
    int retrievedInput = realStore.loadData(inputResp.getId());
    assertEquals(16, retrievedInput);
    // Stores result
    StorageResponse resultResp = realStore.insertResult("2,3,5,7,11,13");
    assertEquals(StoreStatus.SUCCESS, resultResp.getStatus());
    assertNotNull(resultResp.getId());
    // verify retrieval.
    String retrievedResult = realStore.loadResult(resultResp.getId());
    assertEquals("2,3,5,7,11,13", retrievedResult);

  }

}
