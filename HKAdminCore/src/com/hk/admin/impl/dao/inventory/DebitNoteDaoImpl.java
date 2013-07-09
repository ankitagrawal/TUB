package com.hk.admin.impl.dao.inventory;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class DebitNoteDaoImpl extends BaseDaoImpl implements DebitNoteDao {

  public Page searchDebitNote(GoodsReceivedNote grn, DebitNoteStatus debitNoteStatus, String tinNumber, String supplierName, Warehouse warehouse, int pageNo, int perPage) {

    DetachedCriteria debitNoteCriteria = DetachedCriteria.forClass(DebitNote.class);
    if (StringUtils.isNotBlank(tinNumber) || StringUtils.isNotBlank(supplierName)) {
      DetachedCriteria supplierCriteria = debitNoteCriteria.createCriteria("supplier");
      if (StringUtils.isNotBlank(tinNumber)) {
        supplierCriteria.add(Restrictions.eq("tinNumber", tinNumber));
      }
      if (StringUtils.isNotBlank(supplierName)) {
        supplierCriteria.add(Restrictions.like("name", "%" + supplierName + "%"));
      }
    }
    if (warehouse != null) {
      debitNoteCriteria.add(Restrictions.eq("warehouse", warehouse));
    }
    if (debitNoteStatus != null) {
      debitNoteCriteria.add(Restrictions.eq("debitNoteStatus", debitNoteStatus));
    }
    debitNoteCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
    return list(debitNoteCriteria, pageNo, perPage);

  }
  
  @Override
	public ReconciliationVoucher getRvForDebitNote(DebitNote debitNote) {
		return (ReconciliationVoucher)getSession().createQuery("select dn.reconciliationVoucher from DebitNote dn where dn.id = id").setParameter("id", debitNote.getId());
	}

}
