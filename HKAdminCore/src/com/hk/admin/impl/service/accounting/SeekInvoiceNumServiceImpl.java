package com.hk.admin.impl.service.accounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.accounting.SeekInvoiceNumDao;
import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.domain.warehouse.Warehouse;

/**
 * User: Rahul Agarwal
 */
@Service
public class SeekInvoiceNumServiceImpl implements SeekInvoiceNumService {

		@Autowired
    private SeekInvoiceNumDao seekInvoiceNumDao;

    /**
     * @param invoiceType - corresponds to the prefix in the SeekInvoiceNum table :R for Retail, S for Service and T for
     *            B2B
     * @param warehouse
     * @return InvoiceNum that can be used - This method will increment the InvoiceNum which can be used for other
     *         invoices.
     */
    public String getInvoiceNum(String invoiceType, Warehouse warehouse) {
        return invoiceType +"-"+ getSeekInvoiceNumDao().getInvoiceNum(invoiceType, warehouse);
    }

    public SeekInvoiceNumDao getSeekInvoiceNumDao() {
        return seekInvoiceNumDao;
    }

    public void setSeekInvoiceNumDao(SeekInvoiceNumDao seekInvoiceNumDao) {
        this.seekInvoiceNumDao = seekInvoiceNumDao;
    }

}
