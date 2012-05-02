package com.hk.admin.impl.dao.accounting;

import java.util.Date;
import java.util.List;

import com.hk.domain.accounting.AccountingInvoice;

public interface AccountingInvoiceDao {

    public Long getLastRetailInvoiceId();

    public Long getLastB2BInvoiceId();

    public Long getLastServiceInvoiceId();

    public List<AccountingInvoice> findAccountingInvoicesForTimeFrame(Date startDate, Date endDate);
}
