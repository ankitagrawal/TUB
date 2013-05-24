package com.hk.dto.pricing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akube.framework.gson.JsonSkip;
import com.akube.framework.util.FormatUtils;
import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.OfferTrigger;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.exception.HealthkartDefaultWebException;
import com.hk.pact.dao.BaseDao;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.NumberUtil;
import com.hk.util.OfferTriggerMatcher;

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
public class PricingDto {

    @JsonSkip
    private List<CartLineItem> productLineItems = new ArrayList<CartLineItem>(1);
    @JsonSkip
    private List<CartLineItem> shippingLineItems = new ArrayList<CartLineItem>(1);
    @JsonSkip
    private List<CartLineItem> orderLevelDiscountLineItems = new ArrayList<CartLineItem>(0);
    @JsonSkip
    private List<CartLineItem> codLineItems = new ArrayList<CartLineItem>(0);
    @JsonSkip
    private List<CartLineItem> rewardPointLineItems = new ArrayList<CartLineItem>(0);
    @JsonSkip
    private List<CartLineItem> outOfStockLineItems = new ArrayList<CartLineItem>(0);
    @JsonSkip
    private List<CartLineItem> aggregateProductLineItems = new ArrayList<CartLineItem>(2);


    private Double productsMrpSubTotal = 0.0;
    private Double prepaidServiceMrpSubTotal = 0.0;
    private Double postpaidServiceMrpSubTotal = 0.0;
    private Double payableMrpSubTotal = 0.0;
    private Double productsHkSubTotal = 0.0;
    private Double prepaidServiceHkSubTotal = 0.0;
    private Double postpaidServiceHkSubTotal = 0.0;
    private Double payableHkSubTotal = 0.0;
    private Double productsDiscount = 0.0;
    private Double prepaidServiceDiscount = 0.0;
    private Double postpaidServiceDiscount = 0.0;
    private Double payableDiscount = 0.0;
    private Double productsTotal = 0.0;
    private Double prepaidServicesTotal = 0.0;
    private Double postpaidServicesTotal = 0.0;
    private Long productLineCount = 0L;
    private Long prepaidServicesLineCount = 0L;
    private Long postpaidServicesLineCount = 0L;

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

    private Long subscriptionLineCount = 0L;
    private Double subscriptionDiscount = 0.0;

    private Double totalHkProductsDiscount = 0.0;
    private Double totalHkPrepaidServiceDiscount = 0.0;
    private Double totalHkPostpaidServiceDiscount = 0.0;
    private Double totalPromoDiscount = 0.0;
    private Double totalCashback = 0.0;
    private Double totalPostpaidAmount = 0.0;

    private Double grandTotalPayable = 0.0;
    private Double grandTotalProducts = 0.0;
    private Double grandTotalServices = 0.0;
    private Double grandTotal = 0.0;
    private Double totalDiscount = 0.0;
    private Long totalLineCount = 0L;

    private String city;
    private String state;
    OfferAction offerAction = null;
    Double cashbackLimit = 0.0;
    Double cashbackPercent = 0.0;
    ProductGroup productGroup = null;

    private Set<ComboInstance> comboInstanceSet = new HashSet<ComboInstance>();

    public PricingDto(Set<CartLineItem> cartLineItems, Address address) {
        BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);

        //TODO: # warehouse refactor this way of getting order.

        Order order = null;

        if (cartLineItems != null && cartLineItems.size() > 0) {
            for (CartLineItem cartLineItem : cartLineItems) {
                order = cartLineItem.getOrder();
                if (order != null) break;
            }
        }


        OfferInstance offerInstance = null;
        OfferTrigger offerTrigger = null;
        Boolean offerTriggerActive = false;
        Boolean rewardPointOffer = false;
        if (order != null && order.getOfferInstance() != null) {
            offerInstance = order.getOfferInstance();
            Offer offer = order.getOfferInstance().getOffer();
            if (offer != null) {
                offerAction = offer.getOfferAction();
                if (offerAction != null) {
                    rewardPointOffer = offerAction.isCashback();
                }
                offerTrigger = offer.getOfferTrigger();
	            cashbackPercent = offerAction.getRewardPointDiscountPercent();
	            cashbackLimit = offerAction.getRewardPointCashbackLimit();
                productGroup = offerAction.getProductGroup();
            }
            if (offerInstance != null && Boolean.TRUE.equals(rewardPointOffer)) {
                if (offerTrigger != null) {
                    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
                    OfferTriggerMatcher triggerMatcher = offerTrigger.easyMatch(productCartLineItems);
                    if (triggerMatcher.hasEasyMatch(offerInstance.getOffer().isExcludeTriggerProducts())) {
                        offerTriggerActive = true;
                    }
                } else {
                    offerTriggerActive = true;
                }
            }
        }
        for (CartLineItem cartLineItem : cartLineItems) {
            // round off invoice line values to avoid round off errors
            Double roundedOffHkPrice = NumberUtil.roundOff(cartLineItem.getHkPrice());
            cartLineItem.setHkPrice(roundedOffHkPrice);
            Double roundedOffDiscount = NumberUtil.roundOff(cartLineItem.getDiscountOnHkPrice());
            cartLineItem.setDiscountOnHkPrice(roundedOffDiscount);

	        if (cartLineItem.isType(EnumCartLineItemType.Product)) {
                if (cartLineItem.getComboInstance() == null || (cartLineItem.getComboInstance() != null && cartLineItem.getComboInstance().getCombo().getId().equals("CMB-DIA05"))) {
                    productsMrpSubTotal += cartLineItem.getMarkedPrice() * cartLineItem.getQty();
                    productsHkSubTotal += cartLineItem.getHkPrice() * cartLineItem.getQty();
                    productsDiscount += cartLineItem.getDiscountOnHkPrice();
                    productsTotal += cartLineItem.getHkPrice() * cartLineItem.getQty() - cartLineItem.getDiscountOnHkPrice();
                    totalPostpaidAmount += cartLineItem.getProductVariant().getPostpaidAmount() * cartLineItem.getQty();
                    if (offerTriggerActive) {
                        if (productGroup == null || productGroup.contains(cartLineItem.getProductVariant())) {
                            totalCashback += cartLineItem.getHkPrice() * cartLineItem.getQty();
                        }
                    }
                    productLineCount++;
                } else {
                    Long comboQty = 0L;
			        ComboInstance comboInstance = cartLineItem.getComboInstance();
			        if (!comboInstanceSet.contains(comboInstance)) {
                if(cartLineItem.getComboInstance().getComboInstanceProductVariant(cartLineItem.getProductVariant())!=null){
				         comboQty = cartLineItem.getQty() / cartLineItem.getComboInstance().getComboInstanceProductVariant(cartLineItem.getProductVariant()).getQty();
                }
				        productsMrpSubTotal += cartLineItem.getComboInstance().getCombo().getMarkedPrice() * comboQty;
				        productsHkSubTotal += cartLineItem.getComboInstance().getCombo().getHkPrice() * comboQty;
				        productsTotal += cartLineItem.getComboInstance().getCombo().getHkPrice() * comboQty;
				        productLineCount++;
				        comboInstanceSet.add(comboInstance);
			        }
		        }
                productLineItems.add(cartLineItem);
                if (cartLineItem.getProductVariant() != null && cartLineItem.getProductVariant().isOutOfStock()) {
                    outOfStockLineItems.add(cartLineItem);
                }
            } else if (cartLineItem.isType(EnumCartLineItemType.Shipping)) {
                shippingSubTotal += cartLineItem.getHkPrice();
                shippingDiscount += cartLineItem.getDiscountOnHkPrice();
                shippingTotal += cartLineItem.getHkPrice() - cartLineItem.getDiscountOnHkPrice();

                shippingLineCount++;

                shippingLineItems.add(cartLineItem);
                if (address != null) {
                    city = address.getCity();
                    state = address.getState();
                }

            } else if (cartLineItem.isType(EnumCartLineItemType.OrderLevelDiscount)) {
                orderLevelDiscount += cartLineItem.getDiscountOnHkPrice();
                productsTotal += cartLineItem.getHkPrice() * cartLineItem.getQty() - cartLineItem.getDiscountOnHkPrice();

                orderLevelDiscountLines++;

                orderLevelDiscountLineItems.add(cartLineItem);
            } else if (cartLineItem.isType(EnumCartLineItemType.CodCharges)) {
                codSubTotal += cartLineItem.getHkPrice();
                codDiscount += cartLineItem.getDiscountOnHkPrice();
                codTotal += cartLineItem.getHkPrice() - cartLineItem.getDiscountOnHkPrice();

                codLineCount++;

                codLineItems.add(cartLineItem);

            } else if (cartLineItem.isType(EnumCartLineItemType.RewardPoint)) {
                redeemedRewardPoints += cartLineItem.getDiscountOnHkPrice();
                rewardPointTotal += cartLineItem.getDiscountOnHkPrice();
                rewardPointLineItems.add(cartLineItem);
            } else if(cartLineItem.isType(EnumCartLineItemType.Subscription)) {
                if(cartLineItem.getQty()>0){
                    productsMrpSubTotal += cartLineItem.getMarkedPrice()*cartLineItem.getQty();
                    productsHkSubTotal += cartLineItem.getHkPrice()*cartLineItem.getQty();
                    subscriptionDiscount += cartLineItem.getDiscountOnHkPrice();
                    productsTotal += cartLineItem.getHkPrice()*cartLineItem.getQty() - cartLineItem.getDiscountOnHkPrice();
                    subscriptionLineCount++;
                    productLineCount++; //subscription is also treated as a product for cart purposes
                    productLineItems.add(cartLineItem);
                }
            }

        }

        for (CartLineItem productLineItem : productLineItems) {
            CartLineItem lineItem = new CartLineItem();
            CartLineItem orderLevelDiscountLineItem = null;
            for (CartLineItem OLDLineItem : orderLevelDiscountLineItems) {
                if (OLDLineItem.getProductVariant() != null && OLDLineItem.getProductVariant().equals(productLineItem.getProductVariant())) {
                    orderLevelDiscountLineItem = OLDLineItem;
                    break;
                }
            }

            lineItem.setProductVariant(productLineItem.getProductVariant());
            lineItem.setQty(productLineItem.getQty());
            lineItem.setDiscountOnHkPrice(
                    productLineItem.getComboInstance() == null ? productLineItem.getDiscountOnHkPrice() : productLineItem.getComboInstance().getCombo().getDiscountPercent() -
                            (orderLevelDiscountLineItem != null ? orderLevelDiscountLineItem.getDiscountOnHkPrice() : 0)
            );
            // finally we have the unit price which is "after" the discount has been applied. GA has no provision for discounts, so the
            // unit price fed into GA will be the discounted unit price
            lineItem.setHkPrice((productLineItem.getHkPrice() * productLineItem.getQty() - productLineItem.getDiscountOnHkPrice()) / productLineItem.getQty());

            aggregateProductLineItems.add(lineItem);
        }

        totalCashback = totalCashback * (cashbackPercent != null ? cashbackPercent : 0.0);
        totalCashback = cashbackLimit != null ? totalCashback < cashbackLimit ? totalCashback : cashbackLimit : totalCashback;

        payableMrpSubTotal = prepaidServiceMrpSubTotal + productsMrpSubTotal;
        payableHkSubTotal = prepaidServiceHkSubTotal + productsHkSubTotal;
        payableDiscount = prepaidServiceDiscount + productsDiscount + subscriptionDiscount;

        grandTotal = productsTotal + prepaidServicesTotal + postpaidServicesTotal + shippingTotal + codTotal - rewardPointTotal;
        grandTotalPayable = productsTotal + prepaidServicesTotal + shippingTotal + codTotal - rewardPointTotal;
        grandTotalProducts = productsTotal + shippingTotal + codTotal - rewardPointTotal;
        grandTotalServices = prepaidServicesTotal + postpaidServicesTotal + shippingTotal + codTotal - rewardPointTotal;

        if (FormatUtils.getCurrencyPrecision(grandTotalPayable) < 0)
            throw new HealthkartDefaultWebException("Order Total cannot be lesser than 0");
        totalLineCount = productLineCount + orderLevelDiscountLines + shippingLineCount + codLineCount + subscriptionLineCount;

        totalHkProductsDiscount = productsMrpSubTotal - productsHkSubTotal;
        totalHkPrepaidServiceDiscount = prepaidServiceMrpSubTotal - prepaidServiceHkSubTotal;
        totalHkPostpaidServiceDiscount = postpaidServiceMrpSubTotal - postpaidServiceHkSubTotal;

        totalPromoDiscount = productsDiscount + prepaidServiceDiscount + postpaidServiceDiscount + orderLevelDiscount;
            if(totalPromoDiscount > payableHkSubTotal){
              totalPromoDiscount = payableMrpSubTotal;
            }
        totalDiscount = productsDiscount + prepaidServiceDiscount + postpaidServiceDiscount + shippingDiscount + orderLevelDiscount + codDiscount +subscriptionDiscount;
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
                ", grandTotalPayable=" + grandTotalPayable +
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

    public Long getSubscriptionLineCount() {
        return subscriptionLineCount;
    }

    public void setSubscriptionLineCount(Long subscriptionLineCount) {
        this.subscriptionLineCount = subscriptionLineCount;
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

    public Double getGrandTotalPayable() {
        Double gt = FormatUtils.getCurrencyPrecision(grandTotalPayable);
        if (gt == -0.0) gt = 0.0;
        return gt;
    }

    public Long getTotalLineCount() {
        return totalLineCount;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public List<CartLineItem> getProductLineItems() {
        return productLineItems;
    }

    public List<CartLineItem> getAggregateProductLineItems() {
        return aggregateProductLineItems;
    }

    public List<CartLineItem> getShippingLineItems() {
        return shippingLineItems;
    }

    public List<CartLineItem> getOrderLevelDiscountLineItems() {
        return orderLevelDiscountLineItems;
    }

    public List<CartLineItem> getCodLineItems() {
        return codLineItems;
    }

    public void setCodLineItems(List<CartLineItem> codLineItems) {
        this.codLineItems = codLineItems;
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

    public List<CartLineItem> getOutOfStockLineItems() {
        return outOfStockLineItems;
    }

    public void setOutOfStockLineItems(List<CartLineItem> outOfStockLineItems) {
        this.outOfStockLineItems = outOfStockLineItems;
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

    public Double getTotalHkProductsDiscount() {
        return totalHkProductsDiscount;
    }

    public Double getTotalPromoDiscount() {
        return totalPromoDiscount;
    }

    public List<CartLineItem> getRewardPointLineItems() {
        return rewardPointLineItems;
    }

    public void setRewardPointLineItems(List<CartLineItem> rewardPointLineItems) {
        this.rewardPointLineItems = rewardPointLineItems;
    }

    public Double getRedeemedRewardPoints() {
        return redeemedRewardPoints;
    }

    public void setRedeemedRewardPoints(Double redeemedRewardPoints) {
        this.redeemedRewardPoints = redeemedRewardPoints;
    }

    public Double getPrepaidServiceMrpSubTotal() {
        return prepaidServiceMrpSubTotal;
    }

    public void setPrepaidServiceMrpSubTotal(Double prepaidServiceMrpSubTotal) {
        this.prepaidServiceMrpSubTotal = prepaidServiceMrpSubTotal;
    }

    public Double getPostpaidServiceMrpSubTotal() {
        return postpaidServiceMrpSubTotal;
    }

    public void setPostpaidServiceMrpSubTotal(Double postpaidServiceMrpSubTotal) {
        this.postpaidServiceMrpSubTotal = postpaidServiceMrpSubTotal;
    }

    public Double getPrepaidServiceHkSubTotal() {
        return prepaidServiceHkSubTotal;
    }

    public void setPrepaidServiceHkSubTotal(Double prepaidServiceHkSubTotal) {
        this.prepaidServiceHkSubTotal = prepaidServiceHkSubTotal;
    }

    public Double getPostpaidServiceHkSubTotal() {
        return postpaidServiceHkSubTotal;
    }

    public void setPostpaidServiceHkSubTotal(Double postpaidServiceHkSubTotal) {
        this.postpaidServiceHkSubTotal = postpaidServiceHkSubTotal;
    }

    public Double getPrepaidServiceDiscount() {
        return prepaidServiceDiscount;
    }

    public void setPrepaidServiceDiscount(Double prepaidServiceDiscount) {
        this.prepaidServiceDiscount = prepaidServiceDiscount;
    }

    public Double getPostpaidServiceDiscount() {
        return postpaidServiceDiscount;
    }

    public void setPostpaidServiceDiscount(Double postpaidServiceDiscount) {
        this.postpaidServiceDiscount = postpaidServiceDiscount;
    }

    public Double getPrepaidServicesTotal() {
        return prepaidServicesTotal;
    }

    public void setPrepaidServicesTotal(Double prepaidServicesTotal) {
        this.prepaidServicesTotal = prepaidServicesTotal;
    }

    public Double getPostpaidServicesTotal() {
        return postpaidServicesTotal;
    }

    public void setPostpaidServicesTotal(Double postpaidServicesTotal) {
        this.postpaidServicesTotal = postpaidServicesTotal;
    }

    public Long getPrepaidServicesLineCount() {
        return prepaidServicesLineCount;
    }

    public void setPrepaidServicesLineCount(Long prepaidServicesLineCount) {
        this.prepaidServicesLineCount = prepaidServicesLineCount;
    }

    public Long getPostpaidServicesLineCount() {
        return postpaidServicesLineCount;
    }

    public void setPostpaidServicesLineCount(Long postpaidServicesLineCount) {
        this.postpaidServicesLineCount = postpaidServicesLineCount;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getPayableMrpSubTotal() {
        return payableMrpSubTotal;
    }

    public void setPayableMrpSubTotal(Double payableMrpSubTotal) {
        this.payableMrpSubTotal = payableMrpSubTotal;
    }

    public Double getPayableHkSubTotal() {
        return payableHkSubTotal;
    }

    public void setPayableHkSubTotal(Double payableHkSubTotal) {
        this.payableHkSubTotal = payableHkSubTotal;
    }

    public Double getPayableDiscount() {
        return payableDiscount;
    }

    public void setPayableDiscount(Double payableDiscount) {
        this.payableDiscount = payableDiscount;
    }

    public Double getTotalHkPrepaidServiceDiscount() {
        return totalHkPrepaidServiceDiscount;
    }

    public void setTotalHkPrepaidServiceDiscount(Double totalHkPrepaidServiceDiscount) {
        this.totalHkPrepaidServiceDiscount = totalHkPrepaidServiceDiscount;
    }

    public Double getTotalHkPostpaidServiceDiscount() {
        return totalHkPostpaidServiceDiscount;
    }

    public void setTotalHkPostpaidServiceDiscount(Double totalHkPostpaidServiceDiscount) {
        this.totalHkPostpaidServiceDiscount = totalHkPostpaidServiceDiscount;
    }

    public Double getGrandTotalProducts() {
        return grandTotalProducts;
    }

    public void setGrandTotalProducts(Double grandTotalProducts) {
        this.grandTotalProducts = grandTotalProducts;
    }

    public Double getGrandTotalServices() {
        return grandTotalServices;
    }

    public void setGrandTotalServices(Double grandTotalServices) {
        this.grandTotalServices = grandTotalServices;
    }

    public void setGrandTotalPayable(Double grandTotalPayable) {
        this.grandTotalPayable = grandTotalPayable;
    }

    public Double getTotalCashback() {
        return totalCashback;
    }

    public void setTotalCashback(Double totalCashback) {
        this.totalCashback = totalCashback;
    }

    public Double getTotalPostpaidAmount() {
        return totalPostpaidAmount;
    }

    public void setTotalPostpaidAmount(Double totalPostpaidAmount) {
        this.totalPostpaidAmount = totalPostpaidAmount;
    }

    public Double getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(Double subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }
}
