package com.hk.db.seed.courier;

import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.hkDelivery.RunsheetStatus;
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
public class RunsheetStatusSeedData extends BaseSeedData {

     public void insert(String status, Long id) {
        RunsheetStatus runsheetStatus = new RunsheetStatus();
        runsheetStatus.setStatus(status);
        runsheetStatus.setId(id);
        save(runsheetStatus);
    }

    public void invokeInsert() {
        List<Long> runsheetList = new ArrayList<Long>();

        for (EnumRunsheetStatus enumRunsheetStatus : EnumRunsheetStatus.values()) {

            if (runsheetList.contains(enumRunsheetStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumRunsheetStatus.getId());
            else
                runsheetList.add(enumRunsheetStatus.getId());

            insert(enumRunsheetStatus.getStatus(), enumRunsheetStatus.getId());
        }
    }
}