package com.hk.web.action;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
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

//    private static Logger        logger = LoggerFactory.getLogger(HomeAction.class);

    Category                     category;
    List<CategoryImage>          categoryImages;
    List<PrimaryCategoryHeading> headings;

    @Autowired
    private CategoryService      categoryService;
    @Autowired
    UserManager                  userManager;

    @Autowired
    MenuHelper                   menuHelper;

    @Autowired
    CategoryImageDaoImpl         categoryImageDao;
    @Autowired
    PrimaryCategoryHeadingDao    primaryCategoryHeadingDao;

    /*@Value("#{hkEnvProps['" + Keys.Env.hkNoReplyEmail + "']}")
    private String               testProperty;*/

    @Autowired
    TestTxnService testService;
    
    public Resolution pre() {
        menuHelper.postConstruction();
        // IN CASE OF REVERT COMMENT EVERYTHING EXCEPT THE FORWARD RESOLUTION TO HOME.JSP AND ALSO REPLACE THE DYNAMIC
        // HOME.JSP WITH THE HARD CODED ONE
        System.out.println("1");
        category = getCategoryService().getCategoryByName("home");
        System.out.println("2");
        categoryImages = categoryImageDao.getCategoryImageByCategoryHome(category);
        System.out.println("3");
        headings = primaryCategoryHeadingDao.getHeadingsByCategory(category);
        getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + 900L); // 15 min in future.

        // return new HTTPResponseResolution();
        return new ForwardResolution("/pages/home.jsp");
    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

            RequestDispatcher dispatcher = getContext().getServletContext().getRequestDispatcher("/pages/home.jsp");
            //HttpServletResponse httpResponse = (HttpServletResponse) response;
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

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}
