package com.hk.admin.impl.dao.inventory;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class GrnLineItemDaoImpl extends BaseDaoImpl implements GrnLineItemDao{

    
    @SuppressWarnings("unchecked")
    public GrnLineItem getGrnLineItem(GoodsReceivedNote goodsReceivedNote, ProductVariant productVariant) {
        List<GrnLineItem> grnLineItems = getSession().createQuery("from GrnLineItem li where li.goodsReceivedNote = :goodsReceivedNote and li.sku.productVariant = :productVariant").setParameter(
                "goodsReceivedNote", goodsReceivedNote).setParameter("productVariant", productVariant).list();
        return grnLineItems != null && !grnLineItems.isEmpty() ? grnLineItems.get(0) : null;
    }

  //added by seema
   public List<GrnLineItem> getAllGrnLineItemByGrn(GoodsReceivedNote goodsReceivedNote) {

     List<GrnLineItem> grnLineItems = getSession().createQuery("from GrnLineItem li where li.goodsReceivedNote = :goodsReceivedNote")
         .setParameter("goodsReceivedNote", goodsReceivedNote)
         .list();
     if(grnLineItems!=null && grnLineItems.size()!=0)
       return grnLineItems;
     else
     return null;
   }



}
