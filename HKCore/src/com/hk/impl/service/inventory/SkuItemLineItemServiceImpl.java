package com.hk.impl.service.inventory;

import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.pact.dao.InventoryManagement.InventoryManageDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.inventory.SkuItemLineItemService;
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

    @Override
    public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId) {
        return  getSkuItemLineItemDao().getSkuItemLineItem(lineItem, skuItemStatusId);
    }

    @Override
    public SkuItemLineItem getById(Long skuItemLineItemId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SkuItemLineItem createNewSkuItemLineItem(LineItem lineItem, Long unitNum, SkuItemCLI skuItemCLI) {
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
            List<SkuItem> getAvailableUnbookedSkuItem = getSkuItemDao().getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());

            SkuItem skuItem = getAvailableUnbookedSkuItem.get(0);
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

        return skuItemLineItem;
    }

    @Override
    public SkuItemLineItem save(SkuItemLineItem skuItemLineItem) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<SkuItemLineItem> getSkuItemLineItemForLineItem(LineItem lineItem) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SkuItemLineItemDao getSkuItemLineItemDao() {
        return skuItemLineItemDao;
    }

    public SkuItemDao getSkuItemDao() {
        return skuItemDao;
    }
}
