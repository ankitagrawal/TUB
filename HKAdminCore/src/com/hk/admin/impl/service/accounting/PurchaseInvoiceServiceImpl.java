package com.hk.admin.impl.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * User: Rahul Agarwal
 */
@Service
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
	@Autowired
	PurchaseInvoiceDao purchaseInvoiceDao;

	@Override
	public void markPISettledAndClosedFromPaymentHistory(PurchaseInvoice purchaseInvoice, PaymentHistory paymentHistory) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Page searchPurchaseInvoice(PurchaseInvoice purchaseInvoice, PurchaseInvoiceStatus purchaseInvoiceStatus, User createdBy, String invoiceNumber, String tinNumber,
	                               String supplierName, int pageNo, int perPage, Boolean isReconciled, Warehouse warehouse, Date startDate, Date endDate) {
		return getPurchaseInvoiceDao().searchPurchaseInvoice(purchaseInvoice, purchaseInvoiceStatus, createdBy, invoiceNumber, tinNumber, supplierName, pageNo, perPage, isReconciled,
				warehouse, startDate, endDate);
	}

	@Override
	public PurchaseInvoice save(PurchaseInvoice purchaseInvoice) {
		return (PurchaseInvoice)getPurchaseInvoiceDao().save(purchaseInvoice);
	}

	@Override
	public List<PurchaseInvoice> listPurchaseInvoiceWithProductVariant(ProductVariant productVariant) {
		return getPurchaseInvoiceDao().listPurchaseInvoiceWithProductVariant(productVariant);
	}

	@Override
	public PurchaseInvoice getPurchaseInvoiceById(Long purchaseInvoiceId) {
		return (PurchaseInvoice) getPurchaseInvoiceDao().get(PurchaseInvoice.class, purchaseInvoiceId);
	}
	

	public PurchaseInvoiceDao getPurchaseInvoiceDao() {
		return purchaseInvoiceDao;
	}

	@Override
	public DebitNote getDebitNote(PurchaseInvoice purchaseInvoice) {
		return (DebitNote) getPurchaseInvoiceDao().getDebitNote(purchaseInvoice);
	}
}