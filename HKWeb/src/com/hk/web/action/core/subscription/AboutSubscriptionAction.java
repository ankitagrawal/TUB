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
    private boolean faqs=false;
    public Resolution pre(){
        faqs=false;
        return new ForwardResolution("/pages/subscription/aboutSubscription.jsp");
    }
    public Resolution faqs(){
        faqs=true;
        return new ForwardResolution("/pages/subscription/aboutSubscription.jsp");
    }

    public boolean isFaqs() {
        return faqs;
    }

    public void setFaqs(boolean faqs) {
        this.faqs = faqs;
    }
}
