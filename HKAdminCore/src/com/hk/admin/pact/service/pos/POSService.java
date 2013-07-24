package com.hk.admin.pact.service.pos;

import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/22/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface POSService {

	public Order createOrderForStore(User user, Address address, Store store);

	public User createUserForStore(String email, String name, String password, String roleName);

	public Order createCartLineItems(List<POSLineItemDto> posLineItems, Order order);

	public ShippingOrder createSOForStore(Order order, Warehouse warehouse);

	public POSLineItemDto getPosLineItemWithNonAvailableInventory(List<POSLineItemDto> posLineItemDtoList);

	public void checkoutAndUpdateInventory(List<POSLineItemDto> posLineItems, ShippingOrder shippingOrder);

	public Address createAddressForUser(String line1, String line2, String city, String state, String pincode, String phone, User customer);

	public Address createDefaultAddressForUser(User customer, String phone, Warehouse warehouse);

	public void applyOrderLevelDiscountOnCartLineItems(Order order, Double orderLevelDiscount);

	public List<PosProductSearchDto> searchProductInStore(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId);

	public List<PosSkuGroupSearchDto> searchBatchesInStore(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId);

}
