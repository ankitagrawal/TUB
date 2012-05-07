package com.hk.db.seed.courier;

import java.util.ArrayList;
import java.util.List;

import com.hk.constants.shipment.EnumCourier;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.courier.Courier;

public class CourierSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        Courier courier = new Courier();
        courier.setName(name);
        courier.setId(id);
        save(courier);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumCourier enumCourier : EnumCourier.values()) {

            if (pkList.contains(enumCourier.getId()))
                throw new RuntimeException("Duplicate key " + enumCourier.getId());
            else
                pkList.add(enumCourier.getId());

            insert(enumCourier.getName(), enumCourier.getId());
        }
    }

}
