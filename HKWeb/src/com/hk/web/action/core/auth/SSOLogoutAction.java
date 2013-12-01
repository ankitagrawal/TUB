package com.hk.web.action.core.auth;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 1/6/13
 * Time: 3:08 PM
 */
@Component
public class SSOLogoutAction extends BaseAction {

    private String                     apiKey;

    private String                     redirectUrl;

    public Resolution pre() {
        getSubject().logout();
        if(!StringUtils.isEmpty(apiKey)&&!StringUtils.isEmpty(redirectUrl)) {

            return new RedirectResolution(redirectUrl, false);
        }else{
            return new RedirectResolution(SSOLoginAction.class);
        }

    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
