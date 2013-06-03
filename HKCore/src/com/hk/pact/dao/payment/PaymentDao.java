package com.hk.pact.dao.payment;

import java.util.List;

import com.hk.domain.payment.Payment;
import com.hk.domain.payment.CurrencyConverter;
import com.hk.pact.dao.BaseDao;

public interface PaymentDao extends BaseDao {

    public Payment save(Payment payment);

    public Payment findByGatewayOrderId(String gatewayOrderId);

    public List<Payment> listByOrderId(Long orderId);

    public CurrencyConverter findLatestConversionRate (String baseCurrencyCode , String foreignCurrencyCode );

    public List<Payment> listByRRN(String rrn);
}
