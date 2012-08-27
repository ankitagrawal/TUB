package com.hk.web.action.core.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;

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
import com.hk.domain.catalog.product.Product;
import com.hk.manager.LinkManager;
import com.hk.manager.SolrManager;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.util.ProductReferrerMapper;

@UrlBinding("/search")
@Component
public class SearchAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(SearchAction.class);

  String query;

  Page productPage;
  List<Product> productList = new ArrayList<Product>();
  @Autowired
   ProductDao productDao;
  @Session(key = HealthkartConstants.Session.perPageCatalog) 
  private int perPage;
  @Autowired
  SolrManager solrManager;
  @Autowired
  LinkManager linkManager;

  private int defaultPerPage = 20;

  public Resolution search() throws SolrServerException, MalformedURLException {
    try {
      productPage = solrManager.getSearchResults(query, getPageNo(), getPerPage());
      productList = productPage.getList();
    } catch (Exception e) {
      logger.debug("SOLR NOT WORKING, HITTING DB TO ACCESS DATA");
      productPage = productDao.getProductByName(query, getPageNo(), getPerPage());
      productList = productPage.getList();
      for(Product product : productList){
        product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.SEARCH_PAGE)));
      }
    }
    if (productList != null && productList.size() == 0) {
      addRedirectAlertMessage(new SimpleMessage("No results found."));
    }
    return new ForwardResolution("/pages/search.jsp");
  }

  public List<Product> getProductList() {
    return productList;
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
    params.add("query");
    return params;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
