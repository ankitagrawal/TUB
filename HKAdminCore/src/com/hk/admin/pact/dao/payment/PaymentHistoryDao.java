package com.hk.admin.pact.dao.payment;

import java.util.List;

import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;
import com.hk.pact.dao.BaseDao;

public interface PaymentHistoryDao extends BaseDao {

    public List<PaymentHistory> getByPurchaseOrder(PurchaseOrder purchaseOrder);

    public List<PaymentHistory> getByPurchaseInvoice(PurchaseInvoice purchaseInvoice);
}
