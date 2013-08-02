package com.hk.impl.dao.inventoryManagement;

import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.SQLQuery;
import org.hibernate.Hibernate;
import org.hibernate.transform.Transformers;
import com.hk.pact.service.inventory.*;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageService;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.warehouse.Warehouse;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.catalog.product.EnumUpdatePVPriceStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
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
    UserService userService;
    @Autowired
    WarehouseService warehouseService;

    @Autowired
    UpdatePvPriceDao updatePvPriceDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    ShippingOrderDao shippingOrderDao;


    //     Make Entries in new SkuItemCLI  table
    public List<SkuItemCLI> saveSkuItemCLI(Set<SkuItem> skuItemsToBeBooked, CartLineItem cartLineItem) {
        Long count = 1L;
        List<SkuItemCLI> skuItemCLIList = new ArrayList<SkuItemCLI>();
        for (SkuItem si : skuItemsToBeBooked) {
            SkuItemCLI skuItemCLI = new SkuItemCLI();
            skuItemCLI.setCartLineItem(cartLineItem);
            skuItemCLI.setProductVariant(cartLineItem.getProductVariant());
            skuItemCLI.setUnitNum(count);
            skuItemCLI.setSkuItem(si);
            skuItemCLIList.add(skuItemCLI);
            count++;
        }
        baseDao.saveOrUpdate(skuItemCLIList);
        return  skuItemCLIList;
    }



    public Long getAvailableUnBookedInventory(ProductVariant productVariant) {
        // get Net physical inventory
        List<Sku> skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
        List<Long> statuses = new ArrayList<Long>();
        statuses.add(EnumSkuItemStatus.Checked_IN.getId());
        // only considering CheckedInInventory
        return inventoryManageDao.getNetInventory(skuList, statuses);

    }


    @Override
    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory) {

        List<Long> statusIds = EnumSkuItemStatus.getSkuItemStatusIDs(EnumSkuItemStatus.getStatusForNetPhysicalInventory());
        // considering Checcked in , temp booked , booked ie physical inventory
        Long netInventory = inventoryManageDao.getNetInventory(skuList, statusIds);


        Long bookedInventory = 0L;
        if (!skuList.isEmpty()) {
            ProductVariant productVariant = skuList.get(0).getProductVariant();
            bookedInventory = orderDao.getBookedQtyOfProductVariantInQueue(productVariant) + this.getBookedQty(skuList);

        }
        return (netInventory - bookedInventory);

    }


    private Long getBookedQty(List<Sku> skuList) {
        Long bookedInventory = 0L;
        if (skuList != null && !skuList.isEmpty()) {
            Long bookedInventoryForSKUs = inventoryManageDao.getBookedQtyOfSkuInQueue(skuList);

            bookedInventory = bookedInventoryForSKUs;
        }
        return bookedInventory;
    }


    public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant) {
        return inventoryManageDao.getLatestcheckedInBatchInventoryCount(productVariant);
    }


    public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp) {
        return inventoryManageDao.getClisForOrderInProcessingState(productVariant, skuId, mrp);
    }



    public boolean sicliAlreadyExists(CartLineItem cartLineItem) {
        return inventoryManageDao.sicliAlreadyExists(cartLineItem);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
