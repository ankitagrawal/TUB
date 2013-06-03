package com.hk.pact.dao.catalog.product;

import java.util.List;
import java.util.Set;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.pact.dao.BaseDao;

public interface ProductDao extends BaseDao {

    public Product getProductById(String productId);

    public Product getOriginalProductById(String productId);

    @Transactional
    public Product save(Product product);

    public List<Product> getProductByCategory(String category);

    public List<Product> getProductByCategory(List<String> category);

    /**
     * returns list of all the products irrespective of whether they are deleted or not.
     *
     * @param category
     * @return
     */
    public List<Product> getAllProductByCategory(String category);

    public List<Product> getAllProductsForCatalog();

    public List<Product> getAllSubscribableProductsByCategory(String category);

    public List<Product> getAllProductNotByCategory(List<String> category);

    /**
     * checks if a brand name exists or not
     *
     * @param brandName
     * @return
     */
    public boolean doesBrandExist(String brandName);

    public List<Product> getAllProductBySubCategory(String category);

    public List<Product> getAllNonDeletedProducts();

    public List<Product> getAllProductByBrand(String brand);

    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage);

    public Page getProductByBrand(String brand, int page, int perPage);

    public List<Product> getProductByCategoryAndBrand(String category, String brand);

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage);

    public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand,Boolean onlyCOD, Boolean includeCombo, int page, int perPage);

	public Page getProductByCategoryBrandAndOptions(List<String> categoryNames, String brand, List<Long> filters, int groupsCount, Double minPrice, Double maxPrice,Boolean onlyCOD, Boolean includeCombo, int page, int perPage);

    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage);

    public List<Product> getProductByName(String name);

    public Page getProductByName(String name, int page, int perPage);

    public Page getProductByName(String name,boolean onlyCOD, boolean includeCombo, int page, int perPage);

    public List<Product> getRelatedProducts(Product product);

    public Set<Product> getProductsFromProductIds(String productIds);

    public Page getPaginatedResults(List<String> productIdList, int page, int perPage);

    public List<Product> getRecentlyAddedProducts();

    public ProductImage getProductImageByChecksum(String checksum);

    public List<ProductImage> getImagesByProductForProductMainPage(Product product);

    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value);

    public ProductGroup findProductGroupByName(String name);

    public ProductOption findProductOptionByNameAndValue(String name, String value);

	public List<ProductOption> getProductOptions(List<Long> options);

    List<Product> getAllProductsById(List<String> productIdList);

    List<Product> getProductByCategories(List<String> categoryNames);

    public List<Product> getOOSHiddenDeletedProducts();

}
