package com.hk.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.cache.vo.CategoryVO;
import com.hk.domain.catalog.category.Category;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
public class CategoryCache {
    
    private Logger logger = LoggerFactory.getLogger(CategoryCache.class);

    private static CategoryCache    _instance           = new CategoryCache();
    private CategoryCache           _transient;

    private Map<String, CategoryVO> nameToCategoryCache = new HashMap<String, CategoryVO>();

    private CategoryService         categoryService;

    private CategoryCache() {
    }

    public static CategoryCache getInstance() {
        return _instance;
    }

    public void addCategory(CategoryVO categoryVO) {
        if (StringUtils.isNotBlank(categoryVO.getName())) {
            nameToCategoryCache.put(categoryVO.getName(), categoryVO);
        }

    }

    public CategoryVO getCategoryByName(String categoryName) {
        CategoryVO categoryVO = nameToCategoryCache.get(categoryName);

        /**
         * if category is not in cache try and attempt to find from db
         */
        if (categoryVO == null) {
            //logger.warn("Category with name" + categoryName + " not found in cache, so hitting db" );
            Category category = getCategoryService().getCategoryByName(categoryName);
            //logger.warn("Category with name" + categoryName + " from db" + category );
            categoryVO = new CategoryVO(category);
        }
        return categoryVO;
    }

    /*public List<String> getBrandsInCategory(String categoryName) {
        List<String> brandsInCategory = new ArrayList<String>();
        
        CategoryVO categoryVO = getCategoryByName(categoryName);
        if(categoryVO !=null){
            brandsInCategory.addAll(categoryVO.getBrands());
        }

        return brandsInCategory;
    }
    
    
    public List<String> getBrandsByCategory(List<String> categoryNames){
        List<String> brandsByCategory = new ArrayList<String>();
        
        for(String categoryName : categoryNames){
            brandsByCategory.addAll(getBrandsInCategory(categoryName));
        }
        
        return brandsByCategory;
    }*/

    public void freeze() {
        _instance = this;
    }

    public void reset() {
        _transient = new CategoryCache();
    }

    public CategoryCache getTransientCache() {
        return _transient;
    }

    public CategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = (CategoryService) ServiceLocatorFactory.getService(CategoryService.class);
        }
        return categoryService;
    }
}
