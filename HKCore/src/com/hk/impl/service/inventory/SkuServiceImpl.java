package com.hk.impl.service.inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.pact.service.core.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.EnumTax;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.order.Order;
import com.hk.exception.NoSkuException;
import com.hk.pact.dao.sku.SkuDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.TaxService;
import com.hk.pact.service.inventory.SkuService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class SkuServiceImpl implements SkuService {

   private static Logger logger = LoggerFactory.getLogger(SkuServiceImpl.class);

    @Autowired
    private SkuDao                skuDao;
    @Autowired
    private TaxService            taxService;
    @Autowired
    private ProductService        productService;
    @Autowired
    private ProductVariantService productVariantService;
	@Autowired
	private WarehouseService      warehouseService;

    /**
     * this will create or return sku (instance of product variant at selected warehouses)
     * 
     * @param productVariant
     * @param warehouse
     * @return
     */
    @SuppressWarnings("unused")
    private Sku createSku(ProductVariant productVariant, Warehouse warehouse) {

        Sku sku = new Sku();
        sku.setProductVariant(productVariant);
        sku.setWarehouse(warehouse);
        sku.setTax(getTaxService().findByName(EnumTax.VAT_0));
        sku.setCutOffInventory(0L);
        sku.setForecastedQuantity(0L);
        sku = (Sku) getSkuDao().save(sku);

        return sku;
    }

    public Sku saveSku(Sku sku) {
        if (sku.getCreateDate() == null) {
            sku.setCreateDate(BaseUtils.getCurrentTimestamp());
        }
        sku.setUpdateDate(BaseUtils.getCurrentTimestamp());
        return (Sku) getSkuDao().save(sku);
    }

    /**
     * this will return sku (instance of product variant at selected warehouses)
     * 
     * @param productVariant
     * @param warehouse
     * @return
     */
    public Sku getSKU(ProductVariant productVariant, Warehouse warehouse) {
        Sku sku = getSkuDao().getSku(productVariant, warehouse);

        if (sku == null) {
            logger.debug("No sku for product variant(" + productVariant.getId() + ") and warehouse(" + warehouse.getIdentifier() + ").");
            throw new NoSkuException(productVariant, warehouse);
        }

        return sku;
    }

     /**
     * this will return a list of all sku's (instance of product variant at multiple warehouses)
     * 
     * @param productVariant
     * @return
     */
    public List<Sku> getSKUsForProductVariant(ProductVariant productVariant) {
        return getSkuDao().getSkus(productVariant);
    }

	/**
	 * this will return a list of all sku's (instance of product variant at serviceable warehouses only)
	 *
	 * @param productVariant
	 * @return
	 */
	public List<Sku> getSKUsForProductVariantAtServiceableWarehouses(ProductVariant productVariant) {
		return getSkuDao().getSkus(productVariant, warehouseService.getServiceableWarehouses());
	}

  public List<Sku> getSKUsForProductVariantAtServiceableWarehouses(ProductVariant productVariant, Order order) {
    return getSkuDao().getSkus(productVariant, warehouseService.getServiceableWarehouses(order));
  }

  /**
	 * this will return a list of all sku's (instance of product variant at serviceable warehouses only)
	 *
	 * @param productVariant
	 * @return
	 */
	public List<Sku> getSKUsForMarkingProductOOS(ProductVariant productVariant) {
		return getSkuDao().getSkus(productVariant, warehouseService.getWarehousesToMarkOOS());
	}

    /**
     * this will return a list of all sku's (instance of product variant at multiple warehouses) based on category.
     * 
     * @param category
     * @return
     */
    public List<Sku> getSKUs(String category, String brand, String productId) {
        List<Sku> skuList = new ArrayList<Sku>();
        List<ProductVariant> superProductVariantList = new ArrayList<ProductVariant>();
        List<ProductVariant> subProductVariantList = new ArrayList<ProductVariant>();
        List<Product> productList = new ArrayList<Product>();
        Set<Product> productSet = new HashSet<Product>();

        if (category != null) {
            productList = getProductService().getAllProductBySubCategory(category);
        }

        if (brand != null) {
            productList = getProductService().getAllProductByBrand(brand);
        }

        if (productId != null) {
            productSet = getProductService().getProductsFromProductIds(productId);
            productList.addAll(productSet);
        }

        if (productList.size() > 0) {
            for (Product product : productList) {
                subProductVariantList = getProductVariantService().getProductVariantsByProductId(product.getId());
                superProductVariantList.addAll(subProductVariantList);
            }
            for (ProductVariant productVariant : superProductVariantList) {
                skuList.addAll(getSkuDao().getSkus(productVariant));
            }
            return skuList;
        } else {
            return null;
        }
    }

    public void insertSKUs(Set<Sku> skuSet) throws Exception {
        for (Sku skuObj : skuSet) {
            if (skuObj.getId() == null) {
                Sku sku = new Sku();
                sku.setProductVariant(skuObj.getProductVariant());
                sku.setCutOffInventory(skuObj.getCutOffInventory());
                sku.setForecastedQuantity(skuObj.getForecastedQuantity());
                sku.setTax(skuObj.getTax());
                sku.setWarehouse(skuObj.getWarehouse());
                saveSku(sku);
            }
        }
    }

	public List<Sku> getSKUsByProductNameAndWarehouse(String productName, Long warehouseId) {
		return skuDao.getSKUsByProductNameAndWarehouse(productName, warehouseId);
	}

  @Override
  public List<Sku> getSkus(ProductVariant productVariant, List<Warehouse> warehouseList) {
    return getSkuDao().getSkus(productVariant, warehouseList);
  }

  public SkuDao getSkuDao() {
        return skuDao;
    }

    public void setSkuDao(SkuDao skuDao) {
        this.skuDao = skuDao;
    }

    public TaxService getTaxService() {
        return taxService;
    }

    public void setTaxService(TaxService taxService) {
        this.taxService = taxService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

}
