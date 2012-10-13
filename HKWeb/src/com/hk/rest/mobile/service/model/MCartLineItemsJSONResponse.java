package com.hk.rest.mobile.service.model;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.order.Order;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.core.CartLineItemType;

import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 7, 2012
 * Time: 11:05:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class MCartLineItemsJSONResponse {

    private Long id;
    private Long orderId;
    private String name;
    private Long order;
    private Long qty;
    private Double markedPrice;
    private Double hkPrice;
    private Double discountOnHkPrice;
    private String lineItemType;
    private String cartLineItemId;
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCartLineItemId() {
        return cartLineItemId;
    }

    public void setCartLineItemId(String cartLineItemId) {
        this.cartLineItemId = cartLineItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getMarkedPrice() {
        return markedPrice;
    }

    public void setMarkedPrice(Double markedPrice) {
        this.markedPrice = markedPrice;
    }

    public Double getHkPrice() {
        return hkPrice;
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = hkPrice;
    }

    public Double getDiscountOnHkPrice() {
        return discountOnHkPrice;
    }

    public void setDiscountOnHkPrice(Double discountOnHkPrice) {
        this.discountOnHkPrice = discountOnHkPrice;
    }

    public String getLineItemType() {
        return lineItemType;
    }

    public void setLineItemType(String lineItemType) {
        this.lineItemType = lineItemType;
    }
}
