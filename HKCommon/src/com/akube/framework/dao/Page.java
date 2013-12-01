package com.akube.framework.dao;

import java.util.List;

@SuppressWarnings("unchecked")
public class Page {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private List          resultList;
    private int              pageSize;
    private int              page;
    private int              totalPages;
    private int              totalResults;

    public Page(List resultList, int pageSize, int page, int totalResults) {
        this.resultList = resultList;
        this.pageSize = pageSize;
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = (totalResults - 1) / pageSize + 1;
    }

    public Page(List resultList, int page, int totalResults) {
        this(resultList, DEFAULT_PAGE_SIZE, page, totalResults);
    }

    /**
     * this is a constructor which we can use to create a new page object with custom values it takes an existing page
     * and sets all parameters from that page. it also takes a List type erasure is happening here
     * 
     * @param page
     * @param list
     */
    @SuppressWarnings( { "unchecked", "hiding" })
    public  Page(Page page, List list) {
        this.pageSize = page.getPageSize();
        this.page = page.getPage();
        this.totalPages = page.getTotalPages();
        this.totalResults = page.getTotalResults();
        // using this cast to circumvent weird compilation error
        // noinspection unchecked
        this.resultList = (List) list;
    }

    /*
     * @SuppressWarnings("hiding") public  Page(Criteria criteria, boolean hasDistinctRootEntity, int page, int
     * pageSize) { this.pageSize = pageSize; if (hasDistinctRootEntity) { List totalResultsList =
     * criteria.setProjection(Projections.countDistinct("id")).list(); totalResults = (Integer) totalResultsList.get(0); }
     * else { List totalResultsList = criteria.setProjection(Projections.rowCount()).list(); totalResults = (Integer)
     * totalResultsList.get(0); } //List totalResultsList =
     * criteria.setProjection(Projections.distinct(Projections.id())).list(); // totalResults = (Integer)
     * totalResultsList.size(); totalPages = (totalResults - 1) / pageSize + 1; if (page > totalPages) { page =
     * totalPages; } this.page = page; criteria.setProjection(null); if (hasDistinctRootEntity) {
     * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); } else {
     * criteria.setResultTransformer(Criteria.ROOT_ENTITY); } resultList = criteria.setFirstResult((page - 1) *
     * pageSize) .setMaxResults(pageSize) .list(); } @SuppressWarnings("hiding") public  Page(Criteria criteria, int
     * page, int pageSize) { this(criteria, false, page, pageSize); }
     */
    /**
     * Use this method if the total result count is known. this will avoid a count query.
     * 
     * @param criteria
     * @param totalResults
     * @param page
     * @param pageSize
     * @param 
     */
    /*
     * @SuppressWarnings("hiding") public  Page(Criteria criteria, int totalResults, int page, int pageSize) {
     * this.totalResults = totalResults; this.pageSize = pageSize; totalPages = (totalResults - 1) / pageSize + 1; if
     * (page > totalPages) { page = totalPages; } this.page = page; resultList = criteria.setFirstResult((page - 1) *
     * pageSize) .setMaxResults(pageSize) .list(); }
     */

    public int getTotalPageResults() {
        if (resultList != null) {
            return resultList.size();
        } else {
            return 0;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    public List getList() {
        return resultList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
