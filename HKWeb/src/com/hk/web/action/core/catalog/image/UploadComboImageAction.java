package com.hk.web.action.core.catalog.image;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.util.ImageManager;

@Component
public class UploadComboImageAction extends BaseAction {

    @Validate(required = true)
    FileBean     fileBean;
    Combo        combo;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String       adminUploadsPath;
    @Autowired
    ImageManager imageManager;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/uploadComboImage.jsp");
    }

    public Resolution uploadComboImage() throws Exception {
        String imageFilePath = adminUploadsPath + "/imageFiles/temp/" + System.currentTimeMillis() + "_" + BaseUtils.getRandomString(4) + ".jpg";
        File imageFile = new File(imageFilePath);
        EnumS3UploadStatus status;
        try {
            imageFile.getParentFile().mkdirs();
            fileBean.save(imageFile);
            // status = imageManager.uploadComboFile(imageFile, combo, false);
        } finally {
            if (imageFile.exists())
                imageFile.delete();
        }
        // addRedirectAlertMessage(new SimpleMessage(status.getMessage()));
        return new ForwardResolution("/pages/uploadComboImage.jsp");
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

}