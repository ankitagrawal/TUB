package com.hk.web.action.admin.queue;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.queue.Bucket;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
 * User: Pratham
 * Date: 11/04/13  Time: 09:05
*/
public class AssignUserBasketAction extends BaseAction {


    List<Bucket> buckets;
    User user;

    @Autowired
    UserService userService;

    @DefaultHandler
    public Resolution pre() {
        buckets = user.getBuckets();
        return new ForwardResolution("/pages/admin/queue/userBasket.jsp");
    }

    public Resolution save() {
        user.setBuckets(buckets);
        userService.save(user);
        return new RedirectResolution(AssignUserBasketAction.class);
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
