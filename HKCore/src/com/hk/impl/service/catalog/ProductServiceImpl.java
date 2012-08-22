package com.hk.impl.service.catalog;

import java.util.*;

import com.hk.domain.search.SolrProduct;
import net.sourceforge.stripes.controller.StripesFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.review.ReviewService;
import com.hk.util.ProductReferrerMapper;
import com.hk.web.filter.WebContext;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao                productDAO;

    @Autowired
    private ComboDao                  comboDao;

    @Autowired
    private ReviewService             reviewService;

    @Autowired
    private PrimaryCategoryHeadingDao primaryCategoryHeadingDao;

    @Autowired
    private LinkManager               linkManager;

    public Product getProductById(String productId) {
        return getProductDAO().getProductById(productId);
    }

    public List<Product> getAllProducts() {
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

    public Page getProductReviews(Product product, List<Long> reviewStatusList, int page, int perPage) {
        return getReviewService().getProductReviews(product, reviewStatusList, page, perPage);
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

	public boolean isComboInStock(String comboId) {
		Combo combo = getComboDao().getComboById(comboId);
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

    public List<Combo> getRelatedCombos(Product product) {
        return getComboDao().getCombos(product);
    }

    public boolean isProductOutOfStock(Product product) {
        List<ProductVariant> productVariants = product.getProductVariants();
        boolean isOutOfStock = true;
        for (ProductVariant pv : productVariants) {
            if (!pv.getOutOfStock() && !pv.getDeleted()) {
                isOutOfStock = false;
                break;
            }
        }
        return isOutOfStock;
    }
/*

    public Map<String, List<String>> getRecommendedProducts(Product findProduct) {
        return getRecommendedProducts(findProduct);
    }
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

    public List<Product> productsSortedByOrder(Long primaryCategoryHeadingId, String productReferrer) {
        PrimaryCategoryHeading primaryCategoryHeading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class, primaryCategoryHeadingId);
        Collections.sort(primaryCategoryHeading.getProducts(), new ProductOrderRankingComparator());
        for (Product product : primaryCategoryHeading.getProducts()) {
            product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(productReferrer)));
        }
        return primaryCategoryHeading.getProducts();
    }

	public Map<String, List<Long>> getGroupedFilters(List<Long> filters){
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

    /*public Product createProduct(SolrProduct solrProduct){
        Product product = new Product();
        if (solrProduct.getKeywords() != null){
            product.setKeywords(solrProduct.getKeywords());
        }
        if (solrProduct.getId() != null){
            product.setId(solrProduct.getId());
        }
        if (solrProduct.getSlug() != null){
            //solrProduct.set(product.getSlug());
        }
        if (solrProduct.getName() != null){
            product.setName(solrProduct.getName());
        }
        if (solrProduct.getBrand() != null){
            product.setBrand(solrProduct.getBrand());
        }
        if (solrProduct.getOverview() != null){
            product.setOverview(solrProduct.getOverview());
        }
        if (solrProduct.getDescription() != null){
            product.setDescription(solrProduct.getDescription());
        }
        product.setGoogleAdDisallowed(solrProduct.getGoogleAdDisallowed());

        product.setOrderRanking(solrProduct.getOrderRanking());
        product.setMainImageId(solrProduct.getMainImageId());

        product.setDeleted(solrProduct.getDeleted());

        product.setOutOfStock(solrProduct.getOutOfStock());

        double price = 0d;

        if (product.getMinimumHKPriceProductVariant().getHkPrice() != null){
            price = product.getMinimumHKPriceProductVariant().getHkPrice();
        }

        Combo combo = comboDao.getComboById(product.getId());
        if (combo!= null){
            solrProduct.setCombo(true);
            solrProduct.setMarkedPrice(combo.getMarkedPrice());
            solrProduct.setHkPrice(combo.getHkPrice());
            solrProduct.setComboDiscountPercent(combo.getDiscountPercent());
        }

        if (product.getService() != null){
            product.setService(solrProduct.getService());
        }

        return product;
    }*/

    public SolrProduct createSolrProduct(Product product){
        SolrProduct solrProduct = new SolrProduct();
        if (product.getKeywords() != null){
            solrProduct.setKeywords(product.getKeywords());
        }
        if (product.getId() != null){
            solrProduct.setId(product.getId());
        }
        if (product.getSlug() != null){
            solrProduct.setSlug(product.getSlug());
        }
        if (product.getName() != null){
            solrProduct.setName(product.getName());
        }
        if (product.getBrand() != null){
            solrProduct.setBrand(product.getBrand());
        }
        if (product.getOverview() != null){
            solrProduct.setOverview(product.getOverview());
        }
        if (product.getDescription() != null){
            solrProduct.setDescription(product.getDescription());
        }
        if (product.isGoogleAdDisallowed() != null){
            solrProduct.setGoogleAdDisallowed(product.isGoogleAdDisallowed());
        }
        if (product.getOrderRanking() != null){
            solrProduct.setOrderRanking(product.getOrderRanking());
        }
        if (product.getMainImageId() != null){
            solrProduct.setMainImageId(product.getMainImageId());
        }

        List<String> categories = new ArrayList<String>();
        if (product.getCategories() != null){
            for (Category category : product.getCategories()){
                categories.add(category.getName());
            }
        }

        if (product.getMinimumMRPProducVariant().getMarkedPrice() != null){
            solrProduct.setMinimumMRPProductVariantMarkedPrice(product.getMinimumMRPProducVariant().getMarkedPrice());
        }
        if (product.getMinimumMRPProducVariant().getDiscountPercent() != null){
            solrProduct.setMinimumMRPProducVariantDiscount(product.getMinimumMRPProducVariant().getDiscountPercent());
        }
        if (product.getMaximumMRPProducVariant().getDiscountPercent() != null){
            solrProduct.setMaximumMRPProducVariantDiscount(product.getMaximumMRPProducVariant().getDiscountPercent());
        }

        if (product.getMaximumDiscountProducVariant().getDiscountPercent() != null){
            solrProduct.setMaximumDiscountProductVariantDiscountPercentage (product.getMaximumDiscountProducVariant().getDiscountPercent());
        }

        if(product.getMaximumMRPProducVariant().getPostpaidAmount() != null){
            solrProduct.setPostpaidPrice(product.getMaximumMRPProducVariant().getPostpaidAmount());
        }

        if (product.getDeleted() != null){
            solrProduct.setDeleted(product.getDeleted().booleanValue());
        }
        if (product.getOutOfStock() != null){
            solrProduct.setOutOfStock(product.getOutOfStock());
        }
        if (product.getOutOfStock() != null){
            solrProduct.setOutOfStock(product.getOutOfStock());
        }

        double price = 0d;

        if (product.getMinimumHKPriceProductVariant().getHkPrice() != null){
            price = product.getMinimumHKPriceProductVariant().getHkPrice();
        }
        solrProduct.setHkPrice(price);
        Combo combo = comboDao.getComboById(product.getId());
        if (combo!= null){
            solrProduct.setCombo(true);
            solrProduct.setMarkedPrice(combo.getMarkedPrice());
            solrProduct.setHkPrice(combo.getHkPrice());
            solrProduct.setComboDiscountPercent(combo.getDiscountPercent());
        }

        if (product.getService() != null){
            solrProduct.setService(product.getService());
        }

        solrProduct.setCategory(categories);
        return solrProduct;
    }

    public List<SolrProduct> getProductsSortedByOrderRanking(PrimaryCategoryHeading primaryCategoryHeading) {
        List<Product> products = primaryCategoryHeading.getProductSortedByOrderRanking();
        List<SolrProduct> solrProducts = new ArrayList<SolrProduct>();
        for (Product product : products){
            solrProducts.add(createSolrProduct(product));
        }
        return solrProducts;
    }

	public List<Product> getSortedByStock(List<Product> productList) {
		List<Product> outOfStockproductList = new ArrayList<Product>();
		for (Product product : productList) {
			if (product.getProductVariants() != null && !product.getProductVariants().isEmpty()) {
				if (isProductOutOfStock(product)) {
					product.setOutOfStock(true);
					outOfStockproductList.add(product);
				}
			} else if (!isComboInStock(product.getId())) {
				product.setOutOfStock(true);
				outOfStockproductList.add(product);
				//productList.remove(product);
			}
		}
		productList.removeAll(outOfStockproductList);
		productList.addAll(outOfStockproductList);
		return productList;
	}
}


