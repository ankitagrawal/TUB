package com.hk.impl.service.subscription;

import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.pact.service.subscription.SubscriptionStatusService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/24/12
 * Time: 5:30 PM
 */
@Service
public class SubscriptionStatusServiceImpl implements SubscriptionStatusService {

    public List<SubscriptionStatus> getSubscriptionStatusesForUsers(){
        List<EnumSubscriptionStatus> subscriptionStatusEnumList=EnumSubscriptionStatus.getStatusForCustomers();
        List<SubscriptionStatus> subscriptionStatusList= new ArrayList<SubscriptionStatus>();
        for(EnumSubscriptionStatus enumSubscriptionStatus:subscriptionStatusEnumList){
            subscriptionStatusList.add(enumSubscriptionStatus.asSubscriptionStatus());
        }
        return subscriptionStatusList;
    }
}
