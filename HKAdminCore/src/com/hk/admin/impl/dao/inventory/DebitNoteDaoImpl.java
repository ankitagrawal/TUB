package com.hk.admin.impl.dao.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.catalog.Supplier;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class DebitNoteDaoImpl extends BaseDaoImpl implements DebitNoteDao {

    public Page searchDebitNote(GoodsReceivedNote grn, DebitNoteStatus debitNoteStatus, String tinNumber, String supplierName, int pageNo, int perPage) {
        List<Supplier> supplierList = new ArrayList<Supplier>();
        if (StringUtils.isNotBlank(tinNumber) || StringUtils.isNotBlank(supplierName)) {
            DetachedCriteria supplierCriteria = DetachedCriteria.forClass(Supplier.class);
            if (StringUtils.isNotBlank(tinNumber)) {
                supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
            }
            if (StringUtils.isNotBlank(supplierName)) {
                supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
            }
            supplierList = findByCriteria(supplierCriteria);
        }


        DetachedCriteria debitNoteCriteria = DetachedCriteria.forClass(DebitNote.class);
        if (supplierList != null && supplierList.size() > 0) {
            debitNoteCriteria.add(Restrictions.eq("supplier", supplierList.get(0)));
        }

        if (debitNoteStatus != null) {
            debitNoteCriteria.add(Restrictions.eq("debitNoteStatus", debitNoteStatus));
        }
        debitNoteCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(debitNoteCriteria, pageNo, perPage);

    }

}
