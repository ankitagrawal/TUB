package com.hk.rest.mobile.service.action;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.helper.MenuHelper;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.category.CategoryImageDao;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.web.HealthkartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 29, 2012
 * Time: 4:33:59 PM
 * To change this template use File | Settings | File Templates.
 */

@Path("/mHome")
@Component

public class MHomeAction {

    Category category;

    List<CategoryImage> categoryImages;
    List<PrimaryCategoryHeading> headings;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    UserManager userManager;

    @Autowired

    MenuHelper menuHelper;

    @Autowired
    CategoryImageDao categoryImageDao;
    @Autowired
   PrimaryCategoryHeadingDao primaryCategoryHeadingDao;

    @GET
    @Path("/gohome/")
    public String home() {
        HealthkartResponse healthkartresponse;
        String jsonBuilder = "";
        String message = "Done";
        String status = HealthkartResponse.STATUS_OK;

        try {
            category = categoryService.getCategoryByName("home");
            //categoryImages = categoryImageDao.getCategoryImageByCategoryHome(category);
            headings = primaryCategoryHeadingDao.getHeadingsByCategory(category);

        } catch (Exception e) {
            message = "No results found.";
            status = HealthkartResponse.STATUS_ERROR;
        }
        healthkartresponse = new HealthkartResponse(status, message, headings);
        jsonBuilder = com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(healthkartresponse);
        return jsonBuilder;

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

    @GET
    @Path("/createtest")
    @Produces("application/json")
    public String createOrderInHK() {
        if (true) {
            String response = menuHelper.getMenuNodes().toString();
            return "{'response':'" + response + "'}";
        } else {
            return "{'error':'invalid json'}";
        }
    }
}