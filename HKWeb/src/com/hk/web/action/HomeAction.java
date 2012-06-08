package com.hk.web.action;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.pact.dao.catalog.category.CategoryDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.FbConstants;
import com.hk.constants.marketing.AnalyticsConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.helper.MenuHelper;
import com.hk.impl.dao.catalog.category.CategoryImageDaoImpl;
import com.hk.manager.UserManager;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;
import com.hk.pact.service.TestTxnService;
import com.hk.pact.service.catalog.CategoryService;

// @HttpCache(expires=20000)
@Component
public class HomeAction extends BaseAction {

    Category                     category;
    List<PrimaryCategoryHeading> headings;

    @Autowired
    PrimaryCategoryHeadingDao    primaryCategoryHeadingDao;

    @Autowired
    CategoryDao categoryDao;

    public Resolution pre() {
        category = categoryDao.getCategoryByName("home");
        headings = primaryCategoryHeadingDao.getHeadingsByCategory(category);
        getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + 900L); // 15 min in future.

        return new ForwardResolution("/pages/home.jsp");
    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

            RequestDispatcher dispatcher = getContext().getServletContext().getRequestDispatcher("/pages/home.jsp");
            response.setDateHeader("Expires", System.currentTimeMillis() + 900L);
            dispatcher.include(request, response);

        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
