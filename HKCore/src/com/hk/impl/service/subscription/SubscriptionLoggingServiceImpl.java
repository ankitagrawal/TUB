package com.hk.impl.service.subscription;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionLifecycle;
import com.hk.domain.subscription.SubscriptionLifecycleActivity;
import com.hk.domain.user.User;
import com.hk.pact.dao.subscription.SubscriptionLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.subscription.SubscriptionLoggingService;

/**
 * Created with IntelliJ IDEA. User: Pradeep Date: 7/16/12 Time: 12:41 PM
 */
@Service
public class SubscriptionLoggingServiceImpl implements SubscriptionLoggingService {

    @Autowired
    private UserService              userService;
    @Autowired
    private SubscriptionLifecycleDao subscriptionLifecycleDao;

    public SubscriptionLifecycle save(SubscriptionLifecycle subscriptionLifecycle) {
        return subscriptionLifecycleDao.save(subscriptionLifecycle);
    }

    public void logSubscriptionActivity(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity) {
        User loggedOnUser = userService.getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = subscription.getUser();
        }

        SubscriptionLifecycleActivity subscriptionLifecycleActivity = enumSubscriptionLifecycleActivity.asSubscriptionLifecycleActivity();
        logSubscriptionActivity(subscription, loggedOnUser, subscriptionLifecycleActivity, null);
    }

    @Override
    public void logSubscriptionActivityByAdmin(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity) {
        SubscriptionLifecycleActivity subscriptionLifecycleActivity = enumSubscriptionLifecycleActivity.asSubscriptionLifecycleActivity();
        logSubscriptionActivity(subscription, userService.getLoggedInUser(), subscriptionLifecycleActivity, null);
    }

    public void logSubscriptionActivity(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity, String comments) {
        User loggedOnUser = userService.getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = subscription.getUser();
        }

        SubscriptionLifecycleActivity subscriptionLifecycleActivity = enumSubscriptionLifecycleActivity.asSubscriptionLifecycleActivity();
        logSubscriptionActivity(subscription, loggedOnUser, subscriptionLifecycleActivity, comments);
    }

    public void logSubscriptionActivityByAdmin(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity, String comments) {
        // User adminUser = UserCache.getInstance().getAdminUser();
        User adminUser = getUserService().getAdminUser();
        SubscriptionLifecycleActivity subscriptionLifecycleActivity = enumSubscriptionLifecycleActivity.asSubscriptionLifecycleActivity();
        logSubscriptionActivity(subscription, adminUser, subscriptionLifecycleActivity, comments);
    }

    public void logSubscriptionActivity(Subscription subscription, User user, SubscriptionLifecycleActivity subscriptionLifecycleActivity, String comments) {
        SubscriptionLifecycle subscriptionLifecycle = new SubscriptionLifecycle();
        subscriptionLifecycle.setSubscription(subscription);
        subscriptionLifecycle.setSubscriptionLifecycleActivity(subscriptionLifecycleActivity);
        subscriptionLifecycle.setUser(user);
        if (StringUtils.isNotBlank(comments)) {
            subscriptionLifecycle.setComments(comments);
        }
        subscriptionLifecycle.setDate(new Date());
        this.save(subscriptionLifecycle);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public SubscriptionLifecycleDao getSubscriptionLifecycleDao() {
        return subscriptionLifecycleDao;
    }

    public void setSubscriptionLifecycleDao(SubscriptionLifecycleDao subscriptionLifecycleDao) {
        this.subscriptionLifecycleDao = subscriptionLifecycleDao;
    }
}
