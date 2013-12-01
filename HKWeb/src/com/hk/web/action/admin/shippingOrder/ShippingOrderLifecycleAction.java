package com.hk.web.action.admin.shippingOrder;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LifecycleReason;
import com.hk.domain.user.User;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS}, authActionBean = AdminPermissionAction.class)
@Component
public class ShippingOrderLifecycleAction extends BaseAction {

    private ShippingOrder shippingOrder;
    private Set<ShippingOrderLifecycle> shippingOrderLifeCycles = new TreeSet<ShippingOrderLifecycle>();

    @Autowired
    UserService userService;
    @Autowired
    ShippingOrderLifecycleDao shippingOrderLifecycleDao;


    @Validate(required = true)
    private String comment;

    LifecycleReason lifecycleReason;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        setActivitiesForView();
        return new ForwardResolution("/pages/admin/shippingOrder/shippingOrderLifecycle.jsp");
    }

    private void setActivitiesForView() {
        if (shippingOrder != null) {
            for (ShippingOrderLifecycle lifecycle : shippingOrder.getShippingOrderLifecycles()) {
                shippingOrderLifeCycles.add(lifecycle);
            }
        }
    }

    public Resolution saveComment() {
        setActivitiesForView();
        User loggedOnUser = userService.getLoggedInUser();

        ShippingOrderLifecycle shippingOrderLifecycle = new ShippingOrderLifecycle();
        shippingOrderLifecycle.setOrder(shippingOrder);
        shippingOrderLifecycle.setShippingOrderLifeCycleActivity(getBaseDao().get(ShippingOrderLifeCycleActivity.class, EnumShippingOrderLifecycleActivity.SO_LoggedComment.getId()));
        shippingOrderLifecycle.setUser(loggedOnUser);
        shippingOrderLifecycle.setComments(comment);
        shippingOrderLifecycle.setActivityDate(new Date());
        shippingOrderLifecycleDao.save(shippingOrderLifecycle);

        addRedirectAlertMessage(new SimpleMessage("Comment saved successfully."));
        return new RedirectResolution(ShippingOrderLifecycleAction.class).addParameter("shippingOrder", shippingOrder);
    }

    @DontValidate
    public Resolution logReasonForAnActivity() {
        getBaseDao().save(lifecycleReason);
        addRedirectAlertMessage(new SimpleMessage("Reason added successfully."));
        setActivitiesForView();
        return new RedirectResolution(ShippingOrderLifecycleAction.class).addParameter("shippingOrder", shippingOrder);
    }

    public LifecycleReason getLifecycleReason() {
        return lifecycleReason;
    }

    public void setLifecycleReason(LifecycleReason lifecycleReason) {
        this.lifecycleReason = lifecycleReason;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<ShippingOrderLifecycle> getShippingOrderLifeCycles() {
        return shippingOrderLifeCycles;
    }

    public void setShippingOrderLifeCycles(Set<ShippingOrderLifecycle> shippingOrderLifeCycles) {
        this.shippingOrderLifeCycles = shippingOrderLifeCycles;
    }
}