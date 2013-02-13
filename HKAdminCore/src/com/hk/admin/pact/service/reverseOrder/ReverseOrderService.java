package com.hk.admin.pact.service.reverseOrder;

import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.courier.CourierPickupDetail;
import com.akube.framework.dao.Page;

import java.util.Map;

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

	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, int page, int perPage);

	public ReverseOrder getReverseOrderById(Long id);

	public void setCourierDetails(ReverseOrder reverseOrder, CourierPickupDetail courierPickupDetail);

	public ReverseOrder save(ReverseOrder reverseOrder);
}
