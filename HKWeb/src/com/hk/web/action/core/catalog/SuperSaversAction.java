package com.hk.web.action.core.catalog;

import net.sourceforge.stripes.action.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.web.HealthkartResponse;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@UrlBinding("/super-savers")
@Component
public class SuperSaversAction extends BasePaginatedAction {
    List<SuperSaverImage> superSaverImages;
    List<String> categories;
    List<String> brands;
    private Integer defaultPerPage = 10;
    Page superSaverPage;

    @Autowired
    SuperSaverImageService superSaverImageService;

    @DefaultHandler
    public Resolution pre() {
        superSaverPage = superSaverImageService.getSuperSaverImages(categories, brands, Boolean.TRUE, getPageNo(), getPerPage());
        superSaverImages = superSaverPage.getList();

        //superSaverImages = superSaverImageService.getSuperSaverImages(Boolean.TRUE, Boolean.TRUE);
        return new ForwardResolution("/pages/superSavers.jsp");
    }

    public List<SuperSaverImage> getSuperSaverImages() {
        return superSaverImages;
    }

    public Resolution getSuperSaversForCategoryAndBrand() {
        /*superSaverPage = superSaverImageService.getSuperSaverImages(categories, brands, Boolean.TRUE, getPageNo(), getPerPage());
        superSaverImages = superSaverPage.getList();
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK,"super savers found",superSaverImages);
        noCache();
        return new JsonResolution(healthkartResponse);*/
        return pre();
    }

    public void setSuperSaverImages(List<SuperSaverImage> superSaverImages) {
        this.superSaverImages = superSaverImages;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return superSaverPage == null ? 0 : superSaverPage.getTotalPages();
    }

    public int getResultCount() {
        return superSaverPage == null ? 0 : superSaverPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("categories");
        params.add("brands");
        return params;
    }
}
