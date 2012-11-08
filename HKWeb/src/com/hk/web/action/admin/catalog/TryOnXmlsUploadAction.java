package com.hk.web.action.admin.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/7/12
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class TryOnXmlsUploadAction extends BaseAction {

        @Validate(required = true)
    FileBean fileBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.eyeTryOnXmls + "']}")
    String eyeTryOnXmlsPath;

    String category;

    private static Logger logger = LoggerFactory.getLogger(TryOnXmlsUploadAction.class);

    @DefaultHandler
    @DontValidate
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/eyeTryOnXmlDump.jsp");
    }

    public Resolution upload() {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            //saving in adminUploads for backup
            String excelFilePath = adminUploadsPath + "/xmlFiles/" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xml";
            File excelFile = new File(excelFilePath);
            excelFile.getParentFile().mkdirs();
            fileBean.save(excelFile);

            //saving in dist to avoid tomcat restart
            String xmlFilePath = eyeTryOnXmlsPath + "/Glasses/" + category + ".xml";
            File xmlFile = new File(xmlFilePath);
            xmlFile.getParentFile().mkdirs();
            fileBean.save(xmlFile);

        } catch (Exception e) {

            logger.error("Exception while uploading excel " + e);
        }

        addRedirectAlertMessage(new SimpleMessage("Feed Updated"));
        return new ForwardResolution("/pages/admin/eyeTryOnXmlDump.jsp");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }
}
