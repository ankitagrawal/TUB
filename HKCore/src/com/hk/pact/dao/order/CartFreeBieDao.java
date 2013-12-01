package com.hk.pact.dao.order;

import java.util.List;

import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.BaseDao;

public interface CartFreeBieDao extends BaseDao {

  public Double getCartValueForProducts(List<String> productList, Order order);

  public Double getCartValueForVariants(List<String> productVariantList, Order order);

  public Double getCartValueForProducts(List<String> productList, ShippingOrder order);

  public Double getCartValueForVariants(List<String> productVariantList, ShippingOrder order);

}
