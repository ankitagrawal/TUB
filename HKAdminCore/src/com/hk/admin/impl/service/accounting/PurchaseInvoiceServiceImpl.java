package com.hk.admin.impl.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Rahul Agarwal
 */
@Service
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
	@Autowired
	PurchaseInvoiceDao purchaseInvoiceDao;

	@Override
	public void markPISettledAndClosedFromPaymentHistory(PurchaseInvoice purchaseInvoice, PaymentHistory paymentHistory) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public Page searchPurchaseInvoice(PurchaseInvoice purchaseInvoice, PurchaseInvoiceStatus purchaseInvoiceStatus, User createdBy, String invoiceNumber,
			String tinNumber, String supplierName, int pageNo, int perPage, Boolean isReconciled, Warehouse warehouse, Date startDate, Date endDate) {
		return getPurchaseInvoiceDao().searchPurchaseInvoice(purchaseInvoice, purchaseInvoiceStatus, createdBy, invoiceNumber, tinNumber, supplierName, pageNo,
				perPage, isReconciled, warehouse, startDate, endDate);
	}

	@Override
	public PurchaseInvoice save(PurchaseInvoice purchaseInvoice) {
		return (PurchaseInvoice) getPurchaseInvoiceDao().save(purchaseInvoice);
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

	public List<PurchaseInvoice> getPurchaseInvoicesCorrespondingToThisPI(PurchaseInvoice purchaseInvoice) {
		Set<PurchaseInvoice> purchaseInvoices = new HashSet<PurchaseInvoice>();
		List<GoodsReceivedNote> goodsReceivedNotes = purchaseInvoice.getGoodsReceivedNotes();
		if (goodsReceivedNotes != null && goodsReceivedNotes.size() > 0) {
			GoodsReceivedNote goodsReceivedNote = goodsReceivedNotes.get(0);
			PurchaseOrder po = goodsReceivedNote.getPurchaseOrder();
			List<GoodsReceivedNote> grnList = po.getGoodsReceivedNotes();
			if(grnList!=null && grnList.size()>0){
				for (GoodsReceivedNote grn : grnList) {
					List<PurchaseInvoice> pis = grn.getPurchaseInvoices();
					purchaseInvoices.addAll(pis);
				}
			}
		}
		return (new ArrayList<PurchaseInvoice>(purchaseInvoices));
	}

	public boolean otherCorrespondingPIsContainThisEILI(PurchaseInvoice invoice, ExtraInventoryLineItem extraInventoryLineItem) {
		List<PurchaseInvoice> invoices = getPurchaseInvoicesCorrespondingToThisPI(invoice);
		for (PurchaseInvoice pi : invoices) {
			List<ExtraInventoryLineItem> eilis = pi.getEiLineItems();
			if (eilis != null && eilis.size() > 0) {
				if (eilis.contains(extraInventoryLineItem)) {
					return true;
				}
			}
		}
		return false;
	}
}