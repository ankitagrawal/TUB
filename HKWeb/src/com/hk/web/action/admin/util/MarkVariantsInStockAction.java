package com.hk.web.action.admin.util;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.service.inventory.InventoryService;

/**
 * Created with IntelliJ IDEA. User: Pratham Date: 7/25/12 Time: 2:46 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class MarkVariantsInStockAction extends BaseAction {

    // private static Logger logger = LoggerFactory.getLogger(MarkVariantsInStockAction.class);
    @Autowired
    ProductVariantDao    productVariantDao;
    @Autowired
    InventoryService     inventoryService;

    List<ProductVariant> productVariants = new ArrayList<ProductVariant>();

    @DefaultHandler
    public Resolution setVariantsInStockHavingInventory() {
        productVariants = productVariantDao.getAllNonDeletedProductVariant();
        for (ProductVariant productVariant : productVariants) {
            inventoryService.checkInventoryHealth(productVariant);
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public Resolution quickInventoryCheck(){
        productVariants = productVariantDao.getVariantsForQuickInventoryCheck();
        for (ProductVariant productVariant : productVariants) {
            inventoryService.checkInventoryHealth(productVariant);
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }
}
