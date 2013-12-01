package com.hk.pact.dao.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;

import com.hk.domain.courier.PickupStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.akube.framework.dao.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 4, 2012
 * Time: 5:23:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReverseOrderDao {

	public ReverseOrder save(ReverseOrder reverseOrder);

	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Long courierId,  Long warehouseId, int page, int perPage ,Date startDate , Date endDate);

	 public List<ReverseOrder> getPickupRequestsForExcel(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Long courierId ,Long warehouseId  , Date startDate , Date endDate);

	public ReverseOrder getReverseOrderById(Long id);

	public ReverseOrder getReverseOrderByShippingOrderId(Long shippingOrderId);

	public Page getReverseOrderWithNoPickupSchedule( int page, int perPage);

  public List<ReverseOrder> findReverseOrderForTimeFrame(Long warehouseId, Date startDate, Date endDate);

	//public List<ReverseOrder> getPickupRequestsByStatuses(Boolean pickupStatus, String reconciliationStatus);


}
