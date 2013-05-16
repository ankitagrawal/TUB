package com.hk.web.action.core.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.catalog.SolrSchemaConstants;
import com.hk.domain.search.SearchFilter;
import com.hk.dto.search.SearchResult;
import com.hk.pact.service.search.ProductSearchService;
import net.sourceforge.stripes.action.*;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.commons.lang.StringUtils;
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
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.util.ProductReferrerMapper;
import com.hk.web.action.core.catalog.product.ProductAction;

@UrlBinding("/search")
@Component
public class SearchAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(SearchAction.class);

  String query;
  String searchSuggestion;
  Page productPage;
  List<Product> productList = new ArrayList<Product>();
  @Autowired
   ProductDao productDao;
  @Session(key = HealthkartConstants.Session.perPageCatalog) 
  private int perPage;
  @Autowired
  ProductSearchService productSearchService;
  @Autowired
  LinkManager linkManager;

  private int defaultPerPage = 24;

	public Resolution search() throws SolrServerException, MalformedURLException {
        boolean includeCombo = true;
        boolean onlyCOD = false;
      if (StringUtils.isNotBlank(query)) {
        try {
          query = query.replace("-", " "); // Handling names with -
          List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
          if (getContext().getRequest().getParameterMap().containsKey("includeCombo")) {
            String[] params = (String[]) getContext().getRequest().getParameterMap().get("includeCombo");
            includeCombo = Boolean.parseBoolean(params[0].toString());
            if (!includeCombo) {
              SearchFilter comboFilter = new SearchFilter(SolrSchemaConstants.isCombo, includeCombo);
              searchFilters.add(comboFilter);
            }
          }
          if (getContext().getRequest().getParameterMap().containsKey("onlyCOD")) {
            String[] params = (String[]) getContext().getRequest().getParameterMap().get("onlyCOD");
            onlyCOD = Boolean.parseBoolean(params[0].toString());
            if (onlyCOD) {
              SearchFilter codFilter = new SearchFilter(SolrSchemaConstants.isCODAllowed, onlyCOD);
              searchFilters.add(codFilter);
            }
          }

        SearchResult sr = productSearchService.getSearchResults(query,searchFilters, getPageNo(), getPerPage(), false);
				productPage = new Page(sr.getSolrProducts(), getPerPage(), getPageNo(), (int) sr.getResultSize());
				productList = productPage.getList();
				for (Product product : productList) {
					product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.SEARCH_PAGE)));
				}
				searchSuggestion = sr.getSearchSuggestions();
			} catch (Exception e) {
				logger.debug("SOLR NOT WORKING, HITTING DB TO ACCESS DATA.." + e.getMessage());
				productPage = productDao.getProductByName(query,onlyCOD, includeCombo, getPageNo(), getPerPage());
				productList = productPage.getList();
				for (Product product : productList) {
					product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.SEARCH_PAGE)));
				}
			}
		}
		if (productList != null && productList.size() == 0) {
			addRedirectAlertMessage(new SimpleMessage("No results found."));
		}
    //Logging search results
    String category = "n/a";
    if (productList != null && !productList.isEmpty()) {
      category = productList.get(0).getPrimaryCategory().getName();
      if (!productList.get(0).getPrimaryCategory().equals(productList.get(productList.size() - 1).getPrimaryCategory())) {
        category = "mixed";
      }
    }
    productSearchService.logSearchResult(query, Long.valueOf(productPage != null ? productPage.getTotalResults() : 0), category);
    if (productList != null && !productList.isEmpty() && productList.size() == 1) {
        Product product = productList.get(0);
        return new RedirectResolution(ProductAction.class).addParameter("productId", product.getId()).addParameter("productSlug", product.getSlug());
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

    public String getSearchSuggestion() {
        return searchSuggestion;
    }

    public void setSearchSuggestion(String searchSuggestion) {
        this.searchSuggestion = searchSuggestion;
    }
}
