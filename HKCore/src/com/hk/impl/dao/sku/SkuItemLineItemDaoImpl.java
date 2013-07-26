package com.hk.impl.dao.sku;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class SkuItemLineItemDaoImpl extends BaseDaoImpl implements SkuItemLineItemDao {

    @Override
    public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId) {
        String sql = "from SkuItemLineItem s where s.lineItem = :lineItem and s.skuItem.skuItemStatus.id= :skuItemStatusId";
        Query query = getSession().createQuery(sql).setParameter("lineItem", lineItem).setParameter("skuItemStatusId", skuItemStatusId);
        return (List<SkuItemLineItem>)query.list();
    }
    
    @Override
    public List<SkuItemCLI> getSkuItemCLI(CartLineItem cartLineItem, List<SkuItemStatus> skuItemStatusIds){
    	String sql = "from SkuItemCLI s where s.cartLineItem = :cartLineItem and s.skuItem.skuItemStatus in (:skuItemStatusIds)";
    	Query query = getSession().createQuery(sql).setParameter("cartLineItem", cartLineItem).setParameterList("skuItemStatusIds", skuItemStatusIds);
    	return (List<SkuItemCLI>)query.list();
    }
    
    @Override
    public SkuItemCLI getSkuItemCLI(SkuItem skuItem){
    	//TODO :ERP Add Status
    	String sql = "from SkuItemCLI s where s.skuItem = :skuItem";
    	return (SkuItemCLI) getSession().createQuery(sql).setParameter("skuItem", skuItem).uniqueResult();
    }
    
    public SkuItemLineItem getSkuItemLineItem(SkuItem skuItem){
    	String sql = "from SkuItemLineItem s where s.skuItem = :skuItem";
    	return (SkuItemLineItem) getSession().createQuery(sql).setParameter("skuItem", skuItem).uniqueResult();
    }
    
    
}
