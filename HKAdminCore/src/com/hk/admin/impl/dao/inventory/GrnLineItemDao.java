package com.hk.admin.impl.dao.inventory;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;

@Repository
public class GrnLineItemDao extends BaseDaoImpl {

    /*
     * public GrnLineItemDao() { super(GrnLineItem.class); }
     */
    @SuppressWarnings("unchecked")
    public GrnLineItem getGrnLineItem(GoodsReceivedNote goodsReceivedNote, ProductVariant productVariant) {
        List<GrnLineItem> grnLineItems = getSession().createQuery("from GrnLineItem li where li.goodsReceivedNote = :goodsReceivedNote and li.sku.productVariant = :productVariant").setParameter(
                "goodsReceivedNote", goodsReceivedNote).setParameter("productVariant", productVariant).list();
        return grnLineItems != null && !grnLineItems.isEmpty() ? grnLineItems.get(0) : null;
    }

}
