package com.hk.domain.builder;

import java.util.Date;

import org.testng.Assert;

import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferTrigger;

/**
 * User: rahul
 * Time: 11 Sep, 2009 11:46:24 AM
 */
public class OfferBuilder {

  private OfferBuilder() {}

  public static OfferBuilder getInstance() {
    return new OfferBuilder();
  }

  private String description;
  private long allowedTimes;
  @SuppressWarnings("unused")
private long alreadyUsed;
  private Boolean excludeTriggerProducts;
  private OfferTrigger offerTrigger;
  private OfferAction offerAction;
  private Date startDate;
  private Date endDate;
  private Boolean carryOverAllowed;
  private String offerIdentifier;

  public OfferBuilder description(String desc) {
    this.description = desc;
    return this;
  }

  public OfferBuilder numberOfTimesAllowed(long allowedTimes) {
    this.allowedTimes = allowedTimes;
    return this;
  }

  public OfferBuilder numberOfTimesUsed(long usedTimes) {
    this.alreadyUsed = usedTimes;
    return this;
  }

  public OfferBuilder startDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public OfferBuilder endDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public OfferBuilder excludeTriggerProducts(Boolean exclude) {
    this.excludeTriggerProducts = exclude;
    return this;
  }

  public OfferBuilder offerTrigger(OfferTrigger offerTrigger) {
    this.offerTrigger = offerTrigger;
    return this;
  }

  public OfferBuilder offerAction(OfferAction offerAction) {
    this.offerAction = offerAction;
    return this;
  }

  public OfferBuilder carryOverAllowed(Boolean carryOverAllowed) {
    this.carryOverAllowed = carryOverAllowed;
    return this;
  }

  public OfferBuilder offerIdentifier(String offerIdentifier) {
    this.offerIdentifier = offerIdentifier;
    return this;
  }

  public Offer build() {

    Assert.assertNotNull(description);
    Assert.assertNotNull(startDate);
    Assert.assertNotNull(excludeTriggerProducts);
    Assert.assertNotNull(offerAction);
    Assert.assertTrue(allowedTimes != 0);

    Offer offer = new Offer();
    offer.setDescription(description);
    offer.setStartDate(startDate);
    offer.setEndDate(endDate);
    offer.setExcludeTriggerProducts(excludeTriggerProducts);
    if (carryOverAllowed == null) carryOverAllowed = false;
    offer.setOfferTrigger(offerTrigger);
    offer.setOfferAction(offerAction);
    offer.setOfferIdentifier(offerIdentifier);

    return offer;
  }
}
