package com.hk.admin.impl.dao.inventory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class PurchaseOrderDaoImpl extends BaseDaoImpl implements PurchaseOrderDao {

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatus(List<Long> purchaseOrderStatusList) {
        return (List<PurchaseOrder>) getSession().createQuery("from PurchaseOrder o where o.purchaseOrderStatus.id not in (:purchaseOrderStatusList)").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).list();
    }

    public List<PurchaseOrder> listPurchaseOrdersWithProductVariant(ProductVariant productVariant) {
        return (List<PurchaseOrder>) getSession().createQuery("select distinct poli.purchaseOrder from PoLineItem poli where poli.sku.productVariant = (:productVariant)").setParameter(
                "productVariant", productVariant).list();
    }

    public List<PurchaseOrder> searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                                        String tinNumber, String supplierName, Warehouse warehouse) {
        return findByCriteria(getPurchaseOrderCriteria(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber,
                tinNumber, supplierName, warehouse));
    }

    public Page searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                         String tinNumber, String supplierName, Warehouse warehouse, int pageNo, int perPage) {
        return list(getPurchaseOrderCriteria(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber,
                tinNumber, supplierName, warehouse), pageNo, perPage);
    }

    private DetachedCriteria getPurchaseOrderCriteria(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
                                                      String tinNumber, String supplierName, Warehouse warehouse) {
        DetachedCriteria purchaseOrderCriteria = DetachedCriteria.forClass(PurchaseOrder.class);
        DetachedCriteria supplierCriteria = null;
        if (purchaseOrder != null) {
            purchaseOrderCriteria.add(Restrictions.eq("id", purchaseOrder.getId()));
        }
        if (purchaseOrderStatus != null) {
            purchaseOrderCriteria.add(Restrictions.eq("purchaseOrderStatus", purchaseOrderStatus));
        }
        if (approvedBy != null) {
            purchaseOrderCriteria.add(Restrictions.eq("approvedBy", approvedBy));
        }
        if (createdBy != null) {
            purchaseOrderCriteria.add(Restrictions.eq("createdBy", createdBy));
        }
        if (StringUtils.isNotBlank(invoiceNumber)) {
            purchaseOrderCriteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        }
        if (warehouse != null) {
            purchaseOrderCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (StringUtils.isNotBlank(tinNumber)) {
            if (supplierCriteria == null) {
                supplierCriteria = purchaseOrderCriteria.createCriteria("supplier");
            }
            supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
        }
        if (StringUtils.isNotBlank(supplierName)) {
            if (supplierCriteria == null) {
                supplierCriteria = purchaseOrderCriteria.createCriteria("supplier");
            }
            supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
        }

        purchaseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        return purchaseOrderCriteria;
    }


    public List<PurchaseOrder> listPurchaseOrdersIncludingStatus(List<Long> purchaseOrderStatusList) {
        return (List<PurchaseOrder>) getSession().createQuery("from PurchaseOrder o where o.purchaseOrderStatus.id in (:purchaseOrderStatusList)").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).list();
    }

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatusInTimeFrame(List<Long> purchaseOrderStatusList, Date startDate, Date endDate) {
        return (List<PurchaseOrder>) getSession().createQuery(
                "from PurchaseOrder o where o.purchaseOrderStatus.id not in (:purchaseOrderStatusList) " + " and o.poDate >= (:startDate) and o.poDate <= (:endDate) ").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }
}
