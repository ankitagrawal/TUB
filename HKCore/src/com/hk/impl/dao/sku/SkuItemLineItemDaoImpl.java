package com.hk.impl.dao.sku;

import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemLineItem;
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
        Query query = getSession().createQuery(sql);
        return (List<SkuItemLineItem>)query.list();

    }
}
