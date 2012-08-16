package com.hk.db.seed.subscription;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.subscription.SubscriptionStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/14/12
 * Time: 2:08 AM
 */
@Component
public class SubscriptionStatusSeedData extends BaseSeedData {
    public void insert(java.lang.String name, java.lang.Long id) {
        SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
        subscriptionStatus.setName(name);
        subscriptionStatus.setId(id);
        save(subscriptionStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumSubscriptionStatus enumSubscriptionStatus : EnumSubscriptionStatus.values()) {

            if (pkList.contains(enumSubscriptionStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumSubscriptionStatus.getId());
            else
                pkList.add(enumSubscriptionStatus.getId());

            insert(enumSubscriptionStatus.getName(), enumSubscriptionStatus.getId());
        }
    }
}
