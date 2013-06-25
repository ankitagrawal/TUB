package com.hk.admin.pact.dao.accounting;

import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.BaseDao;

public interface SupplierTransactionDao extends BaseDao {

    public SupplierTransaction save(SupplierTransaction supplierTransaction);

    public SupplierTransaction getSupplierTransactionFromBusyPaymentId(String busyPaymentId);

    public SupplierTransaction getLastTransactionForSupplier(Supplier supplier);

}
