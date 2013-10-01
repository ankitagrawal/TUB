package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Dixit
 * Date: 9/30/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */

@UrlBinding("/store/splenda")


public class SplendaPageAction extends BaseAction {

    @DefaultHandler
    public Resolution splenda() {
        return new ForwardResolution("/store/splenda/index.jsp");
    }
}
