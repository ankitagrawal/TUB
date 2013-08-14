package com.hk.cache;

import com.hk.cache.vo.ProductVO;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.search.SolrProduct;
import com.hk.pact.service.catalog.ProductService;
import com.hk.service.ServiceLocatorFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ProductCache {

  private Logger logger = LoggerFactory.getLogger(ProductCache.class);

  private static ProductCache _instance = new ProductCache();
  private ProductCache _transient;

  private Map<String, ProductVO> idToProductCache = new HashMap<String, ProductVO>();

  private ProductService productService;

  private ProductCache() {
  }

  public static ProductCache getInstance() {
    return _instance;
  }

  public void addProduct(ProductVO productVO) {
    if (StringUtils.isNotBlank(productVO.getId())) {
      idToProductCache.put(productVO.getId(), productVO);
    }

  }

  public ProductVO getProductCache(String productId) {
//    logger.debug("Getting ProductVO from Cache for Product=" + productId);
    return idToProductCache.get(productId);
  }

  public ProductVO getProductById(String productId) {
    ProductVO productVO = idToProductCache.get(productId);

    /**
     * if product is not in cache try and attempt to find from db
     */
    if (productVO == null) {
      logger.debug("Product Cache is NULL. Creating Cache for Product=" + productId);
      Product product = getProductService().getProductById(productId);
      productVO = getProductService().createProductVO(product);
    }
    return productVO;
  }

  public void refreshCache(Product product) {
    if (product != null && product.getId() != null) {
//      logger.debug("Refreshing Product Cache for Product=" + product.getId());
      idToProductCache.put(product.getId(), getProductService().createProductVO(product));
    }
  }

  public void refreshCache(ProductVO productVO) {
    if (productVO != null && productVO.getId() != null) {
//      logger.debug("Refreshing Product Cache for ProductVO=" + productVO.getId());
      idToProductCache.put(productVO.getId(), productVO);
    }
  }


  public void freeze() {
    _instance = this;
  }

  public void reset() {
    _transient = new ProductCache();
  }

  public ProductCache getTransientCache() {
    return _transient;
  }

  public ProductService getProductService() {
    if (productService == null) {
      productService = ServiceLocatorFactory.getService(ProductService.class);
    }
    return productService;
  }
}