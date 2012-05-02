package com.hk.report.dto.pricing;

import java.util.ArrayList;
import java.util.List;

import com.akube.framework.gson.JsonSkip;
import com.akube.framework.util.FormatUtils;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.Address;

/**
 * the subtotal fields are basically qty * per unit price (hkPrice)
 * <p/>
 * order level discounts are also subtracted from the product total itself
 * so thje product subtotal is the total without discounts. product total is the product subtotal - discounts
 * <p/>
 * in our case taxes are all inclusive and do not serve a purpose in calculations
 * <p/>
 * User: kani
 * Time: 10 Sep, 2009 4:25:24 PM
 */
public class PartialPricingDto {

  @JsonSkip
  private List<LineItem> productCartLineItems = new ArrayList<LineItem>(1);
  @JsonSkip
  private List<LineItem> shippingCartLineItems = new ArrayList<LineItem>(1);
  @JsonSkip
  private List<LineItem> orderLevelDiscountCartLineItems = new ArrayList<LineItem>(0);
  @JsonSkip
  private List<LineItem> codCartLineItems = new ArrayList<LineItem>(0);
  @JsonSkip
  private List<LineItem> rewardPointCartLineItems = new ArrayList<LineItem>(0);
  @JsonSkip
  private List<LineItem> outOfStockCartLineItems = new ArrayList<LineItem>(0);
  @JsonSkip
  private List<LineItem> aggregateProductCartLineItems = new ArrayList<LineItem>(2);

  private Double productsMrpSubTotal = 0.0;
  private Double productsHkSubTotal = 0.0;
  private Double productsDiscount = 0.0;
  private Double productsTotal = 0.0;
  private Long productLineCount = 0L;

  private Double orderLevelDiscount = 0.0;
  private Long orderLevelDiscountLines = 0L;

  private Double redeemedRewardPoints = 0D;
  private Double rewardPointTotal = 0D;

  private Double shippingSubTotal = 0.0;
  private Double shippingDiscount = 0.0;
  private Double shippingTotal = 0.0;
  private Long shippingLineCount = 0L;

  private Double codSubTotal = 0.0;
  private Double codDiscount = 0.0;
  private Double codTax = 0.0;
  private Double codTotal = 0.0;
  private Long codLineCount = 0L;

  private Double totalHkDiscount = 0.0;
  private Double totalPromoDiscount = 0.0;

  private Double grandTotal = 0.0;
  private Double totalDiscount = 0.0;
  private Long totalLineCount = 0L;

  private String city;
  private String state;

  public PartialPricingDto(List<LineItem> lineItems, Address address) {
    /*for (LineItem lineItem : lineItems) {
      // round off invoice line values to avoid round off errors

      Double roundedOffHkPrice = NumberUtil.roundOff(lineItem.getHkPrice());
      lineItem.setHkPrice(roundedOffHkPrice);
      Double roundedOffDiscount = NumberUtil.roundOff(lineItem.getDiscountOnHkPrice());
      lineItem.setDiscountOnHkPrice(roundedOffDiscount);


      //TODO: # warehouse refactor these statuses
      if (lineItem.isType(EnumLineItemType.Product)) {
        if (lineItem.isShippedEmailSent() == null || !lineItem.isShippedEmailSent()) {
          if (lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.READY_FOR_PROCESS.getId()) ||
              lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.GONE_FOR_PRINTING.getId()) ||
              lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.PICKING.getId()) ||
              lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.CHECKEDOUT.getId()) ||
              lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.PACKED.getId()) ||
              lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.HANDED_OVER_TO_COURIER.getId())) {
            productsMrpSubTotal += lineItem.getMarkedPrice() * lineItem.getQty();
            productsHkSubTotal += lineItem.getHkPrice() * lineItem.getQty();
            productsDiscount += lineItem.getDiscountOnHkPrice();
            productsTotal += lineItem.getHkPrice() * lineItem.getQty() - lineItem.getDiscountOnHkPrice();
            productLineCount++;
            productCartLineItems.add(lineItem);
          }
        }
      } else if (lineItem.isType(EnumLineItemType.CodCharges)) {
        codLineCount++;
      } else if (lineItem.isType(EnumLineItemType.RewardPoint)) {
        redeemedRewardPoints += lineItem.getDiscountOnHkPrice();
      }
    }*/

    //TODO: # warehouse fix this.
  }

  @Override
  public String toString() {
    return "PricingDto{" +
        "productsMrpSubTotal=" + productsMrpSubTotal +
        ", productsHkSubTotal=" + productsHkSubTotal +
        ", productsDiscount=" + productsDiscount +
        ", productsTotal=" + productsTotal +
        ", shippingSubTotal=" + shippingSubTotal +
        ", shippingDiscount=" + shippingDiscount +
        ", shippingTotal=" + shippingTotal +
        ", redeemed points=" + redeemedRewardPoints +
        ", grandTotal=" + grandTotal +
        '}';
  }

  public Double getProductsMrpSubTotal() {
    return productsMrpSubTotal;
  }

  public Double getProductsHkSubTotal() {
    return productsHkSubTotal;
  }

  public Double getProductsDiscount() {
    return productsDiscount;
  }

  public Double getProductsTotal() {
    return productsTotal;
  }

  public Long getProductLineCount() {
    return productLineCount;
  }

  public Double getOrderLevelDiscount() {
    return orderLevelDiscount;
  }

  public Long getOrderLevelDiscountLines() {
    return orderLevelDiscountLines;
  }

  public Double getShippingSubTotal() {
    return shippingSubTotal;
  }

  public Double getShippingDiscount() {
    return shippingDiscount;
  }

  public Double getShippingTotal() {
    return shippingTotal;
  }

  public Long getShippingLineCount() {
    return shippingLineCount;
  }

  public Double getGrandTotal() {
    Double gt = FormatUtils.getCurrencyPrecision(grandTotal);
    if (gt == -0.0) gt = 0.0;
    return gt;
  }

  public Long getTotalLineCount() {
    return totalLineCount;
  }

  public Double getTotalDiscount() {
    return totalDiscount;
  }

  public List<LineItem> getProductCartLineItems() {
    return productCartLineItems;
  }

  public List<LineItem> getAggregateProductCartLineItems() {
    return aggregateProductCartLineItems;
  }

  public List<LineItem> getShippingCartLineItems() {
    return shippingCartLineItems;
  }

  public List<LineItem> getOrderLevelDiscountCartLineItems() {
    return orderLevelDiscountCartLineItems;
  }

  public List<LineItem> getCodCartLineItems() {
    return codCartLineItems;
  }

  public void setCodCartLineItems(List<LineItem> codCartLineItems) {
    this.codCartLineItems = codCartLineItems;
  }

  public Double getCodSubTotal() {
    return codSubTotal;
  }

  public void setCodSubTotal(Double codSubTotal) {
    this.codSubTotal = codSubTotal;
  }

  public Double getCodDiscount() {
    return codDiscount;
  }

  public void setCodDiscount(Double codDiscount) {
    this.codDiscount = codDiscount;
  }

  public Double getCodTax() {
    return codTax;
  }

  public void setCodTax(Double codTax) {
    this.codTax = codTax;
  }

  public Double getCodTotal() {
    return codTotal;
  }

  public void setCodTotal(Double codTotal) {
    this.codTotal = codTotal;
  }

  public Long getCodLineCount() {
    return codLineCount;
  }

  public void setCodLineCount(Long codLineCount) {
    this.codLineCount = codLineCount;
  }

  public List<LineItem> getOutOfStockCartLineItems() {
    return outOfStockCartLineItems;
  }

  public void setOutOfStockCartLineItems(List<LineItem> outOfStockCartLineItems) {
    this.outOfStockCartLineItems = outOfStockCartLineItems;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public Double getRewardPointTotal() {
    return rewardPointTotal;
  }

  public Double getTotalHkDiscount() {
    return totalHkDiscount;
  }

  public Double getTotalPromoDiscount() {
    return totalPromoDiscount;
  }

  public List<LineItem> getRewardPointCartLineItems() {
    return rewardPointCartLineItems;
  }

  public void setRewardPointCartLineItems(List<LineItem> rewardPointCartLineItems) {
    this.rewardPointCartLineItems = rewardPointCartLineItems;
  }

  public Double getRedeemedRewardPoints() {
    return redeemedRewardPoints;
  }

  public void setRedeemedRewardPoints(Double redeemedRewardPoints) {
    this.redeemedRewardPoints = redeemedRewardPoints;
  }
}