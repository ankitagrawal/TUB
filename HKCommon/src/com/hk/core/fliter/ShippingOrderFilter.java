package com.hk.core.fliter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrder;

/**
 * @author vaibhav.adlakha
 */
public class ShippingOrderFilter {

  private Set<ShippingOrder> shippingOrders;

  public ShippingOrderFilter(Set<ShippingOrder> shippingOrders) {
    this.shippingOrders = shippingOrders;
  }

  public Set<ShippingOrder> filterShippingOrdersByStatus(List<EnumShippingOrderStatus> selectedOrderStatuses) {
    Set<ShippingOrder> filteredShippingOrders = new HashSet<ShippingOrder>();
    List<Long> selectedOrderStatusIDs = EnumShippingOrderStatus.getShippingOrderStatusIDs(selectedOrderStatuses);

    for (ShippingOrder shippingOrder : shippingOrders) {
      if (selectedOrderStatusIDs.contains(shippingOrder.getOrderStatus().getId())) {
        filteredShippingOrders.add(shippingOrder);
      }
    }

    return filteredShippingOrders;
  }
}
