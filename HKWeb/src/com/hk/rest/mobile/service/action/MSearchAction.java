package com.hk.rest.mobile.service.action;

import com.akube.framework.dao.Page;
import com.akube.framework.gson.JsonUtils;
import com.hk.constants.catalog.SolrSchemaConstants;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.search.SearchFilter;
import com.hk.dto.search.SearchResult;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.rest.mobile.service.model.MCatalogJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.HKImageUtils;
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
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Satish Date: Sep 21, 2012 Time: 11:32:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Path ("/mSearch")
@Component
public class MSearchAction extends MBaseAction {
	private static Logger logger = LoggerFactory.getLogger(MSearchAction.class);

	String searchSuggestion;
	Page productPage;
	List<Product> productList = new ArrayList<Product>();
	@Autowired
	ProductDao productDao;
	/*
	 * @Session(key = HealthkartConstants.Session.perPageCatalog) private int
	 * perPage; private int pageNo;
	 */
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	LinkManager linkManager;

	private int defaultPerPage = 20;

	@GET
	@Path ("/search/")
	@Produces ("application/json")
	public String search(@QueryParam ("query") String query,
	                     @QueryParam ("pageNo") int pageNo,
	                     @QueryParam ("perPage") int perPage,
	                     @Context HttpServletResponse response) throws SolrServerException, MalformedURLException {
		HealthkartResponse healthkartresponse;
		String jsonBuilder = "";
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;
		List<MCatalogJSONResponse> catalogList = new ArrayList<MCatalogJSONResponse>();
		boolean includeCombo = false;
		boolean onlyCOD = true;
		if (StringUtils.isNotBlank(query)) {
			MCatalogJSONResponse catalogJSONResponse;
			try {
				List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
				if (!includeCombo) {
					SearchFilter comboFilter = new SearchFilter(SolrSchemaConstants.isCombo, includeCombo);
					searchFilters.add(comboFilter);
				}
				if (onlyCOD) {
					SearchFilter codFilter = new SearchFilter(SolrSchemaConstants.isCODAllowed, onlyCOD);
					searchFilters.add(codFilter);
				}
				SearchResult sr = productSearchService.getSearchResults(query, searchFilters, pageNo, perPage, false);
				productPage = new Page(sr.getSolrProducts(), perPage, pageNo, (int) sr.getResultSize());
				productList = productPage.getList();
				for (Product product : productList) {
					catalogJSONResponse = new MCatalogJSONResponse();
					catalogJSONResponse = populateCatalogResponse(product, catalogJSONResponse);
					product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.mobile_search.getId()));
					catalogJSONResponse.setProductURL(product.getProductURL());
					catalogList.add(catalogJSONResponse);
				}
			} catch (Exception e) {
				try{
				logger.debug("SOLR NOT WORKING, HITTING DB TO ACCESS DATA. " + e.getMessage());
				productPage = productDao.getProductByName(query, onlyCOD, includeCombo, pageNo, perPage);
				productList = productPage.getList();
				for (Product product : productList) {
					catalogJSONResponse = new MCatalogJSONResponse();
					product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.mobile_search.getId()));
					catalogJSONResponse = populateCatalogResponse(product, catalogJSONResponse);
					catalogJSONResponse.setProductURL(product.getProductURL());
					catalogList.add(catalogJSONResponse);
				}
				}catch(Exception dbe){
					status = MHKConstants.STATUS_ERROR;
					message = MHKConstants.NO_SUCH_PRDCT;
					return  JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, message));
				}
			}
		}
		resultMap.put("hasMore", new Boolean(true));
		if (productList == null || productList.size() == 0) {
			message = MHKConstants.NO_RESULTS;
			status = MHKConstants.STATUS_ERROR;
			resultMap.put("hasMore", new Boolean(false));
		} else if (productList.size() < perPage) {
			resultMap.put("hasMore", new Boolean(false));
		}

		addHeaderAttributes(response);

		resultMap.put("data", catalogList);

		healthkartresponse = new HealthkartResponse(status, message, resultMap);
		jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartresponse);
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
		catalogJSONResponse.setProductReferrerId(EnumProductReferrer.mobile_search.getId());
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
