package com.hk.admin.impl.dao.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class DebitNoteDao extends BaseDaoImpl {

    public Page searchDebitNote(GoodsReceivedNote grn, DebitNoteStatus debitNoteStatus, String tinNumber, String supplierName, int pageNo, int perPage) {
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
        }
        List<GoodsReceivedNote> grnList = new ArrayList<GoodsReceivedNote>();
        Criteria grnCriteria = getSession().createCriteria(GoodsReceivedNote.class);
        if (grn != null) {
            grnCriteria.add(Restrictions.eq("id", grn.getId()));
        }
        if (!poList.isEmpty() && poList.size() > 0) {
            grnCriteria.add(Restrictions.in("purchaseOrder", poList));
        }
        if (grn != null || !poList.isEmpty()) {
            grnList = grnCriteria.list();
        }

        DetachedCriteria debitNoteCriteria = DetachedCriteria.forClass(DebitNote.class);
        if (grnList != null && !grnList.isEmpty() && grnList.size() > 0) {
            debitNoteCriteria.add(Restrictions.in("goodsReceivedNote", grnList));
        }
        if (debitNoteStatus != null) {
            debitNoteCriteria.add(Restrictions.eq("debitNoteStatus", debitNoteStatus));
        }
        debitNoteCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(debitNoteCriteria, pageNo, perPage);

    }

}
