package com.hk.web.action.admin.catalog.product;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Aug 18, 2012
 * Time: 5:31:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEyeConfigAction {
   @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/ AddEyeConfig.jsp");

    }

}
