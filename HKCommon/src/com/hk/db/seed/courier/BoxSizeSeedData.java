package com.hk.db.seed.courier;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.shipment.EnumBoxSize;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.courier.BoxSize;

/**
 * Generated
 */
@Component
public class BoxSizeSeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        BoxSize boxSize = new BoxSize();
        boxSize.setName(name);
        boxSize.setId(id);
        save(boxSize);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumBoxSize enumBoxSize : EnumBoxSize.values()) {

            if (pkList.contains(enumBoxSize.getId()))
                throw new RuntimeException("Duplicate key " + enumBoxSize.getId());
            else
                pkList.add(enumBoxSize.getId());

            insert(enumBoxSize.getName(), enumBoxSize.getId());
        }
    }

}