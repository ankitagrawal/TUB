package com.hk.rest.mobile.service.action;

import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.service.ServiceLocatorFactory;
import com.hk.constants.core.Keys;
import com.shiro.PrincipalImpl;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.CurrencyFormatter;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 2, 2012
 * Time: 9:37:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class MBaseAction extends BaseAction {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    DecimalFormat priceFormat = new DecimalFormat("###,###,###.##");
    public void noCache(@Context HttpServletResponse response) {
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "private");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        response.addHeader("Cache-Control", "s-maxage=0");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Cache-Control", "proxy-revalidate");
    }

    public void addHeaderAttributes(@Context HttpServletResponse response){
        response.addHeader(MHKConstants.ACCESS_CONTROL_ALLOW_CREDENTIALS, MHKConstants.TRUE);
        response.addHeader(MHKConstants.ACCESS_CONTROL_ALLOW_METHODS, MHKConstants.ACCESS_CONTROL_ALLOW_METHODS_LIST);
        response.addHeader(MHKConstants.ACCESS_CONTROL_ALLOW_ORIGIN, MHKConstants.STAR);
    }
    public String getImageUrl(){
        String imageUrl = "";
        String host = (String) ServiceLocatorFactory.getProperty(Keys.Env.virtualHostImage);
        String sslHost = (String) ServiceLocatorFactory.getProperty(Keys.Env.virtualHostImageSsl);

        String useVirtualHostsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.useVirtualHosts);
        String useSslVirtualHostsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.useSslVirtualHosts);

        boolean useVirtualHosts =  Boolean.parseBoolean(useVirtualHostsString);
        boolean useSslVirtualHosts = Boolean.parseBoolean(useSslVirtualHostsString);

            if (useVirtualHosts) {
                if (useSslVirtualHosts) {
                    imageUrl = sslHost;
                }else imageUrl = host;
            }
            else {
                    imageUrl = host;

            }
     return imageUrl+MHKConstants.IMAGEURL;
     //return "http://img.healthkart.com
    }
}
