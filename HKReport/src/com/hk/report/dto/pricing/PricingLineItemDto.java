package com.hk.report.dto.pricing;

import java.util.HashSet;
import java.util.Set;

import com.akube.framework.gson.JsonSkip;
import com.akube.framework.util.FormatUtils;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;

public class PricingLineItemDto {

  private Long id;
  private Long lineItemTypeId;
  private String productVariantId;
  private Long orderId;
  private Long qty;
  private String markedPrice;
  private String hkPrice;
  private String hkDiscount;
  private String discountOnHkPrice;
  private String subTotalMrp;  // qty * markedPrice
  private String subTotalHkPrice;  // qty * hkPrice
  private String subTotalHkDiscount;  // qty * hkDiscount
  private String discount;  // total discount = (hkDiscount + discountOnHkPrice)
  private String total;  // total = subTotalMrp - discount

  @JsonSkip
  private Set<ComboInstance> comboInstanceSet = new HashSet<ComboInstance>();

  public PricingLineItemDto(CartLineItem cartLineItem) {
    id = cartLineItem.getId();
    lineItemTypeId = cartLineItem.getLineItemType() == null ? null : cartLineItem.getLineItemType().getId();
    productVariantId = cartLineItem.getProductVariant() == null ? null : cartLineItem.getProductVariant().getId();
    orderId = cartLineItem.getOrder() == null ? null : cartLineItem.getOrder().getId();
    qty = cartLineItem.getQty();
    Long comboQty = 0L;
    if (cartLineItem.getComboInstance() != null) {
      ComboInstance comboInstance = cartLineItem.getComboInstance();
      if (!comboInstanceSet.contains(comboInstance)) {
        comboQty = cartLineItem.getQty() / cartLineItem.getComboInstance().getComboInstanceProductVariant(cartLineItem.getProductVariant()).getQty();
        markedPrice = FormatUtils.getCurrencyFormat(cartLineItem.getComboInstance().getCombo().getMarkedPrice());
        hkPrice = FormatUtils.getCurrencyFormat(cartLineItem.getComboInstance().getCombo().getHkPrice());
        hkDiscount = FormatUtils.getCurrencyFormat(cartLineItem.getComboInstance().getCombo().getMarkedPrice() - cartLineItem.getComboInstance().getCombo().getHkPrice());
        subTotalMrp = FormatUtils.getCurrencyFormat(cartLineItem.getComboInstance().getCombo().getMarkedPrice() * comboQty);
        subTotalHkPrice = FormatUtils.getCurrencyFormat(cartLineItem.getComboInstance().getCombo().getHkPrice() * comboQty);
        subTotalHkDiscount = FormatUtils.getCurrencyFormat((cartLineItem.getComboInstance().getCombo().getMarkedPrice() - cartLineItem.getComboInstance().getCombo().getHkPrice()) * comboQty);
        double discount = comboQty * (cartLineItem.getComboInstance().getCombo().getMarkedPrice() - cartLineItem.getComboInstance().getCombo().getHkPrice());
        this.discount = FormatUtils.getCurrencyFormat(discount);
        total = FormatUtils.getCurrencyFormat(cartLineItem.getComboInstance().getCombo().getMarkedPrice() * comboQty - discount);
        comboInstanceSet.add(comboInstance);
      }
    } else {
      markedPrice = FormatUtils.getCurrencyFormat(cartLineItem.getMarkedPrice());
      hkPrice = FormatUtils.getCurrencyFormat(cartLineItem.getHkPrice());
      hkDiscount = FormatUtils.getCurrencyFormat(cartLineItem.getMarkedPrice() - cartLineItem.getHkPrice());
      discountOnHkPrice = FormatUtils.getCurrencyFormat(cartLineItem.getDiscountOnHkPrice());
      subTotalMrp = FormatUtils.getCurrencyFormat(cartLineItem.getMarkedPrice() * cartLineItem.getQty());
      subTotalHkPrice = FormatUtils.getCurrencyFormat(cartLineItem.getHkPrice() * cartLineItem.getQty());
      subTotalHkDiscount = FormatUtils.getCurrencyFormat((cartLineItem.getMarkedPrice() - cartLineItem.getHkPrice()) * cartLineItem.getQty());
      double discount = cartLineItem.getQty() * (cartLineItem.getMarkedPrice() - cartLineItem.getHkPrice()) + cartLineItem.getDiscountOnHkPrice();
      this.discount = FormatUtils.getCurrencyFormat(discount);
      total = FormatUtils.getCurrencyFormat(cartLineItem.getMarkedPrice() * cartLineItem.getQty() - discount);
    }
  }


  public Long getId() {
    return id;
  }

  public Long getLineItemTypeId() {
    return lineItemTypeId;
  }

  public String getProductVariantId() {
    return productVariantId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public Long getQty() {
    return qty;
  }

  public String getMarkedPrice() {
    return markedPrice;
  }

  public String getHkPrice() {
    return hkPrice;
  }

  public String getDiscountOnHkPrice() {
    return discountOnHkPrice;
  }

  public String getSubTotalMrp() {
    return subTotalMrp;
  }

  public String getSubTotalHkPrice() {
    return subTotalHkPrice;
  }

  public String getHkDiscount() {
    return hkDiscount;
  }

  public String getSubTotalHkDiscount() {
    return subTotalHkDiscount;
  }

  public String getDiscount() {
    return discount;
  }

  public String getTotal() {
    return total;
  }
}
