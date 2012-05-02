package com.hk.web.action.core.email;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dao.email.EmailRecepientDao;
import com.hk.domain.email.EmailRecepient;

@Component
public class UnsubscribeEmailAction extends BaseAction {

   EmailRecepientDao emailRecepientDao;

  @Validate(required = true)
  private String unsubscribeToken;

  public Resolution pre() {
    EmailRecepient emailRecepient = emailRecepientDao.findByUnsubscribeToken(unsubscribeToken);
    if(emailRecepient == null) {
      addRedirectAlertMessage(new SimpleMessage("Invalid email."));
    } else {
      emailRecepient.setSubscribed(false);
      emailRecepientDao.save(emailRecepient);
      addRedirectAlertMessage(new SimpleMessage("You have been unsubscribed successfully."));
    }
    return new ForwardResolution("/pages/unsubscribeEmail.jsp");
  }

  public void setUnsubscribeToken(String unsubscribeToken) {
    this.unsubscribeToken = unsubscribeToken;
  }
}

