package com.hk.domain.sku;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.shippingOrder.LineItem;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 3:25:11 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "sku_item_line_item")
public class SkuItemLineItem implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;


    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_item_id", nullable = false)
    private SkuItem skuItem;


    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_id", nullable = false)
    private LineItem lineItem;


    @JsonSkip
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_item_cart_line_item_id", nullable = false)
    private SkuItemCLI skuItemCLI;


    @Column(name = "unit_num", nullable = false)
    private Long unitNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(Long unitNumber) {
        this.unitNum = unitNumber;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public SkuItem getSkuItem() {
        return skuItem;
    }

    public void setSkuItem(SkuItem skuItem) {
        this.skuItem = skuItem;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public SkuItemCLI getSkuItemCLI() {
        return skuItemCLI;
    }

    public void setSkuItemCLI(SkuItemCLI skuItemCLI) {
        this.skuItemCLI = skuItemCLI;
    }
}
