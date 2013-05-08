package com.hk.web.action.admin.sku;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.constants.sku.EnumSkuItemTransferMode;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.pact.service.inventory.SkuGroupService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jan 30, 2013
 * Time: 4:50:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ViewSkuItemAction extends BaseAction {

    @Autowired
    private AdminInventoryService adminInventoryService;
    @Autowired
    private CycleCountService cycleCountService;
    @Autowired
    SkuGroupService skuGroupService;
    private Long entityId;
    private RvLineItem rvLineItem;
    private StockTransferLineItem stockTransferLineItem;
    private CycleCount cycleCount;
    private SkuGroup skuGroup;
    List<SkuItem> skuItemList = new ArrayList<SkuItem>(0);

    public Resolution pre() {

        if (entityId == null) {
            return new RedirectResolution("/pages/admin/viewItemBarcode.jsp");
        }

        if (entityId.equals(EnumSkuItemTransferMode.RV_LINEITEM_OUT.getId())) {
            skuItemList = adminInventoryService.getCheckedInOrOutSkuItems(rvLineItem, null, null, null, -1L);
        } else if (entityId.equals(EnumSkuItemTransferMode.STOCK_TRANSFER_IN.getId())) {
            skuItemList = adminInventoryService.getCheckedInOrOutSkuItems(null, stockTransferLineItem, null, null, 1L);
        } else if (entityId.equals(EnumSkuItemTransferMode.STOCK_TRANSFER_OUT.getId())) {
            skuItemList = adminInventoryService.getCheckedInOrOutSkuItems(null, stockTransferLineItem, null, null, -1L);
        } else if (entityId.equals(EnumSkuItemTransferMode.CYCLE_COUNT.getId())) {
            skuItemList = cycleCountService.getScannedSkuItems(skuGroup.getId(), cycleCount.getId());
        } else if (entityId.equals(EnumSkuItemTransferMode.CYCLE_COUNT_SKU_ITEM_MISSED.getId())) {
            skuItemList = skuGroupService.getInStockSkuItems(skuGroup);
        }

        return new ForwardResolution("/pages/admin/viewItemBarcode.jsp");
    }

    public List<SkuItem> getSkuItemList() {
        return skuItemList;
    }

    public void setSkuItemList(List<SkuItem> skuItemList) {
        this.skuItemList = skuItemList;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public RvLineItem getRvLineItem() {
        return rvLineItem;
    }

    public void setRvLineItem(RvLineItem rvLineItem) {
        this.rvLineItem = rvLineItem;
    }

    public StockTransferLineItem getStockTransferLineItem() {
        return stockTransferLineItem;
    }

    public void setStockTransferLineItem(StockTransferLineItem stockTransferLineItem) {
        this.stockTransferLineItem = stockTransferLineItem;
    }

    public CycleCount getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(CycleCount cycleCount) {
        this.cycleCount = cycleCount;
    }

    public SkuGroup getSkuGroup() {
        return skuGroup;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
        this.skuGroup = skuGroup;
    }
}

