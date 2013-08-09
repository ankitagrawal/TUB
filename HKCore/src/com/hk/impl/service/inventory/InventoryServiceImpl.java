package com.hk.impl.service.inventory;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.manager.EmailManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.combo.ComboService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
  SkuGroupService skuGroupService;
  @Autowired
  UserService userService;

  @Autowired
  InventoryHealthService inventoryHealthService;

  @Override
  @Transactional
  public void checkInventoryHealth(ProductVariant productVariant) {
    inventoryHealthService.inventoryHealthCheck(productVariant);
    lowInventoryAction(productVariant);
  }

  @Override
  public Long getAggregateCutoffInventory(ProductVariant productVariant) {
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
    if (productVariant.getNetQty() != null) {
      Long availableUnbookedInventory = productVariant.getNetQty();
      Long aggCutOffInv = getAggregateCutoffInventory(productVariant);
      try {
        //Calling Async method to set all out of stock combos to in stock
        getComboService().markRelatedCombosOutOfStock(productVariant);
        //Update Low Inventory Record
        updateLowInventory(productVariant, availableUnbookedInventory, aggCutOffInv);
      } catch (Exception e) {
        logger.error("Error while marking LowInv and UpdatePv: ", e);
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
  public Long getAvailableUnbookedInventory(ProductVariant productVariant) {
    return inventoryHealthService.getAvailableUnbookedInventory(productVariant);
  }


  @Override
  public long getUnbookedInventoryInProcessingQueue(LineItem lineItem) {
    return inventoryHealthService.getUnbookedInventoryInProcessingQueue(lineItem);
  }

  @Override
  public long getUnbookedInventoryForActionQueue(LineItem lineItem) {
    return inventoryHealthService.getUnbookedInventoryForActionQueue(lineItem);
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

  @SuppressWarnings("unused")
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