package com.hk.db.seed.reversePickup;

import com.hk.constants.reversePickup.EnumReversePickupStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/19/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReversePickupStatusSeedData extends BaseSeedData {

    public void insert(String status, Long id) {
        ReversePickupStatus reversePickupStatus = new ReversePickupStatus();
        reversePickupStatus.setId(id);
        reversePickupStatus.setStatus(status);
        save(reversePickupStatus);

    }

    public void invokeInsert() {
        List<Long> reversePickupStatusIdsList = new ArrayList<Long>();

        for (EnumReversePickupStatus enumReversePickupStatus : EnumReversePickupStatus.values()) {
            if (reversePickupStatusIdsList.contains(enumReversePickupStatus.getId())) {
                throw new RuntimeException("Duplicate key " + enumReversePickupStatus.getId());
            } else {
                reversePickupStatusIdsList.add(enumReversePickupStatus.getId());
            }
            insert(enumReversePickupStatus.getStatus(), enumReversePickupStatus.getId());

        }


    }


}
