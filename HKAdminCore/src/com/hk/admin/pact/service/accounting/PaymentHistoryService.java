package com.hk.admin.pact.service.accounting;

import java.util.List;

import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;

/**
 * Created by IntelliJ IDEA. User: Tarun Mittal Date: Dec 21, 2012  |
 * Settings | File Templates.
 */
public interface PaymentHistoryService {

	public PaymentHistory save(PaymentHistory paymentHistory);

	public PaymentHistory save(PaymentHistory paymentHistory, PurchaseInvoice purchaseInvoice);

	public Double getOutstandingAmountForPurchaseInvoice(PurchaseInvoice purchaseInvoice);

	public boolean deletePaymentHistory(PaymentHistory paymentHistory);

	public List<PaymentHistory> getByPurchaseOrder(PurchaseOrder purchaseOrder);

    public List<PaymentHistory> getByPurchaseInvoice(PurchaseInvoice purchaseInvoice);

}