package com.hk.dao.payment;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.ProductVariantPaymentType;

@SuppressWarnings("unchecked")
@Repository
public class PaymentModeDao extends BaseDaoImpl {

    public PaymentMode getPaymentModeById(Long paymentModeId){
        return get(PaymentMode.class, paymentModeId);
    }
    
    public ProductVariantPaymentType getVariantPaymentTypeById(Long variantPaymentModeId){
        return get(ProductVariantPaymentType.class, variantPaymentModeId);
    }
    
    public List<PaymentMode> listWorkingPaymentModes() {
        List<EnumPaymentMode> workingPaymentModes = EnumPaymentMode.getWorkingPaymentModes();

        List<Long> paymentModeIds = EnumPaymentMode.getPaymentModeIDs(workingPaymentModes);
        Criteria criteria = getSession().createCriteria(PaymentMode.class);
        criteria.add(Restrictions.in("id", paymentModeIds));

        return criteria.list();
    }

    /*
     * public List<PaymentMode> listWorkingPaymentModes() { List<PaymentMode> paymentModeList = new ArrayList<PaymentMode>();
     * //paymentModeList = this.listAll(); paymentModeList.add(this.find(EnumPaymentMode.CashDeposit.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.ChequeDeposit.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.COD.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.COUNTER_CASH.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.FREE_CHECKOUT.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.NEFT.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.TECHPROCESS.getId()));
     * paymentModeList.add(this.find(EnumPaymentMode.CITRUS.getId())); return paymentModeList; }
     */

}
