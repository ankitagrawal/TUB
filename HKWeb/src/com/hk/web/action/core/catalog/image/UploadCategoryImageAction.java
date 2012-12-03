package com.hk.web.action.core.catalog.image;

import java.io.File;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.controller.StripesFilter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.cache.CategoryCache;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.category.CategoryImage;
import com.hk.pact.dao.catalog.category.CategoryImageDao;
import com.hk.util.ImageManager;
import com.hk.web.filter.WebContext;

@Component
public class UploadCategoryImageAction extends BaseAction {
    // @Validate(required = true)
    FileBean                fileBean;
    Category                category;
    String                  name;
    String                  categoryName;
    String                  errorMessage = null;

    @Autowired
    public CategoryImageDao categoryImageDao;

    private static Logger   logger       = Logger.getLogger(UploadCategoryImageAction.class);
    List<CategoryImage>     categoryImages;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                  adminUploadsPath;

    @Autowired
    private ImageManager    imageManager;

    /*
     * @Autowired private CategoryDao categoryDao;
     */

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/uploadCategoryImage.jsp");
    }

    public Resolution uploadCategoryImage() throws Exception {
        String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
        logger.debug("image uplaod path: " + imageFilePath);
        File imageFile = new File(imageFilePath);
        // category = categoryDao.getCategoryByName(categoryName);
        category = CategoryCache.getInstance().getCategoryByName(categoryName).getCategory();
        EnumS3UploadStatus status;
        try {
            imageFile.getParentFile().mkdirs();
            if (fileBean == null) {
                // System.out.println("nullll");
            }

            fileBean.save(imageFile);
            status = imageManager.uploadCategoryFile(imageFile, category, false);
        } finally {
            if (imageFile.exists())
                imageFile.delete();
        }
        addRedirectAlertMessage(new SimpleMessage(status.getMessage()));
        return new ForwardResolution("/pages/uploadCategoryImage.jsp");
    }

    @DontValidate
    public Resolution manageCategoryImages() {
        categoryImages = categoryImageDao.getCategoryImageByCategory(category);
        // mainImageId = category.getMainImageId() != null ? category.getMainImageId().toString() : "";
        if (category.getName().equalsIgnoreCase(CategoryConstants.HOME)) {
            return new ForwardResolution("/pages/manageHomeImages.jsp");
        }
        return new ForwardResolution("/pages/manageCategoryImages.jsp");
    }

    public Resolution editCategoryImageSettings() throws Exception {
        String host = "http://".concat(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());
        String contextPath = WebContext.getRequest().getContextPath();
        String urlString = host.concat(contextPath);
        if (categoryImages != null) {
            for (CategoryImage categoryImage : categoryImages) {
                // For checking whether entered link is correct or not
                if (categoryImage.getLink() != null) {
                    String linkValue = urlString.concat(categoryImage.getLink());
                    try {
                        if (!BaseUtils.remoteFileExists(linkValue)) {
                            logger.debug("heading link " + linkValue + " invalid!");
                            errorMessage = "PLEASE ENTER LINK CORRECTLY .. ENTERED LINK DOES NOT EXIST";
                            return new ForwardResolution("/pages/manageCategoryImages.jsp");
                        }
                    } catch (Exception e) {
                        logger.debug("heading link " + linkValue + " invalid!");
                        errorMessage = "PLEASE ENTER LINK CORRECTLY .. ENTERED LINK DOES NOT EXIST";
                        return new ForwardResolution("/pages/manageCategoryImages.jsp");
                    }
                }
                //
                categoryImageDao.save(categoryImage);
            }
        }
        return new ForwardResolution("/pages/close.jsp");
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryImage> getCategoryImages() {
        return categoryImages;
    }

    public void setCategoryImages(List<CategoryImage> categoryImages) {
        this.categoryImages = categoryImages;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
