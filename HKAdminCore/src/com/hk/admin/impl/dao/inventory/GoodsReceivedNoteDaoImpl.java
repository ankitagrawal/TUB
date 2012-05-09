package com.hk.admin.impl.dao.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class GoodsReceivedNoteDaoImpl extends BaseDaoImpl implements GoodsReceivedNoteDao{

    private static Logger logger = LoggerFactory.getLogger(GoodsReceivedNoteDao.class);

    public List<GoodsReceivedNote> getGRNByPO(PurchaseOrder purchaseOrder) {
        return (List<GoodsReceivedNote>) getSession().createQuery("from GoodsReceivedNote o where o.purchaseOrder = :purchaseOrder").setParameter("purchaseOrder", purchaseOrder).list();
    }

    public List<GoodsReceivedNote> listGRNsExcludingStatus(List<Long> grnStatusList) {
        return (List<GoodsReceivedNote>) getSession().createQuery("from GoodsReceivedNote o where o.grnStatus.id not in (:grnStatusList)").setParameterList("grnStatusList",
                grnStatusList).list();
    }

    public List<GoodsReceivedNote> listGRNsWithProductVariant(ProductVariant productVariant) {
        return (List<GoodsReceivedNote>) getSession().createQuery("select distinct poli.goodsReceivedNote from GrnLineItem poli where poli.sku.productVariant = (:productVariant)").setParameter(
                "productVariant", productVariant).list();
    }

    public Page searchGRN(GoodsReceivedNote grn, GrnStatus grnStatus, String invoiceNumber, String tinNumber, String supplierName, Boolean isReconciled,
            Warehouse warehouse, int pageNo, int perPage) {
        List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>();
        if (StringUtils.isNotBlank(tinNumber) || StringUtils.isNotBlank(supplierName)) {
            Criteria purchaseOrderCriteria = getSession().createCriteria(PurchaseOrder.class);
            Criteria supplierCriteria = purchaseOrderCriteria.createCriteria("supplier");
            if (StringUtils.isNotBlank(tinNumber)) {
                supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
            }
            if (StringUtils.isNotBlank(supplierName)) {

                supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
            }
            poList = purchaseOrderCriteria.list();
            logger.debug("poList=" + poList.size());
        }
        DetachedCriteria grnCriteria = DetachedCriteria.forClass(GoodsReceivedNote.class);
        if (isReconciled != null) {
            if (!isReconciled) {
                grnCriteria.add(Restrictions.or(Restrictions.isNull("reconciled"), Restrictions.eq("reconciled", isReconciled)));
            } else {
                grnCriteria.add(Restrictions.eq("reconciled", isReconciled));
            }
        }
        if (grn != null) {
            grnCriteria.add(Restrictions.eq("id", grn.getId()));
        }

        if (!poList.isEmpty() && poList.size() > 0) {
            grnCriteria.add(Restrictions.in("purchaseOrder", poList));
        }
        if (StringUtils.isNotBlank(invoiceNumber)) {
            grnCriteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        }
        if (grnStatus != null) {
            grnCriteria.add(Restrictions.eq("grnStatus", grnStatus));
        }
        if (warehouse != null) {
            grnCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        grnCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(grnCriteria, pageNo, perPage);

    }

    public List<GoodsReceivedNote> listGRNsIncludingStatus(List<Long> grnStatusList) {
        return (List<GoodsReceivedNote>) getSession().createQuery("from GoodsReceivedNote o where o.grnStatus.id in (:grnStatusList)").setParameterList("grnStatusList",
                grnStatusList).list();
    }

    public List<GoodsReceivedNote> listGRNsExcludingStatusInTimeFrame(Long grnStatusValue, Date startDate, Date endDate, Warehouse warehouse) {
        StringBuilder hqlQuery = new StringBuilder("select o from GoodsReceivedNote o where o.grnDate >= (:startDate) and o.grnDate <= (:endDate)");
        if (grnStatusValue != null) {
            hqlQuery.append(" and o.grnStatus.id = :grnStatusValue");
        }
        if (warehouse != null) {
            hqlQuery.append(" and o.warehouse = :warehouse");
        }
        Query hqlGrnQuery = getSession().createQuery(hqlQuery.toString());
        hqlGrnQuery.setParameter("startDate", startDate);
        hqlGrnQuery.setParameter("endDate", endDate);
        if (grnStatusValue != null) {
            hqlGrnQuery.setParameter("grnStatusValue", grnStatusValue);
        }
        if (warehouse != null) {
            hqlGrnQuery.setParameter("warehouse", warehouse);
        }

        return hqlGrnQuery.list();
    }

}
