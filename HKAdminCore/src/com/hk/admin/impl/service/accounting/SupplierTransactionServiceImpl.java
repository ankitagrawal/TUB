package com.hk.admin.impl.service.accounting;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.accounting.SupplierTransactionDao;
import com.hk.admin.pact.service.accounting.SupplierTransactionService;
import com.hk.constants.inventory.EnumSupplierTransactionType;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.SupplierTransactionType;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * User: Tarun Mittal
 */
@Service
public class SupplierTransactionServiceImpl implements SupplierTransactionService {

    @Autowired
    SupplierTransactionDao supplierTransactionDao;

    @Override
    public SupplierTransaction createOpeningBalanceAccountForSupplier(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date) {
        SupplierTransaction supplierTransaction = new SupplierTransaction();
        supplierTransaction.setSupplier(supplier);
        supplierTransaction.setSupplierTransactionType(supplierTransactionType);
        supplierTransaction.setAmount(amount);
        supplierTransaction.setDate(date);
        supplierTransaction.setCurrentBalance(amount);
        supplierTransaction =  (SupplierTransaction)getSupplierTransactionDao().save(supplierTransaction);

        return supplierTransaction;
    }

    //from purchase invoice
    @Override
    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date, PurchaseInvoice purchaseInvoice) {
        SupplierTransaction supplierTransaction;
        supplierTransaction = getSupplierTransactionDao().getSupplierTransactionFromPurchaseInvoice(purchaseInvoice);

        if(supplierTransaction == null){
            supplierTransaction = new SupplierTransaction();
            supplierTransaction.setSupplier(supplier);
            supplierTransaction.setSupplierTransactionType(supplierTransactionType);
            supplierTransaction.setAmount(amount);
            supplierTransaction.setDate(date);
            supplierTransaction.setCurrentBalance(getUpdatedSupplierBalance(supplier, supplierTransactionType, amount));
            supplierTransaction.setPurchaseInvoice(purchaseInvoice);
            supplierTransaction.setNarration("Supplier invoice no: "+purchaseInvoice.getInvoiceNumber());
            supplierTransaction =  (SupplierTransaction)getSupplierTransactionDao().save(supplierTransaction);
        }

        return supplierTransaction;
    }

    //from debit note
    @Override
    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                         DebitNote debitNote) {
        SupplierTransaction supplierTransaction;
        supplierTransaction = getSupplierTransactionDao().getSupplierTransactionFromDebitNote(debitNote);

        if(supplierTransaction == null){
            supplierTransaction = new SupplierTransaction();
            supplierTransaction.setSupplier(supplier);
            supplierTransaction.setSupplierTransactionType(supplierTransactionType);
            supplierTransaction.setAmount(amount);
            supplierTransaction.setDate(date);
            supplierTransaction.setCurrentBalance(getUpdatedSupplierBalance(supplier, supplierTransactionType, amount));
            supplierTransaction.setDebitNote(debitNote);
            supplierTransaction.setNarration("Debit note no: "+debitNote.getDebitNoteNumber());
            supplierTransaction =  (SupplierTransaction)getSupplierTransactionDao().save(supplierTransaction);
        }

        return supplierTransaction;
    }

    //From credit note
    @Override
    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                         CreditNote creditNote) {
        SupplierTransaction supplierTransaction;
        supplierTransaction = getSupplierTransactionDao().getSupplierTransactionFromCreditNote(creditNote);

        if(supplierTransaction == null){
            supplierTransaction = new SupplierTransaction();
            supplierTransaction.setSupplier(supplier);
            supplierTransaction.setSupplierTransactionType(supplierTransactionType);
            supplierTransaction.setAmount(amount);
            supplierTransaction.setDate(date);
            supplierTransaction.setCurrentBalance(getUpdatedSupplierBalance(supplier, supplierTransactionType, amount));
            supplierTransaction.setCreditNote(creditNote);
            supplierTransaction =  (SupplierTransaction)getSupplierTransactionDao().save(supplierTransaction);
        }

        return supplierTransaction;
    }

    @Override
    public SupplierTransaction createSupplierTransaction(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount, Date date,
                                                         String busyPaymentId, Double busySupplierBalance, String narration) {
        SupplierTransaction supplierTransaction;
        supplierTransaction = getSupplierTransactionFromBusyPaymentId(busyPaymentId);
        if(supplierTransaction == null){
            supplierTransaction = new SupplierTransaction();
            supplierTransaction.setSupplier(supplier);
            supplierTransaction.setSupplierTransactionType(supplierTransactionType);
            supplierTransaction.setAmount(amount);
            supplierTransaction.setDate(date);
            supplierTransaction.setCurrentBalance(getUpdatedSupplierBalance(supplier, supplierTransactionType, amount));
            supplierTransaction.setBusySupplierBalance(busySupplierBalance);
            supplierTransaction.setBusyPaymentId(busyPaymentId);
            supplierTransaction.setNarration(narration);
            supplierTransaction =  (SupplierTransaction)getSupplierTransactionDao().save(supplierTransaction);
        }
        return supplierTransaction;
    }

    @Override
    public SupplierTransaction getSupplierTransactionFromBusyPaymentId(String busyPaymentId) {
        return getSupplierTransactionDao().getSupplierTransactionFromBusyPaymentId(busyPaymentId);
    }

    @Override
    public SupplierTransaction getLastTransactionForSupplier(Supplier supplier) {
        return getSupplierTransactionDao().getLastTransactionForSupplier(supplier);
    }

    public Double getUpdatedSupplierBalance(Supplier supplier, SupplierTransactionType supplierTransactionType, Double amount) {
        SupplierTransaction supplierTransaction = getLastTransactionForSupplier(supplier);
        Double netBalance=null;
        if(supplierTransaction == null){
            return amount;
        }

        if(supplierTransactionType.getId().equals(EnumSupplierTransactionType.Payment.getId())
                || supplierTransactionType.getId().equals(EnumSupplierTransactionType.PurchaseReturn.getId())){
            netBalance = supplierTransaction.getCurrentBalance() - amount;
        }
        else if(supplierTransactionType.getId().equals(EnumSupplierTransactionType.Purchase.getId())){
            netBalance = supplierTransaction.getCurrentBalance() + amount;
        }
        return netBalance;
    }


    @Override
    public List<SupplierTransaction> getLastTransactionListForSuppliers(Supplier supplier) {
        return getSupplierTransactionDao().getLastTransactionListForSuppliers(supplier);
    }

    @Override
    public List<SupplierTransaction> getAllTransactionListForSuppliers(Supplier supplier, Date startDate, Date endDate) {
        return getSupplierTransactionDao().getAllTransactionListForSuppliers(supplier, startDate, endDate);
    }

    public SupplierTransactionDao getSupplierTransactionDao() {
        return supplierTransactionDao;
    }
}