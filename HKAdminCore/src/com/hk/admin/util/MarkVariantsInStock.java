package com.hk.admin.util;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.sku.Sku;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.inventory.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: May 18, 2012
 * Time: 4:13:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarkVariantsInStock {
  private static Logger logger = LoggerFactory.getLogger(MarkVariantsInStock.class);
//  @Inject BinDao binDao;
//  @Inject ProductDao productDao;
  @Autowired

ProductVariantDao productVariantDao
    ;
//  @Inject BinManager binManager;
//  @Inject
//  InventoryService inventoryService;
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

  List<ProductVariant> productVariants = new ArrayList<ProductVariant>();

  public void setVariantsInStockHavingInventory(){
    productVariants = productVariantDao.getAllProductVariant();
    for (ProductVariant productVariant : productVariants) {
      checkInventoryHealth(productVariant);
    }
  }

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
//          emailManager.sendInventoryRedZoneMail(productVariant);
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
//      emailManager.sendOutOfStockMail(productVariant);
    } else if (availableUnbookedInventory > 0 && productVariant.isOutOfStock()) {
      logger.debug("Inventory status is positive now. Setting IN stock.");
      productVariant.setOutOfStock(false);
      productVariant = productVariantDao.save(productVariant);

      lowInventoryDao.deleteFromLowInventoryList(productVariant);
    }
  }



  /*
public void assignBasketCategoryToOrders() {
    Page<Order> orderPage = orderDao.findOrdersByOrderStatusAndLineItemStatus(
        Arrays.asList(EnumLineItemStatus.values()),
        Arrays.asList(EnumOrderStatus.Pending, EnumOrderStatus.OnHold, EnumOrderStatus.Shipped, EnumOrderStatus.Delivered),
        false, 1, 50000
    );
    List<Order> orderList = orderPage.getList();
    for (Order order : orderList) {
      if (order.getBasketCategory() == null) {
        System.out.println(order.getId());
        order.setBasketCategory(orderDao.getBasketCategory(order).getName());
        order = orderDao.save(order);
      }
    }
  }

  public void createBins(Integer maxAisle, Integer maxRack, Integer maxShelf) {
for (int i = 0; i <= maxAisle; i++) {
      for (int j = 0; j <= maxRack; j++) {
        for (int k = 0; k <= maxShelf; k++) {
          Bin bin = new Bin();
          bin.setAisle("A" + i);
          bin.setRack("R" + j);
          bin.setShelf("S" + k);
          bin = binDao.getOrCreateBin(bin);
          logger.info(bin.getBarcode());
        }
      }
    }

  public void assignDefaultBinToAllInStockSkus(){
 productVariants = productVariantDao.listAll();
    List<SkuItem> skuItems = binManager.getInStockSkuItemsOfProductVariants(productVariants);
    Bin bin = new Bin();
    bin.setAisle("D");
    bin.setRack("D");
    bin.setShelf("D");
    bin = binDao.getOrCreateBin(bin);
    binManager.assignBinToSkuItems(skuItems, bin, true);


  }

   public void TestFunction() {
String filepath = "C:\\Users\\PRATHAM\\Desktop\\inventory_count.txt";
    Map<String, Long> doomMap = ParseCsvFile.getMapFromCsv(filepath);
    Map<ProductVariant, Long> origMap = new HashMap<ProductVariant, Long>();


    for (String upc : doomMap.keySet()) {
      List<ProductVariant> productVariants = productVariantDao.findVariantFromBarcode(upc);
      if (productVariants == null || productVariants.isEmpty() || productVariants.size() > 1) {
        System.out.println("bug   " + upc);
        logger.info("bug   " + upc);
      } else {
        origMap.put(productVariants.get(0), productVariants.get(0).getNetInventory());
      }

    }
         */



}
