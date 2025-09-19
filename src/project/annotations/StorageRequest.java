package project.annotations;

public class StorageRequest {
  private byte[] data; // any input/output stored as bytes.

  public StorageRequest(byte[] data) {
    this.data = data;
  }

  public byte[] getData() {
    return data;
  }

}
