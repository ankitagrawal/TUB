package com.hk.impl.service.catalog;

import java.util.*;

import com.hk.constants.core.Keys;
import com.hk.pact.service.mooga.RecommendationEngine;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import net.sourceforge.stripes.controller.StripesFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.review.ReviewService;
import com.hk.web.filter.WebContext;

@Service
public class ProductServiceImpl implements ProductService {

    private final int MAX_PRODUCT_LIMIT = 6;    //Max number of recommended products to be displayed on UI

    @Autowired
    private ProductDao productDAO;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductVariantDao productVariantDao;

    @Autowired
    private RecommendationEngine recommendationService;

    @Value("#{hkEnvProps['" + Keys.Env.moogaEnabled + "']}")
    private Boolean moogaOn;

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

    public Page getProductReviews(Product product, List<Long> reviewStatusList, int page, int perPage){
       return getReviewService().getProductReviews(product,reviewStatusList,page,perPage);
    }

  public Long getAllReviews(Product product, List<Long> reviewStatusList){
       return getReviewService().getAllReviews(product, reviewStatusList);
    }

    public Double getAverageRating(Product product){
       return getReviewService().getProductStarRating(product);
    }

    public ProductDao getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDao productDAO) {
        this.productDAO = productDAO;
    }

  public ReviewService getReviewService() {
    return reviewService;
  }

  public void setReviewService(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  public boolean isComboInStock(Combo combo) {
    for (ComboProduct comboProduct : combo.getComboProducts()) {
      if (!comboProduct.getAllowedProductVariants().isEmpty() && comboProduct.getAllowedInStockVariants().isEmpty()) {
        return false;
      } else if (comboProduct.getProduct().getInStockVariants().isEmpty()) {
        return false;
      }
    }
    return true;
  }

   public Map<String, List<Product>> getRecommendedProducts(String pId){
       Map<String, List<Product>> productsResult = new HashMap<String, List<Product>>();
       List<Product> products = new ArrayList<Product>();
       String source = "";
       if (moogaOn){
           List<String> pvIdList = recommendationService.getRecommendedProducts(pId);
           Iterator it = pvIdList.iterator();
           int productCount = 0;
           source = "MOOGA";
           while (it.hasNext() && (productCount < MAX_PRODUCT_LIMIT)) {
               Product product = productDAO.getProductById((String)it.next());
               if ((product != null) && !product.isDeleted()){
                   products.add(product);
                   ++productCount;
               }
           }
       }

       if (!moogaOn || (products.size() == 0)){
           products = getProductDAO().getProductById(pId).getRelatedProducts();
           source = "DB";
       }
       productsResult.put(source, products);
       return productsResult;
   }
}
