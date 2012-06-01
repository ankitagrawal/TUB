package com.hk.impl.service.catalog;

import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.stripes.controller.StripesFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.web.filter.WebContext;
import com.hk.impl.dao.content.PrimaryCategoryHeadingDaoImpl;
import com.hk.util.ProductReferrerMapper;
import com.hk.manager.LinkManager;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDAO;
    @Autowired
    private PrimaryCategoryHeadingDaoImpl primaryCategoryHeadingDaoImpl;
    @Autowired
    private LinkManager linkManager;

    public Product getProductById(String productId) {
        return getProductDAO().getProductById(productId);
    }
    
    public List<Product> getAllProducts(){
        return getProductDAO().getAll(Product.class);
    }

    public boolean doesBrandExist(String brandName) {
        return getProductDAO().doesBrandExist(brandName);
    }

    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value) {
        return getProductDAO().findProductExtraOptionByNameAndValue(name, value);
    }

    public ProductGroup findProductGroupByName(String name) {
        return getProductDAO().findProductGroupByName(name);
    }

    public ProductOption findProductOptionByNameAndValue(String name, String value) {
        return getProductDAO().findProductOptionByNameAndValue(name, value);
    }
    
    public String getProductUrl(Product product) {
        String host = "http://".concat(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());
        String contextPath = WebContext.getRequest().getContextPath();
        String urlString = host.concat(contextPath).concat("/product/");
        return urlString + product.getSlug() + "/" + product.getId();
      }

    public List<Product> getAllProductByBrand(String brand) {
        return getProductDAO().getAllProductByBrand(brand);
    }

    public List<Product> getAllProductByCategory(String category) {
        return getProductDAO().getAllProductByCategory(category);
    }

    public List<Product> getAllProductBySubCategory(String category) {
        return getProductDAO().getAllProductBySubCategory(category);
    }

    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage) {
        return getProductDAO().getAllProductsByCategoryAndBrand(category, brand, page, perPage);
    }

    public List<ProductImage> getImagesByProductForProductMainPage(Product product) {
        return getProductDAO().getImagesByProductForProductMainPage(product);
    }

    public Page getPaginatedResults(List<String> productIdList, int page, int perPage) {
        return getProductDAO().getPaginatedResults(productIdList, page, perPage);
    }

    public Page getProductByBrand(String brand, int page, int perPage) {
        return getProductDAO().getProductByBrand(brand, page, perPage);
    }

    public List<Product> getProductByCategory(String category) {
        return getProductDAO().getProductByCategory(category);
    }

    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return getProductDAO().getProductByCategoryAndBrand(category, brand);
    }

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrand(category, brand, page, perPage);
    }

    public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrand(categoryNames, brand, page, perPage);
    }

    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrandNew(cat1, cat2, cat3, brand, page, perPage);
    }

    public List<Product> getProductByName(String name) {
        return getProductDAO().getProductByName(name);
    }

    public Page getProductByName(String name, int page, int perPage) {
        return getProductDAO().getProductByName(name, page, perPage);
    }

    public ProductImage getProductImageByChecksum(String checksum) {
        return getProductDAO().getProductImageByChecksum(checksum);
    }

    public Set<Product> getProductsFromProductIds(String productIds) {
        return getProductDAO().getProductsFromProductIds(productIds);
    }

    public List<Product> getRecentlyAddedProducts() {
        return getProductDAO().getRecentlyAddedProducts();
    }

    public List<Product> getRelatedProducts(Product product) {
        return getProductDAO().getRelatedProducts(product);
    }

    public Product save(Product product) {
        return getProductDAO().save(product);
    }

    public ProductDao getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDao productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> ProductsSortedByOrder(Long primaryCategoryHeadingId, String productReferrer){
      PrimaryCategoryHeading primaryCategoryHeading = primaryCategoryHeadingDaoImpl.get(PrimaryCategoryHeading.class, primaryCategoryHeadingId);
      Collections.sort(primaryCategoryHeading.getProducts(), new ProductComparator());
      for(Product product : primaryCategoryHeading.getProducts()){
        product.setProductURL(linkManager.getProductURL(product, ProductReferrerMapper.getProductReferrerid(productReferrer)));
      }
      return primaryCategoryHeading.getProducts();
    }

    public class ProductComparator implements Comparator<Product> {
      public int compare(Product o1, Product o2) {
        if (o1.getOrderRanking() != null && o2.getOrderRanking() != null) {
          return o1.getOrderRanking().compareTo(o2.getOrderRanking());
        }
        return -1;
      }
    }
}
