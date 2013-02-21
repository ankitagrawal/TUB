package com.hk.admin.pact.service.reverseOrder;

import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.courier.Courier;
import com.akube.framework.dao.Page;

import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 8, 2013
 * Time: 11:40:00 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ReverseOrderService {

	public ReverseOrder createReverseOrder (ShippingOrder shippingOrder);

	public void createReverseLineItems(ReverseOrder reverseOrder, Map<LineItem, Long> itemMap); 	

	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Courier courier, int page, int perPage);

	public ReverseOrder getReverseOrderById(Long id);

	public void setCourierDetails(ReverseOrder reverseOrder, CourierPickupDetail courierPickupDetail);

	public ReverseOrder getReverseOrderByShippingOrderId(Long shippingOrderId);

	public ReverseOrder save(ReverseOrder reverseOrder);

	public Page getReverseOrderWithNoPickupSchedule( int page, int perPage);
}
