package com.hk.impl.service.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.catalog.product.EnumProductVariantServiceType;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.ProductVariantServiceType;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    private ProductVariantDao productVariantDao;
    @Autowired
    private ProductService productService;

    public ProductVariant getVariantById(String variantId) {
        return getProductVariantDao().getVariantById(variantId);
    }
    
    public List<ProductVariant> getAllNonDeletedProductVariants(String category, String brand, boolean isPrimaryCategory) {
        List<ProductVariant> allProductVariantList = new ArrayList<ProductVariant>();
        List<ProductVariant> subProductVariantList = new ArrayList<ProductVariant>();
        List<Product> productList = new ArrayList<Product>();

        if (category != null && brand != null) {
          productList = getProductService().getProductByCategoryAndBrand(category, brand);
        } else if (category != null) {
          if (isPrimaryCategory) {
            productList = getProductService().getAllProductByCategory(category);
          } else {
            productList = getProductService().getAllProductBySubCategory(category);
          }
        } else {
          productList = getProductService().getAllProductByBrand(brand);
        }

        if (productList.size() > 0) {
          for (Product productObj : productList) {
            subProductVariantList = productVariantDao.getProductVariantsByProductId(productObj.getId());
            allProductVariantList.addAll(subProductVariantList);
          }
        }
        return allProductVariantList;
      }
    

    public ProductVariantServiceType getVariantServiceType(EnumProductVariantServiceType enumProductVariantServiceType) {
        return getProductVariantDao().get(ProductVariantServiceType.class, enumProductVariantServiceType.getId());
    }

    @Override
    public void findAndSetBlankAffiliateCategory(AffiliateCategory affiliateCategory) {
        getProductVariantDao().findAndSetBlankAffiliateCategory(affiliateCategory);
    }

    @Override
    public Long findNetInventory(ProductVariant productVariant) {
        return getProductVariantDao().findNetInventory(productVariant);
    }

    @Override
    public List<ProductVariant> findVariantFromBarcode(String barcode) {
        return getProductVariantDao().findVariantFromBarcode(barcode);
    }

    @Override
    public ProductVariant findVariantFromUPC(String upc) {
        return getProductVariantDao().findVariantFromUPC(upc);
    }

    @Override
    public List<ProductVariant> findVariantListFromUPC(String upc) {
        return getProductVariantDao().findVariantListFromUPC(upc);
    }

    @Override
    public List<ProductVariant> findVariantsFromUPC(String upc) {
        return getProductVariantDao().findVariantsFromUPC(upc);
    }

    @Override
    public List<ProductVariant> getAllProductVariantsByCategory(String category) {
        return getProductVariantDao().getAllProductVariantsByCategory(category);
    }

    @Override
    public List<ProductVariant> getAllVariantsByCategory(String category) {
        return getProductVariantDao().getAllVariantsByCategory(category);
    }

    @Override
    public List<ProductVariant> getProductVariantsByProductId(String productId) {
        return getProductVariantDao().getProductVariantsByProductId(productId);
    }

    @Override
    public Set<ProductVariant> getProductVariantsFromProductVariantIds(String productVariantIds) {
        return getProductVariantDao().getProductVariantsFromProductVariantIds(productVariantIds);
    }

    @Override
    public ProductVariant save(ProductVariant productVariant) {
        return getProductVariantDao().save(productVariant);
    }

    @Override
    public List<ProductVariant> getAllProductVariant() {
        return getProductVariantDao().getAllProductVariant();
    }
    
    
    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ProductVariantDao getProductVariantDao() {
        return productVariantDao;
    }

    public void setProductVariantDao(ProductVariantDao productVariantDao) {
        this.productVariantDao = productVariantDao;
    }


    
}
