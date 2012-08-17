package com.hk.util;

import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.catalog.product.Product;

public class ProductUtil {

    public static boolean isComboInStock(Combo combo) {
        if (combo.isDeleted() != null && combo.isDeleted()) {
            return false;
        } else {
            for (ComboProduct comboProduct : combo.getComboProducts()) {
                if (!comboProduct.getAllowedProductVariants().isEmpty() && comboProduct.getAllowedInStockVariants().isEmpty()) {
                    return false;
                } else if (comboProduct.getProduct().getInStockVariants().isEmpty()) {
                    return false;
                } else if (comboProduct.getProduct().isDeleted() != null && comboProduct.getProduct().isDeleted()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isDisplayedInResults(SuperSaverImage superSaverImage) {
        Product product = superSaverImage.getProduct();
        if (product != null) {
            if (product instanceof Combo) {
                Combo combo = (Combo) product;
                return isComboInStock(combo);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}

