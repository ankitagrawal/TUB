package com.hk.web.action.core.email;

import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailRecepient;
import com.hk.pact.dao.email.EmailRecepientDao;

@Component
public class UnsubscribeEmailAction extends BaseAction {
  @Autowired
  UserService userService;

  @Validate(required = true)
  private String unsubscribeToken;

  public Resolution pre() {
    User emailRecepient = userService.findByUnsubscribeToken(unsubscribeToken);
    if(emailRecepient == null) {
      addRedirectAlertMessage(new SimpleMessage("Invalid email."));
    } else {
      emailRecepient.setSubscribed(false);
      userService.save(emailRecepient);
      addRedirectAlertMessage(new SimpleMessage("You have been unsubscribed successfully."));
    }
    return new ForwardResolution("/pages/unsubscribeEmail.jsp");
  }

  public void setUnsubscribeToken(String unsubscribeToken) {
    this.unsubscribeToken = unsubscribeToken;
  }
}

