package com.hk.domain.catalog.product;

// Generated 10 Mar, 2011 5:37:39 PM by Hibernate Tools 3.2.4.CR1

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.akube.framework.gson.JsonSkip;
import com.google.gson.annotations.Expose;
import com.hk.constants.core.EnumRole;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.core.ProductVariantServiceType;
import com.hk.domain.courier.BoxSize;
import com.hk.domain.warehouse.Warehouse;
import com.hk.edge.pact.service.StoreVariantService;
import com.hk.edge.response.variant.StoreVariantBasicApiResponse;
import com.hk.service.ServiceLocatorFactory;
import com.hk.web.AppConstants;

@SuppressWarnings("serial")
@Entity
@Table(name = "product_variant")
public class ProductVariant implements java.io.Serializable {

    @Expose
    @Id
    @Column(name = "id", nullable = false, length = 12)
    private String                    id;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product                   product;

    @Column(name = "variant_name", nullable = true)
    private String                    variantName;

    @Expose
    @Column(name = "hk_price", nullable = false)
    private Double                    hkPrice;

    @Expose
    @Column(name = "b2b_price")
    private Double                    b2bPrice;

    @Expose
    @Column(name = "marked_price", nullable = false)
    private Double                    markedPrice;

    @Expose
    @Column(name = "discount_percent", nullable = false)
    private Double                    discountPercent;

    @Expose
    @Column(name = "cost_price", nullable = false)
    private Double                    costPrice;

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "product_variant_has_product_option", joinColumns = { @JoinColumn(name = "product_variant_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "product_option_id", nullable = false, updatable = false) })
    private List<ProductOption>       productOptions      = new ArrayList<ProductOption>(0);

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "product_variant_has_product_extra_option", joinColumns = { @JoinColumn(name = "product_variant_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "product_extra_option_id", nullable = false, updatable = false) })
    private List<ProductExtraOption>  productExtraOptions = new ArrayList<ProductExtraOption>(0);

    @Column(name = "shipping_base_qty", nullable = false)
    private Long                      shippingBaseQty;

    @Column(name = "shipping_base_price", nullable = false)
    private Double                    shippingBasePrice;

    @Column(name = "shipping_add_qty", nullable = false)
    private Long                      shippingAddQty;

    @Column(name = "shipping_add_price", nullable = false)
    private Double                    shippingAddPrice;

    @Column(name = "out_of_stock", nullable = false)
    @Expose
    private boolean                   outOfStock;

    @Column(name = "deleted", nullable = false)
    private boolean                   deleted;

    @Column(name = "order_ranking", nullable = true)
    private Double                    orderRanking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse                 warehouse;

    @Transient
    private Long                      qty;

    @Transient
    private Boolean                   selected;

    @Column(name = "color_hex")
    private String                    colorHex;

    @Column(name = "main_image_id")
    private Long                      mainImageId;

    @Column(name = "main_product_image_id")
    private Long                      mainProductImageId;

    @Column(name = "length")
    private Double                    length;

    @Column(name = "breadth")
    private Double                    breadth;

    @Column(name = "height")
    private Double                    height;

    @Column(name = "weight")
    private Double                    weight;

    @Column(name = "commission")
    private Double                    commission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_size_id")
    private BoxSize                   estimatedBoxSize;

    @Column(name = "upc")
    private String                    upc;                                                       // Universal Product
    // Code

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_category_name")
    private AffiliateCategory         affiliateCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id")
    private ProductVariantPaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_service_type_id")
    private ProductVariantServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_config_id")
    private VariantConfig             variantConfig;

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productVariant")
    private List<ProductImage>        productImages       = new ArrayList<ProductImage>();

    @Column(name = "cut_off_inventory")
    private Long                      cutOffInventory;

    @Column(name = "postpaid_amount")
    private Double                    postpaidAmount;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date                      createdDate         = new Date();

    @Column(name = "clearance_sale")
    private Boolean                   clearanceSale;

    @Column(name = "lead_time", nullable = true)
    private Long                      leadTime;

    @Column(name = "lead_time_factor", nullable = true)
    private Double                    leadTimeFactor;

    @Column(name = "buffer_time", nullable = true)
    private Long                      bufferTime;

    @Column(name = "consumption_time", nullable = true)
    private Long                      consumptionTime;

    @JsonSkip
    @Temporal(TemporalType.DATE)
    @Column(name = "next_available_date", nullable = true, length = 19)
    private Date                      nextAvailableDate;

    @JsonSkip
    @Temporal(TemporalType.DATE)
    @Column(name = "following_available_date", nullable = true, length = 19)
    private Date                      followingAvailableDate;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_product_variant_id", nullable = true)
    private ProductVariant            freeProductVariant;

    @Column(name = "supplier_code")
    private String                    supplierCode;

    @Column(name = "other_remark")
    private String                    otherRemark;

    @Transient
    @Expose
    private String                    optionsAuditString;

    @Column(name = "mrp_qty")
    private Long                      mrpQty;

    @Column(name = "net_qty")
    private Long                      netQty;

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public Long getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(Long mainImageId) {
        this.mainImageId = mainImageId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getVariantName() {
        if (AppConstants.isHybridRelease) {
            StoreVariantService storeVariantService = ServiceLocatorFactory.getService(StoreVariantService.class);
            StoreVariantBasicApiResponse storeVariantBasicApiResponse = storeVariantService.getStoreVariantBasicDetails(this.id);
            if (storeVariantBasicApiResponse != null) {
                return storeVariantBasicApiResponse.getName();
            }
            return variantName;
        }

        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Double getHkPrice() {
        return getHkPrice(null);
    }

    public Double getHkPrice(Set<String> userRoles) {
        if (userRoles != null && this.b2bPrice != null && userRoles.contains(EnumRole.B2B_USER.getRoleName())) {
            return this.b2bPrice;
        } else {
            return this.hkPrice;
        }
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = hkPrice;
    }

    public Double getB2bPrice() {
        return b2bPrice;
    }

    public void setB2bPrice(Double b2bPrice) {
        this.b2bPrice = b2bPrice;
    }

    public Double getDiscountOnHkPrice() {
        return markedPrice * discountPercent;
    }

    public Double getMarkedPrice() {
        return this.markedPrice;
    }

    public void setMarkedPrice(Double markedPrice) {
        this.markedPrice = markedPrice;
    }

    public List<ProductOption> getProductOptions() {
        if (productOptions != null) {
            List<ProductOption> clonedProductOptions = new ArrayList<ProductOption>(productOptions);
            if (clonedProductOptions != null && !clonedProductOptions.isEmpty())
                Collections.sort(clonedProductOptions, new ProductOptionComparator());
            return clonedProductOptions;
        }
        return null;
    }

    public void setProductOptions(List<ProductOption> productOptions) {
        this.productOptions = productOptions;
    }

    public List<ProductExtraOption> getProductExtraOptions() {
        List<ProductExtraOption> clonedProductExtraOptions = new ArrayList<ProductExtraOption>(productExtraOptions);

        return clonedProductExtraOptions;
    }

    public void setProductExtraOptions(List<ProductExtraOption> productExtraOptions) {
        this.productExtraOptions = productExtraOptions;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Long getShippingBaseQty() {
        return shippingBaseQty;
    }

    public void setShippingBaseQty(Long shippingBaseQty) {
        this.shippingBaseQty = shippingBaseQty;
    }

    public Double getShippingBasePrice() {
        return shippingBasePrice;
    }

    public void setShippingBasePrice(Double shippingBasePrice) {
        this.shippingBasePrice = shippingBasePrice;
    }

    public Long getShippingAddQty() {
        return shippingAddQty;
    }

    public void setShippingAddQty(Long shippingAddQty) {
        this.shippingAddQty = shippingAddQty;
    }

    public Double getShippingAddPrice() {
        return shippingAddPrice;
    }

    public void setShippingAddPrice(Double shippingAddPrice) {
        this.shippingAddPrice = shippingAddPrice;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public ProductVariantPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(ProductVariantPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public ProductVariantServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ProductVariantServiceType serviceType) {
        this.serviceType = serviceType;
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

    public Double getShippingForQty(Long qty) {
        double shippingAmount = 0;
        if (qty > this.getShippingBaseQty()) {
            shippingAmount += this.getShippingBasePrice();
            qty -= (this.getShippingBaseQty() + 1);
            shippingAmount += (this.getShippingAddPrice() * (qty / this.getShippingAddQty() + 1));
        } else {
            shippingAmount += this.getShippingBasePrice();
        }
        return shippingAmount;
    }

    private String getOptionsBySeparator(String separator, boolean valueOnly) {
        StringBuffer stringBuffer = new StringBuffer();
        if (productOptions != null && productOptions.size() > 0) {
            for (Iterator<ProductOption> productOptionIterator = productOptions.iterator(); productOptionIterator.hasNext();) {
                ProductOption productOption = productOptionIterator.next();
                if (valueOnly) {
                    stringBuffer.append(productOption.getValue());
                } else {
                    stringBuffer.append(productOption.getName()).append(":").append(productOption.getValue());
                }
                if (productOptionIterator.hasNext()) {
                    stringBuffer.append(separator);
                }
            }
        }
        return stringBuffer.toString();
    }

    public String getOptionsSlashSeparated() {
        return getOptionsBySeparator("/", true);
    }

    public String getOptionsCommaSeparated() {
        return getOptionsBySeparator(",", false);
    }

    public String getOptionsPipeSeparated() {
        return getOptionsBySeparator("|", false);
    }

    public String getExtraOptionsCommaSeparated() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Iterator<ProductExtraOption> productExtraOptionIterator = productExtraOptions.iterator(); productExtraOptionIterator.hasNext();) {
            ProductExtraOption productExtraOption = productExtraOptionIterator.next();
            stringBuffer.append(productExtraOption.getName()).append(":").append(productExtraOption.getValue());
            if (productExtraOptionIterator.hasNext()) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }

    public String getExtraOptionsPipeSeparated() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Iterator<ProductExtraOption> productExtraOptionIterator = productExtraOptions.iterator(); productExtraOptionIterator.hasNext();) {
            ProductExtraOption productExtraOption = productExtraOptionIterator.next();
            stringBuffer.append(productExtraOption.getName()).append(":").append(productExtraOption.getValue());
            if (productExtraOptionIterator.hasNext()) {
                stringBuffer.append("|");
            }
        }
        return stringBuffer.toString();
    }

    public String getColorOptionsValue() {
        for (ProductOption productOption : productOptions) {
            if (productOption.getName().equalsIgnoreCase("Color") || productOption.getName().equalsIgnoreCase("color code")) {
                return productOption.getValue();
            }
        }
        return "";
    }

    public String getProductOptionsWithoutColor() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Iterator<ProductOption> productOptionIterator = productOptions.iterator(); productOptionIterator.hasNext();) {
            ProductOption productOption = productOptionIterator.next();
            if (!productOption.getName().equalsIgnoreCase("Color")) {
                stringBuffer.append(productOption.getName()).append(": ").append(productOption.getValue());
                if (productOptionIterator.hasNext()) {
                    stringBuffer.append(", ");
                }
            }
        }
        return stringBuffer.toString();
    }

    public class ProductOptionComparator implements Comparator<ProductOption> {
        public int compare(ProductOption o1, ProductOption o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    /**
     * For reading in jsp EL
     * 
     * @return
     */
    public Boolean getOutOfStock() {
        return isOutOfStock();
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public boolean isDeleted() {
        return deleted;
    }

    /**
     * For reading in jsp EL
     * 
     * @return
     */
    public Boolean getDeleted() {
        return isDeleted();
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

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductVariant))
            return false;

        ProductVariant that = (ProductVariant) o;

        if (id != null ? !id.equals(that.getId()) : that.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id == null ? "" : id;
    }

    public AffiliateCategory getAffiliateCategory() {
        return affiliateCategory;
    }

    public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
        this.affiliateCategory = affiliateCategory;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getBreadth() {
        return breadth;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Long getCutOffInventory() {
        return cutOffInventory;
    }

    public void setCutOffInventory(Long cutOffInventory) {
        this.cutOffInventory = cutOffInventory;
    }

    public Long getMainProductImageId() {
        return mainProductImageId;
    }

    public void setMainProductImageId(Long mainProductImageId) {
        this.mainProductImageId = mainProductImageId;
    }

    public Double getPostpaidAmount() {
        if (postpaidAmount == null) {
            return 0D;
        }
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

    public VariantConfig getVariantConfig() {
        return variantConfig;
    }

    public void setVariantConfig(VariantConfig variantConfig) {
        this.variantConfig = variantConfig;
    }

    public Boolean isClearanceSale() {
        return clearanceSale;
    }

    public Boolean getClearanceSale() {
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

    public ProductVariant getFreeProductVariant() {
        return freeProductVariant;
    }

    public void setFreeProductVariant(ProductVariant freeProductVariant) {
        this.freeProductVariant = freeProductVariant;
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

    public String getOptionsAuditString() {
        return optionsAuditString;
    }

    public void setOptionsAuditString(String optionsAuditString) {
        this.optionsAuditString = optionsAuditString;
    }

    public Long getMrpQty() {
        return mrpQty;
    }

    public void setMrpQty(Long mrpQty) {
        this.mrpQty = mrpQty;
    }

    public Long getNetQty() {
        return netQty;
    }

    public void setNetQty(Long netQty) {
        this.netQty = netQty;
    }

    public BoxSize getEstimatedBoxSize() {
        return estimatedBoxSize;
    }

    public void setEstimatedBoxSize(BoxSize estimatedBoxSize) {
        this.estimatedBoxSize = estimatedBoxSize;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
