package com.hk.dao.payment;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.FormatUtils;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.payment.Payment;

@SuppressWarnings("unchecked")
@Repository
public class PaymentDao extends BaseDaoImpl {

    /*
     * public PaymentDao() { super(Payment.class); }
     */

    @Transactional
    public Payment save(Payment payment) {
        if (payment != null) {
            if (payment.getCreateDate() == null)
                payment.setCreateDate(BaseUtils.getCurrentTimestamp());
            if (payment.getAmount() != null && payment.getAmount() > 0) {
                // rounding off (see FormatUtils.getCurrencyPrecision javadocs)
                payment.setAmount(FormatUtils.getCurrencyPrecision(payment.getAmount()));
            }
        }
        return (Payment) super.save(payment); // To change body of overridden methods use File | Settings | File
                                                // Templates.
    }

    public Payment findByGatewayOrderId(String gatewayOrderId) {
        return (Payment) getSession().createQuery("from Payment p where p.gatewayOrderId = :gatewayOrderId").setString("gatewayOrderId", gatewayOrderId).uniqueResult();
    }

    public List<Payment> listByOrderId(Long orderId) {
        // noinspection unchecked
        return (List<Payment>) getSession().createQuery("from Payment p where p.order.id = :orderId").setLong("orderId", orderId).list();
    }

}
