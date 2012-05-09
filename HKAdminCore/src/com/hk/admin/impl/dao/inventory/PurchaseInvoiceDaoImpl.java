package com.hk.admin.impl.dao.inventory;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class PurchaseInvoiceDaoImpl extends BaseDaoImpl implements PurchaseInvoiceDao {

    public Page searchPurchaseInvoice(PurchaseInvoice purchaseInvoice, PurchaseInvoiceStatus purchaseInvoiceStatus, User createdBy, String invoiceNumber, String tinNumber,
            String supplierName, int pageNo, int perPage, Boolean isReconciled) {

        DetachedCriteria purchaseInvoiceCriteria = DetachedCriteria.forClass(PurchaseInvoice.class);

        DetachedCriteria supplierCriteria = null;
        if (StringUtils.isNotBlank(tinNumber) || StringUtils.isNotBlank(supplierName)) {
            supplierCriteria = purchaseInvoiceCriteria.createCriteria("supplier");
            if (StringUtils.isNotBlank(tinNumber)) {
                supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
            }
            if (StringUtils.isNotBlank(supplierName)) {
                supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
            }
        }
        if (purchaseInvoice != null) {
            purchaseInvoiceCriteria.add(Restrictions.eq("id", purchaseInvoice.getId()));
        }
        if (purchaseInvoiceStatus != null) {
            purchaseInvoiceCriteria.add(Restrictions.eq("purchaseInvoiceStatus", purchaseInvoiceStatus));
        }
        if (createdBy != null) {
            purchaseInvoiceCriteria.add(Restrictions.eq("createdBy", createdBy));
        }
        if (StringUtils.isNotBlank(invoiceNumber)) {
            purchaseInvoiceCriteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        }
        if (isReconciled != null) {
            if (!isReconciled) {
                purchaseInvoiceCriteria.add(Restrictions.or(Restrictions.isNull("reconciled"), Restrictions.eq("reconciled", isReconciled)));
            } else {
                purchaseInvoiceCriteria.add(Restrictions.eq("reconciled", isReconciled));
            }
        }

        purchaseInvoiceCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(purchaseInvoiceCriteria, pageNo, perPage);

    }

    public List<PurchaseInvoice> listPurchaseInvoiceWithProductVariant(ProductVariant productVariant) {
        return (List<PurchaseInvoice>) getSession().createQuery(
                "select distinct pili.purchaseInvoice from PurchaseInvoiceLineItem pili where pili.sku.productVariant = (:productVariant)").setParameter("productVariant",
                productVariant).list();
    }

}
