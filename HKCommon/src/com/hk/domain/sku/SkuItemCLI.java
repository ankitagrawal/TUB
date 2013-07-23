package com.hk.domain.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 3:02:16 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "sku_Item_CLI")
public class SkuItemCLI implements java.io.Serializable {

       @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long           id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;


     @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_item_id")
    private SkuItem               skuItem;


     @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_line_item_id")
    private CartLineItem cartItem;

    @Column(name = "unit_Number", nullable = false)
    private Long unitNumber;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CartLineItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartLineItem cartItem) {
        this.cartItem = cartItem;
    }


    public Long getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(Long unitNumber) {
        this.unitNumber = unitNumber;
    }
}
