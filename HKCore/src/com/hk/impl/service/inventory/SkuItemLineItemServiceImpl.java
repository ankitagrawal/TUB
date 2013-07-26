package com.hk.impl.service.inventory;

import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:17 AM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class SkuItemLineItemServiceImpl implements SkuItemLineItemService{

    @Autowired
    SkuItemLineItemDao skuItemLineItemDao;

    @Autowired
    SkuItemDao skuItemDao;
    
    @Autowired
    SkuService skuService;

    @Override
    public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId) {
        return  getSkuItemLineItemDao().getSkuItemLineItem(lineItem, skuItemStatusId);
    }

    @Override
    public SkuItemLineItem getById(Long skuItemLineItemId) {
        return getSkuItemDao().get(SkuItemLineItem.class, skuItemLineItemId);
    }

    @Override
    public SkuItemLineItem createNewSkuItemLineItem(LineItem lineItem) {
        Long unitNum = 0L;
        CartLineItem cartLineItem = lineItem.getCartLineItem();
        unitNum = 0L;
        for(SkuItemCLI skuItemCLI : cartLineItem.getSkuItemCLIs()){
            unitNum ++;
            SkuItemLineItem skuItemLineItem= new SkuItemLineItem();
            if(lineItem.getShippingOrder().getWarehouse().equals(skuItemCLI.getSkuItem().getSkuGroup().getSku().getWarehouse())){

                //Make skuItemLine item as copy of skuItemCLI
                skuItemLineItem.setSkuItem(skuItemCLI.getSkuItem());
                skuItemLineItem.setLineItem(lineItem);
                skuItemLineItem.setUnitNum(unitNum);
                skuItemLineItem.setSkuItemCLI(skuItemCLI);
                skuItemLineItem.setProductVariant(skuItemCLI.getSkuItem().getSkuGroup().getSku().getProductVariant());

                //Book the sku item
                skuItemCLI.getSkuItem().setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());

                skuItemLineItem = save(skuItemLineItem);
           }
            else{
                List<Sku> skuList = new ArrayList<Sku>();
                List<Long> skuStatusIdList = new ArrayList<Long>();
                List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();

                skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());
                skuList.add(skuItemCLI.getSkuItem().getSkuGroup().getSku());
                skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

                //get available sku items of the given warehouse at given mrp
                List<SkuItem> availableUnbookedSkuItems = getSkuItemDao().getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());

                SkuItem skuItem = availableUnbookedSkuItems.get(0);
                //Book the sku item first
                skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());

                //create skuItemLineItem entry
                skuItemLineItem.setSkuItem(skuItem);
                skuItemLineItem.setLineItem(lineItem);
                skuItemLineItem.setUnitNum(unitNum);
                skuItemLineItem.setSkuItemCLI(skuItemCLI);
                skuItemLineItem.setProductVariant(skuItem.getSkuGroup().getSku().getProductVariant());

                //Free existing skuitem on skuItemCLI
                skuItemCLI.getSkuItem().setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());

                //save the state
                skuItemLineItem = save(skuItemLineItem);

                //set new sku item on skuItemCLI as well
                skuItemCLI.setSkuItem(skuItem);
                skuItemLineItem.setSkuItemCLI(skuItemCLI);

                skuItemLineItem = save(skuItemLineItem);
            }
            //todo tarun erp
            //make entry in product variant inventory
        }
        return null;
    }

    @Override
    public boolean isWarehouseBeFlippable(ShippingOrder shippingOrder, Warehouse targetWarehouse){
        Sku sku = null;
        List<SkuItem> availableUnbookedSkuItems = null;
        List<Sku> skuList = new ArrayList<Sku>();
        List<Long> skuStatusIdList = new ArrayList<Long>();
        List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();

        skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());        
        skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

        List<SkuItem> toBeFreedSkuItemList = new ArrayList<SkuItem>();
        
        for(LineItem lineItem : shippingOrder.getLineItems()){
            sku = getSkuService().getSKU(lineItem.getSku().getProductVariant(), targetWarehouse);
            skuList.add(sku);
            availableUnbookedSkuItems = getSkuItemDao().getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());
            
            if(availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > lineItem.getQty()){
                List<SkuItemLineItem> skuItemLineItemList = lineItem.getSkuItemLineItems();
                for(SkuItemLineItem skuItemLineItem : skuItemLineItemList){
                    SkuItem toBeFreedSkuItem = skuItemLineItem.getSkuItem();
                    SkuItem skuItem = availableUnbookedSkuItems.get(skuItemLineItem.getUnitNum().intValue()-1);
                    skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());

                    skuItemLineItem.setSkuItem(skuItem);
                    skuItemLineItem.getSkuItemCLI().setSkuItem(skuItem);

                    toBeFreedSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                    toBeFreedSkuItemList.add(toBeFreedSkuItem);
                }
                getSkuItemLineItemDao().saveOrUpdate(skuItemLineItemList);
            }
            else{
                return false;
            }
        }
        if(toBeFreedSkuItemList != null && toBeFreedSkuItemList.size() > 0){
            getSkuItemDao().saveOrUpdate(toBeFreedSkuItemList);
            return true;
        }
        return false;
    }

    public Boolean freeInventoryForSOCancellation(ShippingOrder shippingORder){
        List<SkuItem> skuItemsToBeFreed = new ArrayList<SkuItem>();
        List<SkuItemLineItem> skuItemLineItemsToBeDeleted = new ArrayList<SkuItemLineItem>();
        List<SkuItemCLI> skuItemCLIsToBeDeleted = new ArrayList<SkuItemCLI>();

        for(LineItem lineItem : shippingORder.getLineItems()){
            for (SkuItemLineItem skuItemLineItem: lineItem.getSkuItemLineItems()){
                SkuItem skuItem = skuItemLineItem.getSkuItem();
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                skuItemsToBeFreed.add(skuItem);
            }
            skuItemLineItemsToBeDeleted.addAll(lineItem.getSkuItemLineItems());
            skuItemCLIsToBeDeleted.addAll(lineItem.getCartLineItem().getSkuItemCLIs());
        }
        getSkuItemDao().saveOrUpdate(skuItemsToBeFreed);
        getSkuItemDao().deleteAll(skuItemLineItemsToBeDeleted);
        getSkuItemDao().deleteAll(skuItemCLIsToBeDeleted);
        return true;
    }
    
    
    @Override
    public SkuItemLineItem save(SkuItemLineItem skuItemLineItem) {
        return (SkuItemLineItem)getSkuItemDao().save(skuItemLineItem);
    }

    @Override
    public List<SkuItemLineItem> getSkuItemLineItemForLineItem(LineItem lineItem) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    public SkuItemLineItem getBySkuItemId(Long skuItemId){
    	return getSkuItemDao().get(SkuItemLineItem.class, skuItemId);
    }
    
    public SkuItemLineItemDao getSkuItemLineItemDao() {
        return skuItemLineItemDao;
    }

    public SkuItemDao getSkuItemDao() {
        return skuItemDao;
    }

    public SkuService getSkuService() {
        return skuService;
    }
}
