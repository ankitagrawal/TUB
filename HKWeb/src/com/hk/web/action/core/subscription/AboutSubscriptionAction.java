package com.hk.web.action.core.subscription;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/25/12
 * Time: 11:08 AM
 */
public class AboutSubscriptionAction extends BaseAction {
    public Resolution pre(){
        return new ForwardResolution("/pages/subscription/aboutSubscription.jsp");
    }
}
