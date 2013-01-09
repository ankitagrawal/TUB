package com.hk.impl.service.inventory;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.catalog.product.EnumUpdatePVPriceStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
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
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.combo.ComboService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static Logger              logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private ProductVariantService      productVariantService;
    @Autowired
    private UserManager                userManager;
    @Autowired
    private EmailManager               emailManager;
//    @Autowired
//    private SkuItemDao                 skuItemDao;
    @Autowired
    private SkuService                 skuService;
    @Autowired
    private LowInventoryDao            lowInventoryDao;
    @Autowired
    private ProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    private ShippingOrderDao           shippingOrderDao;
    @Autowired
    private ComboService               comboService;
    @Autowired
    private OrderDao                   orderDao;
    @Autowired
    private BaseDao                    baseDao;
	  @Autowired
    private UpdatePvPriceDao           updatePvPriceDao;
    @Autowired
    private ProductService             productService;
	@Autowired
	SkuGroupService skuGroupService;
	

    @Override
    public void checkInventoryHealth(ProductVariant productVariant) {
        List<Sku> skuList = getSkuService().getSKUsForProductVariant(productVariant);
        if (skuList != null && !skuList.isEmpty()) {
            checkInventoryHealth(skuList, productVariant);
        }else{
            //all variants without sku marked out of stock
            //todo check for product oos as well
            productVariant.setOutOfStock(true);
            productVariantService.save(productVariant);
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
        return baseDao.get(InvTxnType.class, enumInvTxnType.getId());
    }

    private void checkInventoryHealth(List<Sku> skuList, ProductVariant productVariant) {
        Long availableUnbookedInventory = this.getAvailableUnbookedInventory(skuList);
        //ProductVariant productVariant = skuList.get(0).getProductVariant();
	    Product product = productVariant.getProduct();
        boolean isJit = productVariant.getProduct().isJit() != null && productVariant.getProduct().isJit();

	    /*
	    boolean shouldFireOOSEmail = false, shouldFireInStockEmail = false;
	    if(!productVariantService.isAnySiblingVariantInStock(productVariant) && availableUnbookedInventory <= 0 && !productVariant.isOutOfStock()){
		    shouldFireOOSEmail = true;
	    }
	    if(!productVariantService.isAnySiblingVariantInStock(productVariant) && availableUnbookedInventory > 0 && productVariant.isOutOfStock()){
		    shouldFireInStockEmail = true;
	    }*/

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
        boolean isProductOutOfStock = Boolean.FALSE;
        boolean shouldUpdateProduct = Boolean.FALSE;
        // Mark product variant out of stock if inventory is negative or zero
        if (availableUnbookedInventory <= 0 && !productVariant.isOutOfStock() && !isJit) {
            logger.debug("Inventory status is negative now. Setting OUT of stock.");
            List<ProductVariant> productVariants = productVariant.getProduct().getInStockVariants();

            //If there are other Variants in stock then there is no way a Product can be marked OutOfStock
            if (productVariants.size() == 1 && !productVariant.isDeleted()){
                if (productVariants.get(0).getId().equals(productVariant.getId())){
                    if (!isJit && !product.isService() && !product.getDropShipping() && !product.getDeleted()) {
                        isProductOutOfStock = Boolean.TRUE;
                        shouldUpdateProduct = true;
                    }
                }
            }
            productVariant.setOutOfStock(true);
            //First product variant goes out of stock
            productVariant = getProductVariantService().save(productVariant);
            //calling Async method to set all out of stock combos to in stock
            getComboService().markProductOutOfStock(productVariant);
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
            getEmailManager().sendOutOfStockMail(productVariant);
        } else if (availableUnbookedInventory > 0 && productVariant.isOutOfStock()) {
            logger.debug("Inventory status is positive now. Setting IN stock.");
            productVariant.setOutOfStock(false);
          //calling Async method to set all out of stock combos to in stock
            productVariant = getProductVariantService().save(productVariant);
            getComboService().markProductOutOfStock(productVariant);
            product = productVariant.getProduct();
            getLowInventoryDao().deleteFromLowInventoryList(productVariant);
            if (!isJit && !product.isService() && !product.getDropShipping() && !product.getDeleted()) {
                isProductOutOfStock = Boolean.FALSE;
                shouldUpdateProduct = true;
            }
        }

        if(shouldUpdateProduct){
            product.setOutOfStock(isProductOutOfStock);
            productService.save(product);
            logger.debug(String.format("Settting product  %s out_of_stock ", product.getId()));
        }

	    //Now lets check for MRP dynamism
	    boolean isBrandAudited = updatePvPriceDao.isBrandAudited(productVariant.getProduct().getBrand());
	    if (isBrandAudited) {
		    Long bookedInventory = this.getBookedQty(productVariant);
		    SkuGroup leastMRPSkuGroup = skuGroupService.getMinMRPUnbookedSkuGroup(productVariant, bookedInventory);
		    if (leastMRPSkuGroup != null) {
			    //logger.info("leastMRPSkuGroup: "+leastMRPSkuGroup.getId());
			    if (leastMRPSkuGroup != null && leastMRPSkuGroup.getMrp() != null
					    && !productVariant.getMarkedPrice().equals(leastMRPSkuGroup.getMrp())) {
				    //logger.info("MRP: "+productVariant.getMarkedPrice()+"-->"+leastMRPSkuGroup.getMrp());
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
			    }
		    }
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

	    ProductVariant productVariant = !skuList.isEmpty() ? skuList.get(0).getProductVariant() : null;
	    Long bookedInventory = 0L;
	    if (productVariant != null) {
		    bookedInventory = this.getBookedQty(productVariant);
		    logger.debug("booked inventory " + bookedInventory);
	    }
	    long availableUnbookedInventory = netInventory - bookedInventory;
	    logger.debug("net total AvailableUnbookedInventory " + availableUnbookedInventory);
	    return availableUnbookedInventory;
    }

	@Override
	public Long getUnbookedInventoryInProcessingQueue(List<Sku> skuList) {
		Long netInventory = getProductVariantInventoryDao().getNetInventory(skuList);
		Long  bookedInventory = this.getBookedQtyOfSkuInProcessingQueue(skuList);
		return netInventory - bookedInventory;
	}

	public Long getBookedQty(ProductVariant productVariant) {
		Long bookedInventory = 0L;
		if (productVariant != null) {
			Long bookedInventoryForProductVariant = getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
			logger.debug("bookedInventoryForProductVariant " + bookedInventoryForProductVariant);
			Long bookedInventoryForSKUs = getShippingOrderDao().getBookedQtyOfSkuInQueue(skuService.getSKUsForProductVariant(productVariant));
			logger.debug("bookedInventoryForSKUs " + bookedInventoryForSKUs);
			bookedInventory = bookedInventoryForProductVariant + bookedInventoryForSKUs;
		}
		return bookedInventory;
	}

	public boolean allInventoryCheckedIn(GoodsReceivedNote grn){
		for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {
			if(!grnLineItem.getQty().equals(grnLineItem.getCheckedInQty())){
				return false;
			}
		}
		return true;
	}
    
    @Override
    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList){
        return getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList);
    }

	@Override
	public Long getBookedQtyOfSkuInProcessingQueue(List<Sku> skuList){
		return getShippingOrderDao().getBookedQtyOfSkuInProcessingQueue(skuList);
	}

	@Override
    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant){
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