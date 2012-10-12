package com.hk.rest.pact.service;

import com.hk.domain.catalog.product.Product;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 8/28/12
 * Time: 3:46 PM
 */
public interface APIProductService {

    public Product getProductById(String ProductId);

    public String syncContentAndDescription();

    public String syncProductImages();

}
