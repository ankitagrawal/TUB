package com.hk.pact.dao.sku;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.sku.SkuItemStatus;
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
}
