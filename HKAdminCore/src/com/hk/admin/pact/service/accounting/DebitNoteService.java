package com.hk.admin.pact.service.accounting;

import com.hk.admin.dto.inventory.DebitNoteDto;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.payment.PaymentHistory;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Tarun Mittal Date: Dec 21, 2012  |
 * Settings | File Templates.
 */
public interface DebitNoteService {

    public DebitNoteDto generateDebitNoteDto(DebitNote debitNote);

    public DebitNote createDebitNoteLineItem(DebitNote debitNote, List<RtvNoteLineItem> rtvNoteLineItems,
                                             List<ExtraInventoryLineItem> extraInventoryLineItems);
    
    public DebitNote createDebitNoteLineItemWithRVLineItems(DebitNote debitNote, List<RvLineItem> rvLineItems);

    public DebitNote save(DebitNote debitNote, List<DebitNoteLineItem> debitNoteLineItems);

    public DebitNote save(DebitNote debitNote);

}