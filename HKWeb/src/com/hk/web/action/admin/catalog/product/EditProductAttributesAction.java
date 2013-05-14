package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.XslParser;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.*;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.helper.MenuHelper;
import com.hk.impl.dao.catalog.category.CategoryDaoImpl;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.combo.ComboService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.util.HKImageUtils;
import com.hk.util.XslGenerator;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: PRATHAM Date: 3/23/12 Time: 11:31 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class EditProductAttributesAction extends BaseAction {

    private static Logger logger = Logger.getLogger(EditProductAttributesAction.class);
    String productId;
    Product product;
    String tin;
    String brand;
    String description;
    String overview;
    List<ProductImage> productImages;
    List<ProductFeature> productFeatures;
    List<Product> relatedProducts;
    String mainImageId;
    List<ProductVariant> productVariants;
    Affiliate affiliate;
    String affid;
    String primaryCategory;
    String secondaryCategory;
    List<String> options;
    List<String> extraOptions;
    Manufacturer manufacturer;
    @Autowired
    MenuHelper menuHelper;
    @Autowired
    AffiliateDao affiliateDao;
    @Autowired
    ComboDao comboDao;
    @Autowired
    MapIndiaDao mapIndiaDao;
    @Autowired
    XslParser xslParser;
    @Autowired
    XslGenerator xslGenerator;
    @Autowired
    SupplierDao supplierDao;
    @Autowired
    CategoryDaoImpl categoryDao;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    ComboService comboService;
    private String categories;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private BaseDao baseDao;
    @Session(key = HealthkartConstants.Cookie.preferredZone)
    private String preferredZone;
    private String productImageId;

    public Resolution editDescription() {
        product = getProductService().getProductById(productId);
        return new ForwardResolution("/pages/editDescription.jsp");
    }

    public Resolution editRelatedProducts() {
        product = getProductService().getProductById(productId);
        return new ForwardResolution("/pages/editRelatedProducts.jsp");
    }

    public Resolution editOverview() {
        product = getProductService().getProductById(productId);
        return new ForwardResolution("/pages/editOverview.jsp");
    }

    public Resolution editFeatures() {
        logger.debug("product id " + product);
        productFeatures = product.getProductFeatures();
        return new ForwardResolution("/pages/editFeatures.jsp");
    }

    public Resolution editProductVariantDetails() {
        logger.debug("product id " + product);
        productVariants = product.getProductVariants();
        return new ForwardResolution("/pages/editProductVariantDetails.jsp");
    }

    public Resolution saveProductDetails() throws Exception {
        logger.debug("saving product attributes for product " + product.getId());
        String productId = product.getId();
        List<Category> categoriesList = xslParser.getCategroyListFromCategoryString(categories);
        product.setCategories(categoriesList);
        if (primaryCategory == null) {
            addRedirectAlertMessage(new SimpleMessage("Primary Category cannot be null"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        if (product.getMaxDays() == null || product.getMinDays() == null) {
            addRedirectAlertMessage(new SimpleMessage("Min/Max cannot be blank"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        if (product.getMinDays() >= product.getMaxDays()) {
            addRedirectAlertMessage(new SimpleMessage("Min cannot be less than max days"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        List<Category> listFromPrimaryCategoryString = xslParser.getCategroyListFromCategoryString(primaryCategory);
        if (listFromPrimaryCategoryString != null && !listFromPrimaryCategoryString.isEmpty()) {
            product.setPrimaryCategory(listFromPrimaryCategoryString.get(0));
        }
        if (secondaryCategory == null) {
            addRedirectAlertMessage(new SimpleMessage("Secondary Category cannot be null"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        Category secondaryCat = categoryDao.getCategoryByName(Category.getNameFromDisplayName(secondaryCategory));
        if (secondaryCat == null) {
            addRedirectAlertMessage(new SimpleMessage("Please enter a valid Category in Secondary Category"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        product.setSecondaryCategory(secondaryCat);

        // To do remove deleted as not null in DB
        if (product.getDeleted() == null) {
            product.setDeleted(Boolean.FALSE);
        }
        if (product.getService() == null) {
            product.setService(Boolean.FALSE);
        }
        if (product.getGoogleAdDisallowed() == null) {
            product.setGoogleAdDisallowed(Boolean.FALSE);
        }

        if (product.getInstallable() == null) {
            product.setInstallable(Boolean.FALSE);
        }

        if (brand == null) {
            addRedirectAlertMessage(new SimpleMessage("Brand cannot be null"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        logger.debug("loading combo ");
        Combo combo = getBaseDao().get(Combo.class, productId);
        logger.debug("got combo ");
        Supplier supplier = supplierDao.findByTIN(tin);
        if (combo == null && supplier == null) {
            addRedirectAlertMessage(new SimpleMessage("Supplier corresponding to given tin does not exist"));
            return new ForwardResolution("/pages/editProductDetails.jsp");
        }
        product.setSupplier(supplier);
        product.setBrand(brand);
        product.setManufacturer(manufacturer);

        logger.debug("actual save call start ");
        product = getProductService().save(product);
        // Checking inventory of all product variants

        if (product.getDeleted()) {
            getProductVariantService().markProductVariantsAsDeleted(product);
        }
        List<ProductVariant> productVariants = product.getProductVariants();

        if (productVariants != null && productVariants.size() > 0) {
	          /*for (ProductVariant productVariant : productVariants) {
                getInventoryService().checkInventoryHealth(productVariant);
            }*/

            getComboService().markProductOutOfStock(productVariants.get(0));
        }

        logger.debug("actual save call  ");
        return new ForwardResolution("/pages/close.jsp");
    }

    public Resolution editProductDetails() {
        logger.debug("product id " + product);
        categories = xslGenerator.getCategories(product);
        return new ForwardResolution("/pages/editProductDetails.jsp");
    }

    public Resolution saveProductVariantDetails() {
        int i = 0;
        if (productVariants != null) {
            for (ProductVariant productVariant : productVariants) {
                logger.debug("variant id " + productVariant.getId());

                if (productVariant.isClearanceSale() == null || !productVariant.isClearanceSale()) {
                    if (productVariant.getCostPrice() != null && productVariant.getCostPrice() > productVariant.getHkPrice(null)) {
                        addRedirectAlertMessage(new SimpleMessage("HK Price of variant " + productVariant.getId() + " is less than Cost Price. Please fix it."));
                        return new RedirectResolution(EditProductAttributesAction.class, "editProductVariantDetails").addParameter("product", product);
                    }
                }

                if (productVariant.getMarkedPrice() != null && productVariant.getMarkedPrice() < productVariant.getHkPrice(null)) {
                    addRedirectAlertMessage(new SimpleMessage("HK Price of variant " + productVariant.getId() + " is more than Marked Price. Please fix it."));
                    return new RedirectResolution(EditProductAttributesAction.class, "editProductVariantDetails").addParameter("product", product);
                }

                String productOpts = "";
                if (options != null && i < options.size() && options.get(i) != null) {
                    productOpts = options.get(i);
                }
                if (productOpts != null && StringUtils.isNotBlank(productOpts)) {
                    List<ProductOption> productOptionList = getProductOptionList(productOpts);
                    if (!productOptionList.isEmpty()) {
                        productVariant.setProductOptions(productOptionList);
                    }
                } else {
                    productVariant.setProductOptions(null);
                }

                String extraOpt = "";
                if (extraOptions != null && i < extraOptions.size() && extraOptions.get(i) != null) {
                    extraOpt = extraOptions.get(i);
                }
                if (extraOpt != null && StringUtils.isNotBlank(extraOpt)) {
                    List<ProductExtraOption> productExtraOptionList = getProductExtraOptionList(extraOpt);
                    if (!productExtraOptionList.isEmpty()) {
                        productVariant.setProductExtraOptions(productExtraOptionList);
                    }
                } else {
                    productVariant.setProductExtraOptions(null);
                }

                productVariant = getProductVariantService().save(productVariant);
                if (!(productVariant.getProduct().isJit() || productVariant.getProduct().isService())) {
                    getInventoryService().checkInventoryHealth(productVariant);
                }
                i++;
            }
        }
        // Setting deleted after reading variants status
        product.setDeleted(isProductDeleted());
        getProductService().save(product);

        return new ForwardResolution("/pages/close.jsp");
    }

    public List<ProductOption> getProductOptionList(String productOptions) {
        List<ProductOption> productOptionList = new ArrayList<ProductOption>();
        String[] productOptionStrings;
        productOptionStrings = StringUtils.split(productOptions, "|");
        for (String productOptionString : productOptionStrings) {
            String productOptionName = productOptionString.substring(0, productOptionString.indexOf(":"));
            String productOptionValue = productOptionString.substring(productOptionString.indexOf(":") + 1);

            // checking whether the product option already exists or not
            productOptionName = StringUtils.strip(productOptionName);
            productOptionValue = StringUtils.strip(productOptionValue);
            ProductOption productOption = getProductService().findProductOptionByNameAndValue(productOptionName, productOptionValue);
            if (productOption == null) {
                productOption = new ProductOption(productOptionName, productOptionValue);
                productOption = (ProductOption) getBaseDao().save(productOption);
            }

            productOptionList.add(productOption);
        }
        return productOptionList;
    }

    public List<ProductExtraOption> getProductExtraOptionList(String extraOptions) {
        List<ProductExtraOption> productExtraOptionList = new ArrayList<ProductExtraOption>();
        String[] productExtraOptionStrings;
        productExtraOptionStrings = StringUtils.split(extraOptions, "|");
        for (String productExtraOptionString : productExtraOptionStrings) {
            String name = productExtraOptionString.substring(0, productExtraOptionString.indexOf(":"));
            String value = productExtraOptionString.substring(productExtraOptionString.indexOf(":") + 1);

            // checking whether the product extra option already exists or not
            ProductExtraOption productExtraOption = getProductService().findProductExtraOptionByNameAndValue(name, value);
            if (productExtraOption == null) {
                productExtraOption = new ProductExtraOption(name, value);
                productExtraOption = (ProductExtraOption) getBaseDao().save(productExtraOption);
            }

            productExtraOptionList.add(productExtraOption);
        }
        return productExtraOptionList;
    }

    public boolean isProductDeleted() {
        boolean isProductDeleted = true;
        for (ProductVariant productVariant : productVariants) {
            isProductDeleted = isProductDeleted && productVariant.isDeleted();
        }
        return isProductDeleted;
    }

    public Resolution saveDescription() {
        logger.debug("productId: " + productId);
        if (productId != null) {
            product = getProductService().getProductById(productId);
            if (description != null) {
                description = description.trim();
            }
            product.setDescription(description);
            product = getProductService().save(product);
        }
        return new ForwardResolution("/pages/close.jsp");
    }

    public Resolution saveOverview() {
        logger.debug("productId: " + productId);
        if (productId != null) {
            product = getProductService().getProductById(productId);
            if (overview != null) {
                overview = overview.trim();
            }
            product.setOverview(overview);
            product = getProductService().save(product);
        }
        return new ForwardResolution("/pages/close.jsp");
    }

    public Resolution saveFeatures() {
        logger.debug("product id " + product);
        if (productFeatures != null) {
            for (ProductFeature productFeature : productFeatures) {
                if (productFeature != null) {
                    if (StringUtils.isNotBlank(productFeature.getName())) {
                        logger.debug("productFeature id " + productFeature.getId());
                        productFeature.setProduct(product);
                        getBaseDao().save(productFeature);
                    } else {
                        if (productFeature.getId() != null) {
                            logger.debug("Empty :  " + productFeature.getId());
                            ProductFeature featureToDelete = getBaseDao().get(ProductFeature.class, productFeature.getId());
                            getBaseDao().delete(featureToDelete);
                        }
                    }
                }
            }
        }
        getProductService().save(product);
        return new ForwardResolution("/pages/close.jsp");
    }

    public Resolution saveRelatedProducts() {
        logger.debug("product id " + product);
        List<Product> relatedProductsList = new ArrayList<Product>();
        if (relatedProducts != null) {
            for (Product relatedProduct : relatedProducts) {
                if (relatedProduct != null && !relatedProduct.isDeleted() && !Boolean.TRUE.equals(relatedProduct.isGoogleAdDisallowed()) && relatedProductsList.size() < 6) {
                    relatedProductsList.add(relatedProduct);
                }
            }
        }
        product.setRelatedProducts(relatedProductsList);
        getProductService().save(product);
        return new ForwardResolution("/pages/close.jsp");
    }

    public Resolution manageProductImages() {
        logger.debug("productId: " + productId);
        product = getProductService().getProductById(productId);
        productImages = product.getProductImages();
        mainImageId = product.getMainImageId() != null ? product.getMainImageId().toString() : "";
        return new ForwardResolution("/pages/manageImages.jsp");
    }

    public Resolution changeProductImage() {
        logger.debug("productId: " + productId);
        product = getProductService().getProductById(productId);
        productImages = product.getProductImages();
        if (productImageId != null) {
            String productImageLink = HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, Long.parseLong(productImageId), isSecureRequest());
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", productImageLink);
            return new JsonResolution(healthkartResponse);
        } else {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "no image set -fail", "");
            return new JsonResolution(healthkartResponse);
        }
    }

    public Resolution editImageSettings() {
        logger.debug("productId: " + productId);
        product = getProductService().getProductById(productId);

        if (productImages != null) {
            for (ProductImage productImage : productImages) {
                logger.debug("productImageId : " + productImage.getId());
                getBaseDao().save(productImage);
            }
        }
        // checking that mainImageId should not be null , otherwise may throw exception.
        if (mainImageId != null)
            product.setMainImageId(Long.parseLong(mainImageId));
        getProductService().save(product);
        return new ForwardResolution("/pages/close.jsp");
    }

    /*
     * public Resolution productBanner() { affiliate = affiliateDao.getAffiliateByCode(affid); return new
     * ForwardResolution("/pages/affiliate/productBanner.jsp"); }
     */

    /*
     * public Resolution getClosestServiceCenters() throws IOException { product =
     * getProductService().getProductById(productId); if (product.isService()) { MapIndia zone =
     * mapIndiaDao.findByCity(preferredZone); List<AddressDistanceDto> addressDistanceDtos =
     * localityMapDao.getClosestAddressList(zone.getLattitude(), zone.getLongitude(), 50D, 10,
     * product.getManufacturer().getAddresses()); } return new ForwardResolution("/pages/product.jsp"); }
     */

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public List<ProductFeature> getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(List<ProductFeature> productFeatures) {
        this.productFeatures = productFeatures;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public String getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(String mainImageId) {
        this.mainImageId = mainImageId;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public String getAffid() {
        return affid;
    }

    public void setAffid(String affid) {
        this.affid = affid;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(List<String> extraOptions) {
        this.extraOptions = extraOptions;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPreferredZone() {
        return preferredZone;
    }

    public void setPreferredZone(String preferredZone) {
        this.preferredZone = preferredZone;
    }

    public String getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(String productImageId) {
        this.productImageId = productImageId;
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

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public ComboService getComboService() {
        return comboService;
    }
}
