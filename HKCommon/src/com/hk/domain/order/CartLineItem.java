package com.hk.domain.order;

import com.akube.framework.gson.JsonSkip;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.VariantConfigOptionParam;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.subscription.Subscription;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "cart_line_item")
public class CartLineItem implements java.io.Serializable, Comparable<CartLineItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "qty", nullable = false)
    private Long qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Column(name = "marked_price", nullable = false, precision = 10)
    private Double markedPrice;

    @Column(name = "hk_price", nullable = false, precision = 10)
    private Double hkPrice;

    /*
     * @Deprecated @Column(name = "cost_price", nullable = false, precision = 10) private Double costPrice;
     */

    @Column(name = "discount_on_hk_price", nullable = false)
    private Double discountOnHkPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_type_id", nullable = false)
    private CartLineItemType lineItemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_instance_id")
    private ComboInstance comboInstance;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_line_item_config_id")
    private CartLineItemConfig cartLineItemConfig;

    /*
     * @Deprecated @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tax_id") private Tax tax;
     */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cartLineItem", cascade = CascadeType.ALL)
    private List<CartLineItemExtraOption> cartLineItemExtraOptions = new ArrayList<CartLineItemExtraOption>(0);

    /*
     * @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "line_item_status_id", nullable = false) private
     * LineItemStatus lineItemStatus;
     */

    @Column(name = "version", nullable = false)
    private Long version = new Long(1);

    @SuppressWarnings("unused")
    @JsonSkip
    @OneToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "subscription_cart_line_item", joinColumns = @JoinColumn(name = "cart_line_item_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subscription_id", referencedColumnName = "id"))
    private Subscription subscription;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_referrer_id", nullable = true)
    private ProductReferrer productReferrer;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate = new Date();


    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cartLineItem")
    private List<SkuItemCLI> skuItemCLIs = new ArrayList<SkuItemCLI>();


    public CartLineItem() {

    }

    public CartLineItem(CartLineItemType lineItemType, ProductVariant productVariant, Long qty, Double markedPrice, Double hkPrice, Double discountOnHkPrice) {
        this.lineItemType = lineItemType;
        this.productVariant = productVariant;
        this.qty = qty;

        this.markedPrice = markedPrice;
        this.hkPrice = hkPrice;
        this.discountOnHkPrice = discountOnHkPrice;
    }

    public CartLineItem(ProductVariant productVariant, Long qty) {
        this.productVariant = productVariant;
        this.lineItemType = EnumCartLineItemType.Product.asCartLineItemType();
        this.qty = qty;
        // this.tax = productVariant.getTax();
        this.markedPrice = productVariant.getMarkedPrice();
        this.hkPrice = productVariant.getHkPrice(null);
        this.discountOnHkPrice = 0D;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Double getMarkedPrice() {
        if (markedPrice == null) {
            return 0D;
        }
        return markedPrice;
    }

    public void setMarkedPrice(Double markedPrice) {
        this.markedPrice = markedPrice;
    }

    public Double getHkPrice() {
        // TODO: #imp can someone please explain why this 0 was added here

        if (hkPrice == null) {
            return 0D;
        }
        return hkPrice;
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = hkPrice;
    }

    /*
     * @Deprecated public Double getCostPrice() { return costPrice; }
     * @Deprecated public void setCostPrice(Double costPrice) { this.costPrice = costPrice; }
     */
    public CartLineItemType getLineItemType() {
        return lineItemType;
    }

    public void setLineItemType(CartLineItemType lineItemType) {
        this.lineItemType = lineItemType;
    }

    /*
     * public LineItemStatus getLineItemStatus() { return lineItemStatus; } public void setLineItemStatus(LineItemStatus
     * lineItemStatus) { this.lineItemStatus = lineItemStatus; }
     */

    public ComboInstance getComboInstance() {
        return comboInstance;
    }

    public void setComboInstance(ComboInstance comboInstance) {
        this.comboInstance = comboInstance;
    }

    public CartLineItemConfig getCartLineItemConfig() {
        return cartLineItemConfig;
    }

    public void setLineItemConfig(CartLineItemConfig lineItemConfig) {
        this.cartLineItemConfig = lineItemConfig;
    }

    /*
     * @Deprecated public Tax getTax() { return tax; }
     * @Deprecated public void setTax(Tax tax) { this.tax = tax; }
     */

    public List<CartLineItemExtraOption> getCartLineItemExtraOptions() {
        return cartLineItemExtraOptions;
    }

    public void setCartLineItemExtraOptions(List<CartLineItemExtraOption> cartLineItemExtraOptions) {
        this.cartLineItemExtraOptions = cartLineItemExtraOptions;
    }

    public Double getDiscountOnHkPrice() {
        return (discountOnHkPrice != null ? discountOnHkPrice : 0);
    }

    public void setDiscountOnHkPrice(Double discountOnHkPrice) {
        this.discountOnHkPrice = discountOnHkPrice;
    }

    public boolean isType(EnumCartLineItemType enumCartLineItemType) {
        return enumCartLineItemType.getId().equals(this.getLineItemType().getId());
    }

    public ProductReferrer getProductReferrer() {
        return productReferrer;
    }

    public void setProductReferrer(ProductReferrer productReferrer) {
        this.productReferrer = productReferrer;
    }

    public int compareTo(CartLineItem cartLineItem) {
        if (this.getId() < cartLineItem.getId())
            return -1;
        if (this.getId() > cartLineItem.getId())
            return 1;
        return 0;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getExtraOptionsPipeSeparated() {
        StringBuilder stringBuffer = new StringBuilder();
        if (cartLineItemExtraOptions != null) {
            for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItemExtraOptions) {
                stringBuffer.append(cartLineItemExtraOption.getName()).append(":").append(cartLineItemExtraOption.getValue());
                stringBuffer.append(" |");
            }
            if (stringBuffer.length() > 0 && stringBuffer.charAt(stringBuffer.length() - 1) == '|') {
                return stringBuffer.substring(0, stringBuffer.length() - 1);
            }
        }

        return stringBuffer.toString();
    }

    public String getConfigOptionsPipeSeparated() {
        StringBuilder stringBuffer = new StringBuilder();
        if (cartLineItemConfig.getCartLineItemConfigValues() != null) {
            for (CartLineItemConfigValues cartLineItemConfigValue : cartLineItemConfig.getCartLineItemConfigValues()) {
                stringBuffer.append(cartLineItemConfigValue.getVariantConfigOption().getDisplayName()).append(":").append(cartLineItemConfigValue.getValue());
                String additionalParam = cartLineItemConfigValue.getVariantConfigOption().getAdditionalParam();
                if (!(additionalParam.equals(VariantConfigOptionParam.THICKNESS.param()) || additionalParam.equals(VariantConfigOptionParam.BFTHICKNESS.param())
                        || additionalParam.equals(VariantConfigOptionParam.COATING.param()) || additionalParam.equals(VariantConfigOptionParam.BFCOATING.param()))) {
                    String configName = cartLineItemConfigValue.getVariantConfigOption().getName();
                    if (configName.startsWith("R"))
                        stringBuffer.append("(R) ");
                    if (configName.startsWith("L"))
                        stringBuffer.append("(L) ");
                }
                if (additionalParam.equals(VariantConfigOptionParam.ENGRAVING.param())) {
                    stringBuffer.append(" <b>+Rs. ").append(cartLineItemConfigValue.getAdditionalPrice()).append("</b>");
                }
                stringBuffer.append(" |");
            }
            if (stringBuffer.length() > 0 && stringBuffer.charAt(stringBuffer.length() - 1) == '|') {
                return stringBuffer.substring(0, stringBuffer.length() - 1);
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o instanceof CartLineItem) {
            CartLineItem cartLineItem = (CartLineItem) o;

            if (this.id != null && cartLineItem.getId() != null && this.id.equals(cartLineItem.getId()) && this.version.equals(cartLineItem.getVersion())) {
                return true;
            } else {
                EqualsBuilder equalsBuilder = new EqualsBuilder();
                if (this.getOrder() != null && cartLineItem.getOrder() != null) {
                    equalsBuilder.append(this.getOrder().getId(), cartLineItem.getOrder().getId());
                }
                if (this.getProductVariant() != null && cartLineItem.getProductVariant() != null) {
                    equalsBuilder.append(this.getProductVariant().getId(), cartLineItem.getProductVariant().getId());
                }
                if (this.qty != null && cartLineItem.getQty() != null) {
                    equalsBuilder.append(this.qty, cartLineItem.getQty());
                }
                if (this.lineItemType != null && cartLineItem.getLineItemType() != null) {
                    equalsBuilder.append(this.lineItemType.getId(), cartLineItem.getLineItemType().getId());
                }
                if (this.getCartLineItemExtraOptions() != null && cartLineItem.getCartLineItemExtraOptions() != null) {
                    equalsBuilder.append(this.getCartLineItemExtraOptions(), cartLineItem.getCartLineItemExtraOptions());
                }
                if (this.getCartLineItemConfig() != null && cartLineItem.getCartLineItemConfig() != null) {
                    equalsBuilder.append(this.getCartLineItemConfig(), cartLineItem.getCartLineItemConfig());
                }
                return equalsBuilder.isEquals();
            }

        }

        return false;
    }

    @Override
    public int hashCode() {
        if (this.id != null) {
            return new HashCodeBuilder().append(this.id).toHashCode();
        } else {
            HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
            if (this.getOrder() != null) {
                hashCodeBuilder.append(this.getOrder().getId());
            }
            if (this.getProductVariant() != null) {
                hashCodeBuilder.append(this.getProductVariant().getId());
            }
            if (this.lineItemType != null) {
                hashCodeBuilder.append(this.lineItemType.getId());
            }
            return hashCodeBuilder.toHashCode();
        }
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<SkuItemCLI> getSkuItemCLIs() {
        return skuItemCLIs;
    }

    public void setSkuItemCLIs(List<SkuItemCLI> skuItemCLIs) {
        this.skuItemCLIs = skuItemCLIs;
    }
}
