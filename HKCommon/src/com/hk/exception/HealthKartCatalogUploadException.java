package com.hk.exception;


public class HealthKartCatalogUploadException extends HealthkartCheckedException {

  private Integer rowId;

  public HealthKartCatalogUploadException(String message, Integer rowId) {
    super(message);
    this.rowId = rowId;
  }

  public Integer getRowId() {
    return rowId;
  }

}
