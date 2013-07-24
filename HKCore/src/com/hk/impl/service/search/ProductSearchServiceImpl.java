package com.hk.impl.service.search;

import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.SolrSchemaConstants;
import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.search.*;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.dto.search.SearchResult;
import com.hk.exception.SearchException;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.analytics.SearchLogDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.search.ProductIndexService;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.web.filter.WebContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class ProductSearchServiceImpl implements ProductSearchService {
    private static Logger logger        = LoggerFactory.getLogger(ProductSearchServiceImpl.class);

    /*
     * @Autowired CategoryService categoryService;
     */

    @Autowired
    ProductService        productService;

    @Autowired
    CommonsHttpSolrServer solr;

    @Autowired
    ProductIndexService   productIndexService;

    @Autowired
    LinkManager           linkManager;

    @Autowired
    SearchLogDao searchLogDao;

    private final String  SEARCH_SERVER = "SOLR";

    public ProductSearchServiceImpl() {
    }

    public void refreshDataIndexes() throws SearchException {
        try {
            List<Product> productList = productService.getAllNonDeletedProducts();
            // SolrQuery solrQuery = new SolrQuery();
            // clear Solr Index

            List<SolrProduct> products = new ArrayList<SolrProduct>();
            for (Product pr : productList) {
                if (!pr.getDeleted() && !pr.isHidden() && !pr.isGoogleAdDisallowed()) {
                  try {
                    SolrProduct solrProduct = productService.createSolrProduct(pr);
                    productIndexService.updateExtraProperties(pr, solrProduct);
                    products.add(solrProduct);
                  } catch (Exception e) {
                    logger.error("exception while adding solr product:" + pr.getId() , e.getMessage());
                  }
                }
            }
	          solr.deleteByQuery("*:*");
            productIndexService.indexProduct(products);
            buildDictionary();
        } catch (SolrException ex) {
            SearchException e = wrapException("Unable to build indexes. Problem with Solr", ex);
            throw e;
        } catch (Exception ex) {
            SearchException e = wrapException("Unable to build indexes. Problem with Solr", ex);
            throw e;
        }
    }

    public SearchResult getCatalogResults(List<SearchFilter> categories, List<SearchFilter> searchFilters, RangeFilter rangeFilter, PaginationFilter paginationFilter,
            SortFilter sortFilter) throws SearchException {

        SearchResult resultsMap;
        try {
            resultsMap = getCatalogSearchResults(categories, searchFilters, rangeFilter, paginationFilter, sortFilter);
        } catch (SolrServerException ex) {
            SearchException e = wrapException("Unable to get catalog resutls", ex);
            throw e;
        }
        return resultsMap;// new Page(resultsMap.getSolrProducts(), perPage, page, (int)
        // resultsMap.getResultSize().longValue());
    }

    public SearchResult getBrandCatalogResults(String brand, Category topLevelCategory, int page, int perPage, String preferredZone, boolean showGoogleBannedProducts) throws SearchException {
        SolrQuery query = new SolrQuery("*:*");
        //query.addFilterQuery("{!field f= brand}" + brand);
        //query.addFilterQuery(SolrSchemaConstants.brand + ":" + brand);
        query.addFilterQuery(SolrSchemaConstants.brand + ":\"" + brand+"\"");
        if(topLevelCategory != null)
          query.addFilterQuery(SolrSchemaConstants.category + ":" + topLevelCategory.getName());
        if(!showGoogleBannedProducts)
          query.addFilterQuery(SolrSchemaConstants.isGoogleAdDisallowed + ":" + 0);
        query.addFilterQuery(SolrSchemaConstants.isHidden + ":" + 0);
        query.addFilterQuery(SolrSchemaConstants.isDeleted + ":" + 0);

        query.set("fl", "*");
        query.setStart((page - 1) * perPage);
        query.setRows(perPage);
        query.addSortField(SolrSchemaConstants.sortByOutOfStock, SolrQuery.ORDER.asc);
        query.addSortField(SolrSchemaConstants.sortBy, SolrQuery.ORDER.asc);

        List<SolrProduct> solrProductList = new ArrayList<SolrProduct>();
        long resultCount = 0;
        SearchResult searchResult = null;
        try {
            QueryResponse response = solr.query(query);
            // SolrDocumentList documents = response.getResults();
            resultCount = response.getResults().getNumFound();
            solrProductList.addAll(response.getBeans(SolrProduct.class));
            searchResult = getSearchResult(solrProductList, (int) resultCount);
        } catch (SolrServerException ex) {
            SearchException e = wrapException("Unable to get brand catalog results", ex);
            throw e;
        }
        return searchResult;
    }

  public boolean isBrandTerm(String term) throws SearchException {
        SolrQuery query = new SolrQuery("*:*");
        //query.addFilterQuery("{!field f= brand}" + term);
        query.addFilterQuery(SolrSchemaConstants.brand + ":\"" + term+"\"");
    
        try {
            QueryResponse response = solr.query(query);
            long resultCount = response.getResults().getNumFound();
            logger.debug("resultCount@isBrandTerm="+resultCount+" for term="+term);
            if(resultCount > 0)
              return true;
            else{
              SpellCheckResponse spellCheckResponse = this.getSpellCheckResponse(term);
              for (SpellCheckResponse.Suggestion suggestion : spellCheckResponse.getSuggestions()) {
                term = suggestion.getToken();
                query.addFilterQuery(SolrSchemaConstants.brand + ":\"" + term +"\"");
                response = solr.query(query);
                resultCount = response.getResults().getNumFound();
                logger.debug("resultCount@isBrandTerm=" + resultCount + " for term=" + term);
                if (resultCount > 0)
                  return true;
              }
            }
        } catch (SolrServerException ex) {
             logger.error("Unable to get brand term results for brand term->"+term);
            //SearchException e = wrapException("Unable to get brand term results", ex);
            //throw e;
        }
        return false;
    }



    private void buildDictionary() throws SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", "*:*");
        params.set("spellcheck.build", true); // get spelling suggestions
        // String query = params.toString();
        solr.query(params);
    }

    private SearchResult getCatalogSearchResults(List<SearchFilter> categories, List<SearchFilter> searchFilters, RangeFilter rangeFilter, PaginationFilter paginationFilter,
            SortFilter sortFilter) throws SolrServerException {

        SolrQuery solrQuery = new SolrQuery("*:*");

        String query = "*";
        for (SearchFilter searchFilter : searchFilters) {
            if (searchFilter.getValue() != null) {
                String filterQuery = String.format("{!field f= %s}%s", searchFilter.getName(), searchFilter.getValue());
                // solrQuery.addFilterQuery(searchFilter.getName() + ":" + searchFilter.getValue());
                solrQuery.addFilterQuery(filterQuery);
            }
        }
        for (SearchFilter categoryFilter : categories) {
            if (categoryFilter.getValue() != null) {
                // query.addFilterQuery("{!field f= brand}" + brand);
                // Only if it's a valid category
                if (CategoryCache.getInstance().getCategoryByName(categoryFilter.getValue().toString()) != null) {
                    // if (categoryService.getCategoryByName(categoryFilter.getValue().toString()) != null){
                    String categoryQuery = String.format("{!field f= %s}%s", categoryFilter.getName(), categoryFilter.getValue());
                    solrQuery.addFilterQuery(categoryQuery);
                }
            }
        }
        solrQuery.addFilterQuery(SolrSchemaConstants.isDeleted + ":" + 0);
        solrQuery.addFilterQuery(SolrSchemaConstants.isGoogleAdDisallowed + ":" + 0);
        solrQuery.addFilterQuery(SolrSchemaConstants.isHidden + ":" + 0);
        /*
         * solrQuery.addFacetField(rangeFilter.getName()); String rangeQuery= rangeFilter.getName() + ": [" +
         * rangeFilter.getStartRange() + " TO " + rangeFilter.getEndRange() + "]"; solrQuery.addFacetQuery(rangeQuery);
         */
        query += SolrSchemaConstants.queryInnerJoin + rangeFilter.getName() + SolrSchemaConstants.paramAppender + "[" + rangeFilter.getStartRange() + " TO "
                + rangeFilter.getEndRange() + "]" + SolrSchemaConstants.queryTerminator;
        solrQuery.setQuery(query);
        // get all the fields of document
        solrQuery.set("fl", "*");

        int perPage = paginationFilter.getPerPage();
        int page = paginationFilter.getPage();
        if (categories.get(0).equals("services")) {
            perPage = 100;
        }
        solrQuery.setStart((page - 1) * perPage);
        solrQuery.setRows(perPage);

        String sortBy = sortFilter.getName();
        if (!SolrSchemaConstants.sortByRanking.equals(sortFilter.getName()) && !SolrSchemaConstants.sortByHkPrice.equals(sortFilter.getName())) {
            sortBy = SolrSchemaConstants.sortByRanking;
        }
        solrQuery.addSortField(SolrSchemaConstants.sortByOutOfStock, SolrQuery.ORDER.asc);
        solrQuery.addSortField(sortBy, sortFilter.getSortOrder().equals("asc") ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);

        QueryResponse response = solr.query(solrQuery);

        long resultCount = response.getResults().getNumFound();
        List<SolrProduct> productList = new ArrayList<SolrProduct>();
        productList.addAll(response.getBeans(SolrProduct.class));
        SearchResult searchResult = getSearchResult(productList, (int) resultCount);
        searchResult.setResultSize((int) resultCount);
        return searchResult;
    }

   private SearchResult getCategorySearchResults(String category, int page, int perPage) throws SearchException {

     SolrQuery query = new SolrQuery("*:*");
       query.addFilterQuery(SolrSchemaConstants.categoryDisplayName + ":\"" + category+"\"");
        query.addFilterQuery(SolrSchemaConstants.isGoogleAdDisallowed + ":" + 0);
        query.addFilterQuery(SolrSchemaConstants.isHidden + ":" + 0);
        query.addFilterQuery(SolrSchemaConstants.isDeleted + ":" + 0);

        query.set("fl", "*");
        query.setStart((page - 1) * perPage);
        query.setRows(perPage);
        query.addSortField(SolrSchemaConstants.sortByOutOfStock, SolrQuery.ORDER.asc);
        query.addSortField(SolrSchemaConstants.sortBy, SolrQuery.ORDER.asc);

        List<SolrProduct> solrProductList = new ArrayList<SolrProduct>();
        long resultCount = 0;
        SearchResult searchResult = null;
        try {
            QueryResponse response = solr.query(query);
            // SolrDocumentList documents = response.getResults();
            resultCount = response.getResults().getNumFound();
            solrProductList.addAll(response.getBeans(SolrProduct.class));
            searchResult = getSearchResult(solrProductList, (int) resultCount);
        } catch (SolrServerException ex) {
            SearchException e = wrapException("Unable to get brand catalog results", ex);
            throw e;
        }
        return searchResult;
    }

    public SolrProduct getProduct(String productId) {

      SolrQuery query = new SolrQuery("*:*");
      query.addFilterQuery(SolrSchemaConstants.productID + ":" + productId);
      List<SolrProduct> solrProductList = null;
      try {
        QueryResponse response = solr.query(query);
        solrProductList = response.getBeans(SolrProduct.class);
      } catch (SolrServerException ex) {
        logger.error("Exception while getting solr product", ex.getMessage());
      }
      return solrProductList != null && !solrProductList.isEmpty() ? solrProductList.get(0) : null;
    }

   public boolean isCategoryTerm(String term) throws SearchException {
       term = makeItCategoryTerm(term);
        SolrQuery query = new SolrQuery("*:*");
        query.addFilterQuery(SolrSchemaConstants.categoryDisplayName + ":\"" + term+"\"");
        try {
            QueryResponse response = solr.query(query);
            long resultCount = response.getResults().getNumFound();
            logger.debug("resultCount@isCategoryTerm="+resultCount+" for term="+term);
            if(resultCount > 0)
              return true;
        } catch (SolrServerException ex) {
            logger.error("unable to get result ", ex);
            SearchException e = wrapException("Unable to get category term results", ex);
            throw e;
        }
        return false;
    }

    private String makeItCategoryTerm(String category){
        if (category.equals("adult diaper") || category.equals("adult pull-up") || category.equals("adult pull up diapers") ||
                category.equals("adult pull-up diaper") || category.equals("adult biapers") || category.equals("adult diaperes")) {
            return "adult diapers";
        }
        if(category.equalsIgnoreCase("back support") || category.equalsIgnoreCase("back supports") || category.equalsIgnoreCase("backrest") || category.equalsIgnoreCase("backrests")
                || category.equalsIgnoreCase("back rest")|| category.equalsIgnoreCase("back rests") || category.equalsIgnoreCase("back-support") || category.equalsIgnoreCase("back pain")
                || category.equalsIgnoreCase("back-supports")|| category.equalsIgnoreCase("lower back") || category.equalsIgnoreCase("lower back support")){
            return "Back Supports";
        }

        return category;
    }

    private SearchResult getSearchResult(List<SolrProduct> solrProducts, int totalResultCount) {
        List<String> productIds = new ArrayList<String>();
        // This sorting can also be done in database query but decided to do it in Java
        //
        Map<String, Integer> solrProductIndexMap = new HashMap<String, Integer>();
        int counter = 0;
        for (SolrProduct solrProduct : solrProducts) {
            productIds.add(solrProduct.getId());
            solrProductIndexMap.put(solrProduct.getId(), counter);
            counter++;
        }

        List<Product> products = new ArrayList<Product>();
        // List of products sorted as per Solr
        List<Product> sortedProducts = new ArrayList<Product>();
        if (productIds.size() > 0) {
            products = productService.getAllProductsById(productIds);
            sortedProducts = new ArrayList<Product>(products);
            for (Product product : products) {
                product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.searchPage.getId()));
                int index = solrProductIndexMap.get(product.getId());
                sortedProducts.set(index, product);
            }
        }
        return new SearchResult(sortedProducts, productIds, totalResultCount);
    }

  private SpellCheckResponse getSpellCheckResponse(String userQuery) throws SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", userQuery);
        params.set("spellcheck", "on"); // get spelling suggestions

        QueryResponse response = solr.query(params);
        return response.getSpellCheckResponse();
  }

    private SearchResult getProductSuggestions(QueryResponse response, List<SearchFilter> searchFilters, String userQuery, int page, int perPage) throws SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        SearchResult sr = new SearchResult();
        params.set("q", userQuery);
        params.set("spellcheck", "on"); // get spelling suggestions

        // String query = params.toString();
        response = solr.query(params);
        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
        // StringBuilder sb = new StringBuilder();
        boolean canRunSpellQuery = false;
        // List<String> suggestions = new ArrayList<String>();
        String suggestions = "";
        if ((spellCheckResponse != null) && !spellCheckResponse.isCorrectlySpelled()) {

            List<SpellCheckResponse.Suggestion> solrSuggestions = response.getSpellCheckResponse().getSuggestions();
            // Iterator<SpellCheckResponse.Suggestion> iterator = solrSuggestions.iterator();
            if (solrSuggestions.size() > 1) {
                canRunSpellQuery = true;
            } else {
                for (SpellCheckResponse.Suggestion suggestion : solrSuggestions) {
                    /*
                     * String alternativeFinal = ""; for (String alternative : suggestion.getAlternatives()) { { //
                     * suggestions.add(alternative); } }
                     */
                    canRunSpellQuery = true;
                    logger.debug("original token: " + suggestion.getToken() + " - alternatives: " + suggestion.getAlternatives());
                }
            }
            suggestions = spellCheckResponse.getCollatedResult();
        }

        if (canRunSpellQuery) {
            SolrQuery solrQuery = getResultsQuery(suggestions, searchFilters, page, perPage);              
            response = solr.query(solrQuery);
            List<SolrProduct> solrProductList = getQueryResults(response);
            int totalResultCount = (int) response.getResults().getNumFound();
            sr = getSearchResult(solrProductList, totalResultCount);
            sr.setSearchSuggestions(suggestions);
        }
        return sr;
    }

    public SearchResult getSearchResults(String query, List<SearchFilter> searchFilters, int page, int perPage, boolean isRetry) throws SearchException {

        QueryResponse response = null;
        SearchResult searchResult = new SearchResult();
        try {
            query = sanitizeQuery(query);
            //Level 1 check Starts - Ajeet
            if (this.isBrandTerm(query)) {
              return this.getBrandCatalogResults(query, null, page, perPage, null, false);
            } else if (this.isCategoryTerm(query)) {
              return this.getCategorySearchResults(makeItCategoryTerm(query), page, perPage); // ps hack
            }
            //End - Ajeet
            response = solr.query(getResultsQuery(query, searchFilters, page, perPage));
            List<SolrProduct> productList = getQueryResults(response);
            searchResult = getSearchResult(productList, (int) response.getResults().getNumFound());

            long resultsCount = response.getResults().getNumFound();
            if (resultsCount == 0 && !isRetry) {
                searchResult = getProductSuggestions(response, searchFilters, query, page, perPage);
                if ((searchResult != null) && searchResult.getResultSize() == 0) {
                    query = query.replaceAll(" ", "");
                    searchResult = getSearchResults(query, searchFilters, page, perPage, true);
                }
            }
        } catch (SolrServerException ex) {
            SearchException e = wrapException("Unable to get search results from Solr", ex);
            throw e;
        }
        return searchResult;
    }

    private List<SolrProduct> getQueryResults(QueryResponse response) {
        List<SolrProduct> productList = new ArrayList<SolrProduct>();
        productList.addAll(response.getBeans(SolrProduct.class));
        return productList;
    }

    private SolrQuery buildSolrQuery(String query, List<SearchFilter> searchFilters, String qf, int page, int perPage) {
        SolrQuery solrQuery = new SolrQuery();
        String fq = String.format("{!cache=false}hidden:false"); // Do not cache the results*/
        solrQuery.setParam("fq", fq);
        for (SearchFilter searchFilter : searchFilters) {
            if (searchFilter.getValue() != null) {
                String filterQuery = String.format("{!field f= %s}%s", searchFilter.getName(), searchFilter.getValue());
                solrQuery.addFilterQuery(filterQuery);
            }
        }
        solrQuery.setParam("q", query);
        solrQuery.setParam("defType", "dismax");
        solrQuery.setParam("qf", qf);

        solrQuery.setStart((page - 1) * perPage);
        solrQuery.set("fl", "*");
        solrQuery.set("fl", "score");
        solrQuery.setHighlight(true);
        solrQuery.setStart((page - 1) * perPage);
        solrQuery.setRows(perPage);

        solrQuery.addFilterQuery(SolrSchemaConstants.isHidden + ":" + 0);
        solrQuery.addFilterQuery(SolrSchemaConstants.isDeleted + ":" + 0);

        // We want to push out of stock products to the last page
        solrQuery.addSortField(SolrSchemaConstants.sortByOutOfStock, SolrQuery.ORDER.asc);
        solrQuery.addSortField("score", SolrQuery.ORDER.desc);
        return solrQuery;
    }

    private SolrQuery getResultsQuery(String query, List<SearchFilter> searchFilters, int page, int perPage) {

        String qf = "";
        qf += SolrSchemaConstants.name + "^2.0 ";
        qf += SolrSchemaConstants.variantName + "^1.9 ";
        qf += SolrSchemaConstants.brandLiberal + "^1.8 ";
        qf += SolrSchemaConstants.category + "^1.6 ";
        qf += SolrSchemaConstants.metaKeywords + "^1.4 ";
        qf += SolrSchemaConstants.overview + "^1.2 ";
        qf += SolrSchemaConstants.description + "^1.0 ";
        qf += SolrSchemaConstants.keywords + "^0.8 ";
        qf += SolrSchemaConstants.h1 + "^0.5 ";
        qf += SolrSchemaConstants.title + "^0.5 ";
        qf += SolrSchemaConstants.metaDescription + "^0.5 ";
        qf += SolrSchemaConstants.seoDescription + "^0.5 ";
        qf += SolrSchemaConstants.description_title + "^0.5 ";
        SolrQuery solrQuery = buildSolrQuery(query, searchFilters, qf, page, perPage);

        return solrQuery;
    }

    private SearchException wrapException(String msg, Exception ex) {
        logger.error(msg);
        return new SearchException(SEARCH_SERVER, msg, ex);
    }

     private String sanitizeQuery(String query){
        return query.replace(':',' ');
    }

  @Override
  public void logSearchResult(String keyword, Long results, String category) {
    try {
      TrafficTracking trafficTracking = (TrafficTracking) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
      if (trafficTracking != null) {
        Long trafficTrackingId = trafficTracking.getId();
        SearchLog searchLog = new SearchLog();
        searchLog.setTrafficTrackingId(trafficTrackingId);
        searchLog.setKeyword(keyword);
        searchLog.setKeyword(keyword);
        searchLog.setResults(results);
        searchLog.setCategory(category);
        getSearchLogDao().save(searchLog);
      }
    } catch (Exception e) {
      logger.error("Exception while logging search results "+e.getMessage());
    }
  }

  @Override
  public void updatePositionInSearchLog(String position) {
    try {
      TrafficTracking trafficTracking = (TrafficTracking) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
      if (trafficTracking != null) {
        Long trafficTrackingId = trafficTracking.getId();
        SearchLog searchLog = getSearchLogDao().getLatestSearchLog(trafficTrackingId);
        if (searchLog != null && searchLog.getFirstClickPos() == null) {
          searchLog.setFirstClickPos(position);
          getSearchLogDao().save(searchLog);
        }
      }
    } catch (Exception e) {
      logger.error("Exception while updating first click position" + e.getMessage());
    }
  }

  public SearchLogDao getSearchLogDao() {
    return searchLogDao;
  }

  public void setSearchLogDao(SearchLogDao searchLogDao) {
    this.searchLogDao = searchLogDao;
  }
}
