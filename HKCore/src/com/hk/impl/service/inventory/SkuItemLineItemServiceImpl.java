package com.hk.impl.service.inventory;

import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId) {
        return  skuItemLineItemDao.getSkuItemLineItem(lineItem, skuItemStatusId);
    }
}
