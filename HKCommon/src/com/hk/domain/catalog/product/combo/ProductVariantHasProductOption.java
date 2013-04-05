package com.hk.domain.catalog.product.combo;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.ProductOption;
import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 4/4/13
 * Time: 9:32 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "product_variant_has_product_option", uniqueConstraints = @UniqueConstraint(columnNames = {"product_variant_id", "product_option_id"}))
public class ProductVariantHasProductOption implements java.io.Serializable {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption product_option;

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public ProductOption getProduct_option() {
        return product_option;
    }

    public void setProduct_option(ProductOption product_option) {
        this.product_option = product_option;
    }
}
