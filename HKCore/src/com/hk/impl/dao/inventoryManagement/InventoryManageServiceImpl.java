package com.hk.impl.dao.inventoryManagement;

import com.hk.constants.sku.EnumSkuItemOwner;
// import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.SQLQuery;
import org.hibernate.Hibernate;
import org.hibernate.transform.Transformers;
import com.hk.pact.service.inventory.*;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageService;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.warehouse.Warehouse;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
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
    @Autowired
    InventoryHealthService inventoryHealthService;

    @Autowired
    WarehouseService warehouseService;


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

    public void checkoutMethod(LineItem lineItem, SkuItem skuItem) {
        User loggedOnUser = userService.getLoggedInUser();
        List<SkuItemLineItem> skuItemLineItems = skuItemLineItemService.getSkuItemLineItem(lineItem, EnumSkuItemStatus.BOOKED.getId());
        List<SkuItem> skuItemInSkuItemLineItems = new ArrayList<SkuItem>();
        for (SkuItemLineItem item : skuItemLineItems) {
            SkuItem si = item.getSkuItem();
            skuItemInSkuItemLineItems.add(si);
        }
        if (skuItemInSkuItemLineItems.contains(skuItem)) {
            // TODO:
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.CUSTOMER.getSkuItemOwnerStatus());
            skuItem = (SkuItem) baseDao.save(skuItem);
            //          adminInventoryService.inventoryCheckinCheckout(lineItem.getSku(), skuItem, lineItem, lineItem.getShippingOrder(), null, null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1l,loggedOnUser );

        } else {
            //If skuItem is booked

            if (skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.BOOKED.getSkuItemStatus())) {
                SkuItem toReleaseSkuItem = skuItemInSkuItemLineItems.get(0);
                toReleaseSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItem = (SkuItem) baseDao.save(toReleaseSkuItem);
                //       adminInventoryService.inventoryCheckinCheckout(lineItem.getSku(), skuItem, lineItem, lineItem.getShippingOrder(), null, null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1l,loggedOnUser );

                SkuItemLineItem skuItemLineItem = null;
                for (SkuItemLineItem item : skuItemLineItems) {
                    if (item.getSkuItem().equals(skuItem)) {
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
                for (SkuItemLineItem item : skuItemLineItems) {
                    if (item.getSkuItem().equals(skuItem)) {
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


    /*
    // Inventory Health Check


    1.       Get Net inventory of (product_variant) physical  --> checked in , temp-booked - booked
    2.       Available unbooked inventory   -->    those are in checked in status
    3.       if 2 is + ve  instock else out of stock
    4.       if instock -- current variant --- maximum qty in all warehouses at current mrp price.
    5.       if current batch finishes then  search first check in batch at all warehouse  and set its maximum qty  and prices.

    6.    if variant is out of stock  then need to make instock at inventory check in time aand updating all the prices and info

    */

    public void InventoryHealthCheck(ProductVariant productVariant) {
        Long availableBookedInventory = getAvailableUnBookedInventory(productVariant);

        if (availableBookedInventory > 0) {
            // to get Maximum qty in all warehouses at current mrp price
//          Collection<InventoryHealthService.SkuInfo> netPhysicalInvnList =  inventoryHealthService.getNetCheckedInInventory(productVariant, warehouseService.getServiceableWarehouses());
            // total  list of variant of same prices group together of same sku
          Collection<InventoryHealthService.SkuInfo> availableUnBookedInvnList = inventoryHealthService.getCheckedInInventory(productVariant, warehouseService.getServiceableWarehouses());

            Map<Long, InventoryHealthService.SkuInfo> m1 = new HashMap <Long,InventoryHealthService.SkuInfo> ();

             Map<Double, List<InventoryHealthService.SkuInfo>> priceMap = new HashMap <Double ,List<InventoryHealthService.SkuInfo>> ();

            // Available unbooked inventory list se update marna hai cuurent variant ki qty
            if (availableUnBookedInvnList != null && availableUnBookedInvnList.size() > 0) {

                 List<InventoryHealthService.SkuInfo> samePriceSkuInfo = new ArrayList<InventoryHealthService.SkuInfo>();

                // m1 has same price ki different sku ki information hai
                 for ( InventoryHealthService.SkuInfo info : availableUnBookedInvnList){
                      if (priceMap.containsKey(info.getMrp()) ){
                              samePriceSkuInfo.add(info) ;

                      }else {

                      }
                 }
                   InventoryHealthService.SkuInfo  maxSkuinfo = null;
                 for(Map.Entry<Long, InventoryHealthService.SkuInfo> entry: m1.entrySet() ){
                    InventoryHealthService.SkuInfo  skuInfo= entry.getValue() ;
                 }

            }


            


        } else {
            productVariant.setOutOfStock(true);
        }


    }


    public Long getAvailableUnBookedInventory(ProductVariant productVariant) {
        // get Net physical inventory
        List<Sku> skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
        List<EnumSkuItemStatus> statuses = EnumSkuItemStatus.getStatusForNetPhysicalInventory();
        Long netInventory = inventoryManageDao.getNetInventory(skuList, statuses);

        // get Booked Inventory
        List<Long> skuItemBookedStatus = new ArrayList<Long>();
        skuItemBookedStatus.add(EnumSkuItemStatus.BOOKED.getId());
        skuItemBookedStatus.add(EnumSkuItemStatus.TEMP_BOOKED.getId());

        List<Long> skuItemOwnerStatus = new ArrayList<Long>();
        skuItemOwnerStatus.add(EnumSkuItemOwner.SELF.getId());
        Long bookedInventory = inventoryManageDao.getTempOrBookedQtyOfProductVariantInQueue(productVariant, skuItemBookedStatus, skuItemOwnerStatus);

        return (netInventory - bookedInventory);
    }


    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
