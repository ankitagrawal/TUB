package com.hk.impl.dao.inventoryManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.constants.sku.EnumSkuItemStatus;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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

    public void bookSkuLineItemForOrder(Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            cartLineItem.getProductVariant();
            List<Sku> skus = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
            List<SkuItem> skuItems = getSkuItems(skus, cartLineItem.getProductVariant().getMarkedPrice());
            long qtyToBeSet = cartLineItem.getQty();

            for (int i = 0; i < qtyToBeSet; i++) {
                for (SkuItem si : skuItems) {
                  si.setSkuItemStatus(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
                  si.setSkuItemOwnerStatus(null);
                }
            }


            //   get skuItem corresponfing to this variant

        }


    }


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
    
    public void checkoutMethod(LineItem lineItem, SkuItem skuItem){
    	CartLineItem cartLineItem = lineItem.getCartLineItem();
    	if(skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.BOOKED)){
    		SkuItemCLI skuItemCLI = getSkuItemCLI(cartLineItem);
    		if(skuItemCLI.getSkuItem().getId().equals(skuItem.getId())){
    			
    		}
    	}
    	if(!skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.BOOKED)){
    		//flip skuItem with a booked SI.
    		Sku sku = lineItem.getSku();
    	}
    }
    
    //search for SkuItemCLI
    public SkuItemCLI getSkuItemCLI(CartLineItem cartLineItem){
    	SkuItemCLI skuItemCLI = null;
    	return skuItemCLI;
    }


}
