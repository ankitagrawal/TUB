package com.hk.report.dto.inventory;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: May 22, 2012
 * Time: 7:10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class RTOReportDto {
  private Date rtoDate;
  private Long shippingOrderNumber;
  private String productVariantId;
  private String productName;
  private String productOptions;
  private Long rtoCheckinQty;
  private Long rtoDamageCheckinQty;

  public Date getRtoDate() {
    return rtoDate;
  }

  public void setRtoDate(Date rtoDate) {
    this.rtoDate = rtoDate;
  }

  public Long getShippingOrderNumber() {
    return shippingOrderNumber;
  }

  public void setShippingOrderNumber(Long shippingOrderNumber) {
    this.shippingOrderNumber = shippingOrderNumber;
  }

  public String getProductVariantId() {
    return productVariantId;
  }

  public void setProductVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductOptions() {
    return productOptions;
  }

  public void setProductOptions(String productOptions) {
    this.productOptions = productOptions;
  }

  public Long getRtoCheckinQty() {
    return rtoCheckinQty;
  }

  public void setRtoCheckinQty(Long rtoCheckinQty) {
    this.rtoCheckinQty = rtoCheckinQty;
  }

  public Long getRtoDamageCheckinQty() {
    return rtoDamageCheckinQty;
  }

  public void setRtoDamageCheckinQty(Long rtoDamageCheckinQty) {
    this.rtoDamageCheckinQty = rtoDamageCheckinQty;
  }
}
