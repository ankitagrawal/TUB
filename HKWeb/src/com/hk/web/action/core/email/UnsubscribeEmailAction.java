package com.hk.web.action.core.email;

import com.hk.constants.core.Keys;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.jboss.resteasy.client.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailRecepient;
import com.hk.pact.dao.email.EmailRecepientDao;

@Component
public class UnsubscribeEmailAction extends BaseAction {
  @Autowired
  UserService userService;

  @Value("#{hkEnvProps['" + Keys.Env.hkEmailApiUrl + "']}")
  private String hkEmailApiUrl = null;

  @Validate(required = true)
  private String unsubscribeToken;

  public Resolution pre() {
      boolean success = false;
      try{
          ClientRequest cr = new ClientRequest("hkEmailApiUrl" + "/unsubscribe/");
          String result = cr.get(String.class).getEntity();
          success = Boolean.parseBoolean(result);
      }catch (Exception ex){
          success = false;
      }
    if(!success) {
      addRedirectAlertMessage(new SimpleMessage("Unable to unsubscribe from HealthKart. Please try again!!"));
    } else {
      addRedirectAlertMessage(new SimpleMessage("You have been unsubscribed successfully."));
    }
    return new ForwardResolution("/pages/unsubscribeEmail.jsp");
  }

  public void setUnsubscribeToken(String unsubscribeToken) {
    this.unsubscribeToken = unsubscribeToken;
  }
}

