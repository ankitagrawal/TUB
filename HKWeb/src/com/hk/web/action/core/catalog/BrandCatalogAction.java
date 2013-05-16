package com.hk.web.action.core.catalog;

import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.*;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.marketing.ProductReferrerConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;
import com.hk.dto.menu.MenuNode;
import com.hk.dto.search.SearchResult;
import com.hk.helper.MenuHelper;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.util.ProductReferrerMapper;
import com.hk.util.SeoManager;

@UrlBinding("/brand/{topLevelCategory}/{brand}")
@Component
public class BrandCatalogAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(BrandCatalogAction.class);
  @Autowired
   ProductDao productDao;
  @Autowired
  LinkManager linkManager;

  String urlFragment;
  private String brand;
  private String topLevelCategory;
  Page productPage;
  List<Product> productList = new ArrayList<Product>();
  Set<MenuNode> menuNodes = new HashSet<MenuNode>();
  MenuNode menuNode;

  @Session(key = HealthkartConstants.Session.perPageCatalog)
  private int perPage;

  @Session(key = HealthkartConstants.Cookie.preferredZone)
  private String preferredZone;

  private int defaultPerPage = 24;
  /*@Autowired
   SolrManager productSearchService;*/

  @Autowired
  ProductSearchService productSearchService;

  @Autowired
  CategoryDao categoryDao;
  @Autowired
   MenuHelper menuHelper;
  @Autowired
   UserDao userDao;
  @Autowired
   UserManager userManager;
  @Autowired
   SeoManager seoManager;
  List<Category> categories;

  SeoData seoData;

  @SuppressWarnings({ "deprecation", "unchecked" })
@DefaultHandler
  public Resolution pre() throws MalformedURLException, SolrServerException {
	  List<String> categoryNames = new ArrayList<String>();
	  categoryNames.add(topLevelCategory);
	  urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
      Boolean includeCombo = true;
      Boolean onlyCOD = false;
	  if (StringUtils.isBlank(brand)) {
		  return new RedirectResolution("/" + topLevelCategory);
	  } else {
		  try {
              if (getContext().getRequest().getParameterMap().containsKey("includeCombo")){
                  String[] params = (String[])getContext().getRequest().getParameterMap().get("includeCombo");
                  includeCombo = Boolean.parseBoolean( params[0].toString());
              }

              if (getContext().getRequest().getParameterMap().containsKey("onlyCOD")){
                  String[] params = (String[])getContext().getRequest().getParameterMap().get("onlyCOD");
                  onlyCOD = Boolean.parseBoolean( params[0].toString());
              }
			  SearchResult searchResult = productSearchService.getBrandCatalogResults(URLDecoder.decode(brand), categoryDao.getCategoryByName(topLevelCategory), getPageNo(), getPerPage(), preferredZone, false);
			  productPage = new Page(searchResult.getSolrProducts(), getPerPage(), getPageNo(), searchResult.getResultSize());
		  } catch (Exception e) {
			  logger.debug("SOLR NOT WORKING, HITTING DB TO ACCESS DATA");
			  categories = categoryDao.getCategoriesByBrand(brand, topLevelCategory);
			  productPage = productDao.getProductByCategoryAndBrand(categoryNames, URLDecoder.decode(brand),onlyCOD, includeCombo, getPageNo(), getPerPage());
		  }
		  if (productPage != null) {
			  productList = productPage.getList();
			  if (productList != null) {
				  for (Product product : productList) {
					  product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.BRAND_PAGE)));
					  menuNode = menuHelper.getMenoNodeFromProduct(product);
					  menuNodes.add(menuNode);
				  }
			  }
		  }
      if (topLevelCategory != null && categoryDao.getCategoryByName(topLevelCategory) != null)
        seoData = seoManager.generateSeo(brand + "||" + topLevelCategory);
      else
        seoData = seoManager.generateSeo(brand);
	  }
	  return new ForwardResolution("/pages/brand-catalog.jsp");
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getTopLevelCategory() {
    return topLevelCategory;
  }

  public void setTopLevelCategory(String topLevelCategory) {
    this.topLevelCategory = topLevelCategory;
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

  public int getPerPage() {
    return perPage <= 0 ? getPerPageDefault() : perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("brand");
    params.add("topLevelCategory");
    return params;
  }

  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }

  public String getUrlFragment() {
    return urlFragment;
  }

  public void setUrlFragment(String urlFragment) {
    this.urlFragment = urlFragment;
  }

  public Set<MenuNode> getMenuNodes() {
    return menuNodes;
  }

  public void setMenuNodes(Set<MenuNode> menuNodes) {
    this.menuNodes = menuNodes;
  }

  public String getPreferredZone() {
    return preferredZone;
  }

  public void setPreferredZone(String preferredZone) {
    this.preferredZone = preferredZone;
  }

  public SeoData getSeoData() {
    return seoData;
  }

  public void setSeoData(SeoData seoData) {
    this.seoData = seoData;
  }
  
}