package com.hk.web.action.admin.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dao.marketing.EmailCampaignDao;
import com.hk.domain.email.EmailCampaign;

@Component
public class CreateEmailNewsletterCampaign extends BaseAction {

  @ValidateNestedProperties({
      @Validate(field = "name", required = true),
      @Validate(field = "template", required = true),
      @Validate(field = "minDayGap", required = true)
  })
  EmailCampaign emailCampaign;

  @Autowired
  EmailCampaignDao emailCampaignDao;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/createEmailNewsletter.jsp");
  }

  public Resolution create() {
    emailCampaign = emailCampaignDao.save(emailCampaign);

    addRedirectAlertMessage(new SimpleMessage("Email Campaign created : "+emailCampaign.getName()));
    return new RedirectResolution(EmailNewsletterAdmin.class);
  }

  public EmailCampaign getEmailCampaign() {
    return emailCampaign;
  }

  public void setEmailCampaign(EmailCampaign emailCampaign) {
    this.emailCampaign = emailCampaign;
  }
}
