package com.hk.web.action.faq;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.RedirectResolution;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 12, 2012
 * Time: 3:20:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FaqAction extends BaseAction{
    @DefaultHandler
    public Resolution pre(){
        return new RedirectResolution("pages/faq/faq.jsp");     
    }
}
