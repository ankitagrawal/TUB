package com.hk.web.action.core.catalog;

import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.dao.catalog.product.ProductDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.manager.SolrManager;
import com.hk.manager.UserManager;
import com.hk.util.SeoManager;

@UrlBinding("/brand/{topLevelCategory}/{brand}")
@Component
public class BrandCatalogAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(BrandCatalogAction.class);
   ProductDao productDao;

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

  private int defaultPerPage = 20;
   SolrManager solrManager;
   CategoryDao categoryDao;
   MenuHelper menuHelper;
   UserDao userDao;
   UserManager userManager;
   SeoManager seoManager;
  List<Category> categories;

  SeoData seoData;

  @DefaultHandler
  public Resolution pre() throws MalformedURLException, SolrServerException {
    List<String> categoryNames = new ArrayList<String>();
    categoryNames.add(topLevelCategory);
    urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
    if (StringUtils.isBlank(brand)) {
      return new RedirectResolution("/" + topLevelCategory);
    } else {
      try {
        productPage = solrManager.getBrandCatalogResults(URLDecoder.decode(brand), topLevelCategory, getPageNo(), getPerPage(), preferredZone);
      } catch (Exception e) {
        logger.debug("SOLR NOT WORKING, HITTING DB TO ACCESS DATA");
        categories = categoryDao.getCategoriesByBrand(brand, topLevelCategory);
        productPage = productDao.getProductByCategoryAndBrand(categoryNames, URLDecoder.decode(brand), getPageNo(), getPerPage());
      }
      productList = productPage.getList();
      seoData = seoManager.generateSeo(brand);
    }

    for (Product product : productList) {
      menuNode = menuHelper.getMenoNodeFromProduct(product);
      menuNodes.add(menuNode);
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