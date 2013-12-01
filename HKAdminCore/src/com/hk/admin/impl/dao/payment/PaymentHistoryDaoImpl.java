package com.hk.admin.impl.dao.payment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.payment.PaymentHistoryDao;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class PaymentHistoryDaoImpl extends BaseDaoImpl implements PaymentHistoryDao  {

    @SuppressWarnings("unchecked")
    public List<PaymentHistory> getByPurchaseOrder(PurchaseOrder purchaseOrder) {
        String hqlQuery = " from PaymentHistory paymentHistory where paymentHistory.purchaseOrder = :purchaseOrder" + " order by paymentHistory.id desc";
        List<PaymentHistory> paymentHistories = getSession().createQuery(hqlQuery).setParameter("purchaseOrder", purchaseOrder).list();
        return paymentHistories;
    }

    @SuppressWarnings("unchecked")
    public List<PaymentHistory> getByPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        String hqlQuery = " from PaymentHistory paymentHistory where paymentHistory.purchaseInvoice = :purchaseInvoice order by paymentHistory.id desc";
        //List<PaymentHistory> paymentHistories = getSession().createQuery(hqlQuery).setParameter("purchaseInvoice", purchaseInvoice).list();
        return findByNamedParams(hqlQuery, new String[]{"purchaseInvoice"}, new Object[]{purchaseInvoice});
        //return paymentHistories;
    }
}
