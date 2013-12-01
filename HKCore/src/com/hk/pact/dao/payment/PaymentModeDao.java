package com.hk.pact.dao.payment;

import java.util.Date;
import java.util.List;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.pact.dao.BaseDao;

public interface PaymentModeDao extends BaseDao {

    public PaymentMode getPaymentModeById(Long paymentModeId);

    public ProductVariantPaymentType getVariantPaymentTypeById(Long variantPaymentModeId);

    public List<PaymentMode> listWorkingPaymentModes();

}
