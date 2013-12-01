package com.hk.impl.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.LineItemDao;

@Repository
public class LineItemDaoImpl extends BaseDaoImpl implements LineItemDao {

    @Transactional
    public LineItem save(LineItem lineItem) {
        if (lineItem.getDiscountOnHkPrice() == null) {
            lineItem.setDiscountOnHkPrice(0D);
        }
        return (LineItem) super.save(lineItem);
    }

    public List<LineItem> getLineItem(Sku sku, ShippingOrder shippingOrder) {
        String query = "select li from LineItem li where li.sku = :sku  and li.shippingOrder = :shippingOrder";
        return (List<LineItem>) getSession().createQuery(query).setEntity("sku", sku).setEntity("shippingOrder", shippingOrder).list();
    }

    public LineItem getLineItem(CartLineItem cartLineItem) {
        String query = "select li from LineItem li where li.cartLineItem = :cartLineItem order by id desc";
        List<LineItem> lineItems = (List<LineItem>) getSession().createQuery(query).setParameter("cartLineItem", cartLineItem).list();
        return lineItems != null && !lineItems.isEmpty() ? lineItems.get(0) : null;
    }

    public void flipProductVariants(Sku srcSku, Sku dstSku, ShippingOrder shippingOrder) {

        LineItem li = (LineItem) findUniqueByNamedParams(" from LineItem li where li.shippingOrder = :shippingOrder and li.sku = :srcSku ",
            new String[]{"shippingOrder","srcSku"},
            new Object[]{shippingOrder, srcSku});
        li.setSku(dstSku);
        update(li);
        
        /*getSession().createQuery("update LineItem li  set li.sku = :dstSku where li.shippingOrder = :shippingOrder and li.sku = :srcSKu").setParameter("dstSku", dstSku).setParameter(
                "shippingOrder", shippingOrder).setParameter("srcSKu", srcSKu).executeUpdate();*/
    }

	public LineItem getMatchingLineItemForDuplicateShippingOrder(LineItem lineItem, ShippingOrder shippingOrderOld){
		for(LineItem lineItemOld : shippingOrderOld.getLineItems()){
			if(lineItemOld.getCartLineItem().getId().equals(lineItem.getCartLineItem().getId())){
				return lineItemOld;
			}
		}
		return null;
	}

    public List<String> getLineItemListShippedByCourier(Date startDate, Date endDate, Long courier_id) {

        /*
         * if (startDate == null) { Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MONTH, -1);
         * startDate = calendar.getTime(); } if (endDate == null) { endDate = new Date(); } String query = "select
         * distinct li.trackingId " + " from LineItem li where " + " li.courier.id = :AFLWiz " + " and li.shipDate
         * between :startDate and :endDate " + " and li.deliveryDate is null "; return getSession().createQuery(query)
         * .setParameter("AFLWiz", courier_id) .setParameter("startDate", startDate) .setParameter("endDate", endDate)
         * .list();
         */

        return null;

        // TODO: # warehouse fix this

    }

    @Deprecated
    public List<LineItem> getCourierDeliveryReport(Date startDate, Date endDate, Long courier_id) {

        // TODO: # warehouse fix this.

        /*
         * if (startDate == null) { Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MONTH, -1);
         * startDate = calendar.getTime(); } if (endDate == null) { endDate = new Date(); } String query = "select li as
         * line_item " + " from LineItem li,Order o,User u,Address ad,Courier c where " + " li.courier.id = :courier_id " + "
         * and li.order.id = o.id and o.user.id = u.id and ad.user.id = u.id " + " and c.id = li.courier.id and
         * li.deliveryDate is not null " + " and li.shipDate between :startDate and :endDate " + " group by
         * li.trackingId " + " order by li.shipDate "; return getSession().createQuery(query)
         * .setParameter("courier_id", courier_id) .setParameter("startDate", startDate) .setParameter("endDate",
         * endDate) .list();
         */
        /*
         * select order_id,li.tracking_id,c.name,cast(li.ship_date AS DATE) as ship_date, cast(li.delivery_date as DATE)
         * as delivery_date,ad.city,ad.pin, datediff(cast(li.delivery_date AS DATE) , cast(li.ship_date AS DATE)) as
         * "days to deliver" from line_item li,base_order o,user u,address ad,courier c where li.courier_id in (60) and
         * li.order_id=o.id and o.user_id=u.id and ad.user_id=u.id and c.id = li.courier_id and li.delivery_date is not
         * null and li.ship_date BETWEEN '2011-9-1' AND '2011-10-15' group by li.tracking_id order by li.ship_date
         */

        return null;
    }

    // @Deprecated
    // public List<LineItem> getShippedLineItemsBetweenTimeFrame(Date startDate, Date endDate, List<OrderStatus>
    // applicableOrderStatusList, List<LineItemType> applicableLineItemTypes) {
    // TODO: # warehouse fix this.

    /*
     * if (startDate == null) { Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.DATE, 1); startDate =
     * calendar.getTime(); } if (endDate == null) { endDate = new Date(); } String query = "select li as line_item " + "
     * from LineItem li,Order o where " + " li.order.id = o.id" + " and li.shipDate is not null and li.shipDate between
     * :startDate and :endDate " + " and li.lineItemType in (:applicableLineItemTypes) " + " and o.orderStatus in
     * (:applicableOrderStatusList) " + " order by li.shipDate, o.id"; return getSession().createQuery(query)
     * .setParameterList("applicableLineItemTypes", applicableLineItemTypes)
     * .setParameterList("applicableOrderStatusList", applicableOrderStatusList) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .list();
     */

    /*
     * return null; }
     */
}
