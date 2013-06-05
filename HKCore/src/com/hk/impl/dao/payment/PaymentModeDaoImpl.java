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

    @Override
    public List<Payment> searchPayments(Order order, List<PaymentStatus> paymentStatuses, String gatewayOrderId, List<PaymentMode> paymentModes, Date startCreateDate, Date endCreateDate, List<OrderStatus> orderStatuses) {
        DetachedCriteria paymentCriteria = DetachedCriteria.forClass(Payment.class);
        if(paymentModes != null && !paymentModes.isEmpty()){
            paymentCriteria.add(Restrictions.in("paymentMode", paymentModes));
        }
        if(paymentStatuses != null && !paymentStatuses.isEmpty()){
            paymentCriteria.add(Restrictions.in("paymentStatus", paymentStatuses));
        }
        if(gatewayOrderId != null){
            paymentCriteria.add(Restrictions.eq("gatewayOrderId", gatewayOrderId));
        }
        if(order != null){
            paymentCriteria.add(Restrictions.eq("order", order));
        }
        if(startCreateDate != null && endCreateDate != null){
            paymentCriteria.add(Restrictions.between("createDate", startCreateDate, endCreateDate));
        }
        if(orderStatuses != null){
            DetachedCriteria orderCriteria = paymentCriteria.createCriteria("order");
            orderCriteria.add(Restrictions.in("orderStatus", orderStatuses));
        }
        return findByCriteria(paymentCriteria);
    }

}
