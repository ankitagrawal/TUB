package com.hk.web.action.admin.shipment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.admin.util.XslParser;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.pact.dao.MasterDataDao;
import com.hk.pact.service.UserService;
import com.hk.web.BatchProcessWorkManager;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: USER Date: Sep 29, 2011 Time: 5:10:59 PM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
@Component
public class ParseCourierDeliveryStatusExcelAction extends BaseAction {

    // static File inFile = new File("./test/data/catalog/ProductCatalog-Diabetes.xls");
    //  
    // TestDatabaseConnectionHelper connectionHelper;

    private static Logger               logger = LoggerFactory.getLogger(ParseCourierDeliveryStatusExcelAction.class);
  
    @Autowired
    private BatchProcessWorkManager     batchProcessWorkManager;

    @Autowired
    private XslParser                   xslParser;

    @Autowired
    private DeliveryStatusUpdateManager deliveryStatusUpdateManager;

    @Autowired
    private UserService                 userService;

	@Autowired
	private CourierService   courierService;

    @Autowired
    private MasterDataDao               masterDataDao;

    //@Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                              adminUploadsPath;

    String                              messagePostUpdation;

    FileBean                            fileBean;

    private List<Courier>               courierList =new ArrayList<Courier>();


    @ValidationMethod(on = "parse")
    public void validateOnParse() {
        if (fileBean == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please select a file to upload."));
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/parseCourierDeliveryStatusExcel.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution displayCourierIdList(){
        courierList=new ArrayList<Courier>();
        courierList= courierService.getAllCouriers() ;
        return new ForwardResolution("/pages/admin/courierIdList.jsp");
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
            messagePostUpdation = deliveryStatusUpdateManager.updateCourierDeliveryStatusByExcel(excelFile);
            if (messagePostUpdation == "") {
                addRedirectAlertMessage(new SimpleMessage("Upload Successful."));
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new ForwardResolution("/pages/admin/parseCourierDeliveryStatusExcel.jsp");
        }
        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage(messagePostUpdation));
        return new ForwardResolution("/pages/admin/parseCourierDeliveryStatusExcel.jsp");
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


    public List<Courier> getCourierList() {
        return courierList;
    }

    public void setCourierList(List<Courier> courierList) {
        this.courierList = courierList;
    }


}
