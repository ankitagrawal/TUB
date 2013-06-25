package com.hk.admin.pact.dao.accounting;

import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.pact.dao.BaseDao;

public interface SupplierTransactionDao extends BaseDao {

    public SupplierTransaction getSupplierTransactionFromBusyPaymentId(String busyPaymentId);

    public SupplierTransaction getSupplierTransactionFromPurchaseInvoice(PurchaseInvoice purchaseInvoice);

    public SupplierTransaction getSupplierTransactionFromDebitNote(DebitNote debitNote);

    public SupplierTransaction getSupplierTransactionFromCreditNote(CreditNote creditNote);

    public SupplierTransaction getLastTransactionForSupplier(Supplier supplier);

}
