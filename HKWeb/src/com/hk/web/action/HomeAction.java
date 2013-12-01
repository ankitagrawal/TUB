package com.hk.web.action;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.category.CategoryImageDao;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;

// @HttpCache(expires=20000)
@Component
public class HomeAction extends BaseAction {
    Category                     category;
    List<CategoryImage>          categoryImages;
    List<PrimaryCategoryHeading> headings;

    /*
     * @Autowired private CategoryService categoryService;
     */
    @Autowired
    UserManager                  userManager;

    /*
     * @Autowired private MenuHelper menuHelper;
     */

    @Autowired
    CategoryImageDao             categoryImageDao;
    @Autowired
    PrimaryCategoryHeadingDao    primaryCategoryHeadingDao;

    public Resolution pre() {

        category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.HOME).getCategory();
        // category = categoryService.getCategoryByName("home");
        categoryImages = categoryImageDao.getCategoryImageByCategoryHome(category);
        headings = primaryCategoryHeadingDao.getHeadingsByCategory(category);
        // getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + (900 * 1000)); // 15 min in
        // future.

        return new ForwardResolution("/pages/home.jsp");
    }

    /*
     * public class HTTPResponseResolution implements Resolution { public void execute(HttpServletRequest request,
     * HttpServletResponse response) throws Exception { RequestDispatcher dispatcher =
     * getContext().getServletContext().getRequestDispatcher("/pages/home.jsp"); response.setDateHeader("Expires",
     * System.currentTimeMillis() + 900L); dispatcher.include(request, response); } }
     */

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public List<PrimaryCategoryHeading> getHeadingsWithRankingSetSortedByRanking() {
        headings = primaryCategoryHeadingDao.getHeadingsWithRankingByCategory(category);
        return headings;
    }
}
