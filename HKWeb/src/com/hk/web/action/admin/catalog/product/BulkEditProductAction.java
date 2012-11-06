package com.hk.web.action.admin.catalog.product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

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
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_CATALOG}, authActionBean = AdminPermissionAction.class)
@Component
public class BulkEditProductAction extends BasePaginatedAction {

    private static Logger logger = LoggerFactory.getLogger(BulkEditProductAction.class);

    List<Product> products = new ArrayList<Product>();
    String category;
    List<String> secondaryCategory;
    List<String> supplierTin;
    String brand;
    String productVariantId = "";
    // List<String> options;
    // List<String> extraOptions;
    Map<String, Boolean> toBeEditedOptions = new HashMap<String, Boolean>();
    Object toBeEditedOptionsObject;
    private Integer defaultPerPage = 20;
    Page productPage;

    @Autowired
    ProductDao productDao;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    XslParser xslParser;

    @Autowired
    private ComboDao comboDao;
    @Autowired
    private SupplierDao supplierDao;

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
        return new ForwardResolution("/pages/bulkProductDetails.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution defineOptionsMap() {
        if (getContext().getRequest().getParameter("toBeEditedOptions") != null) {
            String[] options = getContext().getRequest().getParameterValues("toBeEditedOptions");
            for (String option : options) {
                toBeEditedOptions.put(option, Boolean.TRUE);
            }
        }
        toBeEditedOptionsObject = new HashMap(toBeEditedOptions);
        return new ForwardResolution(BulkEditProductAction.class, "bulkEdit");
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
        toBeEditedOptions = getMapFromString(toBeEditedOptionsObject.toString());
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
                Combo combo = comboDao.get(Combo.class, product.getId());
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

                if (product.isDeleted().equals(Boolean.FALSE)) {
                    if (combo == null) {
                        product.setDeleted(isProductDeleted(product));
                    } else {
                        product.setDeleted(isComboDeleted(combo));
                    }
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

    @SuppressWarnings("unchecked")
    public Resolution getPVDetails() {
        Map dataMap = new HashMap();
        if (StringUtils.isNotBlank(productVariantId)) {
            ProductVariant pv = getProductVariantService().getVariantById(productVariantId);
            if (pv != null) {
                try {
                    dataMap.put("variant", pv);
                    dataMap.put("product", pv.getProduct().getName());
                    dataMap.put("options", pv.getOptionsCommaSeparated());
                    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant", dataMap);
                    noCache();
                    return new JsonResolution(healthkartResponse);
                } catch (Exception e) {
                    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), dataMap);
                    noCache();
                    return new JsonResolution(healthkartResponse);
                }
            }
        } else {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product VariantID field is empty", null);
            noCache();
            return new JsonResolution(healthkartResponse);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID", null);
        noCache();
        return new JsonResolution(healthkartResponse);
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

    public Boolean isComboDeleted(Combo combo) {
        Boolean isComboDeleted = Boolean.TRUE;
        for (ComboProduct comboProduct : combo.getComboProducts()) {
            isComboDeleted = isComboDeleted && comboProduct.getProduct().isDeleted();
        }
        return isComboDeleted;
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

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
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