package com.hk.web.action.core.catalog;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.domain.catalog.category.Category;

@UrlBinding("/brands")
@Component
public class ShopByBrandsAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(ShopByBrandsAction.class);
  @Autowired
   CategoryDao categoryDao;
  private List<Category> categories;


  public Resolution pre() {
    categories = categoryDao.getPrimaryCategories();

    return new ForwardResolution("/pages/shopByBrands.jsp");
  }

  public List<Category> getCategories() {
    return categories;
  }


}