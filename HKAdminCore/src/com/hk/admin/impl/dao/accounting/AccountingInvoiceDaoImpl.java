package com.hk.admin.impl.dao.accounting;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.accounting.AccountingInvoiceDao;
import com.hk.domain.accounting.AccountingInvoice;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class AccountingInvoiceDaoImpl extends BaseDaoImpl implements AccountingInvoiceDao  {

    public Long getLastRetailInvoiceId() {
        return (Long) findUnique("select max(ai.retailInvoiceId) from AccountingInvoice ai");
    }

    public Long getLastB2BInvoiceId() {
        return (Long) findUnique("select max(ai.b2bInvoiceId) from AccountingInvoice ai");
    }

    public Long getLastServiceInvoiceId() {
        return (Long) findUnique("select max(ai.serviceInvoiceId) from AccountingInvoice ai");
    }

    @SuppressWarnings("unchecked")
    public List<AccountingInvoice> findAccountingInvoicesForTimeFrame(Date startDate, Date endDate) {
        String query = " from AccountingInvoice ai where ai.invoiceDate >= :startDate and ai.invoiceDate <= :endDate order by ai.invoiceDate asc";
        return findByNamedParams(query, new String[] { "startDate", "endDate" }, new Object[] { startDate, endDate });
    }

}
