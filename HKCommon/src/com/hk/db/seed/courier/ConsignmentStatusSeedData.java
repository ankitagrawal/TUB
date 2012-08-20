 package com.hk.db.seed.courier;

import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.hkDelivery.ConsignmentStatus;
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
public class ConsignmentStatusSeedData extends BaseSeedData {

     public void insert(String status, Long id) {
        ConsignmentStatus consignmentStatus = new ConsignmentStatus();
        consignmentStatus.setStatus(status);
        consignmentStatus.setId(id);
        save(consignmentStatus);
    }

    public void invokeInsert() {
        List<Long> consignmentList = new ArrayList<Long>();

        for (EnumConsignmentStatus enumConsignmentStatus : EnumConsignmentStatus.values()) {

            if (consignmentList.contains(enumConsignmentStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumConsignmentStatus.getId());
            else
                consignmentList.add(enumConsignmentStatus.getId());

            insert(enumConsignmentStatus.getStatus(), enumConsignmentStatus.getId());
        }
    }
}