package com.hk.domain.builder;

import org.testng.Assert;

import com.hk.constants.discount.OfferConstants;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.offer.OfferAction;

/**
 * User: rahul Time: 11 Sep, 2009 12:35:19 PM
 */
public class OfferActionBuilder {

    private OfferActionBuilder() {
    }

    public static OfferActionBuilder getInstance() {
        return new OfferActionBuilder();
    }

    private Double       discountAmount;
    private Double       discountPercent;
    private Long         qty;
    @SuppressWarnings("unused")
    private Double       discountFramePercent;
    @SuppressWarnings("unused")
    private Double       discountStoreProductPercent;
    private Double       discountShippingPercent;
    private ProductGroup productGroup;
    private Boolean      validOnIntlShipping;
    private Boolean      discountOnBasePriceOnly;
    private String       description;

    public OfferActionBuilder discountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public OfferActionBuilder discountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
        return this;
    }

    public OfferActionBuilder qty(Long qty) {
        this.qty = qty;
        return this;
    }

    public OfferActionBuilder discountFramePercent(Double discountFramePercent) {
        this.discountFramePercent = discountFramePercent;
        return this;
    }

    public OfferActionBuilder discountStoreProductPercent(Double discountStoreProductPercent) {
        this.discountStoreProductPercent = discountStoreProductPercent;
        return this;
    }

    public OfferActionBuilder discountShippingPercent(Double discountShippingPercent) {
        this.discountShippingPercent = discountShippingPercent;
        return this;
    }

    public OfferActionBuilder productGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
        return this;
    }

    public OfferActionBuilder validOnIntlShipping(Boolean validOnIntlShipping) {
        this.validOnIntlShipping = validOnIntlShipping;
        return this;
    }

    public OfferActionBuilder discountOnBasePriceOnly(Boolean discountOnBasePriceOnly) {
        this.discountOnBasePriceOnly = discountOnBasePriceOnly;
        return this;
    }

    public OfferActionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public OfferAction build() {

        Assert.assertNotNull(validOnIntlShipping);
        Assert.assertNotNull(discountOnBasePriceOnly);
        Assert.assertNotNull(description);

        OfferAction offerAction = new OfferAction();
        offerAction.setOrderLevelDiscountAmount(discountAmount);
        offerAction.setDiscountPercentOnHkPrice(discountPercent);
        offerAction.setDescription(description);
        if (qty == null)
            qty = OfferConstants.INFINITE_QTY;
        offerAction.setQty(qty);
        offerAction.setDiscountPercentOnShipping(discountShippingPercent);
        offerAction.setProductGroup(productGroup);

        return offerAction;
    }
}
