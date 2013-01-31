package com.hk.admin.pact.service.inventory;

import com.hk.domain.user.User;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.Sku;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 6, 2012
 * Time: 1:23:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReconciliationVoucherService {

	public void save(User loggedOnUser, List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher);

	public ReconciliationVoucher save(ReconciliationVoucher reconciliationVoucher);

	public void save(List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher);

	public RvLineItem reconcile(RvLineItem rvLineItem, ReconciliationVoucher reconciliationVoucher, List<SkuItem> skuItemList);

	public void delete(ReconciliationVoucher reconciliationVoucher);

    public RvLineItem createRVLineItemWithBasicDetails (SkuGroup skuGroup , Sku sku);

    public RvLineItem reconcileSKUItems(ReconciliationVoucher reconciliationVoucher, RvLineItem rvLineItem, SkuItem skuItem);
}
