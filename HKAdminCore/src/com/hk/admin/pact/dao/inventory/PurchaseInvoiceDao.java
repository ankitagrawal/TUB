package com.hk.admin.pact.dao.inventory;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface PurchaseInvoiceDao extends BaseDao {

    public Page searchPurchaseInvoice(PurchaseInvoice purchaseInvoice, PurchaseInvoiceStatus purchaseInvoiceStatus, User createdBy, String invoiceNumber, String tinNumber,
            String supplierName, int pageNo, int perPage);

    public List<PurchaseInvoice> listPurchaseInvoiceWithProductVariant(ProductVariant productVariant);

}
