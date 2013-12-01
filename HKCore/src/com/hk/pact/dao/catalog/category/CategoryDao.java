package com.hk.pact.dao.catalog.category;

import java.util.List;
import java.util.Set;

import com.hk.domain.catalog.category.Category;
import com.hk.dto.PriceRangeDto;
import com.hk.dto.ProductOptionDto;
import com.hk.pact.dao.BaseDao;

public interface CategoryDao extends BaseDao {

     public List<String> getBrandsByCategory(List<String> categoryNames);

     public List<String> getBrandsByPrimaryCategory(Category pCategory);

     public List<Category> getCategoriesByBrand(String brand, String topLevelCategory);

    public Set<Category> getCategoriesFromCategoryNames(String categoryNames);

    public Category getCategoryByName(String name);

    public List<Category> getCategoryByNames(List<String> name);

    public List<Category> getPrimaryCategories();

    public List<ProductOptionDto> getProductOptions(String primaryCategory, List<String> categoryNames, List<Long> filterOptions, int groupsCount, Double minPrice, Double maxPrice);

    public PriceRangeDto getPriceRange(String primaryCategory, List<String> categoryNames, List<Long> filterOptions, int groupsCount);

}
