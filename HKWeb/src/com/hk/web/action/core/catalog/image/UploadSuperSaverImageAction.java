package com.hk.web.action.core.catalog.image;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.StripesFilter;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.catalog.product.Product;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.util.ImageManager;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.web.action.core.catalog.SuperSaversAction;
import com.hk.web.filter.WebContext;
import com.akube.framework.util.BaseUtils;
import com.akube.framework.stripes.action.BaseAction;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class UploadSuperSaverImageAction extends BaseAction {
    FileBean fileBean;
    List<SuperSaverImage> superSaverImages;

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

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/uploadSuperSaverImage.jsp");
    }

    public Resolution manageSuperSaverImages() {
        superSaverImages = superSaverImageService.getSuperSaverImages();
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
            FileUtils.deleteQuietly(imageFile);
        }
        addRedirectAlertMessage(new SimpleMessage("Error while uploading image!"));
        return new ForwardResolution("/pages/uploadSuperSaverImage.jsp");
    }

    public Resolution editSuperSaverImageSettings() {
        if (superSaverImages != null) {
            for (SuperSaverImage superSaverImage : superSaverImages) {
                Product superSaverProduct = superSaverImage.getProduct();

                //check whether combo exists or not
                Combo combo = comboDao.getComboById(superSaverProduct.getId());
                if (combo != null) {
                    //while place this check once main image id logic is implemented
                    /*if (superSaverImage.isHidden() == Boolean.TRUE) {
                        if (superSaverImage.isMainImage() == Boolean.TRUE) {
                            logger.debug("main image cannot be set as hidden!");
                            addRedirectAlertMessage(new SimpleMessage("MAIN IMAGE CANNOT BE SET AS HIDDEN!"));
                            return new ForwardResolution("/pages/manageSuperSaverImages.jsp");
                        }
                    }*/
                    superSaverImage.setMainImage(Boolean.TRUE);

                    String altText = superSaverImage.getAltText();
                    String productName = superSaverProduct.getName();
                    superSaverImage.setUrl(productName);
                    superSaverImage.setAltText(StringUtils.isNotBlank(altText) ? altText : productName);
                    superSaverImageService.saveSuperSaverImage(superSaverImage);
                } else {
                    addRedirectAlertMessage(new SimpleMessage("No combo exists with the specified id! Kindly enter a valid combo id."));
                    return new ForwardResolution("/pages/uploadSuperSaverImage.jsp");
                }
            }
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
}
