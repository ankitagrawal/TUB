package com.hk.impl.service.marketing;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.pact.service.marketing.MarketingService;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.domain.catalog.category.Category;
import com.hk.constants.catalog.category.CategoryConstants;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Service
public class MarketingServiceImpl implements MarketingService {

  @Autowired
  private CategoryDao categoryDao;

  public List<Category> marketExpenseCategoriesList() {
    List<Category> marketExpenseList = new ArrayList<Category>();
    String categoryNames = CategoryConstants.HK_BRAND + "," + CategoryConstants.DIABETES + "," +
        CategoryConstants.BEAUTY + "," + CategoryConstants.EYE + "," + CategoryConstants.HOME_DEVICES + "," +
        CategoryConstants.NUTRITION + "," + CategoryConstants.BABY + "," + CategoryConstants.PERSONAL_CARE + "," +
        CategoryConstants.SERVICES + "," + CategoryConstants.SPORTS;
    Set<Category> categorySet = categoryDao.getCategoriesFromCategoryNames(categoryNames);
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
