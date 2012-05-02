package com.hk.service;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.catalog.product.EnumProductVariantServiceType;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.ProductVariantServiceType;

/**
 * @author vaibhav.adlakha
 */
public interface ProductVariantService {

    public ProductVariant getVariantById(String variantId);
    
    public ProductVariantServiceType getVariantServiceType(EnumProductVariantServiceType enumProductVariantServiceType);

    public Set<ProductVariant> getProductVariantsFromProductVariantIds(String productVariantIds);

    public List<ProductVariant> getProductVariantsByProductId(String productId);

    public void findAndSetBlankAffiliateCategory(AffiliateCategory affiliateCategory);

    public ProductVariant findVariantFromUPC(String upc);

    public List<ProductVariant> findVariantListFromUPC(String upc);

    public List<ProductVariant> findVariantsFromUPC(String upc);

    // find by upc, if null, find by variant
    public List<ProductVariant> findVariantFromBarcode(String barcode);

    public Long findNetInventory(ProductVariant productVariant);

    @Transactional
    public ProductVariant save(ProductVariant productVariant);

    /**
     * returns list of all the variants irrespective of whether they are deleted or not.
     * 
     * @param category
     * @return
     */
    public List<ProductVariant> getAllVariantsByCategory(String category);

    /**
     * returns list of all the variants which are not deleted.
     * 
     * @param category
     * @return
     */
    public List<ProductVariant> getAllProductVariantsByCategory(String category);

    public List<ProductVariant> getAllProductVariant();
}
