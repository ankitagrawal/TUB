package com.hk.web.action.admin.offer;

import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.CREATE_OFFER }, authActionBean = AdminPermissionAction.class)
@Component
public class CreateProductGroupAction extends BaseAction {

    @Validate(required = true, maxlength = 80)
    String                name;

    String                categoryNames;
    String                excludedCategoryNames;
    String                productIds;
    String                productVariantIds;
    String                excludeProductVariantIds;

    Set<Category>         categories;
    Set<Category>         categoriesExcluded;
    Set<Product>          products;
    Set<ProductVariant>   productVariants;
    Set<ProductVariant>   excludeProductVariants;

    @Autowired
    CategoryService       categoryService;
    @Autowired
    ProductService        productService;
    @Autowired
    ProductVariantService productVariantService;

    @ValidationMethod
    public void validateCreate() {
        if (StringUtils.isBlank(categoryNames) && StringUtils.isBlank(productIds) && StringUtils.isBlank(productVariantIds) && StringUtils.isBlank(excludeProductVariantIds)
                && StringUtils.isBlank(excludedCategoryNames)) {
            getContext().getValidationErrors().add("e1",
                    new SimpleError("Please enter atleast one of the following: categoryNames, productIds, productVariantIds, excludeProductVariantIds, excludedCategoryNames"));
        }
        // todo - check if any categories, products or pvs are not found
        if (StringUtils.isNotBlank(categoryNames)) {
            categories = getCategoryService().getCategoriesFromCategoryNames(categoryNames);
        }
        if (StringUtils.isNotBlank(productIds)) {
            products = getProductService().getProductsFromProductIds(productIds);
        }
        if (StringUtils.isNotBlank(productVariantIds)) {
            productVariants = getProductVariantService().getProductVariantsFromProductVariantIds(productVariantIds);
        }
        if (StringUtils.isNotBlank(excludeProductVariantIds)) {
            excludeProductVariants = getProductVariantService().getProductVariantsFromProductVariantIds(excludeProductVariantIds);
        }
        if (StringUtils.isNotBlank(excludedCategoryNames)) {
            categoriesExcluded = getCategoryService().getCategoriesFromCategoryNames(excludedCategoryNames);
        }
    }

    @DontValidate
    @DefaultHandler
    public Resolution pre() {

        return new ForwardResolution("/pages/admin/offer/createProductGroup.jsp");
    }

    public Resolution create() {
        ProductGroup productGroup = new ProductGroup();
        productGroup.setName(name);
        productGroup.setCategories(categories);
        productGroup.setProducts(products);
        productGroup.setProductVariants(productVariants);
        productGroup.setExcludeProductVariants(excludeProductVariants);
        productGroup.setCategoriesExcluded(categoriesExcluded);
        productGroup = (ProductGroup) getBaseDao().save(productGroup);

        addRedirectAlertMessage(new SimpleMessage("Created Product Group [{0}]", name));
        return new RedirectResolution(OfferAdminAction.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public void setExcludedCategoryNames(String excludedCategoryNames) {
        this.excludedCategoryNames = excludedCategoryNames;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getProductVariantIds() {
        return productVariantIds;
    }

    public void setProductVariantIds(String productVariantIds) {
        this.productVariantIds = productVariantIds;
    }

    public String getExcludeProductVariantIds() {
        return excludeProductVariantIds;
    }

    public void setExcludeProductVariantIds(String excludeProductVariantIds) {
        this.excludeProductVariantIds = excludeProductVariantIds;
    }

    public Set<Category> getCategoriesExcluded() {
        return categoriesExcluded;
    }

    public void setCategoriesExcluded(Set<Category> categoriesExcluded) {
        this.categoriesExcluded = categoriesExcluded;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }


}
