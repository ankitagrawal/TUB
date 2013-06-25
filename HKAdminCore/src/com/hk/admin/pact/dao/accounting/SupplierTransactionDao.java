package com.hk.admin.pact.dao.accounting;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

public interface SupplierTransactionDao extends BaseDao {

    public SupplierTransaction getSupplierTransactionFromBusyPaymentId(String busyPaymentId);

    public SupplierTransaction getSupplierTransactionFromPurchaseInvoice(PurchaseInvoice purchaseInvoice);

    public SupplierTransaction getSupplierTransactionFromDebitNote(DebitNote debitNote);

    public SupplierTransaction getSupplierTransactionFromCreditNote(CreditNote creditNote);

    public SupplierTransaction getLastTransactionForSupplier(Supplier supplier);

    public List<SupplierTransaction> getLastTransactionListForSuppliers(Supplier supplier);

    public List<SupplierTransaction> getAllTransactionListForSuppliers(Supplier supplier, Date startDate, Date endDate);

}
