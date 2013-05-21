package com.hk.admin.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface CreditNoteDao extends BaseDao {

  public Page searchCreditNote(CreditNoteStatus creditNoteStatus, String customerLogin, Warehouse warehouse, int pageNo, int perPage);

}