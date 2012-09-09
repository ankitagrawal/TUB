package com.hk.web.action.admin.courier;

import java.io.File;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CityCourierTATService;
import com.hk.admin.util.helper.XslCityCourierTATParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.CityCourierTAT;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 21, 2012
 * Time: 12:06:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.COURIER_DELIVERY_REPORTS})
@Component
public class CityCourierTatAction extends BaseAction {
  @Autowired
  XslCityCourierTATParser xslCityCourierTATParser;
  @Autowired
  CityCourierTATService cityCourierTATService;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String adminUploadsPath;
  private FileBean fileBean;


  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/uploadCityCourierTAT.jsp");
  }

  public Resolution uploadCityExcel() {
    String excelFilePath = adminUploadsPath + "/courierFiles/city/" + System.currentTimeMillis() + ".xls";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();
    Set<CityCourierTAT> citySetFromExcel = null;

    try {
      if(fileBean == null){
      addRedirectAlertMessage(new SimpleMessage("choose Excel sheet path"));
        return new RedirectResolution("/pages/admin/uploadCityCourierTAT.jsp");
      }
      fileBean.save(excelFile);
      citySetFromExcel = xslCityCourierTATParser.readCityCourierTATExcel(excelFile);
      if (null != citySetFromExcel && citySetFromExcel.size() > 0) {
        for (CityCourierTAT CityCourierTAT : citySetFromExcel) {
          cityCourierTATService.saveCityCourierTAT(CityCourierTAT);
        }
        addRedirectAlertMessage(new SimpleMessage("database updated"));
        return new RedirectResolution("/pages/admin/uploadCityCourierTAT.jsp");
      } else {

        addRedirectAlertMessage(new SimpleMessage("Empty Excel Sheet"));
        return new RedirectResolution("/pages/admin/uploadCityCourierTAT.jsp");

      }

    } catch (Exception ex) {
      if (citySetFromExcel == null) {
        addRedirectAlertMessage(new SimpleMessage(ex.getMessage()));
      }

      addRedirectAlertMessage(new SimpleMessage("Error in uploading file"));
      return new RedirectResolution("/pages/admin/uploadCityCourierTAT.jsp");
    }

    finally {
      excelFile.delete();
    }
  }
  public void setFileBean(FileBean fileBean) {
    this.fileBean = fileBean;
  }

}
