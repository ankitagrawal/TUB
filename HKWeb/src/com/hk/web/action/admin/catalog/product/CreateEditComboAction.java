package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.report.dto.catalog.ComboProductAndAllowedVariantsDto;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_CATALOG}, authActionBean = AdminPermissionAction.class)
@Component
public class CreateEditComboAction extends BaseAction {

    private static Logger logger = Logger.getLogger(CreateEditComboAction.class);

    @Autowired
    private ComboDao comboDao;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private ProductService productService;
    @Autowired
    private XslParser xslParser;
    @Autowired
    private CategoryService categoryService;


    private Combo combo;
    private ComboProduct comboProduct;

    @Validate(required = true)
    private String categories;

    @Validate(required = true)
    private String primaryCategory;

    private String productId;

    private List<ComboProductAndAllowedVariantsDto> comboProductAndAllowedVariantsDtoList = new ArrayList<ComboProductAndAllowedVariantsDto>();

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        if (combo != null) {
            for (ComboProduct comboProduct : combo.getComboProducts()) {
                ComboProductAndAllowedVariantsDto comboProductAndAllowedVariantsDto = new ComboProductAndAllowedVariantsDto();
                comboProductAndAllowedVariantsDto.setId(comboProduct.getId());
                comboProductAndAllowedVariantsDto.setProduct(comboProduct.getProduct());
                comboProductAndAllowedVariantsDto.setQty(comboProduct.getQty());
                String allowedVariants = "";
                for (ProductVariant productVariant : comboProduct.getAllowedProductVariants()) {
                    allowedVariants += productVariant.getId() + ",";
                }
                comboProductAndAllowedVariantsDto.setAllowedVariants(allowedVariants);
                comboProductAndAllowedVariantsDtoList.add(comboProductAndAllowedVariantsDto);
            }
            categories = "";
            for (Category cat : combo.getCategories()) {
                categories += cat + ">";
            }
        }
        return new ForwardResolution("/pages/admin/createEditCombo.jsp");
    }

    @ValidationMethod(on = "saveCombo")
    public void validateSaveCombo() {
        if (StringUtils.isBlank(categories) || StringUtils.isBlank(primaryCategory) || StringUtils.isBlank(combo.getId()) || StringUtils.isBlank(combo.getName())
                || combo.getDiscountPercent() == null) {
            addValidationError("* marked fields are manadatory", new SimpleError("* marked fields are manadatory"));
        }
        if (comboProductAndAllowedVariantsDtoList == null || comboProductAndAllowedVariantsDtoList.size() == 0) {
            addValidationError("Combo products cannot be empty", new SimpleError("Combo products cannot be empty"));
        } else if (comboProductAndAllowedVariantsDtoList.size() > 0) {
            for (ComboProductAndAllowedVariantsDto comboProductAndAllowedVariantsDto : comboProductAndAllowedVariantsDtoList) {
                if (comboProductAndAllowedVariantsDto.getProduct() == null) {
                    addValidationError("Combo product ID cannot be empty", new SimpleError("Combo product ID cannot be empty"));
                }
                if (comboProductAndAllowedVariantsDto.getQty() == null) {
                    addValidationError("Combo product qty cannot be empty", new SimpleError("Combo product qty cannot be empty"));
                }
              if(comboProductAndAllowedVariantsDto.getAllowedVariants()==null || comboProductAndAllowedVariantsDto.getAllowedVariants().trim().length()==0){
                addValidationError("Combo product allowed variants can't be empty, please enter atleast one allowed variant", new SimpleError("Combo product allowed variants can't be empty, please enter atleast one allowed variant"));
              }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @DontValidate
    public Resolution getProductDetails() {
        Map dataMap = new HashMap();
        Product p = getProductService().getProductById(productId);
        if (p != null) {
            dataMap.put("product", p.getName());
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product", dataMap);
            noCache();
            return new JsonResolution(healthkartResponse);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid ProductID", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }

    public Resolution saveCombo() {
        logger.debug("combo: " + combo.getName());
        List<Category> listFromPrimaryCategoryString = xslParser.getCategroyListFromCategoryString(primaryCategory);
        if (listFromPrimaryCategoryString != null && !listFromPrimaryCategoryString.isEmpty()) {
            combo.setPrimaryCategory(listFromPrimaryCategoryString.get(0));
        }
        if (StringUtils.isBlank(combo.getBrand())) {
            combo.setBrand("Combo");
        }
      if (combo.getOrderRanking() == null || combo.getOrderRanking().equals(0.0D)) {
            combo.setOrderRanking(100000.0D);
        }
        Double comboMrp = 0.0;
        Set<Product> comboProducts = new HashSet<Product>();
        for (ComboProductAndAllowedVariantsDto comboProductAndAllowedVariantsDto : comboProductAndAllowedVariantsDtoList) {
            comboProducts.add(comboProductAndAllowedVariantsDto.getProduct());
            String allowedVariants = comboProductAndAllowedVariantsDto.getAllowedVariants();
            if (StringUtils.isNotBlank(allowedVariants)) {
                String[] pvArr = allowedVariants.split(",");
                if (pvArr.length == comboProductAndAllowedVariantsDto.getQty()) {
                    for (String pvId : pvArr) {
                        ProductVariant productVariant = getProductVariantService().getVariantById(pvId.trim());
                        comboMrp += productVariant.getMarkedPrice();
                    }
                } else {
                    comboMrp += getProductVariantService().getVariantById(pvArr[0].trim()).getMarkedPrice() * comboProductAndAllowedVariantsDto.getQty();
                }
            } else {
                comboMrp += comboProductAndAllowedVariantsDto.getProduct().getProductVariants().get(0).getMarkedPrice() * comboProductAndAllowedVariantsDto.getQty();
            }
        }

        combo.setMarkedPrice(comboMrp);
        combo.setHkPrice(comboMrp * (1.0 - combo.getDiscountPercent()));

        combo.setShippingBaseQty(1L);
        combo.setShippingBasePrice(0D);
        combo.setShippingAddQty(1L);
        combo.setShippingAddPrice(0D);
        combo.setOutOfStock(false);

        combo.setThumbUrl("");
        if (combo.getDeleted() == null) {
            combo.setDeleted(Boolean.FALSE);
        }
	    if (combo.getHidden() == null) {
		    combo.setHidden(Boolean.FALSE);
	    }
//        if (combo.getCodAllowed() == null) {
//            combo.setCodAllowed(Boolean.FALSE);
//        }

        boolean isCodAllowed = isCodAllowedForCombo(new ArrayList<Product>(comboProducts));
        combo.setCodAllowed(isCodAllowed);

        combo = (Combo) getComboDao().save(combo);

        List<Category> catList = getXslParser().getCategroyListFromCategoryString(categories);
        for (Category cat : catList) {
            cat = getCategoryService().save(cat);
        }
        combo.setCategories(catList);
        combo = (Combo) getComboDao().save(combo);

        for (ComboProductAndAllowedVariantsDto comboProductAndAllowedVariantsDto : comboProductAndAllowedVariantsDtoList) {
            ComboProduct comboProduct = comboDao.getComboProduct(comboProductAndAllowedVariantsDto.getProduct(), combo);
            if (comboProduct == null) {
                comboProduct = new ComboProduct();
                comboProduct.setCombo(combo);
            }
            comboProduct.setProduct(comboProductAndAllowedVariantsDto.getProduct());
            comboProduct.setQty(comboProductAndAllowedVariantsDto.getQty());

            List<ProductVariant> allowedProductVariants = new ArrayList<ProductVariant>();
            String allowedVariants = comboProductAndAllowedVariantsDto.getAllowedVariants();
            if (StringUtils.isNotBlank(allowedVariants)) {
                String[] pvArr = allowedVariants.split(",");
                for (String pvId : pvArr) {
                    ProductVariant productVariant = getProductVariantService().getVariantById(pvId.trim());
                    allowedProductVariants.add(productVariant);
                }
                comboProduct.setAllowedProductVariants(allowedProductVariants);
            }
            getComboDao().save(comboProduct);
        }

        addRedirectAlertMessage(new SimpleMessage("Combo - {0} successfully edited", combo.getName()));
        return new RedirectResolution(CreateEditComboAction.class).addParameter("combo", combo);
    }

    @DontValidate
    public Resolution deleteComboProduct() {
        logger.debug("comboProduct: " + comboProduct.getId());
        comboProduct.setAllowedProductVariants(null);
        comboProduct = (ComboProduct) getComboDao().save(comboProduct);
        getComboDao().delete(comboProduct);
        addRedirectAlertMessage(new SimpleMessage("Deleted product from Combo - {0} successfully", comboProduct.getCombo().getName()));
        return new RedirectResolution(CreateEditComboAction.class).addParameter("combo", comboProduct.getCombo());
    }

    private boolean isCodAllowedForCombo(List<Product> comboProducts){
        boolean isCodAllowed = true;
        for (Product comboProduct : comboProducts) {
           isCodAllowed = isCodAllowed && comboProduct.isCodAllowed();
        }
        return isCodAllowed;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public ComboProduct getComboProduct() {
        return comboProduct;
    }

    public void setComboProduct(ComboProduct comboProduct) {
        this.comboProduct = comboProduct;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<ComboProductAndAllowedVariantsDto> getComboProductAndAllowedVariantsDtoList() {
        return comboProductAndAllowedVariantsDtoList;
    }

    public void setComboProductAndAllowedVariantsDtoList(List<ComboProductAndAllowedVariantsDto> comboProductAndAllowedVariantsDtoList) {
        this.comboProductAndAllowedVariantsDtoList = comboProductAndAllowedVariantsDtoList;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ComboDao getComboDao() {
        return comboDao;
    }

    public void setComboDao(ComboDao comboDao) {
        this.comboDao = comboDao;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public XslParser getXslParser() {
        return xslParser;
    }

    public void setXslParser(XslParser xslParser) {
        this.xslParser = xslParser;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

}