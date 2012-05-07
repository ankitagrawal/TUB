package com.hk.pact.dao.payment;

import java.util.List;

import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.pact.dao.BaseDao;

public interface PaymentModeDao extends BaseDao {

    public PaymentMode getPaymentModeById(Long paymentModeId);

    public ProductVariantPaymentType getVariantPaymentTypeById(Long variantPaymentModeId);

    public List<PaymentMode> listWorkingPaymentModes();

}
