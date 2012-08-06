package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.helper.XslAwbParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.pact.service.UserService;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.exception.DuplicateAwbexception;
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
 * Created by IntelliJ IDEA. User: user Date: Dec 27, 2011 Time: 3:04:14 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class CourierAWBAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(CourierServiceInfoAction.class);
    @Autowired
    XslGenerator xslGenerator;
    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    AwbService awbService;
    @Autowired
    CourierService courierService;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;

    Courier courier;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;
    @Autowired
    XslAwbParser xslAwbParser;

    FileBean fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/updateCourierAWB.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution generateCourierAWBExcel() throws Exception {
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

//    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_COURIER_INFO }, authActionBean = AdminPermissionAction.class)
//    public Resolution uploadCourierAWBExcel() throws Exception {
//        String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
//        File excelFile = new File(excelFilePath);
//        excelFile.getParentFile().mkdirs();
//        fileBean.save(excelFile);
//        CourierServiceInfo tmpObj = null;
//        /*
//         * try { Set<CourierServiceInfo> courierServiceInfoSet = xslParser.readCourierServiceInfoList(excelFile); for
//         * (CourierServiceInfo courierServiceInfo : courierServiceInfoSet) { tmpObj = courierServiceInfo;
//         * CourierServiceInfo tmpObj2 = courierServiceInfoDao.findByPincodeAndCourier(courierServiceInfo.getPincode(),
//         * courierServiceInfo.getCourier()); if (tmpObj2 != null) { if (courierServiceInfo.isDelete()) {
//         * courierServiceInfoDao.remove(tmpObj2.getId()); } else {
//         * tmpObj2.setCodAvailable(courierServiceInfo.isCodAvailable()); courierServiceInfoDao.save(tmpObj2); } } else {
//         * courierServiceInfoDao.save(courierServiceInfo); } } } catch (Exception e) { logger.error("Exception while
//         * reading excel sheet.", e); addRedirectAlertMessage(new SimpleMessage("Upload failed for - " +
//         * tmpObj.getPincode() + "; length - " + tmpObj.getPincode().length())); return new
//         * ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp"); }
//         */
//
//        excelFile.delete();
//        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
//        return new ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp");
//    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution uploadCourierAWBExcel() {
        if ((courier == null) || (fileBean == null)) {
            addRedirectAlertMessage(new SimpleMessage("Select Courier  and choose file to upload"));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }

        String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        List<Awb> awbListFromExcel = null;
         Map<Courier, List<String>> courierIdAwbnumberMap ;
        try {
            fileBean.save(excelFile);
            awbListFromExcel = xslAwbParser.readAwbExcel(excelFile);

            if (null != awbListFromExcel && awbListFromExcel.size() > 0) {
                courierIdAwbnumberMap = xslAwbParser.getCourierWithAllAwbsInExcel();
                List<Awb> alreadyExstingAwbInDbList = new ArrayList<Awb>();
                // Hit DB for every pair in Map <courier,list of AWBs for this courier in excel> 
                for (Courier couriern : courierIdAwbnumberMap.keySet()) {
                    List<Awb> courierAwbList = awbService.getAlreadyPresentAwb(couriern, courierIdAwbnumberMap.get(couriern));
                    if (courierAwbList != null && courierAwbList.size() > 0) {
                        alreadyExstingAwbInDbList.addAll(courierAwbList);
                    }
                }

                if (alreadyExstingAwbInDbList != null && alreadyExstingAwbInDbList.size() > 0) {
                    for (int i = 0; i < alreadyExstingAwbInDbList.size(); i++) {
                        awbListFromExcel.remove(alreadyExstingAwbInDbList.get(i));
                    }

                }
                for (Awb awb : awbListFromExcel) {
                    awbService.save(awb);

                }
                addRedirectAlertMessage(new SimpleMessage("database updated"));
                if (alreadyExstingAwbInDbList != null && alreadyExstingAwbInDbList.size() > 0) {
                    addRedirectAlertMessage(new SimpleMessage("Upload Failed   for below listed  " + alreadyExstingAwbInDbList.size() + " Awb records. They are already present in database"));
                    for (Awb awb : alreadyExstingAwbInDbList) {
                        addRedirectAlertMessage(new SimpleMessage("Awb Number :: " + awb.getAwbNumber() + " ,  Courier Id  ::  " + awb.getCourier().getId()));
                    }

                }
                return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
            } else {

                addRedirectAlertMessage(new SimpleMessage("Empty Excel Sheet"));
                return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");

            }

        }
        catch (DuplicateAwbexception dup) {
            addRedirectAlertMessage(new SimpleMessage(dup.getMessage() + " AWB_Number  : " + dup.getAwbNumber() + "  is present in Excel twice for Courier ::   " + dup.getCourier().getId()));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }
        catch (Exception ex) {
            if (awbListFromExcel == null) {
                addRedirectAlertMessage(new SimpleMessage(ex.getMessage()));

            }
            addRedirectAlertMessage(new SimpleMessage("Error in uploading file"));
            return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");


        }

        finally {
            excelFile.delete();
            courierIdAwbnumberMap=null;
        }
    }


    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
