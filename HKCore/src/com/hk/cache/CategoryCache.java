package com.hk.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hk.cache.vo.CategoryVO;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
public class CategoryCache {

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
            // User user = getUserService().get
        }
        return categoryVO;
    }

    public List<String> getBrandsInCategory(String categoryName) {
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
    }

    public void freeze() {
        _instance = this;
    }

    public void reset() {
        _transient = new CategoryCache();
    }

    public CategoryCache getTransientCache() {
        return _transient;
    }

    public CategoryService getUserService() {
        if (categoryService == null) {
            categoryService = (CategoryService) ServiceLocatorFactory.getService(CategoryService.class);
        }
        return categoryService;
    }
}
