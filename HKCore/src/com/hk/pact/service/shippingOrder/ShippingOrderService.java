package com.hk.pact.service.shippingOrder;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

/**
 * @author vaibhav.adlakha
 */
public interface ShippingOrderService {

    public ShippingOrder find(Long shippingOrderId);
    
    public ShippingOrder findByGatewayOrderId(String gatewayOrderId) ;
    
    public ShippingOrder findByTrackingId(String trackingId);

    public ShippingOrder save(ShippingOrder shippingOrder);
    
    public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity( EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity);

    public List<ShippingOrder> getShippingOrdersToSendShipmentEmail();
    
    public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria);
    
    public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, boolean isSearchForWarehouse, int pageNo, int perPage);
    
    public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage);
    
    public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, boolean isSearchForWarehouse) ;

    /**
     * Auto-escalation logic for all successful transactions This method will check inventory availability and escalate
     * orders from action queue to processing queue accordingly.
     * 
     * @param shippingOrder
     * @description shipping order
     * @return true if it passes all the use cases i.e jit or availableUnbookedInventory Ajeet - 15-Feb-2012
     */
    public boolean isShippingOrderAutoEscalable(ShippingOrder shippingOrder);

    public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder);

    public ShippingOrder escalateShippingOrderFromActionQueue(ShippingOrder shippingOrder, boolean isAutoEsc);

    /**
     * Creates a shipping order with basic details
     * 
     * @param baseOrder
     * @param warehouse
     * @return
     */
    public ShippingOrder createSOWithBasicDetails(Order baseOrder, Warehouse warehouse);


    public void logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity);

    public void logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, String comments);

    public void logShippingOrderActivity(ShippingOrder shippingOrder, User user, ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity, String comments);
    
    


}
