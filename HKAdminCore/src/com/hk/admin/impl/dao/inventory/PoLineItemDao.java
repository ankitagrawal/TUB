package com.hk.admin.impl.dao.inventory;

import org.springframework.stereotype.Repository;

import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class PoLineItemDao extends BaseDaoImpl {

  /*public PoLineItemDao() {
    super(PoLineItem.class);
  }*/

  public PoLineItem getPoLineItem(PurchaseOrder purchaseOrder, ProductVariant productVariant) {
    return (PoLineItem) getSession().createQuery("from PoLineItem li where li.purchaseOrder = :purchaseOrder and li.sku.productVariant = :productVariant")
        .setParameter("purchaseOrder", purchaseOrder)
        .setParameter("productVariant", productVariant)
        .uniqueResult();
  }


}

