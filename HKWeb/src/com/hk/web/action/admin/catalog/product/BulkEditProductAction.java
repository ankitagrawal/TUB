package com.hk.web.action.admin.catalog.product;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.lang.reflect.Type;
import java.util.*;

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class BulkEditProductAction extends BasePaginatedAction {

    private static Logger         logger            = LoggerFactory.getLogger(BulkEditProductAction.class);

    List<Product>                 products          = new ArrayList<Product>();
    String                        category;
    List<String>                  secondaryCategory;
    List<String>                  supplierTin;
    String                        brand;
    // List<String> options;
    // List<String> extraOptions;
    Map<String, Boolean>          toBeEditedOptions = new HashMap<String, Boolean>();
    Object                        toBeEditedOptionsObject;
    private Integer               defaultPerPage    = 20;
    Page                          productPage;

    @Autowired
    ProductDao                    productDao;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private CategoryService       categoryService;
    @Autowired
    CategoryDao                   categoryDao;
    @Autowired
    XslParser                     xslParser;

	@Autowired
    private ComboDao              comboDao;
    private SupplierDao           supplierDao;

    @ValidationMethod(on = "bulkEdit")
    public void validateCategoryAndBrand() {
        if (!StringUtils.isBlank(brand)) {
            if (!productDao.doesBrandExist(brand)) {
                getContext().getValidationErrors().add("1", new SimpleError("Brand not found"));
            }
        }

        if (!StringUtils.isBlank(category)) {
            if (categoryDao.getCategoryByName(category) == null) {
                getContext().getValidationErrors().add("1", new SimpleError("Category not found"));
            }
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        toBeEditedOptions.put("productOrderRanking,", Boolean.FALSE);
        toBeEditedOptions.put("productBrand,", Boolean.FALSE);
        toBeEditedOptions.put("productCategories,", Boolean.FALSE);
        toBeEditedOptions.put("productName,", Boolean.FALSE);
        toBeEditedOptions.put("productSupplierTin,", Boolean.FALSE);
        toBeEditedOptions.put("productColorOptions,", Boolean.FALSE);
        toBeEditedOptions.put("productGoogleAd,", Boolean.FALSE);
        toBeEditedOptions.put("productAmazonProduct,", Boolean.FALSE);
        toBeEditedOptions.put("productService,", Boolean.FALSE);
        toBeEditedOptions.put("productDeleted,", Boolean.FALSE);
        toBeEditedOptions.put("productJit,", Boolean.FALSE);
        toBeEditedOptions.put("productMinDays,", Boolean.FALSE);
        toBeEditedOptions.put("productMaxDays,", Boolean.FALSE);
        toBeEditedOptions.put("productSecondaryCategory,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantUpc,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantMRP,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantHKPrice,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantCostPrice,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantB2BPrice,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantPostpaidAmount,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantDiscount,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantClearanceSale,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantAffiliateCategory,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantOutOfStock,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantDeleted,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantInventory,", Boolean.FALSE);
        // toBeEditedOptions.put("productVariantCutOffInventory,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantWeight,", Boolean.FALSE);
        // toBeEditedOptions.put("productVariantLength,", Boolean.FALSE);
        // toBeEditedOptions.put("productVariantBreadth,", Boolean.FALSE);
        // toBeEditedOptions.put("productVariantHeigth;", Boolean.FALSE);

        toBeEditedOptions.put("productVariantConsumptionTime,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantLeadTime,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantLeadTimeFactor,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantBufferTime,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantNextAvailDate,", Boolean.FALSE);
        toBeEditedOptions.put("productVariantFollAvailDate,", Boolean.FALSE);

        return new ForwardResolution("/pages/bulkProductDetails.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution bulkEdit() {
        if (products.isEmpty()) {
            productPage = productDao.getAllProductsByCategoryAndBrand(category, brand, getPageNo(), getPerPage());
            if (productPage != null)
                products.addAll(productPage.getList());
        }
        // During pagination,the param set again passes the map as a string. So,the values of variables to be displayed
        // cannot be rendered.
        // Therefore, the object again needs to be converted to a map.
        if (getPageNo() > 1) {
            toBeEditedOptions = getMapFromString(toBeEditedOptionsObject.toString());
        } else {
            toBeEditedOptionsObject = new HashMap(toBeEditedOptions);
        }
        return new ForwardResolution("/pages/bulkEditProductDetails.jsp");
    }

    public Resolution save() {
        if (products != null) {
            int ctr = 0;

            // bulkEditProductDetails.jsp converts toBeEditedOptionsObject to a string when it is passed on as a hidden
            // parameter for the bean.
            // So for further use,the string needs to be converted again into a map
            toBeEditedOptions = getMapFromString(toBeEditedOptionsObject.toString());

            for (Product product : products) {

                logger.debug("productId: " + product.getId());

                for (ProductVariant productVariant : product.getProductVariants()) {
                    logger.debug("variant id " + productVariant.getId());

                    if (productVariant.getClearanceSale() == null || productVariant.getClearanceSale().equals(Boolean.FALSE)) {
                        if (productVariant.getCostPrice() != null && productVariant.getCostPrice() > productVariant.getHkPrice(null)) {
                            addRedirectAlertMessage(new SimpleMessage("HK Price of variant " + productVariant.getId() + " is less than Cost Price. Please fix it."));
                            return new ForwardResolution(BulkEditProductAction.class, "bulkEdit");
                        }
                    }

                    if (productVariant.getMarkedPrice() != null && productVariant.getMarkedPrice() < productVariant.getHkPrice(null)) {
                        addRedirectAlertMessage(new SimpleMessage("HK Price of variant " + productVariant.getId() + " is more than Marked Price. Please fix it."));
                        return new ForwardResolution(BulkEditProductAction.class, "bulkEdit");
                    }
                    productVariant = getProductVariantService().save(productVariant);
                }
                Set<Category> newCatList = new HashSet<Category>(0);
                String categoryString = product.getCategoriesPipeSeparated();
                logger.debug("categoryString: " + categoryString);
                List<Category> categoryList = xslParser.getCategroyListFromCategoryString(categoryString);
                for (Category category : categoryList) {
                    category = getCategoryService().save(category);
                    newCatList.add(category);
                }
                // to avoid duplicate primary key in the many to many mapping.. converting to set and then back to
                // list.. eliminating duplicate category names
                product.setCategories(new ArrayList<Category>(newCatList));

                // product.setDeleted(isProductDeleted(product));
                if (product.isDeleted().equals(Boolean.FALSE)) {
                    product.setDeleted(isProductDeleted(product));
                }

                if (secondaryCategory != null) {
                    Category secondaryCat = categoryDao.getCategoryByName(Category.getNameFromDisplayName(secondaryCategory.get(ctr)));
                    if (secondaryCat == null) {
                        addRedirectAlertMessage(new SimpleMessage("Please enter a valid Category in Secondary Category for product: " + product.getId()));
                        return new ForwardResolution(BulkEditProductAction.class, "bulkEdit");
                    }
                    product.setSecondaryCategory(secondaryCat);
                }

                if (supplierTin != null) {
                    Combo combo = comboDao.get(Combo.class, product.getId());
                    Supplier supplier = supplierDao.findByTIN(supplierTin.get(ctr));
                    if (combo == null && supplier == null) {
                        addRedirectAlertMessage(new SimpleMessage("Supplier corresponding to given tin does not exist for product: " + product.getId()));
                        return new ForwardResolution("/pages/bulkEditProductDetails.jsp");
                    }
                    product.setSupplier(supplier);
                }

                product = productDao.save(product);
                ctr++;
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new ForwardResolution(BulkEditProductAction.class, "bulkEdit");

    }

    // public List<ProductOption> getProductOptionList(int i) {
    // List<ProductOption> productOptionList = new ArrayList<ProductOption>();
    // String[] productOptionStrings;
    // productOptionStrings = StringUtils.split(options.get(i), "|");
    // for (String productOptionString : productOptionStrings) {
    // String name = productOptionString.substring(0, productOptionString.indexOf(":"));
    // String value = productOptionString.substring(productOptionString.indexOf(":") + 1);
    //
    // //checking whether the product option already exists or not
    // ProductOption productOption = productOptionDao.findByNameAndValue(name, value);
    // if (productOption == null) {
    // productOption = new ProductOption(name, value);
    // productOption = productOptionDao.save(productOption);
    // }
    //
    // productOptionList.add(productOption);
    // }
    // return productOptionList;
    // }
    //
    // public List<ProductExtraOption> getProductExtraOptionList(int i) {
    // List<ProductExtraOption> productExtraOptionList = new ArrayList<ProductExtraOption>();
    // String[] temp;
    // if (!extraOptions.isEmpty()) {
    // temp = extraOptions.get(i).split(",");
    // for (String productExtraOptionString : temp) {
    // ProductExtraOption productExtraOption = new ProductExtraOption();
    // String name = productExtraOptionString.substring(0, productExtraOptionString.indexOf(":"));
    // String value = productExtraOptionString.substring(productExtraOptionString.indexOf(":") + 1);
    // productExtraOption.setName(name);
    // productExtraOption.setValue(value);
    // productExtraOptionList.add(productExtraOption);
    // }
    // }
    // return productExtraOptionList;
    // }

    public boolean isProductDeleted(Product product) {
        boolean isProductDeleted = true;
        for (ProductVariant productVariant : product.getProductVariants()) {
            isProductDeleted = isProductDeleted && productVariant.isDeleted();
        }
        return isProductDeleted;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(List<String> secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public Map<String, Boolean> getToBeEditedOptions() {
        return toBeEditedOptions;
    }

    public void setToBeEditedOptions(Map<String, Boolean> toBeEditedOptions) {
        this.toBeEditedOptions = toBeEditedOptions;
    }

    public Object getToBeEditedOptionsObject() {
        return toBeEditedOptionsObject;
    }

    public void setToBeEditedOptionsObject(Object toBeEditedOptionsObject) {
        this.toBeEditedOptionsObject = toBeEditedOptionsObject;
    }

    private Map<String, Boolean> getMapFromString(String str) {
        str = str.replace("=", ":");
        Map<String, Boolean> map;

        Type mapType = new TypeToken<Map<String, Boolean>>() {
        }.getType();
        map = new Gson().fromJson(str, mapType);
        return map;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return productPage == null ? 0 : productPage.getTotalPages();
    }

    public int getResultCount() {
        return productPage == null ? 0 : productPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("category");
        params.add("brand");
        params.add("toBeEditedOptions");
        params.add("toBeEditedOptionsObject");
        return params;
    }

    public List<String> getSupplierTin() {
        return supplierTin;
    }

    public void setSupplierTin(List<String> supplierTin) {
        this.supplierTin = supplierTin;
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

    // public List<String> getOptions() {
    // return options;
    // }
    //
    // public void setOptions(List<String> options) {
    // this.options = options;
    // }
    //
    // public List<String> getExtraOptions() {
    // return extraOptions;
    // }
    //
    // public void setExtraOptions(List<String> extraOptions) {
    // this.extraOptions = extraOptions;
    // }
}