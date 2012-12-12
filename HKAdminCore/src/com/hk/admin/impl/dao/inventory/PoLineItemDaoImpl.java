package com.hk.admin.impl.dao.inventory;

import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class PoLineItemDaoImpl extends BaseDaoImpl implements PoLineItemDao {

	public PoLineItem getPoLineItem(PurchaseOrder purchaseOrder, ProductVariant productVariant) {
		return (PoLineItem) getSession().createQuery("from PoLineItem li where li.purchaseOrder = :purchaseOrder and li.sku.productVariant = :productVariant").setParameter(
				"purchaseOrder", purchaseOrder).setParameter("productVariant", productVariant).uniqueResult();
	}

	public int getPoLineItemCountBySku(Sku sku) {
		Criteria criteria = getSession().createCriteria(PoLineItem.class);
		criteria.add(Restrictions.eq("sku", sku));
		return (Integer)criteria.setProjection(Projections.rowCount()).uniqueResult();

	}

}
