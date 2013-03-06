package com.hk.admin.impl.dao.inventory;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class GrnLineItemDaoImpl extends BaseDaoImpl implements GrnLineItemDao {


    @SuppressWarnings("unchecked")
    public GrnLineItem getGrnLineItem(GoodsReceivedNote goodsReceivedNote, ProductVariant productVariant) {
        List<GrnLineItem> grnLineItems = getSession().createQuery("from GrnLineItem li where li.goodsReceivedNote = :goodsReceivedNote and li.sku.productVariant = :productVariant").setParameter(
                "goodsReceivedNote", goodsReceivedNote).setParameter("productVariant", productVariant).list();
        return grnLineItems != null && !grnLineItems.isEmpty() ? grnLineItems.get(0) : null;
    }


    public List<GrnLineItem> getAllGrnLineItemBySku(Sku sku) {
        DetachedCriteria criteria = DetachedCriteria.forClass(GrnLineItem.class);
        if (sku != null) {
            criteria.add(Restrictions.eq("sku", sku));
            return (List<GrnLineItem>) findByCriteria(criteria);
        }

        return null;
    }

}
