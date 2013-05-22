package com.hk.web.action.admin.queue;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.queue.Bucket;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 11/04/13  Time: 09:05
*/
public class AssignUserBasketAction extends BaseAction {


    private List<Bucket> buckets = new ArrayList<Bucket>();

    @Validate(required = true)
    private User user;

    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    BucketService bucketService;

    @DefaultHandler
    public Resolution pre() {
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
        return new ForwardResolution("/pages/admin/queue/userBasket.jsp");
    }

    public Resolution save() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
