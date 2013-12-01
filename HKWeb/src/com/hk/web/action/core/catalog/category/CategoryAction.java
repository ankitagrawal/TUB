package com.hk.web.action.core.catalog.category;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.Resolution;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.domain.content.SeoData;
import com.hk.impl.dao.catalog.category.CategoryDaoImpl;
import com.hk.impl.dao.catalog.category.CategoryImageDaoImpl;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;
import com.hk.util.SeoManager;
import com.hk.web.action.core.catalog.product.ProductAction;

@HttpCache(expires = 10000)
@Component
public class CategoryAction extends BaseAction {
    private static Logger        logger = Logger.getLogger(ProductAction.class);

    Category                     category;
    List<PrimaryCategoryHeading> headings;
    SeoData                      seoData;
    List<CategoryImage>          categoryImages;
    @Autowired
    PrimaryCategoryHeadingDao    primaryCategoryHeadingDao;
    @Autowired
    ProductDao                   productDao;
    @Autowired
    CategoryDaoImpl                  categoryDao;
    @Autowired
    SeoManager                   seoManager;
    @Autowired
    CategoryImageDaoImpl             categoryImageDao;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
      
        headings = primaryCategoryHeadingDao.getHeadingsByCategory(category);
        String categoryName = "default";
        if (category != null) {
            categoryName = category.getName();
        }
        seoData = seoManager.generateSeo("/" + categoryName);
        categoryImages = categoryImageDao.getCategoryImageByCategoryHome(category);
        return new ForwardResolution("/pages/category/category.jsp");
    }

    public Resolution editPrimaryCategoryHeadings() {
        logger.debug("headings to be edited for category: " + category.getName());
        headings = primaryCategoryHeadingDao.getHeadingsByCategory(category);
        return new ForwardResolution("/pages/editPrimaryCategoryHeadings.jsp").addParameter("category", category.getName());
    }

    public SeoData getSeoData() {
        return seoData;
    }

    public void setSeoData(SeoData seoData) {
        this.seoData = seoData;
    }

    public List<CategoryImage> getCategoryImages() {
        return categoryImages;
    }

    public void setCategoryImages(List<CategoryImage> categoryImages) {
        this.categoryImages = categoryImages;
    }

    public List<PrimaryCategoryHeading> getHeadings() {
        return headings;
    }

    public void setHeadings(List<PrimaryCategoryHeading> headings) {
        this.headings = headings;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<PrimaryCategoryHeading> getHeadingsWithRankingSetSortedByRanking() {
        headings = primaryCategoryHeadingDao.getHeadingsWithRankingByCategory(category);
        return headings;
    }

    public List<PrimaryCategoryHeading> getHeadingsSortedByRanking() {
        headings = primaryCategoryHeadingDao.getHeadingsOrderedByRankingByCategory(category);
        return headings;
    }
}
