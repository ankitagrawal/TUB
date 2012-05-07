package com.hk.web.action.core.content.seo;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.report.dto.content.seo.SeoProductDto;
import com.hk.util.SeoManager;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = PermissionConstants.UPDATE_SEO_METADATA, authActionBean = AdminPermissionAction.class)
@Component
public class BulkSeoAction extends BaseAction {

    private static Logger       logger = Logger.getLogger(BulkSeoAction.class);

    private List<SeoProductDto> seoProductDtoList;

    @Validate(required = true)
    private String              category;
    @Autowired
    SeoManager                  seoManager;


    @Autowired
    private ProductService      productService;
    @Autowired
    private CategoryService     categoryService;
    

    @ValidationMethod
    public void validateCategory() {
        if (getCategoryService().getCategoriesFromCategoryNames(category) == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Category not found"));
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/bulkSeo.jsp");
    }

    public Resolution edit() {
        seoProductDtoList = new ArrayList<SeoProductDto>();
        List<Product> products = getProductService().getProductByCategory(category);
        for (Product product : products) {
            SeoData seoData = seoManager.generateSeo(product.getId());
            SeoProductDto seoProductDto = new SeoProductDto();
            seoProductDto.setProduct(product);
            seoProductDto.setSeoData(seoData);
            seoProductDtoList.add(seoProductDto);
        }
        return new ForwardResolution("/pages/bulkEditSeo.jsp");
    }

    public Resolution save() {
        if (seoProductDtoList != null) {
            for (SeoProductDto seoProductDto : seoProductDtoList) {
                logger.debug("product id : " + seoProductDto.getProduct().getId());
                getBaseDao().save(seoProductDto.getSeoData());
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes Saved."));
        return new ForwardResolution("/pages/bulkEditSeo.jsp");
    }

    public List<SeoProductDto> getSeoProductDtoList() {
        return seoProductDtoList;
    }

    public void setSeoProductDtoList(List<SeoProductDto> seoProductDtoList) {
        this.seoProductDtoList = seoProductDtoList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        
}

    
}
