package com.hk.impl.dao.inventoryManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.pact.service.inventory.SkuService;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.Sku;

import java.util.Set;
import java.util.List;

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


    public void bookSkuLineItemForOrder(Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            cartLineItem.getProductVariant();
             List<Sku> skus =  skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());

//           List<SkuItem> skuItems =            


            //   get skuItem corresponfing to this variant

        }


    }


}
