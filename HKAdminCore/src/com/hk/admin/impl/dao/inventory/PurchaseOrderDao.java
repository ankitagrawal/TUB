package com.hk.admin.impl.dao.inventory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class PurchaseOrderDao extends BaseDaoImpl {

    public List<PurchaseOrder> listPurchaseOrdersExcludingStatus(List<Long> purchaseOrderStatusList) {
        return (List<PurchaseOrder>) getSession().createQuery("from PurchaseOrder o where o.purchaseOrderStatus.id not in (:purchaseOrderStatusList)").setParameterList(
                "purchaseOrderStatusList", purchaseOrderStatusList).list();
    }

    public List<PurchaseOrder> listPurchaseOrdersWithProductVariant(ProductVariant productVariant) {
        return (List<PurchaseOrder>) getSession().createQuery("select distinct poli.purchaseOrder from PoLineItem poli where poli.sku.productVariant = (:productVariant)").setParameter(
                "productVariant", productVariant).list();
    }

    public Page searchPO(PurchaseOrder purchaseOrder, PurchaseOrderStatus purchaseOrderStatus, User approvedBy, User createdBy, String invoiceNumber,
            String tinNumber, String supplierName, Warehouse warehouse, int pageNo, int perPage) {

        DetachedCriteria purchaseOrderCriteria = DetachedCriteria.forClass(PurchaseOrder.class);
        DetachedCriteria supplierCriteria = purchaseOrderCriteria.createCriteria("supplier");
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
            supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
        }
        if (StringUtils.isNotBlank(supplierName)) {
            supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
        }

        purchaseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(purchaseOrderCriteria, pageNo, perPage);

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
