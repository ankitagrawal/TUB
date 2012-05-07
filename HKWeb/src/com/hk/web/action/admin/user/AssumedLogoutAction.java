package com.hk.web.action.admin.user;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.web.action.admin.AdminHomeAction;

@Component
public class AssumedLogoutAction extends BaseAction {

    public Resolution logout() {
//    auditLogger.info("Assumed user ["+getPrincipal().getId()+"] - "+getPrincipal().getEmail()+" identity being released"+" | Session "+getContext().getRequest().getSession().getId());
    getPrincipal().clearAssumedIdentity();
//    auditLogger.info("Admin ["+getPrincipal().getId()+"] - "+getPrincipal().getEmail()+" has released assumed identity and returned to original identity"+" | Session "+getContext().getRequest().getSession().getId());
    return new RedirectResolution(AdminHomeAction.class);
  }

}
