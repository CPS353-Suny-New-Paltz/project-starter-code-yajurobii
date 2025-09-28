import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.annotations.DataStoreComputeAPI;
import project.annotations.StorageRequest;
import project.annotations.StorageResponse;
import project.annotations.StoreStatus;

public class TestDataStoreComputeAPI {
  @Test
  public void testInsertRequest() {
    DataStoreComputeAPI mockstore = Mockito.mock(DataStoreComputeAPI.class);
    StorageRequest req = new StorageRequest("Hello".getBytes());
    Mockito.when(mockstore.insertRequest(req)).thenReturn(new StorageResponse("id1", StoreStatus.SUCCESS));
    StorageResponse resp = mockstore.insertRequest(req);
    assertEquals(StoreStatus.SUCCESS, resp.getStatus());
    assertEquals("id1", resp.getId());
  }

}
