package com.hk.web.action.core.catalog.image;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.util.BaseUtils;
import com.hk.cache.CategoryCache;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.util.ImageManager;
import com.hk.web.action.core.catalog.SuperSaversAction;

@Component
public class UploadSuperSaverImageAction extends BasePaginatedAction {
    FileBean                       fileBean;
    List<SuperSaverImage>          superSaverImages;
    List<String>                   categories;
    List<String>                   brands;
    Product                        product;
    private Integer                defaultPerPage = 10;
    Page                           superSaverPage;
    private SuperSaverImage        unassignedSuperSaver;

    private static Logger          logger         = Logger.getLogger(SuperSaversAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                         adminUploadsPath;

    @Autowired
    private ImageManager           imageManager;

    @Autowired
    private ProductService         productService;

    @Autowired
    private ComboDao               comboDao;

    @Autowired
    private SuperSaverImageService superSaverImageService;

    @Autowired
    private CategoryService        categoryService;

    @ValidationMethod(on = "getSuperSaversByCategoryAndBrand")
    public void validateCategoryAndBrand() {
        for (String brand : brands) {
            if (!StringUtils.isBlank(brand)) {
                if (!getProductService().doesBrandExist(brand)) {
                    getContext().getValidationErrors().add("1", new SimpleError("Brand not found: " + brand));
                }
            }
        }

        for (String category : categories) {
            if (!StringUtils.isBlank(category)) {
                /*
                 * if (getCategoryService().getCategoryByName(category) == null) {
                 * getContext().getValidationErrors().add("1", new SimpleError("Category not found: " + category)); }
                 */
                if (CategoryCache.getInstance().getCategoryByName(category) == null) {
                    getContext().getValidationErrors().add("1", new SimpleError("Category not found: " + category));
                }
            }
        }
    }

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/uploadSuperSaverImage.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution getSuperSaversForCategoryAndBrand() {
        superSaverPage = getSuperSaverImageService().getSuperSaverImages(categories, brands, Boolean.FALSE, Boolean.TRUE, getPageNo(), getPerPage());
        superSaverImages = superSaverPage.getList();
        return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
    }

    public Resolution getSuperSaversForProduct() {
        if (product != null) {
            if (!product.isDeleted()) {
                Combo combo = null;
                // Always check before getting a combo if it's a combo or not
                if (productService.isCombo(combo)) {
                    combo = getComboDao().getComboById(product.getId());
                }
                if (combo == null) {
                    addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
                    return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
                } else {
                    superSaverImages = getSuperSaverImageService().getSuperSaverImages(product, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
                    superSaverPage = new Page(superSaverImages, defaultPerPage, getPageNo(), superSaverImages.size());

                    superSaverImages = superSaverPage.getList();
                    return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("The product entered has the isDeleted property set to true!"));
                return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
            return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
        }
    }

    public Resolution getSuperSaversWithNoProductAssigned() {
        superSaverImages = getSuperSaverImageService().getSuperSaverImages(null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
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

            status = getImageManager().uploadSuperSaverFile(imageFile, Boolean.TRUE);
            addRedirectAlertMessage(new SimpleMessage(status.getMessage()));
            return new ForwardResolution(UploadSuperSaverImageAction.class, "getSuperSaversWithNoProductAssigned");
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
                    // check whether combo exists or not
                    superSaverImage.setMainImage(Boolean.TRUE);
                    String altText = superSaverImage.getAltText();
                    String productName = superSaverProduct.getName();
                    superSaverImage.setUrl(productName);
                    superSaverImage.setAltText(StringUtils.isNotBlank(altText) ? altText : productName);
                } else {
                    addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
                    return new RedirectResolution(UploadSuperSaverImageAction.class, "getSuperSaversForCategoryAndBrand");
                }
            }
            getSuperSaverImageService().saveSuperSaverImages(superSaverImages);
        }
        return new RedirectResolution(SuperSaversAction.class);
    }

    public Resolution editUnassignedSuperSaver() {
        return new ForwardResolution("/pages/editUnassignedSuperSaver.jsp");
    }

    public Resolution saveUnassignedSuperSaver() {
        superSaverImages = Arrays.asList(unassignedSuperSaver);
        return editSuperSaverImageSettings();
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
        params.add("categories");
        params.add("brands");
        return params;
    }

    public ProductService getProductService() {
        return productService;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public ComboDao getComboDao() {
        return comboDao;
    }

    public SuperSaverImageService getSuperSaverImageService() {
        return superSaverImageService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public SuperSaverImage getUnassignedSuperSaver() {
        return unassignedSuperSaver;
    }

    public void setUnassignedSuperSaver(SuperSaverImage unassignedSuperSaver) {
        this.unassignedSuperSaver = unassignedSuperSaver;
    }
}