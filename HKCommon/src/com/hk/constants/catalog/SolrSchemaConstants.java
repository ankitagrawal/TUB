package com.hk.constants.catalog;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Sep 21, 2011
 * Time: 8:43:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class SolrSchemaConstants {

    public static final String productID = "id";
    public static final String category = "category";
    public static final String categoryDisplayName = "categoryDisplayName";
    public static final String brand = "brand";
    //Does not try to match exact brand..To be used in text search
    public static final String brandLiberal = "brand_liberal";
    public static final String name = "name";
    public static final String variantName = "variantNames";
    public static final String metaKeywords = "metaKeyword";
    public static final String title = "title";
    public static final String description_title = "descriptionTitle";
    public static final String h1 = "h1";
    public static final String overview = "overview";
    public static final String description = "description";
    public static final String metaDescription = "meta_description";
    public static final String keywords = "keywords";
    public static final String seoDescription = "seoDescription";
    public static final String isDeleted = "deleted";
    public static final String isGoogleAdDisallowed = "isGoogleAdDisallowed";
    public static final String isHidden = "hidden";
    public static final String isCombo = "isCombo";
    public static final String isCODAllowed = "isCODAllowed";
    public static final String sortBy = "ranking";
    public static final String sortByRanking = "ranking";
    public static final String sortByHkPrice = "hkPrice";
    public static final String sortByOutOfStock = "outOfStock";
    public static final String hkPrice = "hkPrice";



    public static final String paramAppender = ":";
    public static final String queryTerminator = "\"";
    public static final String queryInnerJoin = " AND _query_:\"";
    public static final String negationQueryInnerJoin = " AND -_query_:\"";
}
