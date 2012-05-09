package com.hk.admin.util;

import java.util.List;

import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;

public class StockProcurementHelper {

    public static PurchaseOrder getPurchaseOrderForPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        PurchaseOrder purchaseOrder = null;
        List<GoodsReceivedNote> grns = purchaseInvoice.getGoodsReceivedNotes();
        for (GoodsReceivedNote grn : grns) {

            purchaseOrder = grn.getPurchaseOrder();
            if (purchaseOrder != null)
                break;
        }
        return purchaseOrder;
    }
}
