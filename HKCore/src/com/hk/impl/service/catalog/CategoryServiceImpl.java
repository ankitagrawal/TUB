package com.hk.impl.service.catalog;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.dto.ProductOptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl  implements CategoryService{

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Category save(Category category){
        return (Category) getCategoryDao().save(category);
    }
    @Override
    public List<String> getBrandsByCategory(List<String> categoryNames) {
        return getCategoryDao().getBrandsByCategory(categoryNames);
    }

    @Override
    public List<Category> getCategoriesByBrand(String brand, String topLevelCategory) {
        return getCategoryDao().getCategoriesByBrand(brand, topLevelCategory);
    }

    @Override
    public Set<Category> getCategoriesFromCategoryNames(String categoryNames) {
        return getCategoryDao().getCategoriesFromCategoryNames(categoryNames);
    }

    @Override
    public Category getCategoryByName(String name) {
        return getCategoryDao().getCategoryByName(name);
    }

    @Override
    public List<Category> getPrimaryCategories() {
        return getCategoryDao().getPrimaryCategories();
    }
    
    @Override
    public Category getTopLevelCategory(Product product) {
        Category topLevelCategory = null;

        if (product.getPrimaryCategory() != null) {
            topLevelCategory = product.getPrimaryCategory();
        } else {
            Category homeHealthDevices = getCategoryByName("home-devices");
            Category diabetes = getCategoryByName("diabetes");
            Category nutrition = getCategoryByName("nutrition");
            Category beauty = getCategoryByName("beauty");
            Category eye = getCategoryByName("eye");
            Category personalCare = getCategoryByName("personal-care");
            Category parenting = getCategoryByName("parenting");
            Category sports = getCategoryByName("sports");
            Category services = getCategoryByName("services");

            if (product.getCategories().contains(homeHealthDevices)) {
                topLevelCategory = homeHealthDevices;
            } else if (product.getCategories().contains(diabetes)) {
                topLevelCategory = diabetes;
            } else if (product.getCategories().contains(nutrition)) {
                topLevelCategory = nutrition;
            } else if (product.getCategories().contains(beauty)) {
                topLevelCategory = beauty;
            } else if (product.getCategories().contains(eye)) {
                topLevelCategory = eye;
            } else if (product.getCategories().contains(personalCare)) {
                topLevelCategory = personalCare;
            } else if (product.getCategories().contains(parenting)) {
                topLevelCategory = parenting;
            } else if (product.getCategories().contains(sports)) {
                topLevelCategory = sports;
            } else if (product.getCategories().contains(services)) {
                topLevelCategory = services;
            }
        }

        return topLevelCategory;
    }

	public List<ProductOptionDto> getFilterOptions(String category){
		return getCategoryDao().getProductOptions(category);
	}

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    
}
