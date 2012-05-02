package com.hk.domain.builder;

import com.hk.domain.offer.OfferTrigger;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: May 30, 2011
 * Time: 4:53:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OfferTriggerBuilder {

  private OfferTriggerBuilder() {
  }

  public static OfferTriggerBuilder getInstance() {
    return new OfferTriggerBuilder();
  }

  private Double minAmount;
  private String description;

  public OfferTriggerBuilder minAmount(Double amount) {
    this.minAmount = amount;
    return this;
  }

  public OfferTriggerBuilder description(String description) {
    this.description = description;
    return this;
  }

  public OfferTrigger build() {

    OfferTrigger offerTrigger = new OfferTrigger();
    offerTrigger.setDescription(description);
    offerTrigger.setAmount(minAmount);

    return offerTrigger;
  }
}
