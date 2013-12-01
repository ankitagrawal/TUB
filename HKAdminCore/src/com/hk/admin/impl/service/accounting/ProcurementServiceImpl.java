package com.hk.admin.impl.service.accounting;

import java.util.List;

import com.hk.admin.pact.service.accounting.SupplierTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.dao.payment.PaymentHistoryDao;
import com.hk.admin.pact.service.accounting.ProcurementService;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.pact.dao.BaseDao;

/**
 * User: Rahul Agarwal
 */
@Service
public class ProcurementServiceImpl implements ProcurementService {

  @Autowired
  BaseDao baseDao;
  @Autowired
  PurchaseInvoiceDao purchaseInvoiceDao;
  @Autowired
  GoodsReceivedNoteDao goodsReceivedNoteDao;
  @Autowired
  PaymentHistoryDao paymentHistoryDao;
  @Autowired
  SupplierTransactionService supplierTransactionService;


  @Transactional
  public Boolean deletePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
    boolean isDeleted = false;
    if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
      List<GoodsReceivedNote> grns = purchaseInvoice.getGoodsReceivedNotes();
        for (GoodsReceivedNote grn : grns) {
          grn.setReconciled(false);
          goodsReceivedNoteDao.save(grn);
        }
        supplierTransactionService.deletePurchaseInvoiceTransaction(purchaseInvoice);
        purchaseInvoiceDao.delete(purchaseInvoice);
        isDeleted = true;
    }
    return isDeleted;
  }
}