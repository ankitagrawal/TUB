package com.hk.pact.dao.catalog.product;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductCount;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface ProductCountDao extends BaseDao {

    public void getOrCreateProductCount(Product product, User user);

    public ProductCount findByProduct(Product product);

}
