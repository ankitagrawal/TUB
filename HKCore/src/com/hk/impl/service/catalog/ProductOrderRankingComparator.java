package com.hk.impl.service.catalog;

import java.util.Comparator;

import com.hk.domain.catalog.product.Product;

public class ProductOrderRankingComparator implements Comparator<Product> {
    public int compare(Product o1, Product o2) {
        if (o1.getOrderRanking() != null && o2.getOrderRanking() != null) {
            return o1.getOrderRanking().compareTo(o2.getOrderRanking());
        }
        return -1;
    }
}