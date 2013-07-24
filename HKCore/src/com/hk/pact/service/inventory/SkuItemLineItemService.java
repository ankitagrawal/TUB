package com.hk.pact.service.inventory;

import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuItemLineItemService {

    public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId);

    public SkuItemLineItem getById(Long skuItemLineItemId);

    public SkuItemLineItem createNewSkuItemLineItem(SkuItem skuItem, LineItem lineItem, Long unitNum, SkuItemCLI skuItemCLI);

    public SkuItemLineItem save(SkuItemLineItem skuItemLineItem);

    public List<SkuItemLineItem> getSkuItemLineItemForLineItem(LineItem lineItem);

}
