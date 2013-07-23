package com.hk.domain.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 3:25:11 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "sku_Item_Line_Item")
public class SkuItemLineItem implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;


    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_item_id")
    private SkuItem skuItem;


    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_id")
    private LineItem lineItem;


    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_item_cli_id")
    private SkuItemCLI skuItemCLI;


    @Column(name = "wait_Number", nullable = false)
    private Long waitNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWaitNumber() {
        return waitNumber;
    }

    public void setWaitNumber(Long waitNumber) {
        this.waitNumber = waitNumber;
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
