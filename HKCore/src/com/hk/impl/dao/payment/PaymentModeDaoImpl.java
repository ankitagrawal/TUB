package com.hk.impl.dao.payment;

import java.util.Date;
import java.util.List;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.payment.PaymentModeDao;

@SuppressWarnings("unchecked")
@Repository
public class PaymentModeDaoImpl extends BaseDaoImpl implements PaymentModeDao {

    public PaymentMode getPaymentModeById(Long paymentModeId) {
        return get(PaymentMode.class, paymentModeId);
    }

    public ProductVariantPaymentType getVariantPaymentTypeById(Long variantPaymentModeId) {
        return get(ProductVariantPaymentType.class, variantPaymentModeId);
    }

    public List<PaymentMode> listWorkingPaymentModes() {
        List<EnumPaymentMode> workingPaymentModes = EnumPaymentMode.getWorkingPaymentModes();

        List<Long> paymentModeIds = EnumPaymentMode.getPaymentModeIDs(workingPaymentModes);
        Criteria criteria = getSession().createCriteria(PaymentMode.class);
        criteria.add(Restrictions.in("id", paymentModeIds));

        return criteria.list();
    }


}
