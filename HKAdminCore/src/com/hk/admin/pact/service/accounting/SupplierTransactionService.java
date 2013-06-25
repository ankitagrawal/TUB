package com.hk.admin.pact.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.SupplierTransactionType;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.po.PurchaseInvoice;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tarun Mittal
 * Date: June 25, 2013
 * To change this template use File |
 * Settings | File Templates.
 */
public interface SupplierTransactionService {

    public SupplierTransaction createOpeningBalanceAccountForSupplier(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date);

    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                          PurchaseInvoice purchaseInvoice);

    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                          DebitNote debitNote);

    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                          CreditNote creditNote);

    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                          String busyPaymentId, Double busySupplierBalance, String narration);

    public SupplierTransaction getSupplierTransactionFromBusyPaymentId(String busyPaymentId);

    public SupplierTransaction getLastTransactionForSupplier(Supplier supplier);

    public List<SupplierTransaction> getLastTransactionListForSuppliers(Supplier supplier);

    public List<SupplierTransaction> getAllTransactionListForSuppliers(Supplier supplier, Date startDate, Date endDate);
}