package com.hk.web.action.admin.queue;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.queue.Bucket;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 11/04/13  Time: 09:05
*/
@Secure(hasAnyPermissions = PermissionConstants.UPDATE_USER, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 2, name = "Edit Basket: {user.login}", context = HealthkartConstants.BreadcrumbContext.admin)
public class AssignUserBasketAction extends BaseAction {


    private List<Bucket> buckets = new ArrayList<Bucket>();
    private String userName;
    private String userLogin;

    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    BucketService bucketService;

    @DefaultHandler
    public Resolution pre() {
       User user = getUserService().getUserById(getPrincipal().getId());
        buckets = getBaseDao().getAll(Bucket.class);
        if (user != null) {
            List<Bucket> userBuckets = user.getBuckets();
            if (userBuckets != null && userBuckets.size() > 0) {
                for (Bucket bucket : buckets) {
                    if (userBuckets.contains(bucket)) {
                        bucket.setSelected(true);
                    }
                }
            }
        }
        userName = user.getName();
        userLogin = user.getLogin();
        return new ForwardResolution("/pages/admin/queue/userBasket.jsp");
    }

    public Resolution save() {
        User user = getUserService().getUserById(getPrincipalUser().getId());
        List<Bucket> userBuckets = new ArrayList<Bucket>();
        for (Bucket bucket : buckets) {
            if (bucket.getId() != null) {
                if (bucket.isSelected()) {
                    userBuckets.add(bucketService.getBucketById(bucket.getId()));
                }
            }
        }
        user.setBuckets(userBuckets);
        userService.save(user);
        addRedirectAlertMessage(new SimpleMessage("Buckets Updated Successfully"));
        return new RedirectResolution(AssignUserBasketAction.class).addParameter("user", user.getId());
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
