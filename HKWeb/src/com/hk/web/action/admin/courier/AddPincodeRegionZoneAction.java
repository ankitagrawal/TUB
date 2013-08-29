package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.PincodeRegionZoneService;
import com.hk.admin.util.helper.XslPincodeParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vidur Malhotra
 * Date: 8/27/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AddPincodeRegionZoneAction extends BaseAction {

    @Autowired
    CourierService courierService;
    @Autowired
    PincodeRegionZoneService pincodeRegionZoneService;
    @Autowired
    PincodeService pincodeService;
    @Autowired
    BaseDao baseDao;
    @Autowired
    XslPincodeParser xslPincodeParser;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    FileBean fileBean;
    private Pincode pincode;
    private PincodeRegionZone pincodeRegionZone;
    private List<PincodeRegionZone> pincodeRegionZoneList = null;
    private List<Pincode> pincodeList;

    @Autowired
    PincodeCourierService pincodeCourierService;


    private static Logger logger = LoggerFactory.getLogger(AddPincodeRegionZoneAction.class);

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public Resolution savePincodeRegionList() {
        for (PincodeRegionZone pincodeRegionZone : pincodeRegionZoneList) {
            pincodeRegionZoneService.saveOrUpdate(pincodeRegionZone);
        }
        addRedirectAlertMessage(new SimpleMessage("Pincode Region saved"));
        return new RedirectResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_MPA_UPDATE}, authActionBean = AdminPermissionAction.class)
    public Resolution savePincodeRegion() {
        if(pincodeRegionZone!=null){
            if (pincodeRegionZone.getPincode() == null) {
                addRedirectAlertMessage(new SimpleMessage("Pincode does not exist in System"));
            } else {
                try {
                    PincodeRegionZone pincodeRegionZoneDb = pincodeRegionZoneService.getPincodeRegionZone(pincodeRegionZone.getCourierGroup(), pincodeRegionZone.getPincode(), pincodeRegionZone.getWarehouse());
                    if (pincodeRegionZoneDb != null) {
                        pincodeRegionZoneDb.setRegionType(pincodeRegionZone.getRegionType());
                    } else {
                        pincodeRegionZoneDb = pincodeRegionZone;
                    }
                    pincodeRegionZoneService.save(pincodeRegionZoneDb);
                } catch (Exception ex) {
                    addRedirectAlertMessage(new SimpleMessage("EXCEPTION IN SAVING" + ex.getMessage()));
                    return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
                }
                addRedirectAlertMessage(new SimpleMessage("Pincode region saved"));
            }
        }
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_MPA_VIEW}, authActionBean = AdminPermissionAction.class)
    public Resolution searchPincodeRegion() {
        Pincode pincode = pincodeRegionZone.getPincode();
        if (pincode == null) {
            addRedirectAlertMessage(new SimpleMessage("Pincode does not exist in System"));
        } else {
            pincodeRegionZoneList = pincodeRegionZoneService.getPincodeRegionZoneList(pincodeRegionZone.getCourierGroup(), pincode, pincodeRegionZone.getWarehouse());
            if (pincodeRegionZoneList == null) {
                addRedirectAlertMessage(new SimpleMessage("Pincode Region zone does not exist for Pincode"));
            }
        }
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public Resolution showRemainingPrz() {
        pincodeList = pincodeService.getPincodeNotInPincodeRegionZone();
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public Resolution directToPincodeRegionZone(){
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }

    public PincodeRegionZoneService getPincodeRegionZoneService() {
        return pincodeRegionZoneService;
    }

    public void setPincodeRegionZoneService(PincodeRegionZoneService pincodeRegionZoneService) {
        this.pincodeRegionZoneService = pincodeRegionZoneService;
    }

    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public XslPincodeParser getXslPincodeParser() {
        return xslPincodeParser;
    }

    public void setXslPincodeParser(XslPincodeParser xslPincodeParser) {
        this.xslPincodeParser = xslPincodeParser;
    }

    public String getAdminDownloadsPath() {
        return adminDownloadsPath;
    }

    public void setAdminDownloadsPath(String adminDownloadsPath) {
        this.adminDownloadsPath = adminDownloadsPath;
    }

    public String getAdminUploadsPath() {
        return adminUploadsPath;
    }

    public void setAdminUploadsPath(String adminUploadsPath) {
        this.adminUploadsPath = adminUploadsPath;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public PincodeRegionZone getPincodeRegionZone() {
        return pincodeRegionZone;
    }

    public void setPincodeRegionZone(PincodeRegionZone pincodeRegionZone) {
        this.pincodeRegionZone = pincodeRegionZone;
    }

    public List<PincodeRegionZone> getPincodeRegionZoneList() {
        return pincodeRegionZoneList;
    }

    public void setPincodeRegionZoneList(List<PincodeRegionZone> pincodeRegionZoneList) {
        this.pincodeRegionZoneList = pincodeRegionZoneList;
    }

    public PincodeCourierService getPincodeCourierService() {
        return pincodeCourierService;
    }

    public void setPincodeCourierService(PincodeCourierService pincodeCourierService) {
        this.pincodeCourierService = pincodeCourierService;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        AddPincodeRegionZoneAction.logger = logger;
    }

    public List<Pincode> getPincodeList() {
        return pincodeList;
    }

    public void setPincodeList(List<Pincode> pincodeList) {
        this.pincodeList = pincodeList;
    }
}
