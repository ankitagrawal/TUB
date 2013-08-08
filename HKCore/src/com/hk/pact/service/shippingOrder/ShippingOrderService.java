package com.hk.pact.service.shippingOrder;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

/**
 * @author vaibhav.adlakha
 */
public interface ShippingOrderService {

	public ShippingOrder find(Long shippingOrderId);

	public ShippingOrder findByGatewayOrderId(String gatewayOrderId);

	public ShippingOrder save(ShippingOrder shippingOrder);

	public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity);

	public List<ShippingOrder> getShippingOrdersToSendShipmentEmail();


	public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria);

	public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, boolean isSearchForWarehouse, int pageNo, int perPage);

	public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage);

	public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, boolean isSearchForWarehouse);

    //Creates a shipping order with basic details
	public ShippingOrder createSOWithBasicDetails(Order baseOrder, Warehouse warehouse);

	public void nullifyCodCharges(ShippingOrder shippingOrder);
	
	public ShippingOrder setGatewayIdAndTargetDateOnShippingOrder(ShippingOrder shippingOrder) ;
	
	public void setTargetDispatchDelDatesOnSO(Date refDate, ShippingOrder shippingOrder);


	public ShippingOrderLifecycle logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity);

	public ShippingOrderLifecycle logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, Reason reason, String comments);

	public ShippingOrderLifecycle logShippingOrderActivity(ShippingOrder shippingOrder, User user, ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity, Reason reason, String comments);

	public ShippingOrderLifecycle logShippingOrderActivityByAdmin(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, Reason reason);

	public boolean shippingOrderHasReplacementOrder(ShippingOrder shippingOrder);
	
	public boolean shippingOrderContainsProductVariant(ShippingOrder shippingOrder, ProductVariant productVariant, Double mrp);

    /*public void revertRewardPointsOnSOCancel(ShippingOrder shippingOrder, String comment) ;*/

    public List<Reason> getReasonForReversePickup(List<Long> listOfReasonIds);

    public void validateShippingOrder(ShippingOrder shippingOrder);

}
