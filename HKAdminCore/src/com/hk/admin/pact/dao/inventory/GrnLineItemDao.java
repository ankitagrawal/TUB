package com.hk.admin.pact.dao.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface GrnLineItemDao extends BaseDao {

    public GrnLineItem getGrnLineItem(GoodsReceivedNote goodsReceivedNote, ProductVariant productVariant);

  public List<GrnLineItem> getAllGrnLineItemByGrn(GoodsReceivedNote goodsReceivedNote);

}
