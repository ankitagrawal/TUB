package com.hk.pact.dao.InventoryManagement;

import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.constants.sku.EnumSkuItemStatus;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 24, 2013
 * Time: 2:26:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InventoryManageDao {

    public Long getAvailableUnBookedInventory(List<Sku> skuList);

    public Long getNetInventory(List<Sku> skuList, List<Long> skuItemStatusIds);

    public Long getNetInventory(List<Sku> skuList, Double mrp);

    // Method to get temp Booked and Booked qty
    public Long getTempOrBookedQtyOfProductVariantInQueue(ProductVariant productVariant, List<Long> skuItemStatusId, List<Long> skuItemOwnerStatusId);

    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory);

    public List<SkuItem> getCheckedInSkuItems(Sku sku, Double mrp);

    public Double getFirstcheckedInBatchMRP(ProductVariant productVariant);

    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList);

    public List<CartLineItem> getClisForInPlacedOrder(ProductVariant productVariant, Double mrp);

    public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant);

    public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp);

    public boolean sicliAlreadyExists(CartLineItem cartLineItem);


}
