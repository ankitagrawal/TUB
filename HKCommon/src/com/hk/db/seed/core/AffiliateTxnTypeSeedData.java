package com.hk.db.seed.core;

import org.springframework.stereotype.Component;

import com.hk.constants.affiliate.EnumAffiliateTxnType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.affiliate.AffiliateTxnType;

@Component
public class AffiliateTxnTypeSeedData extends BaseSeedData {

    public void insert(Long id, java.lang.String name) {
        AffiliateTxnType affiliateTxnType = new AffiliateTxnType();
        affiliateTxnType.setId(id);
        affiliateTxnType.setName(name);
        save(affiliateTxnType);
    }

    public void invokeInsert() {
        for (EnumAffiliateTxnType enumAffiliateTxnType : EnumAffiliateTxnType.values()) {
            AffiliateTxnType affiliateTxnType = getBaseDao().get(AffiliateTxnType.class, enumAffiliateTxnType.getId());
            if (affiliateTxnType == null) {
                insert(enumAffiliateTxnType.getId(), enumAffiliateTxnType.getName());
            } else {
                affiliateTxnType.setName(enumAffiliateTxnType.getName());
                save(affiliateTxnType);
            }
        }
    }
}
