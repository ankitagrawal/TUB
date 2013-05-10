package com.hk.web.action.admin.catalog.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.cache.CategoryCache;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.core.ManufacturerDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class CreateOrSelectProductAction extends BaseAction {

    // private String productId;
    @ValidateNestedProperties( { @Validate(field = "id", required = true, on = "create"), @Validate(field = "name", required = true, on = "create") })
    private Product               product;

    @ValidateNestedProperties( {
            @Validate(field = "id", required = true, on = "createVariant"),
            @Validate(field = "markedPrice", required = true, on = "createVariant"),
            @Validate(field = "discountPercent", required = true, on = "createVariant"),
            @Validate(field = "shippingBasePrice", required = true, on = "createVariant"),
            @Validate(field = "shippingBaseQty", required = true, on = "createVariant"),
            @Validate(field = "shippingAddPrice", required = true, on = "createVariant"),
            @Validate(field = "shippingAddQty", required = true, on = "createVariant"),
            @Validate(field = "tax", required = true, on = "createVariant") })
    private ProductVariant        productVariant;
    private Manufacturer          manufacturer;
    private String                categories;
    String                        options;
    String                        extraOptions;
    String                        secondaryCategory;
    // private List<Category>categories;
    private static Logger         logger = Logger.getLogger(CreateOrSelectProductAction.class);
    @Autowired
    private ProductService        productService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private CategoryService       categoryService;
    @Autowired
    private ManufacturerDao       manufacturerDao;

    @ValidationMethod(on = "select")
    public void validateSelectedProduct() {
        if (product == null)
            getContext().getValidationErrors().add("1", new SimpleError("product not found"));
    }

    @ValidationMethod(on = "create")
    public void validateCreatedProduct() {
        if (getProductService().getProductById(product.getId()) != null) {
            getContext().getValidationErrors().add("2", new SimpleError("product already exists."));
        }
    }

    @ValidationMethod(on = "createVariant")
    public void validateCreatedProductVariant() {
        if (getProductVariantService().getVariantById(productVariant.getId()) != null) {
            getContext().getValidationErrors().add("2", new SimpleError("product variant already exists."));
        }
    }

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/createOrSelectProduct.jsp");
    }

    public Resolution select() {

        logger.debug("productId " + product.getId());
        logger.debug("productId " + product.getName());
        return new ForwardResolution("/pages/admin/createProductVariant.jsp");
    }

    public Resolution create() {
        logger.debug("productId " + product.getId());
        logger.debug("productId " + product.getName());
        product.setThumbUrl("no-image.jpg");
        product.setDeleted(false);
        if (product.getManufacturer() == null) {
            if (manufacturer != null) {
                if (manufacturerDao.findByName(manufacturer.getName()) == null) {
                    manufacturerDao.save(manufacturer);
                }
                product.setManufacturer(manufacturerDao.findByName(manufacturer.getName()));
            }
        }
        if (categories != null) {
            product.setCategories((getCategoryList()));
        }
        product.setCreateDate(new Date());
        if (secondaryCategory == null) {
            addRedirectAlertMessage(new SimpleMessage("Secondary Category cannot be null"));
            return new ForwardResolution("/pages/admin/createOrSelectProduct.jsp");
        }
        //Category secondaryCat = getCategoryService().getCategoryByName(Category.getNameFromDisplayName(secondaryCategory));
        Category secondaryCat = CategoryCache.getInstance().getCategoryByName(Category.getNameFromDisplayName(secondaryCategory)).getCategory();
        
        if (secondaryCat == null) {
            addRedirectAlertMessage(new SimpleMessage("Please enter a valid Category in Secondary Category"));
            return new ForwardResolution("/pages/admin/createOrSelectProduct.jsp");
        }
        product.setSecondaryCategory(secondaryCat);
        getProductService().save(product);
        addRedirectAlertMessage(new SimpleMessage("new product created."));
        return new ForwardResolution("/pages/admin/createProductVariant.jsp");
    }

    public Resolution createVariant() {
        if (getProductVariantService().getVariantById(productVariant.getId()) != null) {
            addValidationError("5", new SimpleError("The product variant already exists. "));
            return new ForwardResolution("/pages/admin/createProductVariant.jsp");
        }
        productVariant.setProduct(product);
        if (options != null)
            productVariant.setProductOptions(getOptionList());
        if (extraOptions != null)
            productVariant.setProductExtraOptions(getExtraOptionList());
        getProductVariantService().save(productVariant);

        addRedirectAlertMessage(new SimpleMessage("new product variant created, Create Another Variant?"));
        return new RedirectResolution(CreateOrSelectProductAction.class).addParameter("product", product.getId());
    }

    private List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<Category>();
        String[] temp;
        temp = categories.split(",");
        for (String displayName : temp) {
            Category category;
            String name = getNameFromDisplayName(displayName);
            // String displayName = catStr.substring(catStr.indexOf(":") + 1);
            //category = getCategoryService().getCategoryByName(name);
            category = CategoryCache.getInstance().getCategoryByName(name).getCategory();
            
            if (category == null) {
                category = new Category();
                category.setName(name);
                category.setDisplayName(displayName);
            }
            getCategoryService().save(category);
            categoryList.add(category);
        }
        return categoryList;
    }

    private List<ProductOption> getOptionList() {
        List<ProductOption> optionList = new ArrayList<ProductOption>();
        String[] temp;
        temp = options.split("\\|");
        for (String optStr : temp) {
//            ProductOption productOption = new ProductOption();
            String name = optStr.substring(0, optStr.indexOf(":"));
            String value = optStr.substring(optStr.indexOf(":") + 1);
            ProductOption productOption = getProductService().findProductOptionByNameAndValue(name, value);
            if(productOption == null){
                productOption = new ProductOption(name,value);
                productOption = (ProductOption) getBaseDao().save(productOption);
            }
            optionList.add(productOption);
        }
        return optionList;
    }

    private List<ProductExtraOption> getExtraOptionList() {
        List<ProductExtraOption> extraOptionList = new ArrayList<ProductExtraOption>();
        String[] temp;
        temp = extraOptions.split("\\|");
        for (String exOptStr : temp) {
            ProductExtraOption productExtraOption = new ProductExtraOption();
            String name = exOptStr.substring(0, exOptStr.indexOf(":"));
            String value = exOptStr.substring(exOptStr.indexOf(":") + 1);
            productExtraOption.setName(name);
            productExtraOption.setValue(value);
            extraOptionList.add(productExtraOption);
        }
        return extraOptionList;
    }

    private static String getNameFromDisplayName(String name) {
        name = name.trim().replaceAll("[&,/\\\\)\\\\(]", " ").replaceAll("[ ]+", " ").replaceAll(" ", "-").toLowerCase();
        return name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(String extraOptions) {
        this.extraOptions = extraOptions;
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
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

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ManufacturerDao getManufacturerDao() {
        return manufacturerDao;
    }

    public void setManufacturerDao(ManufacturerDao manufacturerDao) {
        this.manufacturerDao = manufacturerDao;
    }

}
