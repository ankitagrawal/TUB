package com.hk.web.action.admin.shipment;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
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
import com.akube.framework.util.DateUtils;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: Rahul Date: Jan 15, 2012 Time: 6:00:34 PM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
@Component
public class ParseCourierCollectionChargeExcelAction extends BaseAction {

    private static Logger logger                = LoggerFactory.getLogger(ParseCourierCollectionChargeExcelAction.class);
    @Autowired
    XslParser             xslParser;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                adminUploadsPath;
    @Autowired
    EmailManager          emailManager;

    String                numberOfOrdersUpdated = "";

    @Validate(required = true)
    FileBean              fileBean;
    String                email;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/parseCourierCollectionChargeExcel.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution parse() throws Exception {

        User loggedOnUser = null;
        String messagePostUpdation = "";
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        long uploadTimeInMillis = System.currentTimeMillis();
        String excelFilePath = adminUploadsPath + "/courierCollectionCharge/" + uploadTimeInMillis + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {

            messagePostUpdation = xslParser.updateCourierCollectionCharges(excelFile);
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new ForwardResolution("/pages/admin/parseCourierCollectionChargeExcel.jsp");
        }
        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage(messagePostUpdation));
        if (email != null) {
            emailManager.sendCourierCollectionPostUpdationMessage(email, messagePostUpdation, DateUtils.convertMilliSecondsToDate(uploadTimeInMillis, "dd/MM/yyyy hh:mm:ss.SSS"));
        }
        return new ForwardResolution("/pages/admin/parseCourierCollectionChargeExcel.jsp");
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
