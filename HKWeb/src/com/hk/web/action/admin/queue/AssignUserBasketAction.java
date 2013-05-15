package com.hk.web.action.admin.queue;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.queue.Bucket;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 11/04/13  Time: 09:05
*/
public class AssignUserBasketAction extends BaseAction {


    private List<Bucket> buckets;
    @Validate(required = true)
    private User user;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;
    private Set<Bucket>     bucketSet;

    @DefaultHandler
    public Resolution pre() {
        buckets = user.getBuckets();
        for (Bucket bucket : buckets) {
                    if (user.getBuckets().contains(bucket)) {
                        bucket.setSelected(true);
                    }
//                    buckets.add(bucket);
                }
        return new ForwardResolution("/pages/admin/queue/userBasket.jsp");
    }

    @ValidationMethod(on = "change")
        public void validate() {
            if (buckets == null || buckets.size() == 0) {
                addValidationError("userRoles", new LocalizableError("/ChangeUserRoles.action.no.role.selected"));
            } else {
                bucketSet = new HashSet<Bucket>();
                userDao.refresh(user);
                List<Bucket> userBucketDb = user.getBuckets();
                  for(Bucket bucket : buckets){
                     if(userBucketDb.contains(bucket) && bucket.isSelected()){
                         bucketSet.add(bucket);
                     }
                    else if(!(bucketSet.contains(bucket)) && (bucket.isSelected())){
                         bucketSet.add(bucket);
                     }
                  }
              for(Bucket bucket : userBucketDb){
                if(!buckets.contains(bucket)){
                  bucketSet.add(bucket);
                }
              }
            }

        }

    public Resolution save() {
//        user.setBuckets(buckets);
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
