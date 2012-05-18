package com.hk.web.action.admin.catalog.product;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyRoles = { RoleConstants.GOD }, authActionBean = AdminPermissionAction.class)
@Component
public class RelatedProductAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(RelatedProductAction.class);
    @Autowired
    XslParser             xslParser;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                adminUploadsPath;

    @Validate(required = true)
    FileBean              fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/updateRelatedProducts.jsp");
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/relatedProducts/" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        try {
            logger.info("Related Product Update by " + loggedOnUser.getEmail());
            Set<Product> productSet = xslParser.readAndSetRelatedProducts(excelFile);
            // excelFile.delete();
            addRedirectAlertMessage(new SimpleMessage("Updated Related Products for " + productSet.size()));
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(RelatedProductAction.class);
    }
}