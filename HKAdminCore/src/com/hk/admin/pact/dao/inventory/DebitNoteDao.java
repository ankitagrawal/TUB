package com.hk.admin.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface DebitNoteDao extends BaseDao {

    public Page searchDebitNote(GoodsReceivedNote grn, DebitNote debitNote,String debitNoteNo, DebitNoteStatus debitNoteStatus, String tinNumber, String supplierName, Warehouse warehouse,String supplierInvoice, int pageNo, int perPage);

    public ReconciliationVoucher getRvForDebitNote(DebitNote debitNote);
}
