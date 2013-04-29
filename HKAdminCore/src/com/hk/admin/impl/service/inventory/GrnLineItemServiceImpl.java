package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.inventory.SkuGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 10/29/12
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class GrnLineItemServiceImpl implements GrnLineItemService {

    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    BaseDao baseDao;

    public Long getPoLineItemQty(GrnLineItem grnLineItem) {
        if (grnLineItem != null) {
            List<PoLineItem> poLineItemList = grnLineItem.getGoodsReceivedNote().getPurchaseOrder().getPoLineItems();
            for (PoLineItem poLineItem : poLineItemList) {
                if (grnLineItem.getSku().getId().equals(poLineItem.getSku().getId())) {
                    return poLineItem.getQty();
                }
            }
        }
        return 0L;
    }

    public Long getGrnLineItemQtyAlreadySet(GoodsReceivedNote grn, Sku sku) {
        long grnLineItemQtyAlreadySet = 0;
        if (grn != null && sku != null) {
            List<GoodsReceivedNote> allGrnForThisPO = grn.getPurchaseOrder().getGoodsReceivedNotes();
            for (GoodsReceivedNote goodsReceivedNote : allGrnForThisPO) {
                if (!goodsReceivedNote.getId().equals(grn.getId())) {
                    for (GrnLineItem grnLineItemForOtherGrn : goodsReceivedNote.getGrnLineItems()) {
                        if (sku.getId().equals(grnLineItemForOtherGrn.getSku().getId())) {
                            grnLineItemQtyAlreadySet += grnLineItemForOtherGrn.getQty().longValue();
                        }
                    }
                }
            }
        }
        return grnLineItemQtyAlreadySet;
    }
    
    public Long getGrnLineItemCheckedInQty(GoodsReceivedNote grn ,Sku sku){
    	long grnLineItemQtyAlreadySet = 0;
        if (grn != null && sku != null) {
            List<GoodsReceivedNote> allGrnForThisPO = grn.getPurchaseOrder().getGoodsReceivedNotes();
            for (GoodsReceivedNote goodsReceivedNote : allGrnForThisPO) {
                if (!goodsReceivedNote.getId().equals(grn.getId())) {
                    for (GrnLineItem grnLineItemForOtherGrn : goodsReceivedNote.getGrnLineItems()) {
                        if (sku.getId().equals(grnLineItemForOtherGrn.getSku().getId())) {
                            grnLineItemQtyAlreadySet += grnLineItemForOtherGrn.getCheckedInQty().longValue();
                        }
                    }
                }
            }
        }
        return grnLineItemQtyAlreadySet;
    }

    public boolean isAllSkuItemInCheckedInStatus(GrnLineItem grnLineItem) {
        List<SkuGroup> skuGroupList = skuGroupService.getSkuGroupByGrnLineItem(grnLineItem);
        if (skuGroupList != null) {
            for (SkuGroup skuGroup : skuGroupList) {
                if (skuGroup != null) {
                    {
                        for (SkuItem skuItem : skuGroup.getSkuItems()) {
                            if (!(skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.Checked_IN.getId()))) {
                                return false;
                            }

                        }
                    }

                }
            }
        }
        return true;
    }

     @Transactional
    public void delete(GrnLineItem grnLineItem) {
        GoodsReceivedNote goodsReceivedNote = grnLineItem.getGoodsReceivedNote();
        List<GrnLineItem> grnLineItems = goodsReceivedNote.getGrnLineItems();
        grnLineItems.remove(grnLineItem);
        goodsReceivedNote = (GoodsReceivedNote) baseDao.save(goodsReceivedNote);
        baseDao.delete(grnLineItem);
    }


}
