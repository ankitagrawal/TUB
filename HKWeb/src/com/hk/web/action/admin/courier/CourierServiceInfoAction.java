package com.hk.web.action.admin.courier;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.util.XslGenerator;
import com.hk.web.BatchProcessWorkManager;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_ORDERS })
@Component
public class CourierServiceInfoAction extends BaseAction {

    private static Logger            logger             = LoggerFactory.getLogger(CourierServiceInfoAction.class);

    @Autowired
    XslGenerator                     xslGenerator;
    @Autowired
    BatchProcessWorkManager          workManager;
    @Autowired
    CourierServiceInfoDao            courierServiceInfoDao;
    @Autowired
    PincodeDao                       pincodeDao;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                           adminDownloadsPath;

    Courier                          courier;
    CourierServiceInfo               courierServiceInfo;
    String                           pincode;
    /*private List<CourierServiceInfo> courierServiceList = new ArrayList<CourierServiceInfo>();*/

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                           adminUploadsPath;

    @Autowired
    XslParser                        xslParser;

    FileBean                         fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_COURIER_INFO }, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_COURIER_INFO }, authActionBean = AdminPermissionAction.class)
    public Resolution generateCourierServiceInfoExcel() throws Exception {
        String courierName = "All";
        List<CourierServiceInfo> courierServiceInfoList = new ArrayList<CourierServiceInfo>();
        if (courier != null) {
            courierServiceInfoList = courierServiceInfoDao.getCourierServiceInfo(courier.getId());
            courierName = courier.getName();
        } else {
            courierServiceInfoList = courierServiceInfoDao.getAll(CourierServiceInfo.class);
        }
        String excelFilePath = adminDownloadsPath + "/courierExcelFiles/Courier_" + courierName + ".xls";
        final File excelFile = new File(excelFilePath);

        xslGenerator.generateCouerierServiceInfoXsl(courierServiceInfoList, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

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

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_COURIER_INFO }, authActionBean = AdminPermissionAction.class)
    public Resolution uploadCourierServiceInfoExcel() throws Exception {

        String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        CourierServiceInfo tmpObj = null;
        try {
            Set<CourierServiceInfo> courierServiceInfoSet = xslParser.readCourierServiceInfoList(excelFile);
            for (CourierServiceInfo courierServiceInfo : courierServiceInfoSet) {
                tmpObj = courierServiceInfo;
                CourierServiceInfo tmpObj2 = courierServiceInfoDao.getCourierServiceByPincodeAndCourierWithoutCOD(courierServiceInfo.getCourier().getId(),
                        courierServiceInfo.getPincode().getPincode().toString());
                if (tmpObj2 != null) {
                    if (courierServiceInfo.isDeleted()) {
                        courierServiceInfoDao.delete(tmpObj2);
                    } else {
                        tmpObj2.setCodAvailable(courierServiceInfo.isCodAvailable());
                        tmpObj2.setGroundShippingAvailable(courierServiceInfo.isGroundShippingAvailable());
                        tmpObj2.setCodAvailableOnGroundShipping(courierServiceInfo.isCodAvailableOnGroundShipping());
                        if (courierServiceInfo.isCodAvailableOnGroundShipping()){
                           tmpObj2.setCodAvailable(true);
                        }
                        courierServiceInfoDao.save(tmpObj2);
                    }
                } else if (courierServiceInfo != null) {
                    courierServiceInfoDao.save(courierServiceInfo);
                }
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed for -  " + tmpObj.getPincode() + "; courier - " + tmpObj.getCourier().getId()));
            return new ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp");
        }

        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp");
    }

    public Resolution save() {

        Pincode pincodeObj = pincodeDao.getByPincode(pincode);

        System.out.print(pincode);

        if (pincodeObj == null) {
            addRedirectAlertMessage(new SimpleMessage("This pincode is not in the master list, add it there first."));
        } else {

            CourierServiceInfo courierServiceInfoLocal = courierServiceInfoDao.getCourierServiceByPincodeAndCourierWithoutCOD(courierServiceInfo.getCourier().getId(), pincode);
            if (courierServiceInfoLocal != null) {
                courierServiceInfoLocal.setCodAvailable(courierServiceInfo.isCodAvailable());
                courierServiceInfoLocal.setGroundShippingAvailable(courierServiceInfo.isGroundShippingAvailable());
                courierServiceInfoLocal.setCodAvailableOnGroundShipping(courierServiceInfo.isCodAvailableOnGroundShipping());
                if(courierServiceInfo.isCodAvailableOnGroundShipping()){
                   courierServiceInfoLocal.setCodAvailable(true); 
                }
                courierServiceInfoDao.save(courierServiceInfoLocal);
            } else {
                courierServiceInfo.setPincode(pincodeObj);
                courierServiceInfo.setPreferred(false);
                courierServiceInfo.setPreferredCod(false);
                courierServiceInfo.setDeleted(false);
                courierServiceInfoDao.save(courierServiceInfo);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved in system."));
        return new RedirectResolution(CourierServiceInfoAction.class);
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public CourierServiceInfo getCourierServiceInfo() {
        return courierServiceInfo;
    }

    public void setCourierServiceInfo(CourierServiceInfo courierServiceInfo) {
        this.courierServiceInfo = courierServiceInfo;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public CourierServiceInfoDao getCourierServiceInfoDao() {
        return courierServiceInfoDao;
    }

    public void setCourierServiceInfoDao(CourierServiceInfoDao courierServiceInfoDao) {
        this.courierServiceInfoDao = courierServiceInfoDao;
    }
}