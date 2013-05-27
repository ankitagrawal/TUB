package com.hk.admin.impl.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.CreditNoteDao;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public class CreditNoteDaoImpl extends BaseDaoImpl implements CreditNoteDao {

  public Page searchCreditNote(CreditNoteStatus creditNoteStatus, String customerLogin, Warehouse warehouse, int pageNo, int perPage) {

    DetachedCriteria creditNoteCriteria = DetachedCriteria.forClass(CreditNote.class);
    if (StringUtils.isNotBlank(customerLogin)) {
      DetachedCriteria customerCriteria = creditNoteCriteria.createCriteria("user");
        customerCriteria.add(Restrictions.like("login", customerLogin));
    }
    if (warehouse != null) {
      creditNoteCriteria.add(Restrictions.eq("warehouse", warehouse));
    }
    if (creditNoteStatus != null) {
      creditNoteCriteria.add(Restrictions.eq("creditNoteStatus", creditNoteStatus));
    }
    creditNoteCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
    return list(creditNoteCriteria, pageNo, perPage);

  }

}