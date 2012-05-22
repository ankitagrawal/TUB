package com.hk.report.dto.inventory;

import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: May 22, 2012
 * Time: 7:12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class RTODamageReportDto {
  private ProductVariant productVariant;
  private Long rtoDamageCheckinCount;

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Long getRtoDamageCheckinCount() {
    return rtoDamageCheckinCount;
  }

  public void setRtoDamageCheckinCount(Long rtoCheckinCount) {
    this.rtoDamageCheckinCount = rtoCheckinCount;
  }
}

