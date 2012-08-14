package com.hk.web.action.admin.newsletter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.util.HKFileUtils;
import com.hk.web.AppConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.SEND_MARKETING_MAILS}, authActionBean = AdminPermissionAction.class)
@Component
public class EmailNewsletterAdmin extends BaseAction {

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/newsletterAdmin.jsp");
  }

  public Resolution getReadMeFileContents(){
    File readMe = new File(AppConstants.appBasePath + "/freemarker/readMeForEmailCampaignAmazon.txt");
    Map<String,String> datamap = new HashMap<String,String>();
    datamap.put("readMeContents", HKFileUtils.fileToString(readMe));
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Read me contents generated", datamap);
    return new JsonResolution(healthkartResponse);
  }
}
