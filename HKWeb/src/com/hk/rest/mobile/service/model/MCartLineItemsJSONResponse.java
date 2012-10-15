package com.hk.rest.mobile.service.model;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.order.Order;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.core.CartLineItemType;
import com.hk.rest.mobile.service.utils.MHKConstants;

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

    private Long id=0l;
    private Long orderId=0l;
    private String name;
    private Long order=0l;
    private Long qty=0l;
    private Double markedPrice=0.0;
    private Double hkPrice=0.0;
    private Double discountOnHkPrice=0.0;
    private String lineItemType;
    private String cartLineItemId;
    private String productId;
    private String imageUrl;
    
    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
	
		this.imageUrl =MHKConstants.getStringNullDefault(imageUrl);
	}

	public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
    	
        this.productId = MHKConstants.getStringNullDefault(productId);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = MHKConstants.getLongNullDefault(orderId);
    }

    public String getCartLineItemId() {
        return cartLineItemId;
    }

    public void setCartLineItemId(String cartLineItemId) {
        this.cartLineItemId = MHKConstants.getStringNullDefault(cartLineItemId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = MHKConstants.getStringNullDefault(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = MHKConstants.getLongNullDefault(id);
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = MHKConstants.getLongNullDefault(order);
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = MHKConstants.getLongNullDefault(qty);
    }

    public Double getMarkedPrice() {
        return markedPrice;
    }

    public void setMarkedPrice(Double markedPrice) {
        this.markedPrice = MHKConstants.getDoubleNullDefault(markedPrice);
    }

    public Double getHkPrice() {
        return hkPrice;
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = MHKConstants.getDoubleNullDefault(hkPrice);
    }

    public Double getDiscountOnHkPrice() {
        return discountOnHkPrice;
    }

    public void setDiscountOnHkPrice(Double discountOnHkPrice) {
        this.discountOnHkPrice = MHKConstants.getDoubleNullDefault(discountOnHkPrice);
    }

    public String getLineItemType() {
        return lineItemType;
    }

    public void setLineItemType(String lineItemType) {
        this.lineItemType = MHKConstants.getStringNullDefault(lineItemType);
    }
}
