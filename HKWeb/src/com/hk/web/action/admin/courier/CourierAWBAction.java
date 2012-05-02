package com.hk.web.action.admin.courier;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.courier.CourierServiceInfoDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Dec 27, 2011
 * Time: 3:04:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CourierAWBAction extends BaseAction{
    private static Logger logger = LoggerFactory.getLogger(CourierServiceInfoAction.class);

      XslGenerator xslGenerator;
      CourierServiceInfoDao courierServiceInfoDao;

    
    //@Named(Keys.Env.adminDownloads)
    String adminDownloadsPath;

    Courier courier;

    
    //@Named(Keys.Env.adminUploads)
    String adminUploadsPath;

    
    XslParser xslParser;

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

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
    public Resolution uploadCourierAWBExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/courierFiles/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        CourierServiceInfo tmpObj = null;
        /*try {
          Set<CourierServiceInfo> courierServiceInfoSet = xslParser.readCourierServiceInfoList(excelFile);
          for (CourierServiceInfo courierServiceInfo : courierServiceInfoSet) {
            tmpObj = courierServiceInfo;
            CourierServiceInfo tmpObj2 = courierServiceInfoDao.findByPincodeAndCourier(courierServiceInfo.getPincode(), courierServiceInfo.getCourier());
            if (tmpObj2 != null) {
              if (courierServiceInfo.isDelete()) {
                courierServiceInfoDao.remove(tmpObj2.getId());
              } else {
                tmpObj2.setCodAvailable(courierServiceInfo.isCodAvailable());
                courierServiceInfoDao.save(tmpObj2);
              }
            } else {
              courierServiceInfoDao.save(courierServiceInfo);
            }
          }
        } catch (Exception e) {
          logger.error("Exception while reading excel sheet.", e);
          addRedirectAlertMessage(new SimpleMessage("Upload failed for -  " + tmpObj.getPincode() + "; length - " + tmpObj.getPincode().length()));
          return new ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp");
        }*/

        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/updateCourierServiceInfo.jsp");
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
