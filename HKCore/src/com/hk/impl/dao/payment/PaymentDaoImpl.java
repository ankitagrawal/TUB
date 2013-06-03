package com.hk.impl.dao.payment;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.FormatUtils;
import com.hk.domain.payment.Payment;
import com.hk.domain.payment.CurrencyConverter;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.payment.PaymentDao;

@SuppressWarnings("unchecked")
@Repository
public class PaymentDaoImpl extends BaseDaoImpl implements PaymentDao {

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


    public CurrencyConverter findLatestConversionRate (String baseCurrencyCode , String foreignCurrencyCode ){
//         String query = "from CurrencyConverter cc where cc.updateDate in ( select max( cc1.updateDate )from CurrencyConverter  cc1 where  cc1.baseCurrencyCode = baseCurrencyCode  and  cc1.foreignCurrencyCode = foreignCurrencyCode) ";
          String query = "from CurrencyConverter cc where  cc.baseCurrencyCode = baseCurrencyCode  and  cc.foreignCurrencyCode = foreignCurrencyCode";
          return (CurrencyConverter) getSession().createQuery(query).uniqueResult();
    }

    @Override
    public List<Payment> listByRRN(String rrn) {
        return (List<Payment>) getSession().createQuery("from Payment p where p.rrn = :rrn").setString("rrn",rrn);
    }

}
