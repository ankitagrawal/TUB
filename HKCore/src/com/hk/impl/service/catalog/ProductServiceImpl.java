package com.hk.impl.service.catalog;

import com.akube.framework.dao.Page;
import com.hk.cache.vo.ProductVO;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.Keys;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.domain.api.HKApiSkuResponse;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.*;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.content.SeoData;
import com.hk.domain.search.SolrProduct;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.seo.SeoDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.image.ProductImageService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.review.ReviewService;
import com.hk.pact.service.search.ProductIndexService;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.HKImageUtils;
import com.hk.web.filter.WebContext;
import com.hk.cache.vo.ProductVO;
import com.hk.cache.ProductCache;
import net.sourceforge.stripes.controller.StripesFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("#{hkEnvProps['" + Keys.Env.projectEnv + "']}")
	  private String projectEnv;

    @Autowired
    private ProductDao                productDAO;

    @Autowired
    private ComboDao                  comboDao;

    @Autowired
    private ReviewService             reviewService;

    @Autowired
    private LinkManager               linkManager;

    @Autowired
    ProductIndexService               productIndexService;

    @Autowired
    ProductSearchService productSearchService;

    @Autowired
    ProductImageService               productImageService;

    @Autowired
    private SeoDao                    seoDao;

    @Autowired
    private BaseDao                     baseDao;

    // @Autowired
    private UserService               userService;
  
     @Autowired
   private WarehouseService         warehouseService;

    private static Logger             logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public Product getProductById(String productId) {
      Product product = getProductDAO().getProductById(productId);
      
      return product;
    }

    public List<Product> getAllProducts() {
        return getProductDAO().getAll(Product.class);
    }

    public List<Product> getAllNonDeletedProducts() {
        return getProductDAO().getAllNonDeletedProducts();
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

    public String getProductUrl(Product product, boolean isSecure) {
        String prefix = "http://";
        if (isSecure) {
            prefix = "https://";
        }
        String host = prefix.concat(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());
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

    public List<Product> getAllProductNotByCategory(List<String> categoryNames) {
        return getProductDAO().getAllProductNotByCategory(categoryNames);
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

    public List<Product> getAllProductsById(List<String> productIdList) {
        return getProductDAO().getAllProductsById(productIdList);
    }

    public Page getProductByBrand(String brand, int page, int perPage) {
        return getProductDAO().getProductByBrand(brand, page, perPage);
    }

    public List<Product> getProductByCategory(String category) {
        return getProductDAO().getProductByCategory(category);
    }

    public List<Product> getProductByCategory(List<String> category) {
        return getProductDAO().getProductByCategory(category);
    }

    public List<Product> getProductByCategories(List<String> categoryNames) {
        return getProductDAO().getProductByCategories(categoryNames);
    }

    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return getProductDAO().getProductByCategoryAndBrand(category, brand);
    }

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrand(category, brand, page, perPage);
    }

    public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand, boolean onlyCOD, boolean includeCombo, int page, int perPage) {
        return getProductDAO().getProductByCategoryAndBrand(categoryNames, brand, onlyCOD, includeCombo, page, perPage);
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
        //Product oldProduct = getProductDAO().getOriginalProductById(product.getId());
        Product savedProduct = getProductDAO().save(product);
        productIndexService.indexProduct(savedProduct);

        // Audit Trail of Product
        // TODO: Old and New JSON are same as of now. Need to do some fixes to avoid hibernate reattachment
        // That's why commneting md5 check as they are same
        /*try {
          EntityAuditTrail eat = new EntityAuditTrail();
          eat.setEntityId(product.getId());
          //Gson gson = JsonUtils.getGsonDefault();
          Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
          String oldJson = "";
          if(oldProduct != null)
            oldJson = gson.toJson(oldProduct);
          eat.setOldJson(oldJson);
          savedProduct.setCategoriesPipeSeparated(savedProduct.getPipeSeparatedCategories());
          String newJson = gson.toJson(savedProduct);
          eat.setNewJson(newJson);
          if (!BaseUtils.getMD5Checksum(oldJson).equals(BaseUtils.getMD5Checksum(newJson))) {
            eat.setUserEmail(getUserService().getLoggedInUser().getLogin());
            eat.setCallingClass(Reflection.getCallerClass(2).getName());         
            eat.setStackTrace(JsonUtils.getGsonDefault().toJson(Thread.currentThread().getStackTrace()));
            eat.setCreateDt(new Date());
            getProductDAO().save(eat);
          }
        } catch (Exception e) {
            logger.error("Error while entering audit trail for product->" + product.getId());
        }
*/
      //Ajeet - Cache Product
      ProductCache.getInstance().refreshCache(savedProduct);
      
        return savedProduct;
    }

    public Page getProductReviewsForCustomer(Product product, List<Long> reviewStatusList, int page, int perPage) {
        return getReviewService().getProductReviewsForCustomer(product, reviewStatusList, page, perPage);
    }

    public Long getAllReviews(Product product, List<Long> reviewStatusList) {
        return getReviewService().getAllReviews(product, reviewStatusList);
    }

    public Double getAverageRating(Product product) {
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

    public ComboDao getComboDao() {
        return comboDao;
    }

    public void setComboDao(ComboDao comboDao) {
        this.comboDao = comboDao;
    }

    public boolean isComboInStock(Combo combo) {
        if (combo.isDeleted() != null && combo.isDeleted()) {
            return false;
        } else {
            for (ComboProduct comboProduct : combo.getComboProducts()) {
                if (!comboProduct.getAllowedProductVariants().isEmpty() && comboProduct.getAllowedInStockVariants().isEmpty()) {
                    return false;
                } else if (comboProduct.getProduct().getInStockVariants().isEmpty()) {
                    return false;
                } else if (comboProduct.getProduct().isDeleted() != null && comboProduct.getProduct().isDeleted()) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean isComboInStock(Product product) {
        boolean isComboInStock = true;
        // if (Hibernate.getClass(product).equals(Combo.class)){
        if (isCombo(product)) {
            Combo combo = (Combo) product;
            isComboInStock = isComboInStock(combo);
        }
        return isComboInStock;
    }

    public List<Product> getOOSHiddenDeletedProducts(){
         return productDAO.getOOSHiddenDeletedProducts();
    }

    public List<Combo> getRelatedCombos(Product product) {
        return getComboDao().getCombos(product);
    }

    public boolean isCombo(Product product) {
        return product instanceof Combo;
    }

    public boolean isProductOutOfStock(Product product) {
        List<ProductVariant> productVariants = product.getProductVariants();
        boolean isOutOfStock = true;
        if (isCombo(product)) {
            return !isComboInStock(product);
        }
        for (ProductVariant pv : productVariants) {
            if (!pv.getOutOfStock() && !pv.getDeleted()) {
                isOutOfStock = false;
                break;
            }
        }
        return isOutOfStock;
    }

    @Override
    public ProductVariant validTryOnProductVariant(Product product) {

	    //Commenting the code which check validTryOnProductVariant, it will always retrun null. 7th July 2013

        /*if (product.getPrimaryCategory().getName().equals(CategoryConstants.EYE)) {
            // Category virtualTryOnCategory = new Category("Virtual Try On", "Try It Online");
            // if (product.getCategories().contains(virtualTryOnCategory)) {
            for (ProductVariant productVariant : product.getInStockVariants()) {
                int optionsCounter = 0;
                for (ProductOption productOption : productVariant.getProductOptions()) {
                    if (productOption.getName().equalsIgnoreCase("Color") || productOption.getName().equalsIgnoreCase("Gender") || productOption.getName().equalsIgnoreCase("Size")
                            || productOption.getName().equalsIgnoreCase("Virtual Try On")) {
                        optionsCounter++;
                    }
                }
                if (optionsCounter != 4) {
                    return null;
                }
                if (!productImageService.searchProductImages(EnumImageType.FrontFacingEye.getId(), product, productVariant, false, null).isEmpty()
                        && !productImageService.searchProductImages(EnumImageType.SideFacingEye.getId(), product, productVariant, false, null).isEmpty()) {
                    return productVariant;
                }
            }
            // }
        }*/
        return null;
    }

    /*
     * public Map<String, List<String>> getRecommendedProducts(Product findProduct) { return
     * getRecommendedProducts(findProduct); }
     */

    public Map<String, List<String>> getRecommendedProducts(Product findProduct) {
        Map<String, List<String>> productsResult = new HashMap<String, List<String>>();
        List<String> productsList = new ArrayList<String>();
        Set<String> products = new HashSet<String>();
        String source = "";
        boolean moogaOn = false;
        if (!moogaOn || (products.size() < 6)) {
            if (products.size() > 0) {
                source += " + ";
            }
            List<Product> productList = getProductDAO().getProductById(findProduct.getId()).getRelatedProducts();
            for (Product product : productList) {
                if (isProductValid(product)) {
                    products.add(product.getId());
                }
            }
            source += "DB";
        }
        for (String product : products) {
            productsList.add(product);
        }
        if (!productsList.isEmpty()) {
            productsResult.put(source, productsList);
        }
        return productsResult;
    }

    private boolean isProductValid(Product product) {
        boolean isDeleted = product.isDeleted() == null ? false : product.isDeleted();
        boolean isHidden = product.isHidden() == null ? false : product.isHidden();
        boolean isGoogleAdDisallowed = product.isGoogleAdDisallowed() == null ? false : product.isGoogleAdDisallowed();
        if ((product != null) && !isDeleted && !isGoogleAdDisallowed && !isHidden && !isProductOutOfStock(product)) {
            return true;
        }
        return false;
    }

    // public List<Product> productsSortedByOrder(Long primaryCategoryHeadingId, String productReferrer) {
    // PrimaryCategoryHeading primaryCategoryHeading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class,
    // primaryCategoryHeadingId);
    // Collections.sort(primaryCategoryHeading.getProducts(), new ProductOrderRankingComparator());
    // List<Product> sortedProductsByOrder = new ArrayList<Product>();
    // for (Product product : primaryCategoryHeading.getProducts()) {
    // product.setProductURL(linkManager.getRelativeProductURL(product,
    // ProductReferrerMapper.getProductReferrerid(productReferrer)));
    // if (isProductValid(product)){
    // sortedProductsByOrder.add(product);
    // }
    // }
    // return sortedProductsByOrder;
    // }

    public Map<String, List<Long>> getGroupedFilters(List<Long> filters) {
        Map<String, List<Long>> filterMap = new HashMap<String, List<Long>>();
        List<Long> groupedFilters;
        List<ProductOption> options = getProductDAO().getProductOptions(filters);
        for (ProductOption option : options) {
            String group = option.getName().toUpperCase();
            if (filterMap.containsKey(group)) {
                groupedFilters = filterMap.get(group);
            } else {
                groupedFilters = new ArrayList<Long>(0);
            }
            groupedFilters.add(option.getId());
            filterMap.put(group, groupedFilters);
        }
        return filterMap;
    }

    /*
     * public Product createProduct(SolrProduct solrProduct){ Product product = new Product(); if
     * (solrProduct.getKeywords() != null){ product.setKeywords(solrProduct.getKeywords()); } if (solrProduct.getId() !=
     * null){ product.setId(solrProduct.getId()); } if (solrProduct.getSlug() != null){
     * //solrProduct.set(product.getSlug()); } if (solrProduct.getName() != null){
     * product.setName(solrProduct.getName()); } if (solrProduct.getBrand() != null){
     * product.setBrand(solrProduct.getBrand()); } if (solrProduct.getOverview() != null){
     * product.setOverview(solrProduct.getOverview()); } if (solrProduct.getDescription() != null){
     * product.setDescription(solrProduct.getDescription()); }
     * product.setGoogleAdDisallowed(solrProduct.getGoogleAdDisallowed());
     * product.setOrderRanking(solrProduct.getOrderRanking()); product.setMainImageId(solrProduct.getMainImageId());
     * product.setDeleted(solrProduct.getDeleted()); product.setOutOfStock(solrProduct.getOutOfStock()); double price =
     * 0d; if (product.getMinimumHKPriceProductVariant().getHkPrice() != null){ price =
     * product.getMinimumHKPriceProductVariant().getHkPrice(); } Combo combo = comboDao.getComboById(product.getId());
     * if (combo!= null){ solrProduct.setCombo(true); solrProduct.setMarkedPrice(combo.getMarkedPrice());
     * solrProduct.setHkPrice(combo.getHkPrice()); solrProduct.setComboDiscountPercent(combo.getDiscountPercent()); } if
     * (product.getService() != null){ product.setService(solrProduct.getService()); } return product; }
     */

    public SolrProduct createSolrProduct(Product product) {
        SolrProduct solrProduct = new SolrProduct();
        SeoData seoData = seoDao.getSeoById(product.getId());
        if (product.getKeywords() != null) {
            solrProduct.setKeywords(product.getKeywords());
        }
        if (product.getId() != null) {
            solrProduct.setId(product.getId());
        }
        if (product.getSlug() != null) {
            solrProduct.setSlug(product.getSlug());
        }
        if (product.getName() != null) {
            solrProduct.setName(product.getName());
        }
        if (product.getBrand() != null) {
            List<String> brands = new ArrayList<String>();
            if (product instanceof Combo) {
                Combo combo = (Combo) product;
                for (ComboProduct comboProduct : combo.getComboProducts()) {
                    String cpBrand = comboProduct.getProduct().getBrand();
                    if (cpBrand != null) {
                        brands.add(cpBrand);
                        this.addBrandSynonyms(brands, cpBrand);
                    }
                }
              brands.add(product.getBrand());
            } else {
                String pBrand = product.getBrand();
                brands.add(pBrand);
                this.addBrandSynonyms(brands, pBrand);
            }
            solrProduct.setBrand(brands);
        }
        if (product.getOverview() != null) {
            solrProduct.setOverview(StringEscapeUtils.escapeHtml(product.getOverview().trim()));
        }
        if (product.getDescription() != null) {
            solrProduct.setDescription(StringEscapeUtils.escapeHtml(product.getDescription()));
        }
        if (seoData != null) {
            if (seoData.getH1() != null) {
                solrProduct.setH1(seoData.getH1());
            }
            if (seoData.getTitle() != null) {
                solrProduct.setTitle(seoData.getTitle());
            }
            if (seoData.getMetaKeyword() != null) {
                solrProduct.setMetaKeyword(seoData.getMetaKeyword());
            }
            if (seoData.getMetaDescription() != null) {
                solrProduct.setMetaDescription(seoData.getMetaDescription());
            }
            if (seoData.getDescriptionTitle() != null) {
                solrProduct.setDescriptionTitle(seoData.getDescriptionTitle());
            }
            if (seoData.getDescription() != null) {
                solrProduct.setSeoDescription(seoData.getDescription());
            }
        }
        if (product.isGoogleAdDisallowed() != null) {
            solrProduct.setGoogleAdDisallowed(product.isGoogleAdDisallowed());
        }
        if (product.getOrderRanking() != null) {
            solrProduct.setOrderRanking(product.getOrderRanking());
        }
        if (product.getMainImageId() != null) {
            solrProduct.setMainImageId(product.getMainImageId());
        }

        List<String> categories = new ArrayList<String>();
        List<String> categoryDisplayNames = new ArrayList<String>();
        if (product.getCategories() != null) {
            for (Category category : product.getCategories()) {
                categories.add(category.getName());
                categoryDisplayNames.add(category.getDisplayName());
            }
        }

        ProductVariant productVariant = product.getMinimumMRPProducVariant();
        if (productVariant.getMarkedPrice() != null) {
            solrProduct.setMinimumMRPProductVariantMarkedPrice(productVariant.getMarkedPrice());
        }
        if (productVariant.getDiscountPercent() != null) {
            solrProduct.setMinimumMRPProducVariantDiscount(productVariant.getDiscountPercent());
        }
        productVariant = product.getMaximumMRPProducVariant();
        if (productVariant.getDiscountPercent() != null) {
            solrProduct.setMaximumMRPProducVariantDiscount(productVariant.getDiscountPercent());
        }
        
        productVariant = product.getMaximumMRPProducVariant();
        if (productVariant.getPostpaidAmount() != null) {
            solrProduct.setPostpaidPrice(productVariant.getPostpaidAmount());
        }

        if (product.getDeleted() != null) {
            solrProduct.setDeleted(product.getDeleted().booleanValue());
        }
        if (product.getOutOfStock() != null) {
            solrProduct.setOutOfStock(product.getOutOfStock().booleanValue());
        }

        if (product.getHidden() != null) {
            solrProduct.setHidden(product.getHidden().booleanValue());
        } else {
            solrProduct.setHidden(false);
        }

        if (product.getCodAllowed() != null) {
            solrProduct.setCODAllowed(product.getCodAllowed());
        } else {
            solrProduct.setCODAllowed(false);
        }

        Product freebieProduct = product.hasFreebie();
        if(freebieProduct != null){
            solrProduct.setFreebieDesc(freebieProduct.getName());  //can be simply called as freebie as well
        } else {
           solrProduct.setFreebieDesc("");
        }

        solrProduct.setProductUrl(convertToWww(getProductUrl(product, false)));
        if (product.getMainImageId() != null) {
            solrProduct.setSmallImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, product.getMainImageId()));
        }

        if (isCombo(product)) {
          Combo combo = comboDao.getComboById(product.getId());
          solrProduct.setCombo(true);
          solrProduct.setMarkedPrice(combo.getMarkedPrice());
          solrProduct.setHkPrice(combo.getHkPrice());
          solrProduct.setComboDiscountPercent(combo.getDiscountPercent());
        } else {
          productVariant = product.getInStockMaximumDiscountProductVariant();
          if (productVariant.getDiscountPercent() != null) {
            solrProduct.setHkPrice(productVariant.getHkPrice());
            solrProduct.setMarkedPrice(productVariant.getMarkedPrice());
            solrProduct.setMaximumDiscountProductVariantDiscountPercentage(productVariant.getDiscountPercent());
            solrProduct.setMaximumDiscountProductVariantHKPrice(productVariant.getHkPrice());
            solrProduct.setMaximumDiscountProductVariantMRP(productVariant.getMarkedPrice());
          }
        }

        if (product.getService() != null) {
            solrProduct.setService(product.getService());
        }

        solrProduct.setCategory(categories);
        solrProduct.setCategoryDisplayName(categoryDisplayNames);
        return solrProduct;
    }

    // Thing logic should be centralized. SOLR syns cann't work for brand terms like on etc.
    private List<String> addBrandSynonyms(List<String> brandList, String brand) {
        if (brand.equals("ON")) {
            brandList.add("Optimum Nutrition");
        } else if (brand.equals("MuscleBlaze")) {
            brandList.add("MB");
            brandList.add("Muscle Blaze");
        }
        return brandList;
    }

    public List<Product> getSimilarProducts(Product product) {
        List<Product> inStockSimilarProducts = new ArrayList<Product>();
        int ctr = 0;
        for (SimilarProduct similarProduct : product.getSimilarProducts()) {
            Product sProduct = similarProduct.getSimilarProduct();
            if (!sProduct.isDeleted() && !sProduct.isOutOfStock()) {
                sProduct.setProductURL(linkManager.getRelativeProductURL(sProduct, EnumProductReferrer.relatedProductsPage.getId()));
                inStockSimilarProducts.add(sProduct);
                ctr++;
                if (ctr >= 5)
                    break;
            }
        }
        return inStockSimilarProducts;
    }

    // todo : isko thik kar do - for now hardcoding logic to convert admin.healthkart.com to www.healthkart.com
    public static String convertToWww(String productUrl) {
        return productUrl.replaceAll("admin\\.healthkart\\.com", "www.healthkart.com");
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = ServiceLocatorFactory.getService(UserService.class);
        }
        return userService;
    }


  @Override
  public ProductVO getProductVO(String productId) {
    ProductVO productVO = ProductCache.getInstance().getProductCache(productId);
    if (productVO == null) {
      SolrProduct solrProduct = productSearchService.getProduct(productId);
      if (solrProduct != null) {
//        logger.debug("Getting ProductVO from SOLR for Product=" +productId);
        productVO = this.createProductVO(solrProduct);
      } else {
//        logger.debug("Getting ProductVO from DB for Product=" +productId);
        Product product = this.getProductById(productId);
        productVO = this.createProductVO(product);
      }
    }
    return productVO;
  }

  public ProductVO createProductVO(SolrProduct solrProduct) {
    ProductVO productVO = new ProductVO();
    productVO.setId(solrProduct.getId());
    productVO.setName(solrProduct.getName());
    productVO.setMaxDiscount(solrProduct.getMaximumDiscountProductVariantDiscountPercentage());
    productVO.setMaxDiscountHKPrice(solrProduct.getMaximumDiscountProductVariantHKPrice() + (solrProduct.getService() ? solrProduct.getPostpaidPrice() : 0.0));
    productVO.setMaxDiscountMRP(solrProduct.getMaximumDiscountProductVariantMRP());
    productVO.setProductURL(solrProduct.getProductUrl());
    productVO.setMainImageId(solrProduct.getMainImageId());
    productVO.setFreebieDesc(solrProduct.getFreebieDesc());  //can be simply called as freebie as well
    productVO.setGoogleAdDisallowed(solrProduct.getGoogleAdDisallowed());
    productVO.setDeleted(solrProduct.getDeleted());
    productVO.setHidden(solrProduct.isHidden());
    productVO.setOutOfStock(solrProduct.getOutOfStock());
    productVO.setCombo(solrProduct.getCombo());
    productVO.setService(solrProduct.getService());

    //Refreshing Cache for ProductVO
    ProductCache.getInstance().refreshCache(productVO);
    
    return productVO;
  }

  public ProductVO createProductVO(Product product) {
    ProductVO productVO = new ProductVO();
    productVO.setId(product.getId());
    productVO.setName(product.getName());

    productVO.setProductURL(linkManager.getProductURL(product, 0L));
    productVO.setMainImageId(product.getMainImageId());
      Product freebieProduct = product.hasFreebie();
      if (freebieProduct != null) {
          productVO.setFreebieDesc(freebieProduct.getName());  //can be simply called as freebie as well
      } else {
          productVO.setFreebieDesc("");
      }
      productVO.setGoogleAdDisallowed(product.isGoogleAdDisallowed());
    productVO.setDeleted(product.isDeleted());
    productVO.setHidden(product.isHidden());
    productVO.setOutOfStock(product.isOutOfStock());
    if (product.isCombo())
      productVO.setCombo(true);
    else {
      productVO.setCombo(false);
      ProductVariant maxDiscountVariant = product.getInStockMaximumDiscountProductVariant();
      if (maxDiscountVariant != null && maxDiscountVariant.getId() != null) {
        productVO.setMaxDiscount(maxDiscountVariant.getDiscountPercent());
        productVO.setMaxDiscountHKPrice(maxDiscountVariant.getHkPrice() + (product.getService() ? maxDiscountVariant.getPostpaidAmount() : 0.0));
        productVO.setMaxDiscountMRP(maxDiscountVariant.getMarkedPrice());
      }
    }
    productVO.setService(product.getService());

    //Refreshing Cache for ProductVO
    ProductCache.getInstance().refreshCache(productVO);

    return productVO;
  }

  public String getAppendedProductURL(String baseUrl, String parameter, String value) {
      if (StringUtils.isNotBlank(baseUrl) && StringUtils.isNotBlank(parameter) && StringUtils.isNotBlank(value)) {
        if (StringUtils.equals(projectEnv, "admin")) {
          baseUrl = baseUrl.replace("www.healthkart.com", "admin.healthkart.com");
        }
        if (baseUrl.contains("?")) {
          return baseUrl.concat("&" + parameter + "=" + value);
        } else {
          return baseUrl.concat("?" + parameter + "=" + value);
        }
      }
      return baseUrl;
    }

	public List<String> getAllBrands(String brandLike) {
		return productDAO.getAllBrands(brandLike);
	}

 @Override
    public List<Product> getProductListWithFreebie(ProductVariant productVariant) {
        return productDAO.getProductsWithFreebie(productVariant);
    }

    public ProductVariant updatePVForBrightInventory(HKApiSkuResponse hKApiSkuResponse, ProductVariant productVariant){
    				logger.debug("hKApiSkuResponse got -"+hKApiSkuResponse.getVariantId()+", qty - "+hKApiSkuResponse.getQty()+", MRP -"+hKApiSkuResponse.getMrp());
            productVariant.setCostPrice(hKApiSkuResponse.getCostPrice());
            productVariant.setMarkedPrice(hKApiSkuResponse.getMrp());
            productVariant.setOutOfStock(hKApiSkuResponse.isOutOfStock());
            productVariant.setMrpQty(hKApiSkuResponse.getQty());
            productVariant.setNetQty(hKApiSkuResponse.getNetQty());
            productVariant.setHkPrice(hKApiSkuResponse.getHkPrice());

            Double newHkPrice = hKApiSkuResponse.getMrp() * (1 - productVariant.getDiscountPercent());
            productVariant.setHkPrice(newHkPrice);  

            Warehouse warehouse = warehouseService.getWarehouse(hKApiSkuResponse.getFcCode());
//            Warehouse warehouse = baseDao.get(Warehouse.class, hKApiSkuResponse.getWarehouseId());
            productVariant.setWarehouse(warehouse);
            productVariant = (ProductVariant) baseDao.save(productVariant);
            Product product = productVariant.getProduct();
            List<ProductVariant> inStockVariants = product.getInStockVariants();
            if (inStockVariants != null && inStockVariants.isEmpty()) {
                product.setOutOfStock(true);
            } else {
                product.setOutOfStock(false);
            }
            baseDao.save(product);
            return productVariant;
        }

}
