package com.hk.pact.service.search;

import com.akube.framework.dao.Page;

import java.util.List;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.search.PaginationFilter;
import com.hk.domain.search.RangeFilter;
import com.hk.domain.search.SearchFilter;
import com.hk.domain.search.SortFilter;
import com.hk.dto.search.SearchResult;
import com.hk.exception.SearchException;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: Jun 4, 2012
 * Time: 1:25:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface ProductSearchService {
    void refreshDataIndexes() throws SearchException;

    SearchResult getCatalogResults(List<SearchFilter> categories,
                                   List<SearchFilter> searchFilters,
                                   RangeFilter rangeFilter,
                                   PaginationFilter paginationFilter,
                                   SortFilter sortFilter) throws SearchException;

    SearchResult getSearchResults(String query, int page, int perPage) throws SearchException;

    SearchResult getBrandCatalogResults(String brand, String topLevelCategory, int page, int perPage, String preferredZone) throws SearchException;

    void indexProduct(Product product) throws SearchException;
}
