package com.hk.impl.service.inventory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.catalog.product.EnumUpdatePVPriceStatus;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.manager.EmailManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.combo.ComboService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmailManager emailManager;
    @Autowired
    private SkuService skuService;
    @Autowired
    private LowInventoryDao lowInventoryDao;
    @Autowired
    private ProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    private ShippingOrderDao shippingOrderDao;
    @Autowired
    private ComboService comboService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private UpdatePvPriceDao updatePvPriceDao;
    @Autowired
    private ProductService productService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    UserService userService;
    
    @Autowired InventoryHealthService inventoryHealthService;

    @Override
    @Transactional
    public void checkInventoryHealth(ProductVariant productVariant) {
    	inventoryHealthService.checkInventoryHealth(productVariant);
    	lowInventoryAction(productVariant);
    }

    @Override
    public Long getAggregateCutoffInventory(ProductVariant productVariant) {
        //List<Sku> skuList = skuService.getSKUsForProductVariant(productVariant);
        List<Sku> skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
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
        return baseDao.get(InvTxnType.class, enumInvTxnType.getId());
    }

	private void lowInventoryAction(ProductVariant productVariant) {
		productVariant = productVariantService.getVariantById(productVariant.getId());
    	if(productVariant.getNetQty() != null) {
    		Long availableUnbookedInventory = productVariant.getNetQty();
    		Long aggCutOffInv = getAggregateCutoffInventory(productVariant);
            try {
                //Calling Async method to set all out of stock combos to in stock
                getComboService().markRelatedCombosOutOfStock(productVariant);

                //Update Low Inventory Record
                updateLowInventory(productVariant, availableUnbookedInventory, aggCutOffInv);

                //Now lets check for MRP dynamism
                updatePvPriceRecord(productVariant);
            } catch (Exception e) {
                logger.error("Error while marking LowInv and UpdatePv: ", e);
            }
    	}
	}

    @Transactional
    private void updatePvPriceRecord(ProductVariant productVariant) {
        //Warehouse warehouse = userService.getWarehouseForLoggedInUser();
        boolean isAuditClosed = updatePvPriceDao.isAuditClosed(productVariant);
        if (isAuditClosed) {
            Long bookedInventory = this.getBookedQty(productVariant);
            SkuGroup leastMRPSkuGroup = skuGroupService.getMinMRPUnbookedSkuGroup(productVariant, bookedInventory);
            if (leastMRPSkuGroup != null && leastMRPSkuGroup.getMrp() != null
                    && !productVariant.getMarkedPrice().equals(leastMRPSkuGroup.getMrp())) {
                UpdatePvPrice updatePvPrice = updatePvPriceDao.getPVForPriceUpdate(productVariant, EnumUpdatePVPriceStatus.Pending.getId());
                if (updatePvPrice == null) {
                    updatePvPrice = new UpdatePvPrice();
                }
                updatePvPrice.setProductVariant(productVariant);
                updatePvPrice.setOldCostPrice(productVariant.getCostPrice());
                updatePvPrice.setNewCostPrice(leastMRPSkuGroup.getCostPrice());
                updatePvPrice.setOldMrp(productVariant.getMarkedPrice());
                updatePvPrice.setNewMrp(leastMRPSkuGroup.getMrp());
                updatePvPrice.setOldHkprice(productVariant.getHkPrice());
                Double newHkPrice = leastMRPSkuGroup.getMrp() * (1 - productVariant.getDiscountPercent());
                updatePvPrice.setNewHkprice(newHkPrice);
                updatePvPrice.setTxnDate(new Date());
                updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Pending.getId());
	              baseDao.save(updatePvPrice);
/*
                updatePvPrice = (UpdatePvPrice) baseDao.save(updatePvPrice);

                // If price is in range - update it Automatically
                // Tolerance Limit is 15% either way
                if (Math.abs((leastMRPSkuGroup.getMrp() - productVariant.getMarkedPrice()) / productVariant.getMarkedPrice() * 100) < 15
                    || Math.abs((leastMRPSkuGroup.getCostPrice() - productVariant.getCostPrice()) / productVariant.getCostPrice() * 100) < 15) {
                  productVariant.setCostPrice(updatePvPrice.getNewCostPrice());
                  productVariant.setMarkedPrice(updatePvPrice.getNewMrp());
                  productVariant.setHkPrice(updatePvPrice.getNewHkprice());
                  productVariantService.save(productVariant);

                  updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Updated.getId());
                  updatePvPrice.setUpdateDate(new Date());
                  updatePvPrice.setUpdatedBy(userService.getAdminUser());
                  baseDao.save(updatePvPrice);
                }
*/
            }
        }
    }

    @Transactional
    private void updateLowInventory(ProductVariant productVariant, Long availableUnbookedInventory, Long aggCutOffInv) {
        if (availableUnbookedInventory <= aggCutOffInv) {
            LowInventory lowInventoryInDB = getLowInventoryDao().findLowInventory(productVariant);
            if (lowInventoryInDB == null) {
                LowInventory lowInventory = new LowInventory();
                lowInventory.setProductVariant(productVariant);
                lowInventory.setEntryDate(new Date());
                lowInventory.setAlertEmailSent(true);
                getLowInventoryDao().save(lowInventory);
                logger.debug("Fire RedZone Inventory Email to Category Admins");
                getEmailManager().sendInventoryRedZoneMail(productVariant);

            } else {
                logger.debug("Have already fired RedZone Inventory Email to Category Admins");
            }
            if (availableUnbookedInventory <= 0) {
                logger.debug("Fire OOS Email to Category Admins");
                getEmailManager().sendOutOfStockMail(productVariant);
            }
        } else {
            logger.debug("Inventory status is healthy now. Deleting from low inventory list");
            getLowInventoryDao().deleteFromLowInventoryList(productVariant);
        }
    }

    @Override
    @Deprecated
    public Long getAvailableUnbookedInventory(Sku sku) {
        return getAvailableUnbookedInventory(sku.getProductVariant());
    }

    @Override
    @Deprecated
    public Long getAvailableUnbookedInventory(List<Sku> skuList) {
    	return getAvailableUnbookedInventory(skuList.get(0).getProductVariant());
    }

    @Override
    public Long getAvailableUnbookedInventory(ProductVariant productVariant) {
    	if(productVariant.getMrpQty() == null) {
    		inventoryHealthService.checkInventoryHealth(productVariant);
    	}
        productVariant = productVariantService.getVariantById(productVariant.getId());
        if(productVariant.getMrpQty() == null) {
        	return 0l;
        }
        return productVariant.getMrpQty();
    }

    @Override
    public Long getUnbookedInventoryInProcessingQueue(List<Sku> skuList) {
        Long netInventory = getProductVariantInventoryDao().getNetInventory(skuList);
        Long bookedInventory = this.getBookedQtyOfSkuInProcessingQueue(skuList);
        return netInventory - bookedInventory;
    }

    @Deprecated
    private Long getBookedQty(ProductVariant productVariant) {
        Long bookedInventory = 0L;
        if (productVariant != null) {
            Long bookedInventoryForProductVariant = getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
            logger.debug("bookedInventoryForProductVariant " + bookedInventoryForProductVariant);
            //Long bookedInventoryForSKUs = getShippingOrderDao().getBookedQtyOfSkuInQueue(skuService.getSKUsForProductVariant(productVariant));
            Long bookedInventoryForSKUs = getShippingOrderDao().getBookedQtyOfSkuInQueue(skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant));
            logger.debug("bookedInventoryForSKUs " + bookedInventoryForSKUs);
            bookedInventory = bookedInventoryForProductVariant + bookedInventoryForSKUs;
        }
        return bookedInventory;
    }

    @Deprecated
    private Long getBookedQty(List<Sku> skuList) {
        Long bookedInventory = 0L;
        if (skuList != null && !skuList.isEmpty()) {
            Long bookedInventoryForSKUs = getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList);
            logger.debug("bookedInventoryForSKUs " + bookedInventoryForSKUs);
            bookedInventory = bookedInventoryForSKUs;
        }
        return bookedInventory;
    }

    public boolean allInventoryCheckedIn(GoodsReceivedNote grn) {
        for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {
            if (!grnLineItem.getQty().equals(grnLineItem.getCheckedInQty())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList) {
        return getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList);
    }

    @Override
    public Long getBookedQtyOfSkuInProcessingQueue(List<Sku> skuList) {
        return getShippingOrderDao().getBookedQtyOfSkuInProcessingQueue(skuList);
    }

    @Override
    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant) {
        return getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
    }

    @Override
    public Supplier getSupplierForSKU(Sku sku) {
        List<SkuGroup> availableSkuGroups = skuGroupService.getInStockSkuGroups(sku);
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

    public ComboService getComboService() {
        return comboService;
    }
}