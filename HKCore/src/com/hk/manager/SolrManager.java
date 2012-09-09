package com.hk.manager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.hk.constants.catalog.SolrSchemaConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.marketing.ProductReferrerConstants;
import com.hk.domain.LocalityMap;
import com.hk.domain.MapIndia;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.Address;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.ProductReferrerMapper;

@Component
public class SolrManager {
    private static Logger logger = LoggerFactory.getLogger(SolrManager.class);

    @Value("#{hkEnvProps['" + Keys.Env.solrUrl + "']}")
    String                solrUrl;

    @Autowired
    CategoryService       categoryService;

    @Autowired
    ProductService        productService;

    @Autowired
    LinkManager           linkManager;

    LocalityMapDao        localityMapDao;

    MapIndiaDao           mapIndiaDao;

    public void refreshDataIndexes() {
        try {
            URL myURL = new URL(solrUrl + "dataimport?command=full-import");
            HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            logger.info("Opened Con->" + connection);
            connection.connect();
            int code = connection.getResponseCode();
            logger.info("code ->" + code);
            if (code == HttpURLConnection.HTTP_OK) {
                logger.info("Disconnecting Connection.");
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException in DATAIMPORT", e);
        } catch (IOException e) {
            logger.error("IOException in DATAIMPORT", e);
        } catch (Exception e) {
            logger.error("Catching Exception in DATAIMPORT", e);
        }
    }

    public Page getCatalogResults(String rootCategorySlug, String smallestCategory, String secondSmallestCategory, String thirdSmallestCategory, String brand, String sortBy,
            String sortOrder, Double startRange, Double endRange, int page, int perPage, String preferredZone) throws IOException, SolrServerException {

        SolrServer solr = new CommonsHttpSolrServer(solrUrl);
        SolrQuery solrQuery = new SolrQuery();
        String query = "";

        Category category = getCategoryService().getCategoryByName(smallestCategory);
        if (!StringUtils.isBlank(brand)) {
            query += SolrSchemaConstants.brand + SolrSchemaConstants.paramAppender + "\"" + brand + "\"";
            // query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.brand +
            // SolrSchemaConstants.paramAppender + "\"" + brand + "\"" + SolrSchemaConstants.queryTerminator;
        }

        if (category != null) {
            if (StringUtils.isBlank(query)) {
                query += SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + category.getName();
            } else {
                query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + category.getName()
                        + SolrSchemaConstants.queryTerminator;// " AND _query_:\"category_name:cat\""
            }
        }

        if (secondSmallestCategory != null) {
            Category secondaryCategory = getCategoryService().getCategoryByName(secondSmallestCategory);
            if (secondaryCategory != null) {
                if (StringUtils.isNotBlank(query)) {
                    query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + secondaryCategory.getName()
                            + SolrSchemaConstants.queryTerminator;// " AND _query_:\"category_name:cat\""
                } else {
                    query += SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + secondaryCategory.getName();
                }
            }
        }

        if (thirdSmallestCategory != null) {
            Category tertiaryCategory = getCategoryService().getCategoryByName(thirdSmallestCategory);
            if (tertiaryCategory != null) {
                if (StringUtils.isNotBlank(query)) {
                    query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + tertiaryCategory.getName()
                            + SolrSchemaConstants.queryTerminator;// " AND _query_:\"category_name:cat\""
                } else {
                    query += SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + tertiaryCategory.getName();
                }
            }
        }

        Category rootCategory = getCategoryService().getCategoryByName(rootCategorySlug);
        if (rootCategory != null) {
            if (StringUtils.isNotBlank(query)) {
                query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + rootCategory.getName()
                        + SolrSchemaConstants.queryTerminator;// " AND _query_:\"category_name:cat\""
            } else {
                query += SolrSchemaConstants.category + SolrSchemaConstants.paramAppender + rootCategory.getName();
            }
        }

        // dont show deleted products and google disallowed products
        query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.isDeleted + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;
        query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.isGoogleAdDisallowed + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;
        query += SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.hkPrice + SolrSchemaConstants.paramAppender + "[" + startRange + " TO " + endRange + "]"
                + SolrSchemaConstants.queryTerminator;
        solrQuery.setQuery(query);
        solrQuery.set("fl", SolrSchemaConstants.productID);
        if (rootCategorySlug.equals("services")) {
            perPage = 100;
        }
        solrQuery.setStart((page - 1) * perPage);
        solrQuery.setRows(perPage);
        if (!SolrSchemaConstants.sortByRanking.equals(sortBy) && !SolrSchemaConstants.sortByHkPrice.equals(sortBy)) {
            sortBy = SolrSchemaConstants.sortByRanking;
        }
        solrQuery.addSortField(sortBy, sortOrder.equals("asc") ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);

        QueryResponse response = solr.query(solrQuery);

        List<Product> productList = new ArrayList<Product>();
        long resultCount = response.getResults().getNumFound();
        SolrDocumentList documents = response.getResults();
        for (Object document : documents) {
            SolrDocument doc = (SolrDocument) document;
      			Product product = getProductService().getProductById((String) doc.getFieldValue(SolrSchemaConstants.productID));
            product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(rootCategorySlug)));
			      productList.add(product);
        }
        if (rootCategorySlug.equals("services")) {
            productList = trimListByDistance(productList, preferredZone);
            resultCount = productList != null ? productList.size() : 0;
        }
        return new Page(productList, perPage, page, (int) resultCount);
    }

    public Page getSearchResults(String query, int page, int perPage) throws MalformedURLException, SolrServerException {
        SolrServer solr = new CommonsHttpSolrServer(solrUrl);
        SolrQuery solrQuery = new SolrQuery(); // &defType=dismax&qf=
        String qf = "";
        qf += SolrSchemaConstants.name + "^2.0 ";
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
        solrQuery.setParam("q", query);
        solrQuery.setParam("defType", "dismax");
        solrQuery.setParam("qf", qf);
        // solrQuery.setQuery(query);
        solrQuery.setStart((page - 1) * perPage);
        solrQuery.set("fl", SolrSchemaConstants.productID);
        solrQuery.setHighlight(true);
        solrQuery.setStart((page - 1) * perPage);
        solrQuery.setRows(perPage);
        QueryResponse response = solr.query(solrQuery);
        List<Product> productList = new ArrayList<Product>();

        SolrDocumentList documents = response.getResults();
        long resultCount = response.getResults().getNumFound();
        for (Object document : documents) {
            SolrDocument doc = (SolrDocument) document;
            Product product = getProductService().getProductById((String) doc.getFieldValue(SolrSchemaConstants.productID));
            product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.SEARCH_PAGE)));
            productList.add(product);
        }
        return new Page(productList, perPage, page, (int) resultCount);
    }

    public Page getBrandCatalogResults(String brand, String topLevelCategory, int page, int perPage, String preferredZone) throws IOException, SolrServerException {
        SolrServer solr = new CommonsHttpSolrServer(solrUrl);
        SolrQuery query = new SolrQuery();
        String myQuery = SolrSchemaConstants.brand + SolrSchemaConstants.paramAppender + "\"" + brand + "\"" + SolrSchemaConstants.queryInnerJoin + SolrSchemaConstants.category
                + SolrSchemaConstants.paramAppender + topLevelCategory + SolrSchemaConstants.queryTerminator + SolrSchemaConstants.queryInnerJoin
                + SolrSchemaConstants.isGoogleAdDisallowed + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator + SolrSchemaConstants.queryInnerJoin
                + SolrSchemaConstants.isDeleted + SolrSchemaConstants.paramAppender + 0 + SolrSchemaConstants.queryTerminator;
        query.setQuery(myQuery);
        query.set("fl", SolrSchemaConstants.productID);
        query.setStart((page - 1) * perPage);
        query.setRows(perPage);
        query.addSortField(SolrSchemaConstants.sortBy, SolrQuery.ORDER.asc);
        QueryResponse response = solr.query(query);
        SolrDocumentList documents = response.getResults();
        long resultCount = response.getResults().getNumFound();
        List<Product> productList = new ArrayList<Product>();
        for (Object document : documents) {
            SolrDocument doc = (SolrDocument) document;
			      Product product = getProductService().getProductById((String) doc.getFieldValue(SolrSchemaConstants.productID));
            product.setProductURL(linkManager.getRelativeProductURL(product, ProductReferrerMapper.getProductReferrerid(ProductReferrerConstants.BRAND_PAGE)));
			      productList.add(product);
        }
        // productList = trimListByDistance(productList, preferredZone);
        // resultCount = productList != null ? productList.size() : 0;
        return new Page(productList, perPage, page, (int) resultCount);
    }

    private List<Product> trimListByDistance(List<Product> productListNonCityFiltered, String preferredZone) throws IOException {
        if (preferredZone == null) {
            preferredZone = "Delhi"; // hardcoded default results
        } else if (preferredZone.equals("All")) {// returning the whole list if preferred zone is blank
            return productListNonCityFiltered;
        }
        MapIndia mi = getMapIndiaDao().findByCity(preferredZone);
        List<Product> cityFiltered = new ArrayList<Product>();
        for (Iterator<Product> productIterator = productListNonCityFiltered.iterator(); productIterator.hasNext();) {
            Product product = productIterator.next();
            if (product.isService()) {
                Manufacturer manufacturer = product.getManufacturer();
                if (manufacturer != null) {
                    List<Address> manufacturerAddresses = manufacturer.getAddresses();
                    if (manufacturerAddresses != null && manufacturerAddresses.size() > 0) {
                        for (Address serviceAddress : manufacturerAddresses) {
                            LocalityMap lm = getLocalityMapDao().findByAddress(serviceAddress);
                            if (lm != null) {
                                if (getLocalityMapDao().getDistanceInMeters(lm.getLattitude(), lm.getLongitude(), mi.getLattitude(), mi.getLongitude()) < 60) {
                                    cityFiltered.add(product);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (manufacturer != null && manufacturer.isAvailableAllOverIndia() != null && manufacturer.isAvailableAllOverIndia()) {
                    if (!cityFiltered.contains(product))
                        cityFiltered.add(product);
                }
            }
        }
        return cityFiltered;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public LocalityMapDao getLocalityMapDao() {
        return localityMapDao;
    }

    public void setLocalityMapDao(LocalityMapDao localityMapDao) {
        this.localityMapDao = localityMapDao;
    }

    public MapIndiaDao getMapIndiaDao() {
        return mapIndiaDao;
    }

    public void setMapIndiaDao(MapIndiaDao mapIndiaDao) {
        this.mapIndiaDao = mapIndiaDao;
    }

}
