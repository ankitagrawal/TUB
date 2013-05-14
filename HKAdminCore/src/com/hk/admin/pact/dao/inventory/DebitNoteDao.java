package com.hk.admin.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface DebitNoteDao extends BaseDao {

    public Page searchDebitNote(GoodsReceivedNote grn, DebitNoteStatus debitNoteStatus, String tinNumber, String supplierName, Warehouse warehouse, int pageNo, int perPage);

}
