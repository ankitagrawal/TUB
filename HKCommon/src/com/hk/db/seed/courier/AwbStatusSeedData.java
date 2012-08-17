package com.hk.db.seed.courier;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.courier.EnumAwbStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.courier.AwbStatus;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 9:13:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AwbStatusSeedData extends BaseSeedData {

     public void insert(java.lang.String status, java.lang.Long id) {
        AwbStatus awbStatus = new AwbStatus();
        awbStatus.setStatus(status);
        awbStatus.setId(id);
        save(awbStatus);
    }

    public void invokeInsert() {
        List<Long> awbList = new ArrayList<Long>();

        for (EnumAwbStatus enumAwbStatus : EnumAwbStatus.values()) {

            if (awbList.contains(enumAwbStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumAwbStatus.getId());
            else
                awbList.add(enumAwbStatus.getId());

            insert(enumAwbStatus.getStatus(), enumAwbStatus.getId());
        }
    }
}

