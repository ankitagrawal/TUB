package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.util.helper.XslPincodeCourierMapping;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.pact.service.core.PincodeService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 12/28/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PincodeCourierMappingAction extends BaseAction {

    Pincode pincode;
    FileBean fileBean;
    String pin;

    List<Courier> courierList;
    Courier updateCourier;
    List<PincodeCourierMapping> pincodeCourierMappings;
    private List<PincodeDefaultCourier> pincodeDefaultCouriers;
    List<Courier> availableCouriers;
    Map<String, Boolean> applicableShipmentServices = new HashMap<String, Boolean>();

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Autowired
    PincodeService pincodeService;

    @Autowired
    XslPincodeCourierMapping xslPincodeCourierMapping;

    @Autowired
    PincodeCourierService pincodeCourierService;

    @Autowired
    CourierService courierService;

    private static Logger logger = LoggerFactory.getLogger(PincodeCourierMappingAction.class);


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/courier/pincodeCourierMapping.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_PCM_VIEW}, authActionBean = AdminPermissionAction.class)
    public Resolution search() {
        pincode = pincodeService.getByPincode(pin);
        if (pincode == null) {
            addRedirectAlertMessage(new SimpleMessage("No Pincode Exist in the System!!!"));
            return new RedirectResolution(PincodeCourierMappingAction.class);
        }
        pincodeCourierMappings = pincodeCourierService.getApplicablePincodeCourierMappingList(pincode, null, null, null);
        pincodeDefaultCouriers = pincodeCourierService.searchPincodeDefaultCourierList(pincode, null, null, null);
        return new ForwardResolution("/pages/admin/courier/pincodeCourierMapping.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_PCM_VIEW}, authActionBean = AdminPermissionAction.class)
    public Resolution detailedAnalysis() {
        pincode = pincodeService.getByPincode(pin);
        if (pincode == null) {
            addRedirectAlertMessage(new SimpleMessage("No Pincode Exist in the System!!!"));
            return new RedirectResolution(PincodeCourierMappingAction.class);
        }
        pincodeCourierMappings = pincodeCourierService.getApplicablePincodeCourierMappingList(pincode, null, null, true);
        applicableShipmentServices = pincodeCourierService.generateDetailedAnalysis(pincodeCourierMappings);
        return new ForwardResolution("/pages/admin/courier/pincodeCourierMapping.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_PCM_UPDATE}, authActionBean = AdminPermissionAction.class)
    public Resolution update() {
        boolean flag = true;
        for (PincodeCourierMapping pincodeCourierMapping : pincodeCourierMappings) {
            PincodeCourierMapping pincodeCourierMappingDb = pincodeCourierService.getApplicablePincodeCourierMapping(pincodeCourierMapping.getPincode(), Arrays.asList(pincodeCourierMapping.getCourier()), null, null);
            boolean isValidMapping = pincodeCourierMapping.isCodGround() || pincodeCourierMapping.isPrepaidGround() || pincodeCourierMapping.isPrepaidAir() || pincodeCourierMapping.isCodAir();
            if (pincodeCourierMappingDb != null) {
                if (!pincodeCourierService.changePincodeCourierMapping(pincodeCourierMappingDb, pincodeCourierMapping)) {
                    flag = false;
                    break;
                }
                if (!isValidMapping) {
                    pincodeCourierService.deletePincodeCourierMapping(pincodeCourierMappingDb);
                    continue;
                }
            }
            pincodeCourierService.savePincodeCourierMapping(pincodeCourierMapping);
        }
        if (flag) {
            addRedirectAlertMessage(new SimpleMessage("Mappings Updated"));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Some of the mappings have dependencies in PDC, Please correct them before saving"));
        }
        return new RedirectResolution(PincodeCourierMappingAction.class, "search").addParameter("pin", pin);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_PCM_UPLOAD}, authActionBean = AdminPermissionAction.class)
    public Resolution uploadExcel() {
      if(fileBean==null){
        addRedirectAlertMessage(new SimpleMessage("Please Select a File!!"));
        return new RedirectResolution(PincodeCourierMappingAction.class,"search").addParameter("pin",pin);
      }
        try {
            Set<PincodeCourierMapping> pincodeCourierMappingSet = new HashSet<PincodeCourierMapping>();
            String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
            File excelFile = new File(excelFilePath);
            excelFile.getParentFile().mkdirs();
            fileBean.save(excelFile);
            pincodeCourierMappingSet = xslPincodeCourierMapping.readPincodeCourierMappings(excelFile);
            for (PincodeCourierMapping pincodeCourierMapping : pincodeCourierMappingSet) {
                pincodeCourierService.savePincodeCourierMapping(pincodeCourierMapping);
            }
        } catch (Exception e) {
            logger.error("Exception while uploading pincode courier mapping excel " + e.getMessage());
        }
        addRedirectAlertMessage(new SimpleMessage("Mapping Updated!!"));
        return new ForwardResolution("/pages/admin/courier/pincodeCourierMapping.jsp");
    }


    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_PCM_DOWNLOAD}, authActionBean = AdminPermissionAction.class)
    public Resolution generateExcel() throws Exception {
        pincodeCourierMappings = pincodeCourierService.getApplicablePincodeCourierMappingList(pincode, Arrays.asList(updateCourier), null, null);
        String excelFilePath = adminDownloadsPath + "/courierExcelFiles/Courier_" + updateCourier.getName() + ".xls";
        final File excelFile = new File(excelFilePath);

        xslPincodeCourierMapping.generatePincodeCourierMappingXsl(pincodeCourierMappings, excelFilePath);

        addRedirectAlertMessage(new SimpleMessage("Download complete"));

        return new Resolution() {

            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
                res.setContentLength((int) excelFile.length());
                res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
                out = res.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[4096];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
        };
    }

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public List<PincodeCourierMapping> getPincodeCourierMappings() {
        return pincodeCourierMappings;
    }

    public void setPincodeCourierMappings(List<PincodeCourierMapping> pincodeCourierMappings) {
        this.pincodeCourierMappings = pincodeCourierMappings;
    }

    public Courier getUpdateCourier() {
        return updateCourier;
    }

    public void setUpdateCourier(Courier updateCourier) {
        this.updateCourier = updateCourier;
    }

    public List<Courier> getCourierList() {
        return courierList;
    }

    public void setCourierList(List<Courier> courierList) {
        this.courierList = courierList;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Map<String, Boolean> getApplicableShipmentServices() {
        return applicableShipmentServices;
    }

    public void setApplicableShipmentServices(Map<String, Boolean> applicableShipmentServices) {
        this.applicableShipmentServices = applicableShipmentServices;
    }

    public List<Courier> getAvailableCouriers() {
        return availableCouriers;
    }

    public void setAvailableCouriers(List<Courier> availableCouriers) {
        this.availableCouriers = availableCouriers;
    }

    public List<PincodeDefaultCourier> getPincodeDefaultCouriers() {
        return pincodeDefaultCouriers;
    }

    public void setPincodeDefaultCouriers(List<PincodeDefaultCourier> pincodeDefaultCouriers) {
        this.pincodeDefaultCouriers = pincodeDefaultCouriers;
    }
}
