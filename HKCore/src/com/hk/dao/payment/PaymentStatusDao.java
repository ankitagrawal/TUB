package com.hk.dao.payment;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.core.PaymentStatus;

@Repository
public class PaymentStatusDao extends BaseDaoImpl {

    public PaymentStatus getPaymentStatusById(Long paymentStatusId){
        return get(PaymentStatus.class, paymentStatusId);
    }
    
    @SuppressWarnings("unchecked")
    public List<PaymentStatus> listWorkingPaymentStatuses() {
        List<Long> paymentStatusIds = new ArrayList<Long>();

        paymentStatusIds.add(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());
        paymentStatusIds.add(EnumPaymentStatus.ON_DELIVERY.getId());
        paymentStatusIds.add(EnumPaymentStatus.SUCCESS.getId());

        DetachedCriteria criteria = DetachedCriteria.forClass(PaymentStatus.class);
        criteria.add(Restrictions.in("id", paymentStatusIds));

        return findByCriteria(criteria);
    }
}
