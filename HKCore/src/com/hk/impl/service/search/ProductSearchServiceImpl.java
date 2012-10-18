package com.hk.impl.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.catalog.SolrSchemaConstants;
import com.hk.constants.marketing.ProductReferrerConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.search.PaginationFilter;
import com.hk.domain.search.RangeFilter;
import com.hk.domain.search.SearchFilter;
import com.hk.domain.search.SolrProduct;
import com.hk.domain.search.SortFilter;
import com.hk.dto.search.SearchResult;
import com.hk.exception.SearchException;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.search.ProductIndexService;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.util.ProductReferrerMapper;

@Service
class ProductSearchServiceImpl implements ProductSearchService {
    private static Logger logger = LoggerFactory.getLogger(ProductSearchServiceImpl.class);

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    LocalityMapDao localityMapDao;

    @Autowired
    MapIndiaDao mapIndiaDao;

    @Autowired
    CommonsHttpSolrServer solr;

    @Autowired
    ProductIndexService productIndexService;

    @Autowired
    LinkManager linkManager;


    private final String SEARCH_SERVER = "SOLR";

    public ProductSearchServiceImpl(){
    }

    public void refreshDataIndexes() throws SearchException{
        try {
            List<Product> productList = productService.getAllNonDeletedProducts();
            SolrQuery solrQuery = new SolrQuery();
            //clear Solr Index
            solr.deleteByQuery("*:*");
            List<SolrProduct> products = new ArrayList<SolrProduct>();
            for (Product pr : productList){
                if (!pr.getDeleted()){
                    SolrProduct solrProduct = productService.createSolrProduct(pr);
                    updateExtraProperties(pr, solrProduct);
                    products.add(solrProduct);
                }
            }
            productIndexService.indexProduct(products);
            buildDictionary();
        }catch (SolrException ex) {
            SearchException e = wrapException("Unable to build indexes. Problem with Solr", ex);
            throw e;
            } catch (Exception ex) {
                SearchException e = wrapException("Unable to build indexes. Problem with Solr", ex);
                throw e;
            }
    }

    private void updateExtraProperties(Product pr, SolrProduct solrProduct){
        for (ProductVariant pv : pr.getProductVariants()){
            if (pv.getProductOptions() != null){
                for (ProductOption po : pv.getProductOptions()){
                    if (po.getValue() != null){
                        solrProduct.getVariantNames().add(pr.getName() + " " + po.getValue());
                    }
                }
            }
        }
    }

    public SearchResult getCatalogResults(List<SearchFilter> categories,
                                          List<SearchFilter> searchFilters,
                                          RangeFilter rangeFilter,
                                          PaginationFilter paginationFilter,
                                          SortFilter sortFilter) throws SearchException{

        SearchResult resultsMap;
        try{
            resultsMap = getCatalogSearchResults(categories,
                    searchFilters, rangeFilter, paginationFilter, sortFilter);
        }catch (SolrServerException ex){
            SearchException e = wrapException("Unable to get catalog resutls", ex);
            throw e;
        }
        return resultsMap;//new Page(resultsMap.getSolrProducts(), perPage, page, (int) resultsMap.getResultSize().longValue());
    }

    public SearchResult getBrandCatalogResults(String brand, String topLevelCategory, int page, int perPage, String preferredZone) throws SearchException {
        SolrQuery query = new SolrQuery();
        //Create the query syntax
        String myQuery = SolrSchemaConstants.brand + SolrSchemaConstants.paramAppender +  brand  + SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.category
                + SolrSchemaConstants.paramAppender + topLevelCategory + SolrSchemaConstants.queryTerminator + SolrSchemaConstants.queryInnerJoin
                + SolrSchemaConstants.isGoogleAdDisallowed + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator + SolrSchemaConstants.queryInnerJoin
                + SolrSchemaConstants.isHidden + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator + SolrSchemaConstants.queryInnerJoin
                + SolrSchemaConstants.isDeleted + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;

        query.setQuery(myQuery);
        query.set("fl", "*");
        query.setStart((page - 1) * perPage);
        query.setRows(perPage);
        query.addSortField(SolrSchemaConstants.sortByOutOfStock, SolrQuery.ORDER.asc);
        query.addSortField(SolrSchemaConstants.sortBy, SolrQuery.ORDER.asc);
        List<SolrProduct> solrProductList = new ArrayList<SolrProduct>();
        long resultCount = 0;
        SearchResult searchResult = null;
        try{
            QueryResponse response = solr.query(query);
            SolrDocumentList documents = response.getResults();
            resultCount = response.getResults().getNumFound();
            solrProductList.addAll(response.getBeans(SolrProduct.class));
            searchResult = getSearchResult(solrProductList, (int)resultCount);
        }catch (SolrServerException ex){
            SearchException e = wrapException("Unable to get brand catalog results", ex);
            throw e;
        }
        return searchResult;
    }

    private void buildDictionary() throws SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", "*:*");
        params.set("spellcheck.build", true);  //get spelling suggestions
        String query = params.toString();
        solr.query(params);
    }

    private String buildCategoryQuery(String query, List<SearchFilter> categories){
        for (SearchFilter searchFilter : categories){
            if (searchFilter.getValue() != null){
                Category category = categoryService.getCategoryByName(searchFilter.getValue().toString());
                if (category != null){
                    if (StringUtils.isNotBlank(query)) {
                        query += SolrSchemaConstants.queryInnerJoin + searchFilter.getName() + SolrSchemaConstants.paramAppender + category.getName()
                                + SolrSchemaConstants.queryTerminator;// " AND _query_:\"category_name:cat\""
                    } else {
                        query += SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + category.getName();
                    }
                }
            }
        }
        return query;
    }

    private SearchResult getCatalogSearchResults(List<SearchFilter> categories,
                                                 List<SearchFilter> searchFilters,
                                                 RangeFilter rangeFilter,
                                                 PaginationFilter paginationFilter,
                                                 SortFilter sortFilter)
            throws SolrServerException {

        SolrQuery solrQuery = new SolrQuery();
        String query = "*";
        for (SearchFilter searchFilter : searchFilters){
            //if (!StringUtils.isBlank(searchFilter.getValue()))
            {
                //query += SolrSchemaConstants.brand + SolrSchemaConstants.paramAppender + "\"" + brand + "\"";
                if (searchFilter.getValue() instanceof  String)
                {
                    if (!StringUtils.isBlank(searchFilter.getValue().toString())){
                        query += SolrSchemaConstants.queryInnerJoin + searchFilter.getName() + SolrSchemaConstants.paramAppender  + searchFilter.getValue() ;
                    }
                }
                else
                {
                    query += SolrSchemaConstants.queryInnerJoin + searchFilter.getName() + SolrSchemaConstants.paramAppender + searchFilter.getValue() + SolrSchemaConstants.queryTerminator;
                }
            }
        }
        query = buildCategoryQuery(query, categories);
        // don't show deleted products
        solrQuery.set(SolrSchemaConstants.isDeleted, 0);
        // dont show deleted products and google disallowed products
        query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.isDeleted + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;
        query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.isGoogleAdDisallowed + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;
        query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.isHidden + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;
        query += SolrSchemaConstants.queryInnerJoin + rangeFilter.getName() + SolrSchemaConstants.paramAppender +
                "[" + rangeFilter.getStartRange() + " TO " + rangeFilter.getEndRange() + "]" + SolrSchemaConstants.queryTerminator;
        solrQuery.setQuery(query);
        //get all the fields of document
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
        SearchResult searchResult = getSearchResult(productList, (int)resultCount);
        searchResult.setResultSize((int)resultCount);
        return searchResult;
    }

    private SearchResult getSearchResult(List<SolrProduct> solrProducts, int totalResultCount){
        List<String> productIds = new ArrayList<String>();
        //This sorting can also be done in database query but decided to do it in Java
        //
        Map<String, Integer> solrProductIndexMap = new HashMap<String, Integer>();
        int counter = 0;
        for (SolrProduct solrProduct : solrProducts){
            productIds.add(solrProduct.getId());
            solrProductIndexMap.put(solrProduct.getId(), counter);
            counter++;
        }

        List<Product> products = new ArrayList<Product>();
        //List of products sorted as per Solr
        List<Product> sortedProducts = new ArrayList<Product>();
        if (productIds.size() > 0){
            products = productService.getAllProductsById(productIds);
            sortedProducts = new ArrayList<Product>(products);
            for (Product product : products){
                product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.SEARCH_PAGE)));
                int index = solrProductIndexMap.get(product.getId());
                sortedProducts.set(index, product);
            }
        }
        return new SearchResult(sortedProducts, totalResultCount);
    }

    private SearchResult getProductSuggestions(QueryResponse response,List<SearchFilter> searchFilters, String userQuery, int page, int perPage) throws SolrServerException
    {
        ModifiableSolrParams params = new ModifiableSolrParams();
        SearchResult sr = new SearchResult();
        params.set("q", userQuery);
        params.set("spellcheck", "on");  //get spelling suggestions

        String query = params.toString();
        response = solr.query(params);
        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
        StringBuilder sb = new StringBuilder();
        boolean canRunSpellQuery = false;
        //List<String> suggestions = new ArrayList<String>();
        String suggestions = "";
        if ((spellCheckResponse != null) && !spellCheckResponse.isCorrectlySpelled()) {

            List<SpellCheckResponse.Suggestion> solrSuggestions =   response.getSpellCheckResponse().getSuggestions();
            Iterator<SpellCheckResponse.Suggestion> iterator = solrSuggestions.iterator();
            if (solrSuggestions.size() > 1){
                canRunSpellQuery = true;
            }
            else{
                for (SpellCheckResponse.Suggestion suggestion : solrSuggestions) {
                    String alternativeFinal = "";
                    for (String alternative : suggestion.getAlternatives()){
                        {
                            //suggestions.add(alternative);
                        }
                    }
                    canRunSpellQuery = true;
                    logger.debug("original token: " + suggestion.getToken() + " - alternatives: " + suggestion.getAlternatives());
                }
            }
            suggestions = spellCheckResponse.getCollatedResult();
        }

        if (canRunSpellQuery)
        {
            SolrQuery solrQuery = getResultsQuery(suggestions,searchFilters, page,perPage);
            response = solr.query(solrQuery);
            List<SolrProduct> solrProductList = getQueryResults(response);
            int totalResultCount = (int)response.getResults().getNumFound();
            sr = getSearchResult(solrProductList, totalResultCount);
            sr.setSearchSuggestions(suggestions);
        }
        return sr;
    }

    public SearchResult getSearchResults(String query,List<SearchFilter> searchFilters, int page, int perPage, boolean isRetry) throws SearchException {

        QueryResponse response = null;
        SearchResult searchResult = new SearchResult();
        try{
            response = solr.query(getResultsQuery(query,searchFilters, page, perPage));
            List<SolrProduct> productList = getQueryResults(response);
            searchResult = getSearchResult(productList, (int)response.getResults().getNumFound());

            long resultsCount = response.getResults().getNumFound();
            if(resultsCount == 0 && !isRetry){
                searchResult = getProductSuggestions(response,searchFilters, query, page, perPage);
                if ((searchResult != null) && searchResult.getResultSize() == 0){
                    query = query.replaceAll(" ","");
                    searchResult = getSearchResults(query,searchFilters, page, perPage, true);
                }
            }
        }catch (SolrServerException ex ){
            SearchException e = wrapException("Unable to get search results from Solr", ex);
            throw e;
        }
        return searchResult;
    }

    private  List<SolrProduct> getQueryResults(QueryResponse response){
        List<SolrProduct> productList = new ArrayList<SolrProduct>();
        productList.addAll(response.getBeans(SolrProduct.class));
        return productList;
    }

    private SolrQuery buildSolrQuery(String query,List<SearchFilter> searchFilters, String qf,  int page, int perPage){
        SolrQuery solrQuery = new SolrQuery(); // &defType=dismax&qf=
        String fq = String.format("{!cache=false}hidden:false");  //Do not cache the results*/
        solrQuery.setParam("fq", fq);
        String finalfq = "fq = hidden:false";
        for (SearchFilter searchFilter : searchFilters){
            String fq1 = searchFilter.getName() + ":" + searchFilter.getValue();
            solrQuery = solrQuery.setParam("fq", fq1);
            /*String fq1 = "fq = " + searchFilter.getName() + ":" + searchFilter.getValue();
            finalfq += " & " + fq1;*/
        }
        solrQuery.setParam("q", query);
        //solrQuery.setParam("fq","+" + fq);
        solrQuery.setParam("defType", "dismax");
        solrQuery.setParam("qf", qf);

        solrQuery.setStart((page - 1) * perPage);
        solrQuery.set("fl", "*");
        solrQuery.set("fl", "score");
        solrQuery.setHighlight(true);
        solrQuery.setStart((page - 1) * perPage);
        solrQuery.setRows(perPage);
        //We want to push out of stock products to the last page
        solrQuery.addSortField(SolrSchemaConstants.sortByOutOfStock, SolrQuery.ORDER.asc);
        solrQuery.addSortField("score", SolrQuery.ORDER.desc);
        return solrQuery;
    }

    private SolrQuery getResultsQuery(String query,List<SearchFilter> searchFilters, int page, int perPage){

        String qf = "";
        qf += SolrSchemaConstants.name + "^2.0 ";
        qf += SolrSchemaConstants.variantName + "^1.9 ";
        qf += SolrSchemaConstants.brand + "^1.8 ";
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
        SolrQuery solrQuery = buildSolrQuery(query, searchFilters,qf, page, perPage);

        return solrQuery;
    }

    private SearchException wrapException(String msg, Exception ex){
        logger.error(msg, ex);
        return new SearchException(SEARCH_SERVER, msg,ex);
    }
}
