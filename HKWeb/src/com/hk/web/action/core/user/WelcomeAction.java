package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;

@Secure
public class WelcomeAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/welcome.jsp");
  }

}