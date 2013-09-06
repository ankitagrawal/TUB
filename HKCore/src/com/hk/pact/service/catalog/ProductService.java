package com.hk.pact.service.catalog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.*;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.search.SolrProduct;
import com.hk.cache.vo.ProductVO;

public interface ProductService {

    public Product getProductById(String productId);

    public Product save(Product product);

    public List<Product> getProductByCategory(String category);

    public List<Product> getProductByCategory(List<String> category);

    public List<Product> getProductByCategories(List<String> categoryNames);

    /**
     * returns list of all the products irrespective of whether they are deleted or not.
     * 
     * @param category
     * @return
     */
    public List<Product> getAllProductByCategory(String category);

    /**
     * checks if a brand name exists or not
     * 
     * @param brandName
     * @return
     */
    public boolean doesBrandExist(String brandName);
    
    public String getProductUrl(Product product,boolean isSecure);
    
    public List<Product> getAllProducts();

    /**
     * returns the product which are not deleted
     * @return
     */
    List<Product> getAllNonDeletedProducts();

    public List<Product> getAllProductBySubCategory(String category);

    public List<Product> getAllProductNotByCategory(List<String> categoryNames);

    public List<Product> getAllProductByBrand(String brand);

    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage);

    public Page getProductByBrand(String brand, int page, int perPage);

    public List<Product> getProductByCategoryAndBrand(String category, String brand);

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage);

    public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand,boolean onlyCOD, boolean includeCombo, int page, int perPage);

    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage);

    public List<Product> getProductByName(String name);

    public Page getProductByName(String name, int page, int perPage);

    public List<Product> getRelatedProducts(Product product);

    public Set<Product> getProductsFromProductIds(String productIds);

    public Page getPaginatedResults(List<String> productIdList, int page, int perPage);

    public List<Product> getAllProductsById(List<String> productIdList);

    public List<Product> getRecentlyAddedProducts();

    public ProductImage getProductImageByChecksum(String checksum);

    public List<ProductImage> getImagesByProductForProductMainPage(Product product);

    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value);

    public ProductGroup findProductGroupByName(String name);

    public ProductOption findProductOptionByNameAndValue(String name, String value);

    public boolean isComboInStock(Combo combo);

    public Page getProductReviewsForCustomer(Product product, List<Long> reviewStatusList, int page, int perPage);

    public Long getAllReviews(Product product, List<Long> reviewStatusList);

    public Double getAverageRating(Product product);

    public List<Combo> getRelatedCombos(Product product);

    public Map<String,List<String>> getRecommendedProducts(Product product);

    public Map<String, List<Long>> getGroupedFilters(List<Long> filters);

	  public boolean isProductOutOfStock(Product product);

    public ProductVariant validTryOnProductVariant(Product product);

    SolrProduct createSolrProduct(Product pr);

	  public List<Product> getSimilarProducts(Product product);

    boolean isCombo(Product product);

    boolean isComboInStock(Product product);

    public List<Product> getOOSHiddenDeletedProducts();

    public ProductVO getProductVO(String productId);

    public ProductVO createProductVO(SolrProduct solrProduct);

    public ProductVO createProductVO(Product product);

    public String getAppendedProductURL(String baseUrl, String parameter, String value);

		public List<String> getAllBrands(String brandLike);

    public List<Product> getProductListWithFreebie(ProductVariant productVariant);

}
