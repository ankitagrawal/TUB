package com.hk.web.action;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@HttpCache(expires=20000)
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
        getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + (900*1000)); // 15 min in future.

        return new ForwardResolution("/pages/home.jsp");
    }

    /*public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

            RequestDispatcher dispatcher = getContext().getServletContext().getRequestDispatcher("/pages/home.jsp");
            response.setDateHeader("Expires", System.currentTimeMillis() + 900L);
            dispatcher.include(request, response);

        }
    }
*/
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
