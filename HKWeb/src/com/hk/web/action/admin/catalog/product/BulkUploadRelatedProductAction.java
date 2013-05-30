package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 5/30/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkUploadRelatedProductAction extends BaseAction {

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    String category;
                 FileBean fileBean;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/bulkUploadRelatedProduct.jsp");
    }

    public Resolution save() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        String excelFilePath = adminUploadsPath + "/relatedProductFiles/" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        File excelFile= new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        try{

        }
        catch (Exception ex){

        }

        addRedirectAlertMessage(new SimpleMessage("Database Successfully Updated."));
        return new ForwardResolution("/pages/admin/bulkUploadRelatedProduct.jsp");
    }
}
