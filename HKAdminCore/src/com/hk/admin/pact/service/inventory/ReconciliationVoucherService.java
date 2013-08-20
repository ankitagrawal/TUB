package com.hk.admin.pact.service.inventory;

import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.user.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 6, 2012
 * Time: 1:23:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReconciliationVoucherService {

    public void reconcileAddRV(User loggedOnUser, List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher);

    public ReconciliationVoucher save(ReconciliationVoucher reconciliationVoucher);

    //public void save(List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher);

   // public RvLineItem reconcile(RvLineItem rvLineItem, ReconciliationVoucher reconciliationVoucher, List<SkuItem> skuItemList);

    public void delete(ReconciliationVoucher reconciliationVoucher);

    public RvLineItem createRVLineItemWithBasicDetails(SkuGroup skuGroup, Sku sku);

    public RvLineItem reconcileSKUItems(ReconciliationVoucher reconciliationVoucher, ReconciliationType reconciliationType, SkuItem skuItem, String remarks);

    public void reconcileSubtractRV(ReconciliationVoucher reconciliationVoucher, List<RvLineItem> rvLineItemList);

    public ReconciliationVoucher createReconciliationVoucher(ReconciliationType reconciliationType ,String remark );

    public RvLineItem reconcileInventoryForPV(RvLineItem rvLineItem , List<SkuItem> inStockSkuItems,Sku sku);
    
    public DebitNote getDebitNote(ReconciliationVoucher reconciliationVoucher);

    public void validateSkuItem(SkuItem skuItem);
}
