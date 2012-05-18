package com.hk.web.action.core.catalog.product;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.util.ImageManager;

public class BulkUploadPVImageAction extends BaseAction {

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String       adminUploadsPath;
    @Autowired
    ImageManager imageManager;

    @DefaultHandler
    public Resolution bulkUpload() throws Exception {
        File directory = new File(adminUploadsPath + "/imageFiles/");
        directory.mkdirs();
        Long imagesUploaded = imageManager.bulkUploadProductVariant(directory);
        addRedirectAlertMessage(new SimpleMessage(imagesUploaded.toString() + " new images uploaded"));
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }
}
