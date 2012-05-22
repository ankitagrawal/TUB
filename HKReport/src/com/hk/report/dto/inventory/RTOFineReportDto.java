package com.hk.report.dto.inventory;

import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: May 22, 2012
 * Time: 7:12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RTOFineReportDto {
  private ProductVariant productVariant;
  private Long rtoCheckinCount;

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Long getRtoCheckinCount() {
    return rtoCheckinCount;
  }

  public void setRtoCheckinCount(Long rtoCheckinCount) {
    this.rtoCheckinCount = rtoCheckinCount;
  }
}

