package com.hk.web.action.core.user;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/SslLogin.action", disallowRememberMe = true)
@Component
@HttpCache(allow = false)
public class HelloSslAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/hello.jsp");
  }


}


