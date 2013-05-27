package com.hk.rest.mobile.service.action;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.dao.Page;
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.SolrSchemaConstants;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.domain.LocalityMap;
import com.hk.domain.MapIndia;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.content.SeoData;
import com.hk.domain.search.PaginationFilter;
import com.hk.domain.search.RangeFilter;
import com.hk.domain.search.SearchFilter;
import com.hk.domain.search.SortFilter;
import com.hk.domain.user.Address;
import com.hk.dto.menu.MenuNode;
import com.hk.dto.search.SearchResult;
import com.hk.helper.MenuHelper;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.rest.mobile.service.model.MCatalogJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.HKImageUtils;
import com.hk.util.SeoManager;
import com.hk.web.ConvertEncryptedToNormalDouble;
import com.hk.web.HealthkartResponse;
import com.hk.web.filter.WebContext;

/**
 * Created by IntelliJ IDEA. User: Satish Date: Sep 18, 2012 Time: 2:51:59 PM To change this template use File |
 * Settings | File Templates.
 */

@Path("/mCatalog")
@Component
public class MCatalogAction extends MBaseAction {

    private static Logger logger                     = LoggerFactory.getLogger(MCatalogAction.class);

    private String        rootCategorySlug;
    private String        childCategorySlug;
    private String        secondaryChildCategorySlug;
    private String        tertiaryChildCategorySlug;
    private SeoData       seoData;
    String                urlFragment;
    String                topCategoryUrlSlug;
    String                allCategories;
    Category              category;
    String                brand;
    List<String>          brandList;
    private String        redirectUrl;

    String                displayMode;
    String                feed;

    Page                  productPage;
    List<Product>         productList                = new ArrayList<Product>();
    List<Product>         productListNonCityFiltered = new ArrayList<Product>();
    List<String>          productIDList              = new ArrayList<String>();
    List<MenuNode>        menuNodes;

    List<Long>            filterOptions              = new ArrayList<Long>();
    List<ProductOption>   filterProductOptions       = new ArrayList<ProductOption>();
    Double                minPrice;
    Double                maxPrice;

    MenuNode              menuNode;

    String                sortBy;
    String                sortOrder;
    Double                startRange;
    @Validate(converter = ConvertEncryptedToNormalDouble.class)
    Double                endRange;

    @Autowired
    MenuHelper            menuHelper;
    @Autowired
    LocalityMapDao        localityMapDao;
    @Autowired
    MapIndiaDao           mapIndiaDao;
    @Autowired
    ProductDao            productDao;
    @Autowired
    ProductService        productService;
    @Autowired
    ComboDao              comboDao;
    @Autowired
    CategoryService       categoryService;
    @Autowired
    SeoManager            seoManager;
    @Autowired
    ProductSearchService  productSearchService;
    @Autowired
    UserManager           userManager;
    @Autowired
    LinkManager           linkManager;

    @Session(key = HealthkartConstants.Cookie.preferredZone)
    private String        preferredZone;

    String                appBasePath;
    private int           defaultPerPage             = 20;

    @SuppressWarnings("unchecked")
    @GET
    @Path("/catalog/")
    @Produces("application/json")
    public String fetchCatalog(@QueryParam("primaryCategory")
    String primaryCat, @QueryParam("secondaryCategory")
    String secondaryCat, @QueryParam("pageNo")
    int pageNo, @QueryParam("perPage")
    int perPage, @Context
    HttpServletResponse response) throws SolrServerException, MalformedURLException, IOException {

        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        List<MCatalogJSONResponse> catalogList = new ArrayList<MCatalogJSONResponse>();
        MCatalogJSONResponse catalogResponse;
        rootCategorySlug = primaryCat;
        childCategorySlug = secondaryCat;

        // category = categoryService.getCategoryByName(rootCategorySlug);
        category = CategoryCache.getInstance().getCategoryByName(rootCategorySlug).getCategory();
        if (category == null) {
            logger.error("No category found for root category slug : " + rootCategorySlug);
        }

        String smallestCategory = null;
        String secondSmallestCategory = null;
        String thirdSmallestCategory = null;
        if (StringUtils.isNotBlank(tertiaryChildCategorySlug)) {
            smallestCategory = tertiaryChildCategorySlug;
            secondSmallestCategory = secondaryChildCategorySlug;
            thirdSmallestCategory = childCategorySlug;
        } else if (StringUtils.isNotBlank(secondaryChildCategorySlug)) {
            smallestCategory = secondaryChildCategorySlug;
            secondSmallestCategory = childCategorySlug;
        } else if (StringUtils.isNotBlank(childCategorySlug)) {
            smallestCategory = childCategorySlug;
            secondSmallestCategory = rootCategorySlug;
        } else {
            smallestCategory = rootCategorySlug;
        }

        boolean includeCombo = false;
        boolean onlyCOD = true;
        try {

            if (!filterOptions.isEmpty() || (minPrice != null && maxPrice != null)) {
                if (!filterOptions.isEmpty()) {
                    filterProductOptions = getBaseDao().getAll(ProductOption.class, filterOptions, "id");
                }
                logger.error("Using filters. SOLR can't return results so hitting DB");
                throw new Exception("Using filters. SOLR can't return results so hitting DB");
            }
            List<SearchFilter> categoryList = new ArrayList<SearchFilter>();
            SearchFilter searchFilter = new SearchFilter(SolrSchemaConstants.category, rootCategorySlug);
            categoryList.add(searchFilter);
            searchFilter = new SearchFilter(SolrSchemaConstants.category, smallestCategory);
            categoryList.add(searchFilter);
            searchFilter = new SearchFilter(SolrSchemaConstants.category, secondSmallestCategory);
            categoryList.add(searchFilter);
            searchFilter = new SearchFilter(SolrSchemaConstants.category, thirdSmallestCategory);
            categoryList.add(searchFilter);

            SortFilter sortFilter = new SortFilter(getCustomSortBy(), getCustomSortOrder());
            PaginationFilter paginationFilter = new PaginationFilter(pageNo, perPage);
            RangeFilter rangeFilter = new RangeFilter(SolrSchemaConstants.hkPrice, getCustomStartRange(), getCustomEndRange());

            List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
            if (StringUtils.isNotBlank(brand)) {
                SearchFilter brandFilter = new SearchFilter(SolrSchemaConstants.brand, brand);
                searchFilters.add(brandFilter);
            }
            if (!includeCombo) {
                SearchFilter comboFilter = new SearchFilter(SolrSchemaConstants.isCombo, false);
                searchFilters.add(comboFilter);
            }
            if (onlyCOD) {
                SearchFilter codFilter = new SearchFilter(SolrSchemaConstants.isCODAllowed, true);
                searchFilters.add(codFilter);
            }

            SearchResult searchResult = productSearchService.getCatalogResults(categoryList, searchFilters, rangeFilter, paginationFilter, sortFilter);

            List<Product> filteredProducts = searchResult.getSolrProducts();
            if (rootCategorySlug.equals(MHKConstants.SERVICES)) {
                productList = trimListByDistance(filteredProducts, preferredZone);
            }
            // Find out how many products have been filtered
            //int diff = 0;
            long totalResultSize = searchResult.getResultSize();
            // totalResultSize = filteredProducts.size();

            productPage = new Page(filteredProducts, perPage, pageNo, (int) totalResultSize);
            if (productPage != null) {
                productList = productPage.getList();
            }
            // category = categoryService.getCategoryByName(smallestCategory);
            category = CategoryCache.getInstance().getCategoryByName(smallestCategory).getCategory();
        } catch (Exception e) {
            // urlFragment =
            // getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
            // logger.error("SOLR NOT WORKING, HITTING DB TO ACCESS DATA for "+urlFragment, e);
            logger.error("SOLR NOT WORKING, HITTING DB TO ACCESS DATA for " + urlFragment);
            List<String> categoryNames = new ArrayList<String>();
            // Category primaryCategory =
            // categoryService.getCategoryByName(Category.getNameFromDisplayName(smallestCategory));
            Category primaryCategory = CategoryCache.getInstance().getCategoryByName(Category.getNameFromDisplayName(smallestCategory)).getCategory();
            category = primaryCategory;
            if (primaryCategory != null) {
                categoryNames.add(primaryCategory.getName());
            }

            Category secondaryCategory = null;
            if (secondSmallestCategory != null) {
                // secondaryCategory =
                // categoryService.getCategoryByName(Category.getNameFromDisplayName(secondSmallestCategory));
                secondaryCategory = CategoryCache.getInstance().getCategoryByName(Category.getNameFromDisplayName(secondSmallestCategory)).getCategory();
                if (secondaryCategory != null) {
                    categoryNames.add(secondaryCategory.getName());
                }
            }

            Category tertiaryCategory = null;
            if (thirdSmallestCategory != null) {
                // tertiaryCategory =
                // categoryService.getCategoryByName(Category.getNameFromDisplayName(thirdSmallestCategory));
                tertiaryCategory = CategoryCache.getInstance().getCategoryByName(Category.getNameFromDisplayName(thirdSmallestCategory)).getCategory();
                if (tertiaryCategory != null) {
                    categoryNames.add(tertiaryCategory.getName());
                }
            }

            /*
             * if (categoryNames.size() == 0) { return new RedirectResolution(HomeAction.class); }
             */
            if (!filterOptions.isEmpty() || (minPrice != null && maxPrice != null)) {
                int groupsCount = 0;
                if (!filterOptions.isEmpty()) {
                    Map<String, List<Long>> groupedFilters = productService.getGroupedFilters(filterOptions);
                    groupsCount = groupedFilters.size();
                }
                productPage = productDao.getProductByCategoryBrandAndOptions(categoryNames, brand, filterOptions, groupsCount, minPrice, maxPrice, onlyCOD, includeCombo, pageNo,
                        perPage);
                if (productPage != null) {
                    productList = productPage.getList();
                    for (Product product : productList) {
                        product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.mobile_catalog.getId()));
                    }
                }
                trimListByCategory(productList, secondaryCategory);

            } else {
                if (StringUtils.isBlank(brand)) {
                    productPage = productDao.getProductByCategoryAndBrand(categoryNames, null, onlyCOD, includeCombo, pageNo, perPage);
                } else {
                    productPage = productDao.getProductByCategoryAndBrand(categoryNames, brand, onlyCOD, includeCombo, pageNo, perPage);
                }
                if (productPage != null) {
                    productList = productPage.getList();
                    for (Product product : productList) {
                        product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.mobile_catalog.getId()));
                    }
                }
                trimListByCategory(productList, secondaryCategory);
                if (rootCategorySlug.equals(MHKConstants.SERVICES)) {
                    productList = trimListByDistance(productList, preferredZone);
                }
            }

        }

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        for (Product product : productList) {
            catalogResponse = new MCatalogJSONResponse();
            catalogResponse = populateCatalogResponse(product, catalogResponse);
            catalogResponse.setCurrentCategory(secondaryCat);
            product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.mobile_catalog.getId()));
            catalogResponse.setProductURL(product.getProductURL());

            catalogList.add(catalogResponse);

        }
        resultMap.put("data", catalogList);
        if (productList.size() < perPage) {
            resultMap.put("hasMore", new Boolean(false));

        } else {
            resultMap.put("hasMore", new Boolean(true));
        }

        // urlFragment =
        // getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
        /*
         * menuNodes = menuHelper.getSiblings(urlFragment); if (menuNodes == null) { logger.debug(urlFragment);
         * urlFragment = urlFragment.replace("/" + smallestCategory, ""); menuNodes =
         * menuHelper.getSiblings(urlFragment); WebContext.getResponse().setStatus(301); //redirection //argumentative,
         * need to discuss, it will work without setting header as well but the url shown in browser will be wrong,
         * though output same, hence page is loaded twice //WebContext.getResponse().setHeader("Location",
         * getContext().getRequest().getContextPath() + "/" + rootCategorySlug + "/" + secondSmallestCategory); }
         */

        /*
         * if (jspFile.exists() && menuNodes == null) { //WebContext.getResponse().setHeader("Location",
         * getContext().getRequest().getContextPath() + "/" + rootCategorySlug); return new
         * ForwardResolution(jspRelativePath); }
         */
        /*
         * menuNode = menuHelper.getMenuNode(urlFragment); topCategoryUrlSlug = menuHelper.getTopCategorySlug(menuNode);
         * allCategories = menuHelper.getAllCategoriesString(menuNode); brandList =
         * categoryDao.getBrandsByCategory(menuHelper.getAllCategoriesList(menuNode));
         */
        /*
         * if (StringUtils.isNotBlank(brand)) { String keyForBrandInCat =
         * urlFragment.concat(SeoManager.KEY_BRAND_IN_CAT).concat(brand); seoData =
         * seoManager.generateSeo(keyForBrandInCat); } else { seoData = seoManager.generateSeo(urlFragment); }
         */

        /*
         * if (StringUtils.isNotBlank(feed)) { if (feed.equals("xml")) { return new
         * ForwardResolution("/pages/category/catalogFeedXml.jsp"); } }
         */

        // return new ForwardResolution("/pages/category/service.jsp");
        addHeaderAttributes(response);
        healthkartResponse = new HealthkartResponse(status, message, resultMap);
        jsonBuilder = com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(healthkartResponse);
        return jsonBuilder;

    }

    private MCatalogJSONResponse populateCatalogResponse(Product product, MCatalogJSONResponse catalogJSONResponse) {
        if (null != product.getProductURL())
            catalogJSONResponse.setProductURL(product.getProductURL());
        if (null != product.getSlug())
            catalogJSONResponse.setProductSlug(product.getSlug());
        if (null != product.getId()) {
            if (null != product.getMainImageId())
                catalogJSONResponse.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, product.getMainImageId(), false));
            else
                catalogJSONResponse.setImageUrl(getImageUrl() + product.getId() + MHKConstants.IMAGETYPE);
        }
        if (null != product.getManufacturer())
            catalogJSONResponse.setManufacturer(product.getManufacturer().getName());
        if (null != product.getBrand())
            catalogJSONResponse.setBrand(product.getBrand());
        if (null != product.isCodAllowed())
            catalogJSONResponse.setCodAllowed(product.isCodAllowed());
        if (null != product.getDeleted())
            catalogJSONResponse.setDeleted(product.getDeleted());
        if (null != product.getDescription())
            catalogJSONResponse.setDescription(product.getDescription());
        catalogJSONResponse.setDropShipping(product.getDropShipping());
        if (null != product.getGoogleAdDisallowed())
            catalogJSONResponse.setGoogleAdDisallowed(product.getGoogleAdDisallowed());
        if (null != product.getId())
            catalogJSONResponse.setId(product.getId());
        if (null != product.getJit())
            catalogJSONResponse.setJit(product.getJit());
        if (null != product.getMaxDays())
            catalogJSONResponse.setMaxDays(product.getMaxDays());
        if (null != product.getMinDays())
            catalogJSONResponse.setMinDays(product.getMinDays());
        if (null != product.getName())
            catalogJSONResponse.setName(product.getName());
        if (null != product.getOrderRanking())
            catalogJSONResponse.setOrderRanking(product.getOrderRanking());
        if (null != product.isOutOfStock())
            catalogJSONResponse.setOutOfStock(product.isOutOfStock());
        if (null != product.getOverview())
            catalogJSONResponse.setOverview(product.getOverview());
        if (null != product.getProductHaveColorOptions())
            catalogJSONResponse.setProductHaveColorOptions(product.getProductHaveColorOptions());
        if (null != product.getService())
            catalogJSONResponse.setService(product.getService());
        if (null != product.getThumbUrl())
            catalogJSONResponse.setThumbUrl(product.getThumbUrl());
        if (null != product.getMinimumMRPProducVariant().getHkPrice())
            catalogJSONResponse.setHkPrice(priceFormat.format(product.getMinimumMRPProducVariant().getHkPrice()));
        if (null != product.getMinimumMRPProducVariant().getMarkedPrice())
            catalogJSONResponse.setMarkedPrice(priceFormat.format(product.getMinimumMRPProducVariant().getMarkedPrice()));
        if (null != product.getMinimumMRPProducVariant().getDiscountPercent())
            catalogJSONResponse.setDiscountPercentage(Double.valueOf(decimalFormat.format(product.getMinimumMRPProducVariant().getDiscountPercent() * 100)));
        catalogJSONResponse.setProductReferrerId(EnumProductReferrer.mobile_catalog.getId());
        return catalogJSONResponse;
    }

    public int getPageDefault() {
        return 1;
    }

    /*
     * public int getPageNo() { return pageNo <= 0 ? 1 : pageNo; } public void setPageNo(int pageNo) { this.pageNo =
     * pageNo; } public int getPerPage() { return perPage <= 0 ? getPerPageDefault() : perPage; } public void
     * setPerPage(int perPage) { this.perPage = perPage; }
     */

    private List<Product> trimListByDistance(List<Product> productListNonCityFiltered, String preferredZone) throws IOException {
        if (preferredZone == null) {
            preferredZone = "Delhi"; // hardcoded default results
        } else if (preferredZone.equals("All")) {// returning the whole list if preferred zone is blank
            return productListNonCityFiltered;
        }
        MapIndia mi = mapIndiaDao.findByCity(preferredZone);
        List<Product> cityFiltered = new ArrayList<Product>();
        for (Iterator<Product> productIterator = productListNonCityFiltered.iterator(); productIterator.hasNext();) {
            Product product = productIterator.next();
            if (product.isService()) {
                Manufacturer manufacturer = product.getManufacturer();
                if (manufacturer != null) {
                    List<Address> manufacturerAddresses = manufacturer.getAddresses();
                    if (manufacturerAddresses != null && manufacturerAddresses.size() > 0) {
                        for (Address serviceAddress : manufacturerAddresses) {
                            LocalityMap lm = localityMapDao.findByAddress(serviceAddress);
                            if (lm != null && mi != null) {
                                if (localityMapDao.getDistanceInMeters(lm.getLattitude(), lm.getLongitude(), mi.getLattitude(), mi.getLongitude()) < 100) {
                                    cityFiltered.add(product);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (manufacturer != null && manufacturer.isAvailableAllOverIndia() != null && manufacturer.isAvailableAllOverIndia()) {
                    if (!cityFiltered.contains(product))
                        cityFiltered.add(product);
                }
            } else {
                cityFiltered.add(product);
            }
        }
        return cityFiltered;
    }

    public Resolution setCookie() {
        Cookie preferredZoneCookie = new Cookie(HealthkartConstants.Cookie.preferredZone, CryptoUtil.encrypt(preferredZone));
        preferredZoneCookie.setPath("/");
        preferredZoneCookie.setMaxAge(365 * 24 * 3600);
        WebContext.getResponse().addCookie(preferredZoneCookie);
        WebContext.getResponse().setContentType("text/html");
        return new RedirectResolution(redirectUrl);
    }

    private void trimListByCategory(List<Product> productList, Category category) {
        if (category != null) {
            for (Iterator<Product> productIterator = productList.iterator(); productIterator.hasNext();) {
                Product product = productIterator.next();
                if (!product.getCategories().contains(category)) {
                    productIterator.remove();
                }
            }
        }
    }

    public void setRootCategorySlug(String rootCategorySlug) {
        this.rootCategorySlug = rootCategorySlug;
    }

    public void setChildCategorySlug(String childCategorySlug) {
        this.childCategorySlug = childCategorySlug;
    }

    public void setSecondaryChildCategorySlug(String secondaryChildCategorySlug) {
        this.secondaryChildCategorySlug = secondaryChildCategorySlug;
    }

    public String getRootCategorySlug() {
        return rootCategorySlug;
    }

    public String getChildCategorySlug() {
        return childCategorySlug;
    }

    public String getSecondaryChildCategorySlug() {
        return secondaryChildCategorySlug;
    }

    public List<MenuNode> getMenuNodes() {
        return menuNodes;
    }

    public MenuNode getMenuNode() {
        return menuNode;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public String getUrlFragment() {
        return urlFragment;
    }

    public void setUrlFragment(String urlFragment) {
        this.urlFragment = urlFragment;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTopCategoryUrlSlug() {
        return topCategoryUrlSlug;
    }

    public String getAllCategories() {
        return allCategories;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return productPage == null ? 0 : productPage.getTotalPages();
    }

    public int getResultCount() {
        return productPage == null ? 0 : productPage.getTotalResults();
    }

    public SeoData getSeoData() {
        return seoData;
    }

    public void setSeoData(SeoData seoData) {
        this.seoData = seoData;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public List<String> getProductIDList() {
        return productIDList;
    }

    public void setProductIDList(List<String> productIDList) {
        this.productIDList = productIDList;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("rootCategorySlug");
        params.add("childCategorySlug");
        params.add("secondaryChildCategorySlug");
        params.add("tertiaryChildCategorySlug");
        params.add("brand");
        params.add("sortBy");
        params.add("sortOrder");
        params.add("startRange");
        params.add("endRange");
        params.add("maxPrice");
        params.add("minPrice");
        if (filterOptions != null && !filterOptions.isEmpty()) {
            for (int i = 0; i < filterOptions.size(); i++) {
                params.add("filterOptions[" + i + "]");
            }

        }
        if (getTopCategoryUrlSlug().equals("services")) {
            params.add("preferredZone");
        }
        return params;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getCustomSortBy() {
        return StringUtils.isBlank(sortBy) ? "ranking" : sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getCustomSortOrder() {
        return StringUtils.isBlank(sortOrder) ? "asc" : sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Double getCustomStartRange() {
        return startRange != null ? startRange : 0.0;
    }

    public Double getStartRange() {
        return startRange;
    }

    public void setStartRange(Double startRange) {
        this.startRange = startRange;
    }

    public Double getEndRange() {
        return endRange;
    }

    public Double getCustomEndRange() {
        return endRange != null ? endRange : 1000000;
    }

    public void setEndRange(Double endRange) {
        this.endRange = endRange;
    }

    public String getPreferredZone() {
        return getTopCategoryUrlSlug().equals("services") ? preferredZone : null;
    }

    public void setPreferredZone(String preferredZone) {
        this.preferredZone = preferredZone;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getTertiaryChildCategorySlug() {
        return tertiaryChildCategorySlug;
    }

    public void setTertiaryChildCategorySlug(String tertiaryChildCategorySlug) {
        this.tertiaryChildCategorySlug = tertiaryChildCategorySlug;
    }

    public List<String> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<String> brandList) {
        this.brandList = brandList;
    }

    public List<Long> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(List<Long> filterOptions) {
        this.filterOptions = filterOptions;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<ProductOption> getFilterProductOptions() {
        return filterProductOptions;
    }
}