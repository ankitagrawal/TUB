package com.hk.report.dto.order;

/**
 * Created by IntelliJ IDEA.
 * User: AdminUser
 * Date: Feb 14, 2012
 * Time: 11:37:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class LineItemConfigValuesDTO {

//  private double price;
  //  private String value;
  private long optionId;
  private long valueId;

  public long getOptionId() {
    return optionId;
  }

  public void setOptionId(long optionId) {
    this.optionId = optionId;
  }

  public long getValueId() {
    return valueId;
  }

  public void setValueId(long valueId) {
    this.valueId = valueId;
  }
}
