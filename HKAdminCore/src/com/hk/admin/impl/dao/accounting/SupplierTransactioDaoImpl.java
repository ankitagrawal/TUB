package com.hk.admin.impl.dao.accounting;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.accounting.AccountingInvoiceDao;
import com.hk.admin.pact.dao.accounting.SupplierTransactionDao;
import com.hk.domain.accounting.AccountingInvoice;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class SupplierTransactioDaoImpl extends BaseDaoImpl implements SupplierTransactionDao {

    @Override
    public SupplierTransaction getSupplierTransactionFromBusyPaymentId(String busyPaymentId) {
        String query = "from SupplierTransaction st where st.busyPaymentId = :busyPaymentId ";
        return (SupplierTransaction) findUniqueByNamedParams(query, new String[]{"busyPaymentId"}, new Object[]{busyPaymentId});
    }

    @Override
    public SupplierTransaction getLastTransactionForSupplier(Supplier supplier) {
        String query = "from SupplierTransaction st where st.supplier.id = :supplierId ORDER BY id desc";
        List<SupplierTransaction> supplierTransactionList = (List<SupplierTransaction>) findByNamedParams(query, new String[]{"supplierId"}, new Object[]{supplier.getId()});
        if(supplierTransactionList != null && supplierTransactionList.size() > 0){
            return supplierTransactionList.get(0);
        }
        return null;
    }

    @Override
    public SupplierTransaction getSupplierTransactionFromPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        String query = "from SupplierTransaction st where st.purchaseInvoice.id = :purchaseInvoiceId ";
        return (SupplierTransaction) findUniqueByNamedParams(query, new String[]{"purchaseInvoiceId"}, new Object[]{purchaseInvoice.getId()});
    }

    @Override
    public SupplierTransaction getSupplierTransactionFromDebitNote(DebitNote debitNote) {
        String query = "from SupplierTransaction st where st.debitNote.id = :debitNoteId ";
        return (SupplierTransaction) findUniqueByNamedParams(query, new String[]{"debitNoteId"}, new Object[]{debitNote.getId()});
    }

    @Override
    public SupplierTransaction getSupplierTransactionFromCreditNote(CreditNote creditNote) {
        String query = "from SupplierTransaction st where st.creditNote.id = :creditNoteId ";
        return (SupplierTransaction) findUniqueByNamedParams(query, new String[]{"creditNoteId"}, new Object[]{creditNote.getId()});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SupplierTransaction> getLastTransactionListForSuppliers(Supplier supplier) {
        List<SupplierTransaction> supplierTransactionList = new ArrayList<SupplierTransaction>();
        if(supplier != null){
            String query = "from SupplierTransaction st where st.supplier.id = :supplierId ORDER BY id DESC";
            List<SupplierTransaction> supplierTransactionListTemp =
                    (List<SupplierTransaction>) findByNamedParams(query, new String[]{"supplierId"}, new Object[]{supplier.getId()});
            if(supplierTransactionListTemp != null && supplierTransactionListTemp.size() > 0){
                supplierTransactionList.add(supplierTransactionListTemp.get(0));
            }
        }
        else {
            String query = "SELECT max(id), supplier, supplierTransactionType, amount, date, purchaseInvoice, debitNote, creditNote, busyPaymentId, busySupplierBalance, " +
                    "currentBalance, narration FROM supplierTransaction";

            supplierTransactionList = (List<SupplierTransaction>) findByNamedQuery(query);
        }

        return supplierTransactionList;
    }

    @Override
    public Page getAllTransactionListForSuppliers(Supplier supplier, Date startDate, Date endDate, int pageNo, int perPage){
        DetachedCriteria supplierTransactionCriteria = DetachedCriteria.forClass(SupplierTransaction.class);
        if(supplier != null){
            supplierTransactionCriteria.add(Restrictions.eq("supplier.id", supplier.getId()));
        }

        if(startDate != null){
            supplierTransactionCriteria.add(Restrictions.ge("date", startDate));
        }

        if(endDate != null){
            supplierTransactionCriteria.add(Restrictions.ge("date", endDate));
        }

        return list(supplierTransactionCriteria, pageNo, perPage);
    }
}
