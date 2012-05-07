package com.hk.admin.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.admin.impl.dao.inventory.AdminSkuItemDao;
import com.hk.admin.impl.dao.warehouse.BinDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.Bin;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.sku.SkuDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.inventory.SkuService;

/**
 * Created by IntelliJ IDEA. User: PRATHAM Date: 1/16/12 Time: 7:01 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class BinManager {

    private SkuService      skuService;
    private ProductService  productService;
    private UserService     userService;

    private BinDao          binDao;
    private AdminSkuItemDao adminSkuItemDao;
    private SkuDao          skuDao;

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
