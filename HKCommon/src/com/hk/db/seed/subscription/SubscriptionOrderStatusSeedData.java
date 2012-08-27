package com.hk.db.seed.subscription;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.subscription.SubscriptionOrderStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/14/12
 * Time: 2:12 AM
 */
@Component
public class SubscriptionOrderStatusSeedData extends BaseSeedData {
    public void insert(java.lang.String name, java.lang.Long id) {
        SubscriptionOrderStatus subscriptionOrderStatus = new SubscriptionOrderStatus();
        subscriptionOrderStatus.setName(name);
        subscriptionOrderStatus.setId(id);
        save(subscriptionOrderStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumSubscriptionOrderStatus enumSubscriptionOrderStatus : EnumSubscriptionOrderStatus.values()) {

            if (pkList.contains(enumSubscriptionOrderStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumSubscriptionOrderStatus.getId());
            else
                pkList.add(enumSubscriptionOrderStatus.getId());

            insert(enumSubscriptionOrderStatus.getName(), enumSubscriptionOrderStatus.getId());
        }
    }
}
