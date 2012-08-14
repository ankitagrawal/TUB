package com.hk.pact.dao.catalog.category;

import java.util.List;
import java.util.Set;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.pact.dao.BaseDao;
import com.hk.dto.ProductOptionDto;
import com.hk.dto.PriceRangeDto;

public interface CategoryDao extends BaseDao {

    public List<String> getBrandsByCategory(List<String> categoryNames);

    public List<Category> getCategoriesByBrand(String brand, String topLevelCategory);

    public Set<Category> getCategoriesFromCategoryNames(String categoryNames);

    public Category getCategoryByName(String name);

    public List<Category> getPrimaryCategories();

	public List<ProductOptionDto> getProductOptions(String category);

	public PriceRangeDto getPriceRange(String category);

}
