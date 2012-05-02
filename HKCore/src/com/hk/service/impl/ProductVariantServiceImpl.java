package com.hk.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.catalog.product.EnumProductVariantServiceType;
import com.hk.dao.catalog.product.ProductVariantDao;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.ProductVariantServiceType;
import com.hk.service.ProductVariantService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    private ProductVariantDao productVariantDao;

    public ProductVariant getVariantById(String variantId) {
        return getProductVariantDao().getVariantById(variantId);
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

    public ProductVariantDao getProductVariantDao() {
        return productVariantDao;
    }

    public void setProductVariantDao(ProductVariantDao productVariantDao) {
        this.productVariantDao = productVariantDao;
    }


    @Override
    public List<ProductVariant> getAllProductVariant() {
        return getProductVariantDao().getAllProductVariant();
    }
}
