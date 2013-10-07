package com.hk.pact.dao.sku;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuItemLineItemDao extends BaseDao {

  public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId);

  public List<SkuItemCLI> getSkuItemCLI(CartLineItem cartLineItem, List<SkuItemStatus> skuItemStatusIds);

  public SkuItemCLI getSkuItemCLI(SkuItem skuItem);

  public SkuItemLineItem getSkuItemLineItem(SkuItem skuItem);

  public List<SkuItemCLI> getSkuItemCLIs(CartLineItem cartLineItem);

  public List<SkuItemLineItem> getSkuItemLIsTemp();

  public List<SkuItemLineItem> getSkuItemLIsTemp(SkuItem skuItem);

  public boolean sicliAlreadyExists(CartLineItem cartLineItem);

  public Long getUnbookedCLICount(ProductVariant productVariant);

  public Long getUnbookedLICount(List<Sku> skuList, Double mrp);
}
