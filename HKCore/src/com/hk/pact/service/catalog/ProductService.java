package com.hk.pact.service.catalog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.combo.Combo;

public interface ProductService {

    public Product getProductById(String productId);

    public Product save(Product product);

    public List<Product> getProductByCategory(String category);

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

    public List<Product> getAllProductBySubCategory(String category);

    public List<Product> getAllProductNotByCategory(List<String> categoryNames);

    public List<Product> getAllProductByBrand(String brand);

    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage);

    public Page getProductByBrand(String brand, int page, int perPage);

    public List<Product> getProductByCategoryAndBrand(String category, String brand);

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage);

    public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand, int page, int perPage);

    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage);

    public List<Product> getProductByName(String name);

    public Page getProductByName(String name, int page, int perPage);

    public List<Product> getRelatedProducts(Product product);

    public Set<Product> getProductsFromProductIds(String productIds);

    public Page getPaginatedResults(List<String> productIdList, int page, int perPage);

    public List<Product> getRecentlyAddedProducts();

    public ProductImage getProductImageByChecksum(String checksum);

    public List<ProductImage> getImagesByProductForProductMainPage(Product product);

    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value);

    public ProductGroup findProductGroupByName(String name);

    public ProductOption findProductOptionByNameAndValue(String name, String value);

    public List<Product> productsSortedByOrder(Long primaryCategoryHeadingId, String productReferrer);

    public boolean isComboInStock(Combo combo);

    public Page getProductReviews(Product product, List<Long> reviewStatusList, int page, int perPage);

    public Long getAllReviews(Product product, List<Long> reviewStatusList);

    public Double getAverageRating(Product product);

    public List<Combo> getRelatedCombos(Product product);

    public Map<String,List<String>> getRecommendedProducts(Product product);

    Map<String, List<String>> getRelatedMoogaProducts(Product findProduct);
}
