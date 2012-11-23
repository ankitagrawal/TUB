package com.hk.admin.pact.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import java.util.Date;
import java.util.List;

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