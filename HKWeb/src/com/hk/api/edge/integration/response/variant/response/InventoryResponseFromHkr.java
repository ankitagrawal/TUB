package com.hk.api.edge.integration.response.variant.response;


import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

public class InventoryResponseFromHkr extends AbstractResponseFromHKR {

  private Long unbookedInventory;

  public Long getUnbookedInventory() {
    return unbookedInventory;
  }

  public void setUnbookedInventory(Long unbookedInventory) {
    this.unbookedInventory = unbookedInventory;
  }

  @Override
  protected String[] getKeys() {
    return new String[]{"unbkdInv", "msgs", "exception"};
  }

  @Override
  protected Object[] getValues() {
    return new Object[]{this.unbookedInventory, this.msgs, this.exception};
  }
}
