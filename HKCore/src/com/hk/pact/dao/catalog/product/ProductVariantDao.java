package com.hk.pact.dao.catalog.product;

import java.util.List;
import java.util.Set;

import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

public interface ProductVariantDao extends BaseDao {

    public ProductVariant getVariantById(String variantId);

//    public ProductVariant getVariantByTryOn(String variantId);

    public Set<ProductVariant> getProductVariantsFromProductVariantIds(String productVariantIds);

    public List<ProductVariant> getProductVariantsByProductId(String productId);

    public void findAndSetBlankAffiliateCategory(AffiliateCategory affiliateCategory);

    public ProductVariant findVariantFromUPC(String upc);

    public List<ProductVariant> findVariantListFromUPC(String upc);

    // to debug mulitple PV with same upc, ideally should return 1 only
    public List<ProductVariant> findVariantsFromUPC(String upc);

    // find by upc, if null, find by variant
    public List<ProductVariant> findVariantFromBarcode(String barcode);

    public Long findNetInventory(ProductVariant productVariant);
    
    public List<ProductVariant> findVariantsFromFreeVariant(ProductVariant freeProductVariant);

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

    public List<ProductVariant> getAllNonDeletedProductVariant();

    public Product getProductForProudctVariant(String variantId);

    public List<ProductVariant> getVariantsForQuickInventoryCheck();
    
    public void markProductVariantsAsDeleted(Product Product);
}                                                               