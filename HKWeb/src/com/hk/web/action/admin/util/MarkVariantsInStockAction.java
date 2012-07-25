package com.hk.web.action.admin.util;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.service.inventory.InventoryService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/25/12
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MarkVariantsInStockAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(MarkVariantsInStockAction.class);
    @Autowired
    ProductVariantDao productVariantDao;
    @Autowired
    InventoryService inventoryService;
/*
    @Autowired
    SkuService skuService;
    @Autowired
    ProductVariantInventoryDao productVariantInventoryDaoProvider;
    @Autowired
    ShippingOrderDao shippingOrderDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    LowInventoryDao lowInventoryDao;
    @Autowired
    EmailManager emailManager;
*/

    List<ProductVariant> productVariants = new ArrayList<ProductVariant>();


    public Resolution setVariantsInStockHavingInventory() {
        productVariants = productVariantDao.getAllProductVariant();
        for (ProductVariant productVariant : productVariants) {
            inventoryService.checkInventoryHealth(productVariant);
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

/*
    public void checkInventoryHealth(ProductVariant productVariant) {
        List<Sku> skuList = skuService.getSKUsForProductVariant(productVariant);
        if (skuList != null && !skuList.isEmpty()) {
            checkInventoryHealth(skuList);
        }
    }

    public Long getAvailableUnbookedInventory(List<Sku> skuList) {
        Long netInventory = productVariantInventoryDaoProvider.getNetInventory(skuList);
        Long bookedInventory = shippingOrderDao.getBookedQtyOfSkuInQueue(skuList);
        ProductVariant productVariant = skuList.get(0).getProductVariant();
        Long bookedInventoryForProductVariant = 0L;
        if (productVariant != null) {
            bookedInventoryForProductVariant = orderDao.getBookedQtyOfProductVariantInQueue(productVariant);
        }

        return netInventory - bookedInventory - bookedInventoryForProductVariant;
    }

    public Long getAggregateCutoffInventory(List<Sku> skuList) {
        Long cutoff = 0L;
        for (Sku sku : skuList) {
            if (sku.getCutOffInventory() != null) {
                cutoff += sku.getCutOffInventory();
            }
        }
        return cutoff;
    }


    private void checkInventoryHealth(List<Sku> skuList) {
        Long availableUnbookedInventory = this.getAvailableUnbookedInventory(skuList);
        ProductVariant productVariant = skuList.get(0).getProductVariant();
        boolean isJit = productVariant.getProduct().isJit() != null && productVariant.getProduct().isJit();
        logger.debug("isJit: " + isJit);
        if (getAggregateCutoffInventory(skuList) != null && !isJit) {
            if (availableUnbookedInventory <= getAggregateCutoffInventory(skuList)) {
                LowInventory lowInventoryInDB = lowInventoryDao.findLowInventory(productVariant);
                if (lowInventoryInDB == null) {
                    logger.debug("Fire RedZone Inventory Email to Category Admins");
                    emailManager.sendInventoryRedZoneMail(productVariant);
                    //Entry in low inventory table
                    LowInventory lowInventory = new LowInventory();
                    lowInventory.setProductVariant(productVariant);
                    lowInventory.setEntryDate(new Date());
                    lowInventory.setAlertEmailSent(true);
                    lowInventoryDao.save(lowInventory);
                } else {
                    logger.debug("Have already fired RedZone Inventory Email to Category Admins");
                }
            } else {
                logger.debug("Inventory status is healthy now. Deleting from low inventory list");
                lowInventoryDao.deleteFromLowInventoryList(productVariant);
            }
        }

        //Mark product variant out of stock if inventory is negative or zero
        if (availableUnbookedInventory <= 0 && !productVariant.isOutOfStock() && !isJit) {
            logger.debug("Inventory status is negative now. Setting OUT of stock.");
            productVariant.setOutOfStock(true);
            productVariant = productVariantDao.save(productVariant);

            LowInventory lowInventoryInDB = lowInventoryDao.findLowInventory(productVariant);
            if (lowInventoryInDB == null) {
                LowInventory lowInventory = new LowInventory();
                lowInventory.setProductVariant(productVariant);
                lowInventory.setEntryDate(new Date());
                lowInventory.setAlertEmailSent(true);
                lowInventory.setOutOfStock(true);
                lowInventoryDao.save(lowInventory);
            } else {
                lowInventoryInDB.setEntryDate(new Date());
                lowInventoryInDB.setOutOfStock(true);
                lowInventoryDao.save(lowInventoryInDB);
            }

            logger.debug("Fire Out of Stock Email to Category Admins");
            emailManager.sendOutOfStockMail(productVariant);
        } else if (availableUnbookedInventory > 0 && productVariant.isOutOfStock()) {
            logger.debug("Inventory status is positive now. Setting IN stock.");
            productVariant.setOutOfStock(false);
            productVariant = productVariantDao.save(productVariant);

            lowInventoryDao.deleteFromLowInventoryList(productVariant);
        }
    }
*/
}
