package com.hk.db.seed.courier;

import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 9:13:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ConsignmentLifecycleStatusSeedData extends BaseSeedData {

     public void insert(String status, Long id) {
        ConsignmentLifecycleStatus consignmentLifecycleStatus = new ConsignmentLifecycleStatus();
        consignmentLifecycleStatus.setStatus(status);
        consignmentLifecycleStatus.setId(id);
        save(consignmentLifecycleStatus);
    }

    public void invokeInsert() {
        List<Long> consignmentList = new ArrayList<Long>();

        for (EnumConsignmentLifecycleStatus enumConsignmentLifecycleStatus : EnumConsignmentLifecycleStatus.values()) {

            if (consignmentList.contains(enumConsignmentLifecycleStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumConsignmentLifecycleStatus.getId());
            else
                consignmentList.add(enumConsignmentLifecycleStatus.getId());

            insert(enumConsignmentLifecycleStatus.getStatus(), enumConsignmentLifecycleStatus.getId());
        }
    }
}