package com.hk.web.action.core.content.seo;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.content.SeoData;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = PermissionConstants.UPDATE_SEO_METADATA, authActionBean = AdminPermissionAction.class)
@Component
public class SeoAction extends BaseAction {
    private SeoData seoData;


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/editSeoData.jsp");
    }

    public Resolution saveSeoData() {
        getBaseDao().save(seoData);
        return new ForwardResolution("/pages/close.jsp");
    }

    public SeoData getSeoData() {
        return seoData;
    }

    public void setSeoData(SeoData seoData) {
        this.seoData = seoData;
    }

    
    
}
