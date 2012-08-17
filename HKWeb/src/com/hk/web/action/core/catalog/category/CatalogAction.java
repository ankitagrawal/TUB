package com.hk.web.action.core.catalog.category;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.LocalityMap;
import com.hk.domain.MapIndia;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.content.SeoData;
import com.hk.domain.user.Address;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.impl.dao.catalog.category.CategoryDaoImpl;
import com.hk.impl.dao.catalog.category.CategoryImageDaoImpl;
import com.hk.manager.LinkManager;
import com.hk.manager.SolrManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.ProductReferrerMapper;
import com.hk.util.SeoManager;
import com.hk.web.AppConstants;
import com.hk.web.ConvertEncryptedToNormalDouble;
import com.hk.web.action.HomeAction;
import com.hk.web.filter.WebContext;

@UrlBinding ("/{rootCategorySlug}/{childCategorySlug}/{secondaryChildCategorySlug}/{tertiaryChildCategorySlug}")
public class CatalogAction extends BasePaginatedAction {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CatalogAction.class);

	private String rootCategorySlug;
	private String childCategorySlug;
	private String secondaryChildCategorySlug;
	private String tertiaryChildCategorySlug;
	private SeoData seoData;

	String urlFragment;
	String topCategoryUrlSlug;
	String allCategories;
	Category category;
	String brand;
	List<String> brandList;
	private String redirectUrl;

	String displayMode;

	String feed;

	Page productPage;
	List<Product> productList = new ArrayList<Product>();
	List<Product> productListNonCityFiltered = new ArrayList<Product>();
	List<String> productIDList = new ArrayList<String>();
	List<MenuNode> menuNodes;

	List<Long> filterOptions = new ArrayList<Long>();
	Double minPrice;
	Double maxPrice;

	MenuNode menuNode;

	String sortBy;
	String sortOrder;
	Double startRange;
	@Validate (converter = ConvertEncryptedToNormalDouble.class)
	Double endRange;

	@Autowired
	MenuHelper menuHelper;
	@Autowired
	LocalityMapDao localityMapDao;
	@Autowired
	MapIndiaDao mapIndiaDao;
	@Autowired
	CategoryImageDaoImpl categoryImageDao;
	@Autowired
	ProductDao productDao;
	@Autowired
	ProductService productService;
	@Autowired
	ComboDao comboDao;
	//@Autowired
	//@Named(Keys.App.appBasePath)
	String appBasePath;
	@Autowired
	CategoryDaoImpl categoryDao;
	@Autowired
	SeoManager seoManager;
	@Autowired
	SolrManager solrManager;
	@Autowired
	UserDao userDao;
	@Autowired
	UserManager userManager;
	@Autowired
	LinkManager linkManager;

	@Session (key = HealthkartConstants.Cookie.preferredZone)
	private String preferredZone;

	@Session (key = HealthkartConstants.Session.perPageCatalog)
	private int perPage;

	private int defaultPerPage = 20;

	@DefaultHandler
	public Resolution pre() throws IOException, SolrServerException {
		category = categoryDao.getCategoryByName(rootCategorySlug);
		String jspRelativePath = "/pages/category/category.jsp";

		File jspFile = new File(AppConstants.appBasePath + jspRelativePath);
		if (category != null) {
			if (jspFile.exists() && StringUtils.isBlank(childCategorySlug) && StringUtils.isBlank(secondaryChildCategorySlug)) {
				return new ForwardResolution(CategoryAction.class, "pre").addParameter("category", category.getName());
			}
		} else {
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

		try {
			if(!filterOptions.isEmpty()){
				throw new Exception("Using filters. SOLR can't return results so hitting DB");
			}
			productPage = solrManager.getCatalogResults(rootCategorySlug, smallestCategory, secondSmallestCategory, thirdSmallestCategory, brand, getCustomSortBy(), getCustomSortOrder(), getCustomStartRange(), getCustomEndRange(), getPageNo(), getPerPage(), preferredZone);
			if (productPage != null) {
				productList = productPage.getList();
			}
			category = categoryDao.getCategoryByName(smallestCategory);
		} catch (Exception e) {
			logger.info("SOLR NOT WORKING, HITTING DB TO ACCESS DATA");
			urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
			logger.info(urlFragment);
			List<String> categoryNames = new ArrayList<String>();
			Category primaryCategory = categoryDao.getCategoryByName(smallestCategory);
			category = primaryCategory;
			if (primaryCategory != null) {
				categoryNames.add(primaryCategory.getName());
			}

			Category secondaryCategory = null;
			if (secondSmallestCategory != null) {
				secondaryCategory = categoryDao.getCategoryByName(secondSmallestCategory);
				if (secondaryCategory != null) {
					categoryNames.add(secondaryCategory.getName());
				}
			}

			Category tertiaryCategory = null;
			if (thirdSmallestCategory != null) {
				tertiaryCategory = categoryDao.getCategoryByName(thirdSmallestCategory);
				if (tertiaryCategory != null) {
					categoryNames.add(tertiaryCategory.getName());
				}
			}

			if (categoryNames.size() == 0) {
				return new RedirectResolution(HomeAction.class);
			}

			if (!filterOptions.isEmpty() || (minPrice != null && maxPrice != null)) {
				int groupsCount = 0;
				if(!filterOptions.isEmpty()){
					Map<String, List<Long>> groupedFilters = productService.getGroupedFilters(filterOptions);
					groupsCount = groupedFilters.size();
				}
				productPage = productDao.getProductByCategoryBrandAndOptions(categoryNames, brand, filterOptions, groupsCount, minPrice, maxPrice, getPageNo(), getPerPage());
				if (productPage != null) {
					productList = productPage.getList();
					for (Product product : productList) {
						product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(rootCategorySlug)));
					}
				}
				trimListByCategory(productList, secondaryCategory);
			} else {
				if (StringUtils.isBlank(brand)) {
					productPage = productDao.getProductByCategoryAndBrand(categoryNames, null, getPageNo(), getPerPage());
					if (productPage != null) {
						productList = productPage.getList();
						for (Product product : productList) {
							product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(rootCategorySlug)));
						}
					}
					trimListByCategory(productList, secondaryCategory);
					if (rootCategorySlug.equals("services")) {
						productList = trimListByDistance(productList, preferredZone);
					}
				} else {
					productPage = productDao.getProductByCategoryAndBrand(categoryNames, brand, getPageNo(), getPerPage());
					if (productPage != null) {
						productList = productPage.getList();
						for (Product product : productList) {
							product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(rootCategorySlug)));
						}
					}
					trimListByCategory(productList, secondaryCategory);
					if (rootCategorySlug.equals("services")) {
						productList = trimListByDistance(productList, preferredZone);
					}
				}
			}

		}
		urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
		menuNodes = menuHelper.getSiblings(urlFragment);
		if (menuNodes == null) {
			logger.debug(urlFragment);
			urlFragment = urlFragment.replace("/" + smallestCategory, "");
			menuNodes = menuHelper.getSiblings(urlFragment);
			WebContext.getResponse().setStatus(301); //redirection
			//argumentative, need to discuss, it will work without setting header as well but the url shown in browser will be wrong, though output same, hence page is loaded twice
			WebContext.getResponse().setHeader("Location", getContext().getRequest().getContextPath() + "/" + rootCategorySlug + "/" + secondSmallestCategory);
		}

		if (jspFile.exists() && menuNodes == null) {
			WebContext.getResponse().setHeader("Location", getContext().getRequest().getContextPath() + "/" + rootCategorySlug);
			return new ForwardResolution(jspRelativePath);
		}
		menuNode = menuHelper.getMenuNode(urlFragment);
		topCategoryUrlSlug = menuHelper.getTopCategorySlug(menuNode);
		allCategories = menuHelper.getAllCategoriesString(menuNode);
		brandList = categoryDao.getBrandsByCategory(menuHelper.getAllCategoriesList(menuNode));

		if (StringUtils.isNotBlank(brand)) {
			String keyForBrandInCat = urlFragment.concat(SeoManager.KEY_BRAND_IN_CAT).concat(brand);
			seoData = seoManager.generateSeo(keyForBrandInCat);
		} else {
			seoData = seoManager.generateSeo(urlFragment);
		}

		if (StringUtils.isNotBlank(feed)) {
			if (feed.equals("xml")) {
				return new ForwardResolution("/pages/category/catalogFeedXml.jsp");
			}
		}

		return new ForwardResolution("/pages/category/catalog.jsp");
	}

	private List<Product> trimListByDistance(List<Product> productListNonCityFiltered, String preferredZone) throws IOException {
		if (preferredZone == null) {
			preferredZone = "Delhi"; //hardcoded default results
		} else if (preferredZone.equals("All")) {//returning the whole list if preferred zone is blank
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
					if (!cityFiltered.contains(product)) cityFiltered.add(product);
				}
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

	public int getPerPage() {
		return perPage <= 0 ? getPerPageDefault() : perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
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
}

