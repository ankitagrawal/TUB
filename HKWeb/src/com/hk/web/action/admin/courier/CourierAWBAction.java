package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.util.helper.XslAwbParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.util.XslGenerator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
  AwbDao awbDao;


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
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();
    Set<Awb> awbSetFromExcel = null;
    try {
      fileBean.save(excelFile);
      awbSetFromExcel = xslAwbParser.readAwbExcel(excelFile);
      if (null != awbSetFromExcel && awbSetFromExcel.size() > 0) {
        List<Awb> awbDatabase = awbDao.getAvailableAwbForCourierByWarehouseAndCod(courier, null, null);

        List<String> commonCourierIdsList = XslAwbParser.getIntersection(awbDatabase, new ArrayList(awbSetFromExcel));

        if (commonCourierIdsList.size() > 0) {
          addRedirectAlertMessage(new SimpleMessage("Upload Failed   Courier Ids" + "     " + commonCourierIdsList + "   " +
              "     are already present and used in database"));
          return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
        }

        for (Awb awb : awbSetFromExcel) {
          awbDao.save(awb);

        }

        addRedirectAlertMessage(new SimpleMessage("database updated"));
        return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");
      } else {

        addRedirectAlertMessage(new SimpleMessage("Empty Excel Sheet"));
        return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");

      }

    } catch (Exception ex) {

      if (awbSetFromExcel == null) {
        addRedirectAlertMessage(new SimpleMessage(ex.getMessage()));

      }
      addRedirectAlertMessage(new SimpleMessage("Error in uploading file"));
      return new RedirectResolution("/pages/admin/updateCourierAWB.jsp");


    }

    finally {
      excelFile.delete();
    }
  }


  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }
}
