package com.hk.impl.service.inventory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hk.domain.catalog.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.manager.EmailManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static Logger              logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private ProductVariantService      productVariantService;
    @Autowired
    private UserManager                userManager;
    @Autowired
    private EmailManager               emailManager;
    @Autowired
    private SkuItemDao                 skuItemDao;
    @Autowired
    private SkuService                 skuService;
    @Autowired
    private LowInventoryDao            lowInventoryDao;
    @Autowired
    private ProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    private ShippingOrderDao           shippingOrderDao;
    @Autowired
    private OrderDao                   orderDao;
    @Autowired
    private BaseDao                    baseDao;

    @Override
    public void checkInventoryHealth(ProductVariant productVariant) {
        List<Sku> skuList = getSkuService().getSKUsForProductVariant(productVariant);
        if (skuList != null && !skuList.isEmpty()) {
            checkInventoryHealth(skuList);
        }
    }

    @Override
    public Long getAggregateCutoffInventory(ProductVariant productVariant) {
        List<Sku> skuList = skuService.getSKUsForProductVariant(productVariant);
        return this.getAggregateCutoffInventory(skuList);
    }

    @Override
    public Long getAggregateCutoffInventory(List<Sku> skuList) {
        Long cutoff = 0L;
        for (Sku sku : skuList) {
            if (sku.getCutOffInventory() != null) {
                cutoff += sku.getCutOffInventory();
            }
        }
        return cutoff;
    }

    @Override
    public InvTxnType getInventoryTxnType(EnumInvTxnType enumInvTxnType) {
        return getBaseDao().get(InvTxnType.class, enumInvTxnType.getId());
    }

    private void checkInventoryHealth(List<Sku> skuList) {
        Long availableUnbookedInventory = this.getAvailableUnbookedInventory(skuList);
        ProductVariant productVariant = skuList.get(0).getProductVariant();
        boolean isJit = productVariant.getProduct().isJit() != null && productVariant.getProduct().isJit();
        logger.debug("isJit: " + isJit);
        if (getAggregateCutoffInventory(skuList) != null && !isJit) {
            if (availableUnbookedInventory <= getAggregateCutoffInventory(skuList)) {
                LowInventory lowInventoryInDB = getLowInventoryDao().findLowInventory(productVariant);
                if (lowInventoryInDB == null) {
                    logger.debug("Fire RedZone Inventory Email to Category Admins");
                    getEmailManager().sendInventoryRedZoneMail(productVariant);
                    // Entry in low inventory table
                    LowInventory lowInventory = new LowInventory();
                    lowInventory.setProductVariant(productVariant);
                    lowInventory.setEntryDate(new Date());
                    lowInventory.setAlertEmailSent(true);
                    getLowInventoryDao().save(lowInventory);
                } else {
                    logger.debug("Have already fired RedZone Inventory Email to Category Admins");
                }
            } else {
                logger.debug("Inventory status is healthy now. Deleting from low inventory list");
                getLowInventoryDao().deleteFromLowInventoryList(productVariant);
            }
        }

        // Mark product variant out of stock if inventory is negative or zero
        if (availableUnbookedInventory <= 0 && !productVariant.isOutOfStock() && !isJit) {
            logger.debug("Inventory status is negative now. Setting OUT of stock.");
            productVariant.setOutOfStock(true);
            productVariant = getProductVariantService().save(productVariant);

            LowInventory lowInventoryInDB = getLowInventoryDao().findLowInventory(productVariant);
            if (lowInventoryInDB == null) {
                LowInventory lowInventory = new LowInventory();
                lowInventory.setProductVariant(productVariant);
                lowInventory.setEntryDate(new Date());
                lowInventory.setAlertEmailSent(true);
                lowInventory.setOutOfStock(true);
                getLowInventoryDao().save(lowInventory);
            } else {
                lowInventoryInDB.setEntryDate(new Date());
                lowInventoryInDB.setOutOfStock(true);
                getLowInventoryDao().save(lowInventoryInDB);
            }
            List<ProductVariant> productVariants = productVariant.getProduct().getInStockVariants();
            //If there are other Variants in stock then there is no way a Product can be marked OutOfStock
            if (productVariants.size() == 1){
                if (productVariants.get(0).getId().equals(productVariant.getId())){
                    productVariant.getProduct().setOutOfStock(Boolean.TRUE);
                    getBaseDao().save(productVariant.getProduct());
                }
            }

            logger.debug("Fire Out of Stock Email to Category Admins");
            getEmailManager().sendOutOfStockMail(productVariant);
        } else if (availableUnbookedInventory > 0 && productVariant.isOutOfStock()) {
            logger.debug("Inventory status is positive now. Setting IN stock.");
            productVariant.setOutOfStock(false);
            productVariant = getProductVariantService().save(productVariant);
            Product product = productVariant.getProduct();
            product.setOutOfStock(Boolean.FALSE);
            getLowInventoryDao().deleteFromLowInventoryList(productVariant);
            getBaseDao().save(product);
        }
    }

    @Override
    public Long getAvailableUnbookedInventory(Sku sku) {
        return getAvailableUnbookedInventory(Arrays.asList(sku));
    }

    @Override
    public Long getAvailableUnbookedInventory(List<Sku> skuList) {
        Long netInventory = getProductVariantInventoryDao().getNetInventory(skuList);
        logger.debug("net inventory " + netInventory);
        Long bookedInventory = getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList);
        logger.debug("booked inventory " + bookedInventory);
        ProductVariant productVariant = !skuList.isEmpty() ? skuList.get(0).getProductVariant() : null;
        Long bookedInventoryForProductVariant = 0L;
        if (productVariant != null) {
            bookedInventoryForProductVariant = getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
            logger.debug("bookedInventoryForProductVariant " + bookedInventoryForProductVariant);
        }
        long availableUnbookedInventory = netInventory - (bookedInventory + bookedInventoryForProductVariant);
        logger.debug("net total AvailableUnbookedInventory " + availableUnbookedInventory);
        return availableUnbookedInventory;
    }
    
    @Override
    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList){
        return getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList);
    }

    @Override
    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant){
        return getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
    }
    /*
     * public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty) { Long
     * bookedInventoryOfSplitOrders = shippingOrderDaoProvider.get().getBookedQtyOfSkuInQueue(skuList); Long
     * bookedInventoryOfUnsplitOrders = 0L; if (skuList != null && !skuList.isEmpty()) { bookedInventoryOfUnsplitOrders =
     * orderDaoProvider.get().getBookedQtyOfProductVariantInQueue(skuList.get(0).getProductVariant()); // Here order
     * status of the order is placed thus the 'qty' is already included. } Long netQtyCheck =
     * bookedInventoryOfSplitOrders + bookedInventoryOfUnsplitOrders; return
     * skuItemDaoProvider.get().getWarehousesForSkuAndQty(skuList, netQtyCheck); }
     */
    @Override
    public Supplier getSupplierForSKU(Sku sku) {
        List<SkuGroup> availableSkuGroups = getSkuItemDao().getInStockSkuGroups(sku);
        return (availableSkuGroups != null && !availableSkuGroups.isEmpty()) ? availableSkuGroups.get(0).getGoodsReceivedNote().getPurchaseOrder().getSupplier() : null;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public SkuItemDao getSkuItemDao() {
        return skuItemDao;
    }

    public void setSkuItemDao(SkuItemDao skuItemDao) {
        this.skuItemDao = skuItemDao;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public LowInventoryDao getLowInventoryDao() {
        return lowInventoryDao;
    }

    public void setLowInventoryDao(LowInventoryDao lowInventoryDao) {
        this.lowInventoryDao = lowInventoryDao;
    }

    public ProductVariantInventoryDao getProductVariantInventoryDao() {
        return productVariantInventoryDao;
    }

    public void setProductVariantInventoryDao(ProductVariantInventoryDao productVariantInventoryDao) {
        this.productVariantInventoryDao = productVariantInventoryDao;
    }

    public ShippingOrderDao getShippingOrderDao() {
        return shippingOrderDao;
    }

    public void setShippingOrderDao(ShippingOrderDao shippingOrderDao) {
        this.shippingOrderDao = shippingOrderDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}