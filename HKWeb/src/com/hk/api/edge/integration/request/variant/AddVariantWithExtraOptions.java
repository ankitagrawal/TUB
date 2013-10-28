package com.hk.api.edge.integration.request.variant;


public class AddVariantWithExtraOptions extends AddProductVariantToCartRequest{

  private String leftExtOpt;
  private String rightExtOpt;

  public String getLeftExtOpt() {
    return leftExtOpt;
  }

  public void setLeftExtOpt(String leftExtOpt) {
    this.leftExtOpt = leftExtOpt;
  }

  public String getRightExtOpt() {
    return rightExtOpt;
  }

  public void setRightExtOpt(String rightExtOpt) {
    this.rightExtOpt = rightExtOpt;
  }
}
