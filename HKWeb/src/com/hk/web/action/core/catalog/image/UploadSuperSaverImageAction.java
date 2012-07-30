package com.hk.web.action.core.catalog.image;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.SimpleError;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.catalog.product.Product;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.util.ImageManager;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.web.action.core.catalog.SuperSaversAction;
import com.hk.web.filter.WebContext;
import com.akube.framework.util.BaseUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.dao.Page;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

@Component
public class UploadSuperSaverImageAction extends BasePaginatedAction {
    FileBean fileBean;
    List<SuperSaverImage> superSaverImages;
    String category;
    String brand;
    Product product;
    private Integer defaultPerPage = 10;
    Page superSaverPage;

    private static Logger logger = Logger.getLogger(SuperSaversAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Autowired
    ImageManager imageManager;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDao productDao;

    @Autowired
    ComboDao comboDao;

    @Autowired
    SuperSaverImageService superSaverImageService;

    @Autowired
    CategoryService categoryService;

    @ValidationMethod(on = "getSuperSaversByCategoryAndBrand")
    public void validateCategoryAndBrand() {
        if (!StringUtils.isBlank(brand)) {
            if (!productDao.doesBrandExist(brand)) {
                getContext().getValidationErrors().add("1", new SimpleError("Brand not found"));
            }
        }

        if (!StringUtils.isBlank(category)) {
            if (categoryService.getCategoryByName(category) == null) {
                getContext().getValidationErrors().add("1", new SimpleError("Category not found"));
            }
        }
    }

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/uploadSuperSaverImage.jsp");
    }

    public Resolution manageSuperSaverImages() {
        superSaverPage = superSaverImageService.getSuperSaverImages(null, null, Boolean.FALSE, getPageNo(), getPerPage());
        superSaverImages = superSaverPage.getList();
        return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
    }

    public Resolution getSuperSaversForCategoryAndBrand() {
        superSaverPage = superSaverImageService.getSuperSaverImages(category, brand, Boolean.FALSE, getPageNo(), getPerPage());
        superSaverImages = superSaverPage.getList();
        return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
    }

    public Resolution getSuperSaversForProduct() {
        if (product != null) {
            Combo combo = comboDao.getComboById(product.getId());
            if (combo == null) {
                addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
                return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
            }
        }
        superSaverImages = superSaverImageService.getSuperSaverImages(product, Boolean.FALSE, Boolean.FALSE);
        superSaverPage = new Page(superSaverImages, defaultPerPage, getPageNo(), superSaverImages.size());
        
        superSaverImages = superSaverPage.getList();
        return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
    }

    public Resolution uploadSuperSaverImage() {
        File imageFile = null;
        try {
            String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
            imageFile = new File(imageFilePath);
            imageFile.getParentFile().mkdirs();
            fileBean.save(imageFile);
            EnumS3UploadStatus status;


            status = imageManager.uploadSuperSaverFile(imageFile, Boolean.TRUE);
            addRedirectAlertMessage(new SimpleMessage(status.getMessage()));
            return new ForwardResolution(UploadSuperSaverImageAction.class, "manageSuperSaverImages");
        } catch (IOException ioe) {
            logger.error("Error while uploading super saver image: " + ioe);
        } finally {
            if (imageFile != null) {
                FileUtils.deleteQuietly(imageFile);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Error while uploading image!"));
        return new ForwardResolution("/pages/uploadSuperSaverImage.jsp");
    }

    public Resolution editSuperSaverImageSettings() {
        if (superSaverImages != null) {
            for (SuperSaverImage superSaverImage : superSaverImages) {
                Product superSaverProduct = superSaverImage.getProduct();
                if (superSaverProduct != null) {
                    //check whether combo exists or not
                    Combo combo = comboDao.getComboById(superSaverProduct.getId());
                    if (combo != null) {

                        superSaverImage.setMainImage(Boolean.TRUE);

                        String altText = superSaverImage.getAltText();
                        String productName = superSaverProduct.getName();
                        superSaverImage.setUrl(productName);
                        superSaverImage.setAltText(StringUtils.isNotBlank(altText) ? altText : productName);
                        //superSaverImageService.saveSuperSaverImage(superSaverImage);
                    } else {
                        addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
                        return new RedirectResolution(UploadSuperSaverImageAction.class, "manageSuperSaverImages");
                    }
                } else {
                    addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
                    return new RedirectResolution(UploadSuperSaverImageAction.class, "manageSuperSaverImages");
                }
            }
            superSaverImageService.saveSuperSaverImages(superSaverImages);
        }
        return new RedirectResolution(SuperSaversAction.class);
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public List<SuperSaverImage> getSuperSaverImages() {
        return superSaverImages;
    }

    public void setSuperSaverImages(List<SuperSaverImage> superSaverImages) {
        this.superSaverImages = superSaverImages;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        params.add("category");
        params.add("brand");
        return params;
    }
}
