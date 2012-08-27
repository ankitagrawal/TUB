package com.hk.db.seed.email;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumEmailType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.EmailType;

@Component
public class EmailTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        EmailType emailType = new EmailType();
        emailType.setName(name);
        emailType.setId(id);
        save(emailType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumEmailType enumEmailType : EnumEmailType.values()) {

            if (pkList.contains(enumEmailType.getId()))
                throw new RuntimeException("Duplicate key " + enumEmailType.getId());
            else
                pkList.add(enumEmailType.getId());

            insert(enumEmailType.getName(), enumEmailType.getId());
        }
    }

}
