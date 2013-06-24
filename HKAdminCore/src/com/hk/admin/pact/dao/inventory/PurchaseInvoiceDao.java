package com.hk.admin.pact.dao.inventory;

import java.util.List;
import java.util.Date;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface PurchaseInvoiceDao extends BaseDao {

    public Page searchPurchaseInvoice(PurchaseInvoice purchaseInvoice, PurchaseInvoiceStatus purchaseInvoiceStatus, User createdBy, String invoiceNumber, String tinNumber,
            String supplierName, int pageNo, int perPage,Boolean isReconciled, Warehouse warehouse, Date startDate, Date endDate);

    public List<PurchaseInvoice> listPurchaseInvoiceWithProductVariant(ProductVariant productVariant);
    
    public DebitNote getDebitNote(PurchaseInvoice purchaseInvoice);

}
