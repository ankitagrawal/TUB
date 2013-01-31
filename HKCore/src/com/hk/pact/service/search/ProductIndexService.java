package com.hk.pact.service.search;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.search.SolrProduct;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/3/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductIndexService {
    void indexProduct(Product product);

    void indexProduct(List<SolrProduct> products);

    void updateExtraProperties(Product pr, SolrProduct solrProduct);
}
