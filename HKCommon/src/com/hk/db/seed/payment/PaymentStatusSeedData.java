package com.hk.db.seed.payment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.PaymentStatus;

@Component
public class PaymentStatusSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setName(name);
        paymentStatus.setId(id);
        save(paymentStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumPaymentStatus enumPaymentStatus : EnumPaymentStatus.values()) {

            if (pkList.contains(enumPaymentStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumPaymentStatus.getId());
            else
                pkList.add(enumPaymentStatus.getId());

            insert(enumPaymentStatus.getName(), enumPaymentStatus.getId());
        }
    }

}
