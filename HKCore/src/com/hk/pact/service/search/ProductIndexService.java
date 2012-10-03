package com.hk.pact.service.search;

import com.hk.domain.catalog.product.Product;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/3/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductIndexService {
    void indexProduct(Product product);
}
