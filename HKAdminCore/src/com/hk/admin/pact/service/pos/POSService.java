package com.hk.admin.pact.service.pos;

import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/22/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface POSService {

	public Order createOrderForStore(User user, Address address);

	public User createUserForStore(String email, String name, String password, String roleName);

	public void createCartLineItems(List<POSLineItemDto> posLineItems, Order order);

	public ShippingOrder createSOForStore(Order order, Warehouse warehouse);

}
