package com.hk.constants;

/**
 * User: nitin
 * Date: 3 Jun, 2011
 * Time: 12:23:00 AM
 */
public enum EnumS3UploadStatus {
  Failed("Image could not be uploaded"),
  Checked("Image already exists."),
  Uploaded("Image uploaded successfully"),
  NotFound("Product not found.");
  private String message;

  public String getMessage() {
    return message;
  }

  EnumS3UploadStatus(String message) {

    this.message = message;
  }
}
