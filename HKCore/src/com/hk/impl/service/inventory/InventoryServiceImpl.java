package com.hk.impl.service.inventory;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.manager.EmailManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemDao;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
  @Autowired
  SkuItemDao inventoryManageDao;

  @Override
  @Transactional
  public void checkInventoryHealth(ProductVariant productVariant) {
    inventoryHealthService.inventoryHealthCheck(productVariant);
    lowInventoryAction(productVariant);
  }

  @Override
  public Long getAggregateCutoffInventory(ProductVariant productVariant) {
    List<Sku> skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
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
  public Long getAvailableUnbookedInventory(Sku sku, Double mrp) {
    //TODO: Write proper DAO method
    return 0L;
  }

  @Override
  public Long getAllowedStepUpInventory(ProductVariant productVariant) {
    if (productVariant.getMrpQty() == null) {
      checkInventoryHealth(productVariant);
    }
    productVariant = productVariantService.getVariantById(productVariant.getId());
    if (productVariant.getMrpQty() == null) {
      return 0l;
    }
    return productVariant.getMrpQty();
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

  //Migrated from Inventory Manage Service
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