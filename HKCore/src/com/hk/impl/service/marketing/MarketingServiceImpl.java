package com.hk.impl.service.marketing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.service.marketing.MarketingService;

@Service
public class MarketingServiceImpl implements MarketingService {

    @Autowired
    private CategoryDao categoryDao;

    public List<Category> marketExpenseCategoriesList() {
        List<Category> marketExpenseList = new ArrayList<Category>();
        String categoryNames = CategoryConstants.HK_BRAND + "," + CategoryConstants.DIABETES + "," + CategoryConstants.BEAUTY + "," + CategoryConstants.EYE + ","
                + CategoryConstants.HEALTH_DEVICES + "," + CategoryConstants.NUTRITION + "," + CategoryConstants.BABY + "," + CategoryConstants.PERSONAL_CARE + ","
                + CategoryConstants.SERVICES + "," + CategoryConstants.SPORTS + "," +CategoryConstants.DYNAMIC_REMARKETING + "," +CategoryConstants.HOME_LIVING;

        Set<Category> categorySet = new HashSet<Category>();

        for (String categoryName : categoryNames.split(",")) {
            categorySet.add(CategoryCache.getInstance().getCategoryByName(categoryName).getCategory());
        }
        //Set<Category> categorySet = categoryDao.getCategoriesFromCategoryNames(categoryNames);
        for (Category category : categorySet) {
            marketExpenseList.add(category);
        }
        return marketExpenseList;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
}
