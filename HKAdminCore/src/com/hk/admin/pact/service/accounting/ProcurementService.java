package com.hk.admin.pact.service.accounting;

import com.hk.domain.inventory.po.PurchaseInvoice;

/**
 * Created by IntelliJ IDEA. User: Rahul Agarwal Date: Mar 13, 2012 Time: 7:33:48 AM To change this template use File |
 * Settings | File Templates.
 */
public interface ProcurementService {

    /**
     * @param PurchaseInvoice
     * @return true if deletion successful, false if failed
     */
    public Boolean deletePurchaseInvoice(PurchaseInvoice purchaseInvoice);

}