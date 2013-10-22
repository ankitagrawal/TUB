package com.hk.edge.helper;

import java.util.Locale;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import com.hk.edge.response.menu.CatalogMenuNode;
import com.hk.edge.response.variant.AbstractStoreVariantResponse;
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

    public static String getVariantUrl(AbstractStoreVariantResponse abstractStoreVariantApiResponse) {
        //StringBuilder variantUrl = new StringBuilder("/variant");
        StringBuilder variantUrl = new StringBuilder("/sv");
        variantUrl.append(abstractStoreVariantApiResponse.getUrlFragment());
        variantUrl.append("?" + NAV_KEY + "=").append(abstractStoreVariantApiResponse.getNavKey());

        RedirectResolution redirectResolution = new RedirectResolution(variantUrl.toString());

        return getUrlFromResolution(redirectResolution);
    }

}
