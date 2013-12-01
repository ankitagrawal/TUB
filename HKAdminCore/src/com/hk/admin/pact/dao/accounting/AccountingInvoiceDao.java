package com.hk.admin.pact.dao.accounting;

import java.util.Date;
import java.util.List;

import com.hk.domain.accounting.AccountingInvoice;
import com.hk.pact.dao.BaseDao;

public interface AccountingInvoiceDao extends BaseDao {

    public Long getLastRetailInvoiceId();

    public Long getLastB2BInvoiceId();

    public Long getLastServiceInvoiceId();

    public List<AccountingInvoice> findAccountingInvoicesForTimeFrame(Date startDate, Date endDate);
}
