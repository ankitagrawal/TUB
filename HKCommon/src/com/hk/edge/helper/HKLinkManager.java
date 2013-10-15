package com.hk.edge.helper;

import java.util.Locale;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import org.apache.commons.lang.StringUtils;

import com.hk.edge.response.menu.CatalogMenuNode;
import com.hk.edge.response.variant.AbstractStoreVariantResponse;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;

/**
 * @author vaibhav.adlakha
 */
public class HKLinkManager {
    private static final String NAV_KEY             = "navKey";
    public static final String  BRAND_LP_IDENTIFIER = "BR-";

    public static String getMenuNodeUrl(CatalogMenuNode catalogMenuNode) {

        if (StringUtils.isNotBlank(catalogMenuNode.getNavKey())) {
            String navKey = catalogMenuNode.getNavKey();
            if (navKey.contains(BRAND_LP_IDENTIFIER)) {
                return getBrandPageUrl(navKey, catalogMenuNode.getUrlFragment());
            }
            return getGenericLandingPageUrl(catalogMenuNode.getUrlFragment(), catalogMenuNode.getNavKey());
        }

        return "#";

        /*
         * StringBuilder menuNodeUrl = new StringBuilder(catalogMenuNode.getUrlFragment()); menuNodeUrl.append("?" +
         * NAV_KEY + "=").append(catalogMenuNode.getNavKey()); RedirectResolution redirectResolution = new
         * RedirectResolution(menuNodeUrl.toString()); return getUrlFromResolution(redirectResolution);
         */

    }

    public static String getGenericLandingPageUrl(String urlFragment, String navKey) {
        StringBuilder url = new StringBuilder(urlFragment);
        url.append("?" + NAV_KEY + "=").append(navKey);

        RedirectResolution redirectResolution = new RedirectResolution(url.toString());

        return getUrlFromResolution(redirectResolution);
    }

    public static String getBrandPageUrl(String brandNavKey, String brandUrlFragment) {
        StringBuilder brandPageUrl = new StringBuilder("/brand");

        brandPageUrl.append(brandUrlFragment);
        brandPageUrl.append("?" + NAV_KEY + "=").append(brandNavKey);

        RedirectResolution redirectResolution = new RedirectResolution(brandPageUrl.toString());

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
        // StringBuilder variantUrl = new StringBuilder("/variant");
        StringBuilder variantUrl = new StringBuilder("/sv");
        variantUrl.append(abstractStoreVariantApiResponse.getUrlFragment());
        variantUrl.append("?" + NAV_KEY + "=").append(abstractStoreVariantApiResponse.getNavKey());

        RedirectResolution redirectResolution = new RedirectResolution(variantUrl.toString());

        return getUrlFromResolution(redirectResolution);
    }

}
