package com.hk.pact.dao.InventoryManagement;

import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.shippingOrder.LineItem;

import java.util.Set;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 4:05:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InventoryManageService {

    public void saveSkuItemCLI(Set<SkuItem> skuItemsToBeBooked, CartLineItem cartLineItem);

    public void releaseSkuItemCLIForOrder(Order order);

    public List<SkuItem> getSkuItems(List<Sku> skus, Double mrp);

    public Long getAvailableUnBookedInventory(ProductVariant productVariant);

    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory);

    public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant);

    public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp);

    public boolean sicliAlreadyExists(CartLineItem cartLineItem,  Long unitNum);

    public boolean sicliAlreadyExists(CartLineItem cartLineItem);


}
