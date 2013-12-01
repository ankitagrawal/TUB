package com.hk.domain.store;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/21/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="store_product")
public class StoreProduct {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", unique = true, nullable = false)
    private Store store;

    @Column(name = "store_price", nullable = false)
    private Double storePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", unique = true, nullable = false)
    private ProductVariant productVariant;

    @Column(name = "hidden", nullable = false)
    private boolean hidden;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Double getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(Double storePrice) {
        this.storePrice = storePrice;
    }
}
