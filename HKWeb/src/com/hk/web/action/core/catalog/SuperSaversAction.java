package com.hk.web.action.core.catalog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.pact.service.catalog.ProductService;
import com.hk.domain.catalog.product.Product;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;

@UrlBinding("/super-savers")
@Component
public class SuperSaversAction extends BasePaginatedAction {
    List<SuperSaverImage> superSaverImages = new ArrayList<SuperSaverImage>();
    List<String> categories;
    List<String> brands;
    private Integer defaultPerPage = 10;
    Page superSaverPage;

    @Autowired
    private SuperSaverImageService superSaverImageService;

    @Autowired
    private ProductService productService;

    @DefaultHandler
    public Resolution pre() {
      superSaverPage = getSuperSaverImageService().getSuperSaverImages(categories, brands, Boolean.TRUE, Boolean.FALSE, getPageNo(), getPerPage());
      List<SuperSaverImage> superSaverImageList = superSaverPage.getList();
      for(SuperSaverImage superSaverImage : superSaverImageList){
        String productId = superSaverImage.getProduct().getId();
        Product product = getProductService().getProductById(productId);
        if(!product.getHidden() && !product.getDeleted()){
          superSaverImages.add(superSaverImage);
        }
      }
      return new ForwardResolution("/pages/superSavers.jsp");
    }

    public List<SuperSaverImage> getSuperSaverImages() {
        return superSaverImages;
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

    public SuperSaverImageService getSuperSaverImageService() {
        return superSaverImageService;
    }

  public ProductService getProductService() {
    return productService;
  }
}
