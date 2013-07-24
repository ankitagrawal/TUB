package com.hk.impl.dao.inventoryManagement;

import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.constants.sku.EnumSkuItemStatus;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 4:06:36 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class InventoryManageServiceImpl {

    @Autowired
    SkuService skuService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    SkuItemLineItemService skuItemLineItemService;
    @Autowired
    AdminInventoryService adminInventoryService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    UserService userService;


    // Call this method from payment action  java
    public void bookSkuLineItemForOrder(Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            cartLineItem.getProductVariant();
            List<Sku> skus = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
            List<SkuItem> skuItems = getSkuItems(skus, cartLineItem.getProductVariant().getMarkedPrice());
            long qtyToBeSet = cartLineItem.getQty();
            Set<SkuItem> skuItemsToBeBooked = new HashSet<SkuItem>();
            if (skuItems.size() > 0) {
                for (int i = 0; i < qtyToBeSet; i++) {
                    for (SkuItem skuItem : skuItems) {
                        skuItem.setSkuItemStatus(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
                        skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                        skuItem = (SkuItem) getBaseDao().save(skuItem);
                        skuItemsToBeBooked.add(skuItem);
                    }
                }
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
      public void releaseSkuLineItemForOrder(Order order){
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems){
            // get Entries of SkuItemCLI corresponding to cartLineItem
             List<SkuItemCLI> skuItemCLIs =  cartLineItem.getSkuItemCLIs();
            Iterator<SkuItemCLI> iterator = skuItemCLIs.iterator();
              while(iterator.hasNext()){
                   SkuItemCLI skuItemCLI = iterator.next();
                   SkuItem skuItem =  skuItemCLI.getSkuItem();
                   skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                   getBaseDao().save(skuItem);
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

    public void checkoutMethod(LineItem lineItem, SkuItem skuItem) {
        User loggedOnUser = userService.getLoggedInUser();
        List<SkuItemLineItem> skuItemLineItems = skuItemLineItemService.getSkuItemLineItem(lineItem, EnumSkuItemStatus.BOOKED.getId());
        List<SkuItem> skuItemInSkuItemLineItems = new ArrayList<SkuItem>();
        for(SkuItemLineItem item : skuItemLineItems){
            SkuItem si = item.getSkuItem();
            skuItemInSkuItemLineItems.add(si);
        }
        if (skuItemInSkuItemLineItems.contains(skuItem)) {
            // TODO:
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.CUSTOMER.getSkuItemOwnerStatus());
            skuItem = (SkuItem)baseDao.save(skuItem);
            adminInventoryService.inventoryCheckinCheckout(lineItem.getSku(), skuItem, lineItem, lineItem.getShippingOrder(), null, null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1l,loggedOnUser );

        } else {
            //If skuItem is booked

            if (skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.BOOKED.getSkuItemStatus())) {
                SkuItem toReleaseSkuItem = skuItemInSkuItemLineItems.get(0);
                toReleaseSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItem = (SkuItem)baseDao.save(toReleaseSkuItem);
                adminInventoryService.inventoryCheckinCheckout(lineItem.getSku(), skuItem, lineItem, lineItem.getShippingOrder(), null, null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1l,loggedOnUser );

                SkuItemLineItem skuItemLineItem = null;
                for(SkuItemLineItem item: skuItemLineItems){
                         if(item.getSkuItem().equals(skuItem)){
                             skuItemLineItem = item;
                         }
                }
                SkuItemLineItem item = new SkuItemLineItem();
                item.setSkuItem(skuItem);
                item.setProductVariant(lineItem.getSku().getProductVariant());
                item.setUnitNum(skuItemLineItem.getUnitNum());
                item.setLineItem(lineItem);
                baseDao.save(item);

                //release the already present

                //delete the entry from table t1
                baseDao.delete(toReleaseSkuItem);
            }
            if (skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.Checked_IN.getSkuItemStatus())) {
                // flip skuItem with a booked SI.
                // 1. Find a booked SI for this one

                //release the already present
                SkuItem toReleaseSkuItem = skuItemInSkuItemLineItems.get(0);
                toReleaseSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                baseDao.save(toReleaseSkuItem);

                //book the CSI
                skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItem = (SkuItem) baseDao.save(skuItem);

                SkuItemLineItem skuItemLineItem = null;
                for(SkuItemLineItem item: skuItemLineItems){
                    if(item.getSkuItem().equals(skuItem)){
                        skuItemLineItem = item;
                    }
                }
                SkuItemLineItem item = new SkuItemLineItem();
                item.setSkuItem(skuItem);
                item.setProductVariant(lineItem.getSku().getProductVariant());
                item.setUnitNum(skuItemLineItem.getUnitNum());
                item.setLineItem(lineItem);
                baseDao.save(item);


                //delete the entry from table t1
                baseDao.delete(toReleaseSkuItem);
            }
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
