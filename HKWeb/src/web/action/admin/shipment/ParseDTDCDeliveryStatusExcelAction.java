package web.action.admin.shipment;

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
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.admin.manager.ProductManager;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.service.UserService;
import com.hk.web.BatchProcessWorkManager;

/**
 * Created by IntelliJ IDEA. User: USER Date: Sep 29, 2011 Time: 5:10:59 PM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
@Component
public class ParseDTDCDeliveryStatusExcelAction extends BaseAction {

    // static File inFile = new File("./test/data/catalog/ProductCatalog-Diabetes.xls");
    //  
    // TestDatabaseConnectionHelper connectionHelper;

    private static Logger               logger = LoggerFactory.getLogger(ParseDTDCDeliveryStatusExcelAction.class);

    @Autowired
    private ProductManager              productManager;

    @Autowired
    private BatchProcessWorkManager     batchProcessWorkManager;

    @Autowired
    private XslParser                   xslParser;

    @Autowired
    private DeliveryStatusUpdateManager deliveryStatusUpdateManager;

    @Autowired
    private UserService                 userService;

    //@Named(Keys.Env.adminUploads)
    String                              adminUploadsPath;

    String                              messagePostUpdation;

    @Validate(required = true)
    FileBean                            fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public String getMessagePostUpdation() {
        return messagePostUpdation;
    }

    public void setMessagePostUpdation(String messagePostUpdation) {
        this.messagePostUpdation = messagePostUpdation;
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/parseDTDCDeliveryStatusExcel.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution parse() throws Exception {

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        String excelFilePath = adminUploadsPath + "/deliveryStatus/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {

            // messagePostUpdation = deliveryStatusUpdateManager.updateDeliveryStatusDTDC(excelFile, loggedOnUser);
            messagePostUpdation = deliveryStatusUpdateManager.updateDeliveryStatusDTDC(excelFile);
            if (messagePostUpdation == "") {
                addRedirectAlertMessage(new SimpleMessage("Upload Successful."));
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new ForwardResolution("/pages/admin/parseDTDCDeliveryStatusExcel.jsp");
        }
        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage(messagePostUpdation));
        return new ForwardResolution("/pages/admin/parseDTDCDeliveryStatusExcel.jsp");
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public BatchProcessWorkManager getBatchProcessWorkManager() {
        return batchProcessWorkManager;
    }

    public void setBatchProcessWorkManager(BatchProcessWorkManager batchProcessWorkManager) {
        this.batchProcessWorkManager = batchProcessWorkManager;
    }

    public XslParser getXslParser() {
        return xslParser;
    }

    public void setXslParser(XslParser xslParser) {
        this.xslParser = xslParser;
    }

    public DeliveryStatusUpdateManager getDeliveryStatusUpdateManager() {
        return deliveryStatusUpdateManager;
    }

    public void setDeliveryStatusUpdateManager(DeliveryStatusUpdateManager deliveryStatusUpdateManager) {
        this.deliveryStatusUpdateManager = deliveryStatusUpdateManager;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
