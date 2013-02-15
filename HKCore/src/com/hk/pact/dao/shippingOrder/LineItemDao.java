package com.hk.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;

public interface LineItemDao extends BaseDao {

    public LineItem save(LineItem lineItem);

    public List<LineItem> getLineItem(Sku sku, ShippingOrder shippingOrder);

    public LineItem getLineItem(CartLineItem cartLineItem);

    public void flipProductVariants(Sku srcSKu, Sku dstSku, ShippingOrder shippingOrder);

	public LineItem getMatchingLineItemForDuplicateShippingOrder(LineItem lineItem, ShippingOrder originalShippingOrder);

    public List<String> getLineItemListShippedByCourier(Date startDate, Date endDate, Long courier_id);

    @Deprecated
    public List<LineItem> getCourierDeliveryReport(Date startDate, Date endDate, Long courier_id);

}
