package com.hk.pact.dao.InventoryManagement;

import com.hk.domain.sku.Sku;
import com.hk.domain.catalog.product.ProductVariant;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 2:39:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductVariantInventoryDao  {

    public Long getNetInventory(Sku sku);
    public Long getNetInventory(List<Sku> skuList);
    public Long getNetInventory(List<Sku> skuList, Double mrp);





  // Method to get temp Booked and Booked qty
   public Long getTempOrBookedQtyOfProductVariantInQueue(ProductVariant productVariant ,Long skuItemStatusId, Long skuItemOwnerStatusId) ;

    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory);



}
