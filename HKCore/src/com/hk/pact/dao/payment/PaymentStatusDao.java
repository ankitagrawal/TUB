package com.hk.pact.dao.payment;

import java.util.List;

import com.hk.domain.core.PaymentStatus;
import com.hk.pact.dao.BaseDao;

public interface PaymentStatusDao extends BaseDao {

    public PaymentStatus getPaymentStatusById(Long paymentStatusId);

    public List<PaymentStatus> listWorkingPaymentStatuses();

    public List<PaymentStatus> listActionablePaymentStatuses();

    List<PaymentStatus> listSuccessfulPaymentStatuses();
}
