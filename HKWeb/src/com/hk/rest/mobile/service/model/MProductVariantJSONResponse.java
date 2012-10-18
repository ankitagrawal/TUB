package com.hk.rest.mobile.service.model;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.rest.mobile.service.utils.MHKConstants;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 6, 2012
 * Time: 6:02:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MProductVariantJSONResponse {

        private String id="";
        private Product product;
        private String variantName="";
        private String hkPrice="0.0";
        private String b2bPrice="0.0";
        private String markedPrice="0.0";
        private Double discountPercent=0.0;
        private String costPrice="0.0";
        private Long shippingBaseQty;
        private String shippingBasePrice="0.0";
        private Long shippingAddQty;
        private String shippingAddPrice="0.0";
        private boolean outOfStock;
        private boolean deleted;
        private Double orderRanking;
        private Long qty=0l;
        private Boolean selected;
        private String colorHex;
        private Long mainImageId;
        private Long mainProductImageId;
        private Double length;
        private Double breadth;
        private Double height;
        private Double weight;
        private String upc;    // Universal Product Code
        private String affiliateCategory;
        private String paymentType;
        private String serviceType;
        private String variantConfig;
        private Long cutOffInventory;
        private Double postpaidAmount;
        private Date createdDate;
        private Boolean clearanceSale;
        private Long leadTime;
        private Double leadTimeFactor;
        private Long bufferTime;
        private Long consumptionTime;
        private Date nextAvailableDate;
        private Date followingAvailableDate;
        private String supplierCode;
        private String otherRemark;
        private String imageUrl="";
        private String extraOptions;
        private String colourOptions;
        private List<ProductOption> productOptions = new ArrayList<ProductOption>();
        private String productId="";

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = MHKConstants.getStringNullDefault(productId);
    }

    public List<ProductOption> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOption> productOptions) {
        this.productOptions = productOptions;
    }

    public String getColourOptions() {
        return colourOptions;
    }

    public void setColourOptions(String colourOptions) {
        this.colourOptions = colourOptions;
    }

    public String getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(String extraOptions) {
        this.extraOptions = extraOptions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = MHKConstants.getStringNullDefault(imageUrl);
    }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = MHKConstants.getStringNullDefault(id);
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public String getVariantName() {
            return variantName;
        }

        public void setVariantName(String variantName) {
            this.variantName = MHKConstants.getStringNullDefault(variantName);
        }

        public String getHkPrice() {
            return hkPrice;
        }

        public void setHkPrice(String hkPrice) {
            this.hkPrice =MHKConstants.getStringNullDefault(hkPrice);
        }

        public String getB2bPrice() {
            return b2bPrice;
        }

        public void setB2bPrice(String b2bPrice) {
            this.b2bPrice = b2bPrice;
        }

        public String getMarkedPrice() {
            return markedPrice;
        }

        public void setMarkedPrice(String markedPrice) {
            this.markedPrice = MHKConstants.getStringNullDefault(markedPrice);
        }

        public Double getDiscountPercent() {
            return discountPercent;
        }

        public void setDiscountPercent(Double discountPercent) {
            this.discountPercent = MHKConstants.getDoubleNullDefault(discountPercent);
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = MHKConstants.getStringNullDefault(costPrice);
        }

        public Long getShippingBaseQty() {
            return shippingBaseQty;
        }

        public void setShippingBaseQty(Long shippingBaseQty) {
            this.shippingBaseQty = MHKConstants.getLongNullDefault(shippingBaseQty);
        }

        public String getShippingBasePrice() {
            return shippingBasePrice;
        }

        public void setShippingBasePrice(String shippingBasePrice) {
            this.shippingBasePrice = MHKConstants.getStringNullDefault(shippingBasePrice);
        }

        public Long getShippingAddQty() {
            return shippingAddQty;
        }

        public void setShippingAddQty(Long shippingAddQty) {
            this.shippingAddQty = shippingAddQty;
        }

        public String getShippingAddPrice() {
            return shippingAddPrice;
        }

        public void setShippingAddPrice(String shippingAddPrice) {
            this.shippingAddPrice = shippingAddPrice;
        }

        public boolean isOutOfStock() {
            return outOfStock;
        }

        public void setOutOfStock(boolean outOfStock) {
            this.outOfStock = outOfStock;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public Double getOrderRanking() {
            return orderRanking;
        }

        public void setOrderRanking(Double orderRanking) {
            this.orderRanking = orderRanking;
        }

        public Long getQty() {
            return qty;
        }

        public void setQty(Long qty) {
            this.qty = MHKConstants.getLongNullDefault(qty);
        }

        public Boolean isSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected!=null?selected:false;
        }

        public String getColorHex() {
            return colorHex;
        }

        public void setColorHex(String colorHex) {
            this.colorHex = MHKConstants.getStringNullDefault(colorHex);
        }

        public Long getMainImageId() {
            return mainImageId;
        }

        public void setMainImageId(Long mainImageId) {
            this.mainImageId = MHKConstants.getLongNullDefault(mainImageId);
        }

        public Long getMainProductImageId() {
            return mainProductImageId;
        }

        public void setMainProductImageId(Long mainProductImageId) {
            this.mainProductImageId = MHKConstants.getLongNullDefault(mainProductImageId);
        }

        public Double getLength() {
            return length;
        }

        public void setLength(Double length) {
            this.length = MHKConstants.getDoubleNullDefault(length);
        }

        public Double getBreadth() {
            return breadth;
        }

        public void setBreadth(Double breadth) {
            this.breadth = MHKConstants.getDoubleNullDefault(breadth);
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = MHKConstants.getDoubleNullDefault(height);
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = MHKConstants.getDoubleNullDefault(weight);
        }

        public String getUpc() {
            return upc;
        }

        public void setUpc(String upc) {
            this.upc = MHKConstants.getStringNullDefault(upc);
        }

        public String getAffiliateCategory() {
            return affiliateCategory;
        }

        public void setAffiliateCategory(String affiliateCategory) {
            this.affiliateCategory = MHKConstants.getStringNullDefault(affiliateCategory);
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = MHKConstants.getStringNullDefault(paymentType);
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getVariantConfig() {
            return variantConfig;
        }

        public void setVariantConfig(String variantConfig) {
            this.variantConfig = variantConfig;
        }

        public Long getCutOffInventory() {
            return cutOffInventory;
        }

        public void setCutOffInventory(Long cutOffInventory) {
            this.cutOffInventory = cutOffInventory;
        }

        public Double getPostpaidAmount() {
            return postpaidAmount;
        }

        public void setPostpaidAmount(Double postpaidAmount) {
            this.postpaidAmount = postpaidAmount;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public Boolean isClearanceSale() {
            return clearanceSale;
        }

        public void setClearanceSale(Boolean clearanceSale) {
            this.clearanceSale = clearanceSale;
        }

        public Long getLeadTime() {
            return leadTime;
        }

        public void setLeadTime(Long leadTime) {
            this.leadTime = leadTime;
        }

        public Double getLeadTimeFactor() {
            return leadTimeFactor;
        }

        public void setLeadTimeFactor(Double leadTimeFactor) {
            this.leadTimeFactor = leadTimeFactor;
        }

        public Long getBufferTime() {
            return bufferTime;
        }

        public void setBufferTime(Long bufferTime) {
            this.bufferTime = bufferTime;
        }

        public Long getConsumptionTime() {
            return consumptionTime;
        }

        public void setConsumptionTime(Long consumptionTime) {
            this.consumptionTime = consumptionTime;
        }

        public Date getNextAvailableDate() {
            return nextAvailableDate;
        }

        public void setNextAvailableDate(Date nextAvailableDate) {
            this.nextAvailableDate = nextAvailableDate;
        }

        public Date getFollowingAvailableDate() {
            return followingAvailableDate;
        }

        public void setFollowingAvailableDate(Date followingAvailableDate) {
            this.followingAvailableDate = followingAvailableDate;
        }

        public String getSupplierCode() {
            return supplierCode;
        }

        public void setSupplierCode(String supplierCode) {
            this.supplierCode = supplierCode;
        }

        public String getOtherRemark() {
            return otherRemark;
        }

        public void setOtherRemark(String otherRemark) {
            this.otherRemark = otherRemark;
        }
}

