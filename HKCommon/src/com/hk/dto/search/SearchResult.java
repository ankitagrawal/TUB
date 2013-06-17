package com.hk.dto.search;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.catalog.product.Product;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 7/12/12
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResult {
    private List<Product> solrProducts = new ArrayList<Product>();
    private List<String> solrProductIds = new ArrayList<String>();
    private int resultSize;
    private String searchSuggestions = "";
    private String primarySuggestion;



    public SearchResult(){

    }
    public SearchResult(List<Product> solrProducts, List<String> solrProductIds, int resultSize){
        this.solrProducts = solrProducts;
        this.solrProductIds= solrProductIds;
        this.resultSize = resultSize;
    }

    public List<Product> getSolrProducts() {
        return solrProducts;
    }

    public void setSolrProducts(List<Product> solrProducts) {
        this.solrProducts = solrProducts;
    }

  public List<String> getSolrProductIds() {
    return solrProductIds;
  }

  public void setSolrProductIds(List<String> solrProductIds) {
    this.solrProductIds = solrProductIds;
  }

  public int getResultSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public String getSearchSuggestions() {
        return searchSuggestions;
    }

    public void setSearchSuggestions(String searchSuggestions) {
        this.searchSuggestions = searchSuggestions;
    }

    public String getPrimarySuggestion() {
        return primarySuggestion;
    }

    public void setPrimarySuggestion(String primarySuggestion) {
        this.primarySuggestion = primarySuggestion;
    }

}
