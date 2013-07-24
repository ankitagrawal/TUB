package com.hk.admin.pact.service.shippingOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;

public interface AdminShippingOrderService {

    //  public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);

    public void cancelShippingOrder(ShippingOrder shippingOrder,String comment);

    public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse);

    public ShippingOrder createSOforManualSplit(Set<CartLineItem> cartLineItems, Warehouse warehouse);    

//    public ShippingOrder createSOForService(CartLineItem serviceCartLineItem);

    public ShippingOrder putShippingOrderOnHold(ShippingOrder shippingOrder);

    public ShippingOrder unHoldShippingOrder(ShippingOrder shippingOrder);

    public ShippingOrder markShippingOrderAsDelivered(ShippingOrder shippingOrder);

    public ShippingOrder markShippingOrderAsShipped(ShippingOrder shippingOrder);

    public ShippingOrder markShippingOrderAsPrinted(ShippingOrder shippingOrder);

    public ShippingOrder moveShippingOrderToPickingQueue(ShippingOrder shippingOrder);

    public ShippingOrder moveShippingOrderBackToActionQueue(ShippingOrder shippingOrder);

    public ShippingOrder moveShippingOrderBackToPackingQueue(ShippingOrder shippingOrder);

    public ShippingOrder moveShippingOrderBackToDropShippingQueue(ShippingOrder shippingOrder);

     ShippingOrder markShippingOrderAsRTO(ShippingOrder shippingOrder);

    public ShippingOrder markShippingOrderAsInstalled(ShippingOrder shippingOrder);

    public ShippingOrder markShippingOrderAsLost(ShippingOrder shippingOrder);

    ShippingOrder initiateRTOForShippingOrder(ShippingOrder shippingOrder, ReplacementOrderReason rtoReason);

    public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId);

	public ReplacementOrderReason getRTOReasonForShippingOrder(ShippingOrder shippingOrder);

	public ReplacementOrderReason getReplacementOrderReasonByName(String replacementOrderReason);

}
