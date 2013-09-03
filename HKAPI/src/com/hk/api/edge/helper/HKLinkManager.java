package com.hk.api.edge.helper;

import java.util.Locale;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import com.hk.api.edge.internal.response.menu.CatalogMenuNode;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;

/**
 * @author vaibhav.adlakha
 */
public class HKLinkManager {
    private static final String NAV_KEY = "navKey";

    public static String getMenuNodeUrl(CatalogMenuNode catalogMenuNode) {

        StringBuilder menuNodeUrl = new StringBuilder(catalogMenuNode.getUrlFragment());
        menuNodeUrl.append("?" + NAV_KEY + "=").append(catalogMenuNode.getNavKey());

        RedirectResolution redirectResolution = new RedirectResolution(menuNodeUrl.toString());

        return getUrlFromResolution(redirectResolution);

    }

    private static String getUrlFromResolution(RedirectResolution redirectResolution) {
        String url = redirectResolution.getUrl(Locale.getDefault());
        if (WebContext.getRequest() != null && WebContext.getResponse() != null) {
            return SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, null);
        }
        String contextPath = AppConstants.contextPath;
        return SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, contextPath);
    }

}
