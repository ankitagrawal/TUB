package com.hk.report.dto.pricing;


import com.akube.framework.util.FormatUtils;
import com.hk.domain.order.CartLineItem;
import com.hk.dto.pricing.PricingDto;


/**
 * This class stores the totals and other pricing summary values in currency formatted strings.
 * this is to be used when returning json responses.
 */
public class PricingSubDto {

  PricingLineItemDto pricingLineItemDto;

  private String productsMrpSubTotal;
  private String totalMrpSubTotal;
  private String productsHkSubTotal;
  private String totalHkSubTotal;
  private String productsDiscount;
  private String productsTotal;
  private Long productLineCount = 0L;

  private String orderLevelDiscount;
  private Long orderLevelDiscountLines = 0L;

  private String totalHkDiscount;
  private String totalPromoDiscount;
  private String totalCashback;
  private String subscriptionDiscount;

  private String shippingSubTotal;
  private String shippingDiscount;
  private String shippingTotal;
  private Long shippingLineCount = 0L;

  private String grandTotalPayable;
  private String postpaidServicesTotal;
  private String grandTotalStrike;
  private String totalDiscount;
  private Long totalLineCount = 0L;

  public PricingSubDto(PricingDto pricingDto,  CartLineItem cartLineItem) {
    if (cartLineItem != null) {
      pricingLineItemDto = new PricingLineItemDto(cartLineItem);
    }

    productsMrpSubTotal = FormatUtils.getCurrencyFormat(pricingDto.getProductsMrpSubTotal());
    totalMrpSubTotal = FormatUtils.getCurrencyFormat(pricingDto.getProductsMrpSubTotal() + pricingDto.getPrepaidServiceMrpSubTotal() + pricingDto.getPostpaidServiceMrpSubTotal());
    productsHkSubTotal = FormatUtils.getCurrencyFormat(pricingDto.getProductsHkSubTotal());
    totalHkSubTotal = FormatUtils.getCurrencyFormat(pricingDto.getProductsHkSubTotal() + pricingDto.getPrepaidServiceHkSubTotal() + pricingDto.getPostpaidServiceHkSubTotal());
    productsDiscount = FormatUtils.getCurrencyFormat(pricingDto.getProductsDiscount());
    productsTotal = FormatUtils.getCurrencyFormat(pricingDto.getProductsTotal());
    productLineCount = pricingDto.getProductLineCount();

    orderLevelDiscount = FormatUtils.getCurrencyFormat(pricingDto.getOrderLevelDiscount());
    orderLevelDiscountLines = pricingDto.getOrderLevelDiscountLines();

    totalHkDiscount = FormatUtils.getCurrencyFormat(pricingDto.getTotalHkProductsDiscount() + pricingDto.getTotalHkPrepaidServiceDiscount() + pricingDto.getPostpaidServiceDiscount());
    totalPromoDiscount = FormatUtils.getCurrencyFormat(pricingDto.getTotalPromoDiscount());
    totalCashback = FormatUtils.getCurrencyFormat(pricingDto.getTotalCashback());
    subscriptionDiscount = FormatUtils.getCurrencyFormat(pricingDto.getSubscriptionDiscount());

    shippingSubTotal = FormatUtils.getCurrencyFormat(pricingDto.getShippingSubTotal());
    shippingDiscount = FormatUtils.getCurrencyFormat(pricingDto.getShippingDiscount());
    shippingTotal = FormatUtils.getCurrencyFormat(pricingDto.getShippingTotal());
    shippingLineCount = pricingDto.getShippingLineCount();

    grandTotalPayable = FormatUtils.getCurrencyFormat(pricingDto.getGrandTotalPayable());
    postpaidServicesTotal = FormatUtils.getCurrencyFormat(pricingDto.getPostpaidServicesTotal());
    grandTotalStrike = FormatUtils.getCurrencyFormat(pricingDto.getGrandTotalPayable() + pricingDto.getTotalDiscount());
    totalDiscount = FormatUtils.getCurrencyFormat(pricingDto.getTotalDiscount());
    totalLineCount = pricingDto.getTotalLineCount();
  }

  public PricingLineItemDto getPricingLineItemDto() {
    return pricingLineItemDto;
  }

  public String getProductsMrpSubTotal() {
    return productsMrpSubTotal;
  }

  public String getProductsHkSubTotal() {
    return productsHkSubTotal;
  }

  public String getProductsDiscount() {
    return productsDiscount;
  }

  public String getProductsTotal() {
    return productsTotal;
  }

  public Long getProductLineCount() {
    return productLineCount;
  }

  public String getOrderLevelDiscount() {
    return orderLevelDiscount;
  }

  public Long getOrderLevelDiscountLines() {
    return orderLevelDiscountLines;
  }

  public String getShippingSubTotal() {
    return shippingSubTotal;
  }

  public String getShippingDiscount() {
    return shippingDiscount;
  }

  public String getShippingTotal() {
    return shippingTotal;
  }

  public Long getShippingLineCount() {
    return shippingLineCount;
  }

  public String getGrandTotalPayable() {
    return grandTotalPayable;
  }

  public String getGrandTotalStrike() {
    return grandTotalStrike;
  }

  public String getTotalDiscount() {
    return totalDiscount;
  }

  public Long getTotalLineCount() {
    return totalLineCount;
  }

  public String getTotalHkDiscount() {
    return totalHkDiscount;
  }

  public String getTotalPromoDiscount() {
    return totalPromoDiscount;
  }

  public String getPostpaidServicesTotal() {
    return postpaidServicesTotal;
  }

  public void setPostpaidServicesTotal(String postpaidServicesTotal) {
    this.postpaidServicesTotal = postpaidServicesTotal;
  }

  public String getTotalMrpSubTotal() {
    return totalMrpSubTotal;
  }

  public void setTotalMrpSubTotal(String totalMrpSubTotal) {
    this.totalMrpSubTotal = totalMrpSubTotal;
  }

  public String getTotalHkSubTotal() {
    return totalHkSubTotal;
  }

  public void setTotalHkSubTotal(String totalHkSubTotal) {
    this.totalHkSubTotal = totalHkSubTotal;
  }

  public String getTotalCashback() {
    return totalCashback;
  }

  public void setTotalCashback(String totalCashback) {
    this.totalCashback = totalCashback;
  }

  public String getSubscriptionDiscount() {
    return subscriptionDiscount;
  }

  public void setSubscriptionDiscount(String subscriptionDiscount) {
    this.subscriptionDiscount = subscriptionDiscount;
  }
}
