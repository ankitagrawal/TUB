package com.hk.impl.dao.payment;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.core.PaymentStatus;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.payment.PaymentStatusDao;

@Repository
public class PaymentStatusDaoImpl extends BaseDaoImpl implements PaymentStatusDao {

    public PaymentStatus getPaymentStatusById(Long paymentStatusId) {
        return get(PaymentStatus.class, paymentStatusId);
    }

    @SuppressWarnings("unchecked")
    public List<PaymentStatus> listWorkingPaymentStatuses() {
        List<Long> paymentStatusIds = new ArrayList<Long>();

        paymentStatusIds.add(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());
        paymentStatusIds.add(EnumPaymentStatus.ON_DELIVERY.getId());
        paymentStatusIds.add(EnumPaymentStatus.SUCCESS.getId());
        paymentStatusIds.add(EnumPaymentStatus.REQUEST.getId());
        paymentStatusIds.add(EnumPaymentStatus.ERROR.getId());

        DetachedCriteria criteria = DetachedCriteria.forClass(PaymentStatus.class);
        criteria.add(Restrictions.in("id", paymentStatusIds));

        return findByCriteria(criteria);
    }

    @Override
    public List<PaymentStatus> listActionablePaymentStatuses() {
        List<Long> paymentStatusIds = new ArrayList<Long>();

        paymentStatusIds.add(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());
        paymentStatusIds.add(EnumPaymentStatus.REQUEST.getId());
        paymentStatusIds.add(EnumPaymentStatus.ERROR.getId());

        DetachedCriteria criteria = DetachedCriteria.forClass(PaymentStatus.class);
        criteria.add(Restrictions.in("id", paymentStatusIds));

        return findByCriteria(criteria);
    }

    @Override
    public List<PaymentStatus> listSuccessfulPaymentStatuses() {
        List<Long> paymentStatusIds = new ArrayList<Long>();

        paymentStatusIds.add(EnumPaymentStatus.SUCCESS.getId());
        paymentStatusIds.add(EnumPaymentStatus.ON_DELIVERY.getId());

        DetachedCriteria criteria = DetachedCriteria.forClass(PaymentStatus.class);
        criteria.add(Restrictions.in("id", paymentStatusIds));

        return findByCriteria(criteria);

    }
}
