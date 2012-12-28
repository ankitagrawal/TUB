package com.hk.pact.service.catalog;

import java.util.List;
import java.util.Set;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.dto.PriceRangeDto;
import com.hk.dto.ProductOptionDto;

public interface CategoryService {

    public Category save(Category category);

     public List<String> getBrandsByCategory(List<String> categoryNames);

     public List<Category> getCategoriesByBrand(String brand, String topLevelCategory);

     public Set<Category> getCategoriesFromCategoryNames(String categoryNames);

     public Category getCategoryByName(String name);

    public List<Category> getPrimaryCategories();

    public Category getTopLevelCategory(Product product);

    public List<ProductOptionDto> getFilterOptions(String primaryCategory, List<String> categoryNames, List<Long> filterOptions, Double minPrice, Double maxPrice);

    public PriceRangeDto getPriceRange(String primaryCategory, List<String> categoryNames, List<Long> filterOptions);
}
