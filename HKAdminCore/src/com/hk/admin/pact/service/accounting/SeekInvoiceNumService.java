package com.hk.admin.pact.service.accounting;

import com.hk.domain.warehouse.Warehouse;

/**
 * Created by IntelliJ IDEA. User: Rahul Agarwal Date: Mar 13, 2012 Time: 7:33:48 AM To change this template use File |
 * Settings | File Templates.
 */
public interface SeekInvoiceNumService {

    /**
     * @param invoiceType - corresponds to the prefix in the SeekInvoiceNum table :R for Retail, S for Service and T for
     *            B2B
     * @param warehouse
     * @return InvoiceNum that can be used - This method will increment the InvoiceNum which can be used for other
     *         invoices.
     */
    public String getInvoiceNum(String invoiceType, Warehouse warehouse);

}
