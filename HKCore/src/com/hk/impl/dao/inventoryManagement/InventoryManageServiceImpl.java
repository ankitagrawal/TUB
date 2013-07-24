package com.hk.impl.dao.inventoryManagement;

import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageService;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.warehouse.Warehouse;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 4:06:36 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class InventoryManageServiceImpl implements InventoryManageService {

    @Autowired
    SkuService skuService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private InventoryManageDao inventoryManageDao;
    @Autowired
    SkuItemLineItemService skuItemLineItemService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    UserService userService;


    // Call this method from payment action  java
    public void tempBookSkuLineItemForOrder(Order order) {
        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
//        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            ProductVariant productVariant = cartLineItem.getProductVariant();
            // assuming we are going to have warehouse on product variant
            /*
            Warehouse warehouse = null;
           Sku sku = skuService.getSKU(productVariant, warehouse); //
             */

            List<Sku> skus = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
            Sku sku = skus.get(0);

//            List<SkuItem> skuItems =inventoryManageDao.getCheckedInSkuItems(sku, productVariant.getMarkedPrice());
            long qtyToBeSet = cartLineItem.getQty();
            Set<SkuItem> skuItemsToBeBooked = new HashSet<SkuItem>();

            for (int i = 0; i < qtyToBeSet; i++) {
                SkuItem skuItem = inventoryManageDao.getCheckedInSkuItems(sku, productVariant.getMarkedPrice()).get(0);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                // todo Pvi entries
                skuItem = (SkuItem) getBaseDao().save(skuItem);
                // todo UpdatePrice and Mrp qyt
                skuItemsToBeBooked.add(skuItem);
            }


            // Call method to make new entries in SKUItemCLI
            saveSkuItemCLI(skuItemsToBeBooked, cartLineItem);

        }
    }


    //     Make Entries in new SkuItemCLI  table
    public void saveSkuItemCLI(Set<SkuItem> skuItemsToBeBooked, CartLineItem cartLineItem) {
        Long count = 1L;
        for (SkuItem si : skuItemsToBeBooked) {
            SkuItemCLI skuItemCLI = new SkuItemCLI();
            skuItemCLI.setCartLineItem(cartLineItem);
            skuItemCLI.setProductVariant(cartLineItem.getProductVariant());
            skuItemCLI.setUnitNum(count);
            skuItemCLI.setSkuItem(si);
            getBaseDao().save(skuItemCLI);
            count++;
        }
    }


    /// Releasing the SkuItem in case of Payment Failure  or Error
    public void releaseSkuItemCLIForOrder(Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            // get Entries of SkuItemCLI corresponding to cartLineItem
            List<SkuItemCLI> skuItemCLIs = cartLineItem.getSkuItemCLIs();
            Iterator<SkuItemCLI> iterator = skuItemCLIs.iterator();
            while (iterator.hasNext()) {
                SkuItemCLI skuItemCLI = iterator.next();
                SkuItem skuItem = skuItemCLI.getSkuItem();
                // todo Pvi entries
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                getBaseDao().save(skuItem);
                // todo UpdatePrice and Mrp qyt
                iterator.remove();
                getBaseDao().delete(skuItemCLI);
            }

        }
    }


    // get list Of SkuItems to booked

    public List<SkuItem> getSkuItems(List<Sku> skus, Double mrp) {
        List<SkuItem> skuItemList = new ArrayList<SkuItem>();
        if (skus != null && skus.size() > 0) {
            for (Sku sku : skus) {
                List<SkuItem> skuItems = skuGroupService.getCheckedInSkuItems(sku);
                if (skuItems != null && skuItems.size() > 0) {
                    for (SkuItem item : skuItems) {
                        if (item.getSkuGroup().getMrp().equals(mrp)) {
                            skuItemList.add(item);
                        }
                    }
                }
            }
        }
        return skuItemList;
    }
    
    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
