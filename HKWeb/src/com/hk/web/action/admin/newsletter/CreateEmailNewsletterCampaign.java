package com.hk.web.action.admin.newsletter;

import java.io.File;
import java.io.IOException;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.web.AppConstants;
import com.hk.constants.core.Keys;

@Component
public class CreateEmailNewsletterCampaign extends BaseAction {

  @ValidateNestedProperties({@Validate(field = "name", required = true), @Validate(field = "template", required = true), @Validate(field = "minDayGap", required = true)})
  EmailCampaign emailCampaign;

  @Autowired
  EmailCampaignDao emailCampaignDao;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                          adminUploadsPath;
  String appBasePath;
  FileBean contentBean;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/createEmailNewsletterAmazon.jsp");
  }

  public Resolution create() throws IOException {
    String contentPath = adminUploadsPath+ "/emailContentFiles/" + emailCampaign.getName() + ".zip";
    File contentFolder = new File(contentPath);
    contentFolder.getParentFile().mkdirs();
    contentBean.save(contentFolder);
    return null;
//    File ftlFile = new File(AppConstants.appBasePath + "/freemarker" + emailCampaign.getTemplate());
//    if (ftlFile.exists()) {
//      emailCampaign = emailCampaignDao.save(emailCampaign);
//    } else {
//      addRedirectAlertMessage(new SimpleMessage("Invalid entry in template! .ftl file does not exist"));
//      return new ForwardResolution("/pages/admin/newsletter/createEmailNewsletter.jsp");
//    }
//
//    addRedirectAlertMessage(new SimpleMessage("Email Campaign created : " + emailCampaign.getName()));
//
//    return new RedirectResolution(EmailNewsletterAdmin.class);
  }

  public EmailCampaign getEmailCampaign() {
    return emailCampaign;
  }

  public void setEmailCampaign(EmailCampaign emailCampaign) {
    this.emailCampaign = emailCampaign;
  }

  public void setContentBean(FileBean contentBean) {
    this.contentBean = contentBean;
  }
}
