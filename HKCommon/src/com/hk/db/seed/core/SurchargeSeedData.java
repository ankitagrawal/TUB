package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumSurcharge;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.Surcharge;

@Component
public class SurchargeSeedData extends BaseSeedData {


    public void insert(Long id, String name, Double value) {
        Surcharge surcharge = new Surcharge();
        surcharge.setName(name);
        surcharge.setId(id);
        surcharge.setValue(value);
        save(surcharge);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumSurcharge enumSurcharge : EnumSurcharge.values()) {

            if (pkList.contains(enumSurcharge.getId()))
                throw new RuntimeException("Duplicate key " + enumSurcharge.getId());
            else
                pkList.add(enumSurcharge.getId());

            insert(enumSurcharge.getId(), enumSurcharge.getName(), enumSurcharge.getValue());
        }
    }

}
