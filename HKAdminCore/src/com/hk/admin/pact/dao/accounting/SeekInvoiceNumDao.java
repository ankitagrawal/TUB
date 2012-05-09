package com.hk.admin.pact.dao.accounting;

import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface SeekInvoiceNumDao extends BaseDao {

    public Long getInvoiceNum(String invoiceType, Warehouse warehouse);

}
