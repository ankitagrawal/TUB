package com.hk.rest.mobile.service.action;

import com.akube.framework.dao.Page;
import com.akube.framework.gson.JsonUtils;
import com.hk.constants.marketing.ProductReferrerConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.dto.search.SearchResult;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.rest.mobile.service.model.MCatalogJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.ProductReferrerMapper;
import com.hk.web.HealthkartResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 21, 2012
 * Time: 11:32:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mSearch")
@Component

public class MSearchAction {
    private static Logger logger = LoggerFactory.getLogger(MSearchAction.class);

    String searchSuggestion;
    Page productPage;
    List<Product> productList = new ArrayList<Product>();
    @Autowired
    ProductDao productDao;
    /*
          @Session(key = HealthkartConstants.Session.perPageCatalog)
          private int perPage;
          private int pageNo;
    */
    @Autowired
    ProductSearchService productSearchService;
    @Autowired
    LinkManager linkManager;

    private int defaultPerPage = 20;

    @GET
    @Path("/search/")
    @Produces("application/json")
    public String search(@QueryParam("query") String query,
                         @QueryParam("pageNo") int pageNo,
                         @QueryParam("perPage") int perPage,
                         @Context HttpServletResponse response) throws SolrServerException, MalformedURLException {
        HealthkartResponse healthkartresponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        List<MCatalogJSONResponse> catalogList = new ArrayList<MCatalogJSONResponse>();
        if (StringUtils.isNotBlank(query)) {
            MCatalogJSONResponse catalogJSONResponse;
            try {
                SearchResult sr = productSearchService.getSearchResults(query, pageNo, perPage, false);
                productPage = new Page(sr.getSolrProducts(), perPage, pageNo, (int) sr.getResultSize());
                productList = productPage.getList();
                for (Product product : productList) {
                    catalogJSONResponse = new MCatalogJSONResponse();
                    catalogJSONResponse.setProductURL(product.getProductURL());
                    catalogJSONResponse = populateCatalogResponse(product, catalogJSONResponse);
                    catalogList.add(catalogJSONResponse);
                }
                searchSuggestion = sr.getSearchSuggestions();
            } catch (Exception e) {
                logger.debug("SOLR NOT WORKING, HITTING DB TO ACCESS DATA", e);
                catalogList = new ArrayList<MCatalogJSONResponse>();
                productPage = productDao.getProductByName(query, pageNo, perPage);
                productList = productPage.getList();
                for (Product product : productList) {
                    catalogJSONResponse = new MCatalogJSONResponse();
                    product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.SEARCH_PAGE)));
                    catalogJSONResponse = populateCatalogResponse(product, catalogJSONResponse);
                    catalogJSONResponse.setProductURL(product.getProductURL());
                    catalogList.add(catalogJSONResponse);
                }
            }
        }
        if (productList != null && productList.size() == 0) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }

        response.addHeader(MHKConstants.ACCESS_CONTROL_ALLOW_CREDENTIALS, MHKConstants.TRUE);
        response.addHeader(MHKConstants.ACCESS_CONTROL_ALLOW_METHODS, MHKConstants.ACCESS_CONTROL_ALLOW_METHODS_LIST);
        response.addHeader(MHKConstants.ACCESS_CONTROL_ALLOW_ORIGIN, MHKConstants.STAR);

        healthkartresponse = new HealthkartResponse(status, message, catalogList);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartresponse);
        return jsonBuilder;
    }

    private MCatalogJSONResponse populateCatalogResponse(Product product, MCatalogJSONResponse catalogJSONResponse) {
        catalogJSONResponse.setManufacturer(product.getManufacturer().getName());
        catalogJSONResponse.setBrand(product.getBrand());
        catalogJSONResponse.setCodAllowed(product.isCodAllowed());
        catalogJSONResponse.setDeleted(product.getDeleted());
        catalogJSONResponse.setDescription(product.getDescription());
        catalogJSONResponse.setDropShipping(product.getDropShipping());
        catalogJSONResponse.setGoogleAdDisallowed(product.getGoogleAdDisallowed());
        catalogJSONResponse.setId(product.getId());
        catalogJSONResponse.setJit(product.getJit());
        catalogJSONResponse.setMaxDays(product.getMaxDays());
        catalogJSONResponse.setMinDays(product.getMinDays());
        catalogJSONResponse.setName(product.getName());
        catalogJSONResponse.setOrderRanking(product.getOrderRanking());
        catalogJSONResponse.setOutOfStock(product.isOutOfStock());
        catalogJSONResponse.setOverview(product.getOverview());
        catalogJSONResponse.setProductHaveColorOptions(product.getProductHaveColorOptions());
        catalogJSONResponse.setService(product.getService());
        catalogJSONResponse.setThumbUrl(product.getThumbUrl());
        catalogJSONResponse.setHkPrice(product.getMinimumMRPProducVariant().getHkPrice());
        catalogJSONResponse.setMarkedPrice(product.getMinimumMRPProducVariant().getMarkedPrice());
        catalogJSONResponse.setDiscountPercentage(product.getMinimumMRPProducVariant().getDiscountPercent());
        return catalogJSONResponse;
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

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("query");
        return params;
    }

    public String getSearchSuggestion() {
        return searchSuggestion;
    }

    public void setSearchSuggestion(String searchSuggestion) {
        this.searchSuggestion = searchSuggestion;
    }

}
