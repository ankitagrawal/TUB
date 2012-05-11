package com.hk.pact.dao.order;

import java.util.List;

import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

public interface CartFreeBieDao extends BaseDao {

    public Double getCartValueForProducts(List<String> productList, Order order);
    
    public Double getCartValueForVariants(List<String> productVariantList, Order order);
    
}
