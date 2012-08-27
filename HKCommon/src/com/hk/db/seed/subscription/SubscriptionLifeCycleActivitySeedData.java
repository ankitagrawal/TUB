package com.hk.db.seed.subscription;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.subscription.SubscriptionLifecycleActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/16/12
 * Time: 12:25 PM
 */
@Component
public class SubscriptionLifeCycleActivitySeedData extends BaseSeedData {
    public void insert(java.lang.String name, java.lang.Long id) {
        SubscriptionLifecycleActivity subscriptionLifecycleActivity = new SubscriptionLifecycleActivity();
        subscriptionLifecycleActivity.setName(name);
        subscriptionLifecycleActivity.setId(id);
        save(subscriptionLifecycleActivity);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity : EnumSubscriptionLifecycleActivity.values()) {

            if (pkList.contains(enumSubscriptionLifecycleActivity.getId()))
                throw new RuntimeException("Duplicate key " + enumSubscriptionLifecycleActivity.getId());
            else
                pkList.add(enumSubscriptionLifecycleActivity.getId());

            insert(enumSubscriptionLifecycleActivity.getName(), enumSubscriptionLifecycleActivity.getId());
        }
    }
}
