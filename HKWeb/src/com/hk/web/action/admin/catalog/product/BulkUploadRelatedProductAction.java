package com.hk.web.action.admin.catalog.product;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.RelatedProductXlsParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.stripesstuff.plugin.security.Secure;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 5/30/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.BULK_CATALOG_UPDATE}, authActionBean = AdminPermissionAction.class)
public class BulkUploadRelatedProductAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(BulkUploadRelatedProductAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    String category;
    FileBean fileBean;
    @Autowired
    RelatedProductXlsParser relatedProductXlsParser;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/bulkUploadRelatedProduct.jsp");
    }

    public Resolution save() throws IOException {
        if (fileBean == null) {
            addRedirectAlertMessage(new SimpleMessage("Select choose file to upload"));
            return new RedirectResolution("/pages/bulkUploadRelatedProduct.jsp");
        }
        if (!(fileBean.getContentType().equals("application/vnd.ms-excel"))) {
            addRedirectAlertMessage(new SimpleMessage("upload xls file only"));
            return new RedirectResolution("/pages/bulkUploadRelatedProduct.jsp");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        String excelFilePath = adminUploadsPath + "/relatedProductFiles/" + sdf.format(new Date()) + "/" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        try {
            logger.info("Related Product Update by " + loggedOnUser.getEmail());
            Set<Product> productSet = relatedProductXlsParser.readRelatedProductExcel(excelFile);
            addRedirectAlertMessage(new SimpleMessage("product updated for" + productSet.size()));
        } catch (Exception ex) {
            logger.error("Exception while reading excel sheet.", ex);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + ex.getMessage()));
        }

        addRedirectAlertMessage(new SimpleMessage("Database Successfully Updated."));
        return new ForwardResolution("/pages/bulkUploadRelatedProduct.jsp");
    }

    public RelatedProductXlsParser getRelatedProductXlsParser() {
        return relatedProductXlsParser;
    }

    public void setRelatedProductXlsParser(RelatedProductXlsParser relatedProductXlsParser) {
        this.relatedProductXlsParser = relatedProductXlsParser;
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
