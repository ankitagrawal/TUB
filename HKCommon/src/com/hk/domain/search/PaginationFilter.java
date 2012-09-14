package com.hk.domain.search;

public class PaginationFilter {
    private int page;
    private int perPage;

    public PaginationFilter(int page, int perPage){
        this.page = page;
        this.perPage = perPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}

