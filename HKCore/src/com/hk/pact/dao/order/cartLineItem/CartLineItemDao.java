package com.hk.pact.dao.order.cartLineItem;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface CartLineItemDao extends BaseDao {

  public void remove(Long id);

  public CartLineItem save(CartLineItem cartLineItem);

  public void flipProductVariants(ProductVariant srcPV, ProductVariant dstPV, Order order);

  public CartLineItem getLineItem(ProductVariant productVariant, Order order);

  public List<CartLineItem> getClisForInPlacedOrder(ProductVariant productVariant, Double mrp);

  public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp);

}
