package com.hk.web.action.admin.marketing;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.helper.MenuHelper;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.report.dto.marketing.AdsProductMetaDataDto;

@Secure
@Component
public class AdsProductMetaDataAction extends BaseAction {

    @Validate(required = true, on = "reportCategory")
    Category                    category;

    @Validate(required = true, on = "reportBrand")
    String                      brand;

    List<AdsProductMetaDataDto> adsProductMetaDataDtos;
    @Autowired
    MenuHelper                  menuHelper;
    @Autowired
    ProductDao                  productDao;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {

        return new ForwardResolution("/pages/admin/adsProductMetaDataSelectCategory.jsp");
    }

    public Resolution reportCategory() {
        adsProductMetaDataDtos = new ArrayList<AdsProductMetaDataDto>();
        for (Product product : category.getProducts()) {
            AdsProductMetaDataDto adsProductMetaDataDto = new AdsProductMetaDataDto();
            adsProductMetaDataDto.setProduct(product);
            adsProductMetaDataDto.setMenuNode(menuHelper.getMenoNodeFromProduct(product));
            adsProductMetaDataDtos.add(adsProductMetaDataDto);
        }

        return new ForwardResolution("/pages/admin/adsProductMetaDataReport.jsp");
    }

    public Resolution reportBrand() {
        adsProductMetaDataDtos = new ArrayList<AdsProductMetaDataDto>();
        List<Product> products = productDao.getAllProductByBrand(brand);

        for (Product product : products) {
            AdsProductMetaDataDto adsProductMetaDataDto = new AdsProductMetaDataDto();
            adsProductMetaDataDto.setProduct(product);
            adsProductMetaDataDto.setMenuNode(menuHelper.getMenoNodeFromProduct(product));
            adsProductMetaDataDtos.add(adsProductMetaDataDto);
        }

        return new ForwardResolution("/pages/admin/adsProductMetaDataReport.jsp");
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<AdsProductMetaDataDto> getAdsProductMetaDataDtos() {
        return adsProductMetaDataDtos;
    }

    public void setAdsProductMetaDataDtos(List<AdsProductMetaDataDto> adsProductMetaDataDtos) {
        this.adsProductMetaDataDtos = adsProductMetaDataDtos;
    }
}
