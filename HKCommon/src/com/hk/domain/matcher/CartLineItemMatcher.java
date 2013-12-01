package com.hk.domain.matcher;

import java.util.List;
import java.util.Set;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.CartLineItemExtraOption;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vaibhav.adlakha
 */
public class CartLineItemMatcher {

    private ProductVariant                productVariant;
    private ComboInstance                 comboInstance;
    private CartLineItemConfig            cartLineItemConfig;
    private List<CartLineItemExtraOption> extraOptions;
    private EnumCartLineItemType          enumCartLineItemType;
    private Long                          cartLineItemTypeId;

    @Autowired
    // ComboInstance comboInstance;
    public CartLineItemMatcher addProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
        return this;
    }

    public CartLineItemMatcher addComboInstance(ComboInstance comboInstance) {
        this.comboInstance = comboInstance;
        return this;
    }

    public CartLineItemMatcher addCartLineItemConfig(CartLineItemConfig cartLineItemConfig) {
        this.cartLineItemConfig = cartLineItemConfig;
        return this;
    }

    public CartLineItemMatcher addExtraOptions(List<CartLineItemExtraOption> extraOptions) {
        this.extraOptions = extraOptions;
        return this;
    }

    public CartLineItemMatcher addCartLineItemType(EnumCartLineItemType enumCartLineItemType) {
        this.enumCartLineItemType = enumCartLineItemType;
        return this;
    }

    public CartLineItemMatcher addCartLineItemTypeId(Long cartLineItemTypeId) {
        this.cartLineItemTypeId = cartLineItemTypeId;
        return this;
    }

    /**
     * this method tries to find a cart line item matching the criteria from list of cartLineItemsToMatchFrom and
     * returns it, returns null if no match found.
     * 
     * @param cartLineItemsToMatchFrom
     * @return
     */
    public CartLineItem match(Set<CartLineItem> cartLineItemsToMatchFrom) {

        for (CartLineItem cartLineItem : cartLineItemsToMatchFrom) {
            boolean matchFound = true;
            if (productVariant != null && !productVariant.equals(cartLineItem.getProductVariant())) {
                matchFound = false;
            }
            if (matchFound && productVariant != null && comboInstance != null && productVariant.equals(cartLineItem.getProductVariant())
                    && comboInstance.getCombo() ==null && cartLineItem.getComboInstance().getCombo() !=null && !comboInstance.getCombo().equals(cartLineItem.getComboInstance().getCombo())) {
                matchFound = false;
            }
            if (matchFound && productVariant != null && comboInstance != null && !productVariant.equals(cartLineItem.getProductVariant())) {
                matchFound = false;
            }
            // if (matchFound && comboInstance != null && !comboInstance.equals(cartLineItem.getComboInstance())) {
            // matchFound = false;
            // }

            if (matchFound && productVariant.equals(cartLineItem.getProductVariant()) && cartLineItemConfig == null && cartLineItem.getCartLineItemConfig() != null) {
                matchFound = false;
            }
            if (matchFound && cartLineItemConfig != null && !cartLineItemConfig.equals(cartLineItem.getCartLineItemConfig())) {
                matchFound = false;
            }
            if (matchFound && extraOptions != null && extraOptions.size() > 0) {
                for (CartLineItemExtraOption extraOption1 : extraOptions) {
                    for (CartLineItemExtraOption extraOption2 : cartLineItem.getCartLineItemExtraOptions()) {
                        if (extraOption1.getName().equals(extraOption2.getName())) {
                            if (!extraOption1.getValue().equals(extraOption2.getValue())) {
                                matchFound = false;
                            }
                        }
                    }
                }
            }
            if (matchFound && enumCartLineItemType != null) {
                if (!cartLineItem.isType(enumCartLineItemType)) {
                    matchFound = false;
                }
            }
            if (matchFound && cartLineItemTypeId != null) {
                if (cartLineItem.getLineItemType().getId().longValue() != cartLineItemTypeId.longValue()) {
                    matchFound = false;
                }
            }
            if (matchFound) {
                return cartLineItem;
            }
        }
        return null;
    }
    /*
     * for (CartLineItem cartLineItem : order.getProductCartLineItems()) { if
     * (productVariant.equals(cartLineItem.getProductVariant()) &&
     * comboInstance.equals(cartLineItem.getComboInstance())) { return cartLineItem; } }
     */

    /*
     * for (CartLineItem cartLineItem : order.getProductCartLineItems()) { if
     * (cartLineItem.getProductVariant().equals(productVariant)) { if
     * (lineItemConfig.equals(cartLineItem.getCartLineItemConfig())) { return cartLineItem; } } } for (CartLineItem
     * cartLineItem : order.getProductCartLineItems()) { if (productVariant.equals(cartLineItem.getProductVariant())) {
     * return cartLineItem; } } for (CartLineItem cartLineItem : order.getProductCartLineItems()) { if
     * (productVariant.equals(cartLineItem.getProductVariant())) { }
     */

}
