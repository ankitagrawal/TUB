package com.hk.web.action.core;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.cache.RoleCache;
import com.hk.constants.core.RoleConstants;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stripesstuff.plugin.security.Secure;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Aug 30, 2013
 * Time: 10:41:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAllRoles = {RoleConstants.GOD})
public class RefreshCacheAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(RefreshCacheAction.class);

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/refreshCache.jsp");
  }

  public Resolution refreshRoles() {
    RoleCache.getInstance().populateRoleCache();
    addRedirectAlertMessage(new SimpleMessage("Refreshed Roles"));
    return new RedirectResolution(RefreshCacheAction.class);
  }
}
