package com.hk.web.action.core.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.pact.service.subscription.SubscriptionService;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/24/12
 * Time: 5:24 PM
 */
@Secure
@Component
@HttpCache(allow = false)
public class CustomerSubscriptionHistoryAction extends BasePaginatedAction{

    private User user;
    private Page subscriptionPage;
    private List<Subscription> subscriptionList;

    @Autowired
    SubscriptionService subscriptionService;

    public Resolution pre() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            subscriptionPage = subscriptionService.getSubscriptionsForUsers(user, getPageNo(), getPerPage());
            subscriptionList = subscriptionPage.getList();
        }
        return new ForwardResolution("/pages/subscription/customerSubscriptionHistory.jsp");
    }

    public int getPerPageDefault() {
        return 20;
    }

    public int getPageCount() {
        return subscriptionPage != null ? subscriptionPage.getTotalPages() : 0;
    }

    public int getResultCount() {
        return subscriptionPage != null ? subscriptionPage.getTotalResults() : 0;
    }

    public Set<String> getParamSet() {
        return new HashSet<String>();
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }
}
