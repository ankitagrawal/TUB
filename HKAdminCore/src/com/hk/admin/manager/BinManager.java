package com.hk.admin.manager;

import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.Bin;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.sku.SkuDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.inventory.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA. User: PRATHAM Date: 1/16/12 Time: 7:01 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class BinManager {
  @Autowired
  private SkuService skuService;
  @Autowired
  private ProductService productService;
  @Autowired
  private UserService userService;
  @Autowired
  private BinDao binDao;
  @Autowired
  private AdminSkuItemDao adminSkuItemDao;
  @Autowired
  private SkuDao skuDao;

  @Autowired
  ProductDao productDao;
  @Autowired
  SkuItemDao skuItemDao;

    private static Logger logger = LoggerFactory.getLogger(BinManager.class);
  public List<ProductVariant> getProductVariantsByCategory(Category category) {
    List<Product> totalProducts = getProductService().getAllProductBySubCategory(category.getName());
    List<ProductVariant> productVariants = new ArrayList<ProductVariant>();

    for (Product product : totalProducts) {
      productVariants.addAll(product.getProductVariants());
    }
    return productVariants;
  }

  public List<ProductVariant> getProductVariantsByBrand(String brand) {
    List<Product> totalProducts = getProductService().getAllProductByBrand(brand);
    List<ProductVariant> productVariants = new ArrayList<ProductVariant>();

    for (Product product : totalProducts) {
      productVariants.addAll(product.getProductVariants());
    }
    return productVariants;
  }

  /*
  * @Deprecated public List<SkuItem> getInStockSkuItemsOfProductVariants(List<ProductVariant> productVariants) {
  *             List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>(); for (ProductVariant productVariant :
  *             productVariants) { inStockSkuItems.addAll(skuItemDao.getInStockSkuItemsBySku(null)); } return
  *             inStockSkuItems; }
  */

  public List<Sku> filterProductVariantsByWarehouse(List<ProductVariant> productVariants) {
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    return getSkuDao().filterProductVariantsByWarehouse(productVariants, warehouse);
  }

  public List<SkuItem> getInStockSkuItemsOfSkus(List<Sku> skus) {
    return getAdminSkuItemDao().getInStockSkuItemsBySku(skus);
  }

  public List<SkuItem> getAlreadyAssignedBinsForSku(List<SkuItem> skuItems) {
    List<SkuItem> alreadyAssignedSkuItems = new ArrayList<SkuItem>();
    for (SkuItem skuItem : skuItems) {
      if (skuItem.getBins() != null && skuItem.getBins().size() > 0) {
        alreadyAssignedSkuItems.add(skuItem);
      }
    }
    return alreadyAssignedSkuItems;
  }

  public void assignBinToSkuItems(List<SkuItem> skuItems, Bin bin, Boolean override) {
    // if bin already assigned to a particular inventory, then check if the user has asked to override, then reset
    // else not
    if (skuItems != null && skuItems.size() > 0) {
      for (SkuItem skuItem : skuItems) {
        boolean assigned = false;
        if (skuItem.getBins() != null && skuItem.getBins().size() > 0) {
          assigned = true;
        }
        if (Boolean.TRUE.equals(override) || !assigned) {
          List<Bin> bins = new ArrayList<Bin>();
          bins.add(bin);
          skuItem.setBins(bins);
          getSkuDao().save(skuItem);
        }
      }
    }
  }

  //added
  public void createBinForSkuItem(Bin bin, SkuGroup skuGroup) {
         if(bin != null) {
       for (SkuItem item : skuGroup.getSkuItems()) {
       List<Bin> binListForSkuItems = new ArrayList<Bin>();
      if (bin != null) {
        binListForSkuItems.add(bin);
       item.setBins(binListForSkuItems);
       skuItemDao.save(item);

      }
    }
        }
  }


  public void assignBinToCategory(Category category, Bin bin, Boolean override) {
    assignBinToSkuItems(getInStockSkuItemsOfSkus(filterProductVariantsByWarehouse(getProductVariantsByCategory(category))), bin, override);
  }

  public void assignBinToBrand(String brand, Bin bin, Boolean override) {
    assignBinToSkuItems(getInStockSkuItemsOfSkus(filterProductVariantsByWarehouse(getProductVariantsByBrand(brand))), bin, override);
  }

  public void assignBinToProduct(Product product, Bin bin, Boolean override) {
    assignBinToSkuItems(getInStockSkuItemsOfSkus(filterProductVariantsByWarehouse(product.getProductVariants())), bin, override);
  }

  public void assignBinToVariant(ProductVariant productVariant, Bin bin, Boolean override) {
    List<SkuItem> skuItems = getInStockSkuItemsOfSkus(filterProductVariantsByWarehouse(Arrays.asList(productVariant)));
    assignBinToSkuItems(skuItems, bin, override);
  }


  // get all unique bins per sku_group
  public List<Long> getListOfBinForSkuItemList(Set<SkuItem> skuItemList) {
    List<Long> binIdLIst = new ArrayList<Long>();

    for (SkuItem skuItem : skuItemList) {
      List<Bin> BinList = skuItem.getBins();
      if (BinList.size() != 0 && BinList.get(0) != null) {
        Bin bin = BinList.get(0);
        if (!binIdLIst.contains(bin.getId())) {
          binIdLIst.add(bin.getId());
        }
      }
    }
    if (binIdLIst.size() != 0) {
      return binIdLIst;
    } else
      return null;

  }

  //added
  public boolean assignBinToSkuItems(Set<SkuItem> skuItemSet, Bin bin) {
    if (skuItemSet != null && skuItemSet.size() > 0) {
      for (SkuItem skuItem : skuItemSet) {
        List<Bin> binList = new ArrayList<Bin>();
        binList.add(bin);
        skuItem.setBins(binList);
        skuItemDao.save(skuItem);
      }
      return true;
    }
    return false;
  }

  public void removeBinAllocated(SkuItem skuItem){
     if(skuItem!= null){
       try{
       skuItem.setBins(null);
       skuItemDao.save(skuItem);
     }catch(Exception skuException){
        logger.error("error in deleting sku items " + skuException);
       }
     }

   }

   public boolean assignBinToVariant(String location, SkuGroup skuGroup  ) {
     boolean status=false;
      Warehouse warehouse = userService.getWarehouseForLoggedInUser();
      Bin bin = binDao.findByBarCodeAndWarehouse(location, warehouse);
     if(bin != null){
      List<SkuItem> inStockSkuItems = adminSkuItemDao.getInStockSkuItems(skuGroup);
      Set <SkuItem> inStockSkuItemsSet= new HashSet<SkuItem>(inStockSkuItems);
       status = this.assignBinToSkuItems(inStockSkuItemsSet, bin);
     }
      return status;
   }

  public SkuService getSkuService() {
    return skuService;
  }

  public void setSkuService(SkuService skuService) {
    this.skuService = skuService;
  }

  public ProductService getProductService() {
    return productService;
  }

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public BinDao getBinDao() {
    return binDao;
  }

  public void setBinDao(BinDao binDao) {
    this.binDao = binDao;
  }

  public AdminSkuItemDao getAdminSkuItemDao() {
    return adminSkuItemDao;
  }

  public void setAdminSkuItemDao(AdminSkuItemDao adminSkuItemDao) {
    this.adminSkuItemDao = adminSkuItemDao;
  }

  public SkuDao getSkuDao() {
    return skuDao;
  }

  public void setSkuDao(SkuDao skuDao) {
    this.skuDao = skuDao;
  }

}
