package com.hk.db.seed.payment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.PaymentMode;

@Component
public class PaymentModeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        PaymentMode paymentMode = new PaymentMode();
        paymentMode.setName(name);
        paymentMode.setId(id);
        save(paymentMode);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumPaymentMode enumPaymentMode : EnumPaymentMode.values()) {

            if (pkList.contains(enumPaymentMode.getId()))
                throw new RuntimeException("Duplicate key " + enumPaymentMode.getId());
            else
                pkList.add(enumPaymentMode.getId());

            insert(enumPaymentMode.getName(), enumPaymentMode.getId());
        }
    }

}
