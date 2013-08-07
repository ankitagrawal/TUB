package com.hk.admin.pact.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Tarun Mittal Date: Mar 13, 2012 Time: 7:33:48 AM To change this template use File |
 * Settings | File Templates.
 */
public interface PurchaseInvoiceService {

	public void markPISettledAndClosedFromPaymentHistory(PurchaseInvoice purchaseInvoice, PaymentHistory paymentHistory);

	public Page searchPurchaseInvoice(PurchaseInvoice purchaseInvoice, PurchaseInvoiceStatus purchaseInvoiceStatus, User createdBy, String invoiceNumber, String tinNumber,
            String supplierName, int pageNo, int perPage,Boolean isReconciled, Warehouse warehouse, Date startDate, Date endDate);

	public PurchaseInvoice save(PurchaseInvoice purchaseInvoice);

	public List<PurchaseInvoice> listPurchaseInvoiceWithProductVariant(ProductVariant productVariant);

	public PurchaseInvoice getPurchaseInvoiceById(Long purchaseInvoiceId);
	
	public DebitNote getDebitNote(PurchaseInvoice purchaseInvoice);
	
	public List<PurchaseInvoice> getPurchaseInvoicesCorrespondingToThisPI(PurchaseInvoice purchaseInvoice);
	
	public boolean otherCorrespondingPIsContainThisEILI(PurchaseInvoice invoice, ExtraInventoryLineItem extraInventoryLineItem);

}