package com.hk.impl.service.catalog;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDAO;

    public Product getProductById(String productId) {
        return getProductDAO().getProductById(productId);
    }
    
    public List<Product> getAllProducts(){
        return getProductDAO().getAll(Product.class);
    }

    @Override
    public boolean doesBrandExist(String brandName) {
        return getProductDAO().doesBrandExist(brandName);
    }

    @Override
    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value) {
        return getProductDAO().findProductExtraOptionByNameAndValue(name, value);
    }

    @Override
    public ProductGroup findProductGroupByName(String name) {
        return getProductDAO().findProductGroupByName(name);
    }

    @Override
    public ProductOption findProductOptionByNameAndValue(String name, String value) {
        return getProductDAO().findProductOptionByNameAndValue(name, value);
    }

    @Override
    public List<Product> getAllProductByBrand(String brand) {
        return getProductDAO().getAllProductByBrand(brand);
    }

    @Override
    public List<Product> getAllProductByCategory(String category) {
        return getProductDAO().getAllProductByCategory(category);
    }

    @Override
    public List<Product> getAllProductBySubCategory(String category) {
        return getProductDAO().getAllProductBySubCategory(category);
    }

    @Override
    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage) {
        return getProductDAO().getAllProductsByCategoryAndBrand(category, brand, page, perPage);
    }

    @Override
    public List<ProductImage> getImagesByProductForProductMainPage(Product product) {
        return getProductDAO().getImagesByProductForProductMainPage(product);
    }

    @Override
    public Page getPaginatedResults(List<String> productIdList, int page, int perPage) {
        return getProductDAO().getPaginatedResults(productIdList, page, perPage);
    }

    @Override
    public Page getProductByBrand(String brand, int page, int perPage) {
        return getProductDAO().getProductByBrand(brand, page, perPage);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return getProductDAO().getProductByCategory(category);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return getProductDAO().getProductByCategoryAndBrand(category, brand);
    }

    @Override
    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrand(category, brand, page, perPage);
    }

    @Override
    public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrand(categoryNames, brand, page, perPage);
    }

    @Override
    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrandNew(cat1, cat2, cat3, brand, page, perPage);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return getProductDAO().getProductByName(name);
    }

    @Override
    public Page getProductByName(String name, int page, int perPage) {
        return getProductDAO().getProductByName(name, page, perPage);
    }

    @Override
    public ProductImage getProductImageByChecksum(String checksum) {
        return getProductDAO().getProductImageByChecksum(checksum);
    }

    @Override
    public Set<Product> getProductsFromProductIds(String productIds) {
        return getProductDAO().getProductsFromProductIds(productIds);
    }

    @Override
    public List<Product> getRecentlyAddedProducts() {
        return getProductDAO().getRecentlyAddedProducts();
    }

    @Override
    public List<Product> getRelatedProducts(Product product) {
        return getProductDAO().getRelatedProducts(product);
    }

    @Override
    public Product save(Product product) {
        return getProductDAO().save(product);
    }

    public ProductDao getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDao productDAO) {
        this.productDAO = productDAO;
    }
}
