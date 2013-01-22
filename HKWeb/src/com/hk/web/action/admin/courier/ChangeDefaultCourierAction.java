package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.util.helper.XslPincodeParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.PincodeService;
import com.hk.web.HealthkartResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS})
@Component
public class ChangeDefaultCourierAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(ChangeDefaultCourierAction.class);

    @Autowired
    private PincodeService pincodeService;
    @Autowired
    XslPincodeParser xslPincodeParser;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    FileBean fileBean;

    private String pincodeString;
    private Pincode pincode;
    private List<PincodeDefaultCourier> pincodeDefaultCouriers;
    private List<PincodeCourierMapping> pincodeCourierMappings;
    private List<Courier> availableCouriers;

    Warehouse warehouse;
    //todo courier can we handle null here to show the whole pincode default courier list?
    boolean cod;
    boolean ground;

    @Autowired
    PincodeCourierService pincodeCourierService;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/courier/changeDefaultCourierAction.jsp");
    }

    public Resolution search() {
        pincode = pincodeService.getByPincode(pincodeString);
        if (pincode == null) {
            addRedirectAlertMessage(new SimpleMessage("No such pincode in system"));
            return new RedirectResolution(ChangeDefaultCourierAction.class);
        } else {
            availableCouriers = pincodeCourierService.getApplicableCouriers(pincode, null, null, true);
            pincodeDefaultCouriers = pincodeCourierService.searchPincodeDefaultCourierList(pincode, warehouse, cod, ground);
            pincodeCourierMappings = pincodeCourierService.getApplicablePincodeCourierMappingList(pincode, cod, ground, true);
        }
        return new ForwardResolution("/pages/admin/courier/changeDefaultCourierAction.jsp");
    }

    public Resolution save() {
        String error = "";
        boolean flag = false;
        for (PincodeDefaultCourier pincodeDefaultCourier : pincodeDefaultCouriers) {
            boolean isDefaultCourierApplicable = pincodeCourierService.isDefaultCourierApplicable(pincode, pincodeDefaultCourier.getCourier(), pincodeDefaultCourier.isGroundShipping(), pincodeDefaultCourier.isCod());
            if (!isDefaultCourierApplicable) {
                error += "(Courier:" + pincodeDefaultCourier.getCourier().getName() + ",COD:" + pincodeDefaultCourier.isCod() + ",GroundShipping:" + pincodeDefaultCourier.isGroundShipping() + " is not a serviceable mapping)-";
                flag = true;
            }
            Courier pincodeDefaultCourierDb = pincodeCourierService.getDefaultCourier(pincode, pincodeDefaultCourier.isCod(), pincodeDefaultCourier.isGroundShipping(), pincodeDefaultCourier.getWarehouse());
            if (pincodeDefaultCourierDb != null && pincodeDefaultCourierDb.equals(pincodeDefaultCourier.getCourier())) {
                error += "(Courier:" + pincodeDefaultCourier.getCourier().getName() + ",COD:" + pincodeDefaultCourier.isCod() + ",GroundShipping:" + pincodeDefaultCourier.isGroundShipping() + " is Already present in the Database)-";
                flag = true;
            }
        }

        if (!flag) {
            for (PincodeDefaultCourier pincodeDefaultCourier : pincodeDefaultCouriers) {
                getBaseDao().save(pincodeDefaultCourier);
            }
            addRedirectAlertMessage(new SimpleMessage("Changes Saved"));
            return new RedirectResolution(ChangeDefaultCourierAction.class, "search").addParameter("pincodeString", pincodeString);
        } else {
            addRedirectAlertMessage(new SimpleMessage("Some Mappings are incorrect"));
            return new RedirectResolution(ChangeDefaultCourierAction.class, "search").addParameter("pincodeString", pincodeString);
        }
    }

    @SuppressWarnings("unchecked")
    public Resolution getPincodeJson() {

        HealthkartResponse healthkartResponse = null;
        Map dataMap = new HashMap();
        Pincode pincode = pincodeService.getByPincode(pincodeString);
        if (pincode != null) {
            dataMap.put("pincode", pincode);
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Pincode", dataMap);
            noCache();
        } else {
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid PinCode !!!");
            noCache();
        }
        return new JsonResolution(healthkartResponse);
    }

    public Resolution generatePincodeExcel() throws Exception {
        pincodeDefaultCouriers = pincodeCourierService.searchPincodeDefaultCourierList(pincode, warehouse, cod, ground);
        String excelFilePath = adminDownloadsPath + "/pincodeExcelFiles/pincodesDefaultCouriers_" + System.currentTimeMillis() + ".xls";
        final File excelFile = new File(excelFilePath);

        xslPincodeParser.generatePincodeDefaultCourierXsl(pincodeDefaultCouriers, excelFilePath);
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

    public Resolution uploadPincodeExcel() throws Exception {
        if (fileBean == null) {
            addRedirectAlertMessage(new SimpleMessage("Please chose a file"));
            return new ForwardResolution("/pages/admin/courier/changeDefaultCourierAction.jsp");
        }
        String excelFilePath = adminUploadsPath + "/pincodeExcelFiles/defaultpincodes" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        try {
            Set<PincodeDefaultCourier> pincodeDefaultCourierSet = xslPincodeParser.readDefaultPincodeList(excelFile);
            for (PincodeDefaultCourier defaultCourier : pincodeDefaultCourierSet) {
                getBaseDao().save(defaultCourier);
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
            return new ForwardResolution("/pages/admin/courier/changeDefaultCourierAction.jsp");
        }
        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/courier/changeDefaultCourierAction.jsp");
    }

    public String getPincodeString() {
        return pincodeString;
    }

    public void setPincodeString(String pincodeString) {
        this.pincodeString = pincodeString;
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

    public List<PincodeDefaultCourier> getPincodeDefaultCouriers() {
        return pincodeDefaultCouriers;
    }

    public void setPincodeDefaultCouriers(List<PincodeDefaultCourier> pincodeDefaultCouriers) {
        this.pincodeDefaultCouriers = pincodeDefaultCouriers;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public List<PincodeCourierMapping> getPincodeCourierMappings() {
        return pincodeCourierMappings;
    }

    public void setPincodeCourierMappings(List<PincodeCourierMapping> pincodeCourierMappings) {
        this.pincodeCourierMappings = pincodeCourierMappings;
    }

    public List<Courier> getAvailableCouriers() {
        return availableCouriers;
    }

    public void setAvailableCouriers(List<Courier> availableCouriers) {
        this.availableCouriers = availableCouriers;
    }
}