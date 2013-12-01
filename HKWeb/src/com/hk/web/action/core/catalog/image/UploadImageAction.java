package com.hk.web.action.core.catalog.image;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.util.ImageManager;

@Component
public class UploadImageAction extends BaseAction {
    @Validate(required = true)
    FileBean       fileBean;
    Product        product;
    ProductVariant productVariant;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String         adminUploadsPath;
    @Autowired
    ImageManager   imageManager;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/uploadImage.jsp");
    }

    @DontValidate
    public Resolution uploadVariantImage() throws Exception {
        return new ForwardResolution("/pages/uploadVariantImage.jsp");
    }

    /**
     * This method uploads the image on S3 in all sizes. If the productId passed from uploadImage.jsp is null, then
     * reads the tag of the image, and find productId from there.
     * 
     * @return
     * @throws Exception
     */

    public Resolution uploadProductImage() throws Exception {
        String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
        File imageFile = new File(imageFilePath);
        EnumS3UploadStatus status;
        try {
            imageFile.getParentFile().mkdirs();
            fileBean.save(imageFile);
            status = imageManager.uploadProductFile(imageFile, product, false);
        } finally {
            if (imageFile.exists())
                imageFile.delete();
        }
        addRedirectAlertMessage(new SimpleMessage(status.getMessage()));
        return new ForwardResolution("/pages/uploadImage.jsp");
    }

    public Resolution uploadProductVariantImage() throws Exception {
        String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
        File imageFile = new File(imageFilePath);
        EnumS3UploadStatus status;
        try {
            imageFile.getParentFile().mkdirs();
            fileBean.save(imageFile);
            status = imageManager.uploadProductVariantFile(imageFile, productVariant);
        } finally {
            if (imageFile.exists())
                imageFile.delete();
        }
        addRedirectAlertMessage(new SimpleMessage(status.getMessage()));
        return new ForwardResolution("/pages/uploadVariantImage.jsp");
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
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
}
