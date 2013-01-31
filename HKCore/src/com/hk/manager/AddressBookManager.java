package com.hk.manager;

import com.hk.pact.service.core.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.UserService;
import com.hk.util.AddressMatchScoreCalculator;

import java.util.Date;

@Component
public class AddressBookManager {

    @Autowired
    private AddressService              addressService;
	@Autowired
    private AddressDao                  addressDao;
    @Autowired
    private UserService                 userService;
    @Autowired
    private OrderManager                orderManager;
    @Autowired
    private AddressMatchScoreCalculator addressMatchScoreCalculator;

    /**
     * Logic: Here we are treating Address as a immutable field. i.e we will never update any field in the database
     * corresponding to the Address infact every time we will create a new address and set the address fields
     * accordingly so that it looks like the edited copy to the user. <p/> This method will maintain the immutability of
     * the Address domain. <p/> First we will set the create date and courier field in the new address andd save it.
     * Then refresh OrderHasAddress if the address to be edited is in the cart order addresses. i.e remove the to be
     * edited address and and the newly created address. Finally remove the address from user's address list.
     * 
     * @param user
     * @param editAddress Address to be edited
     * @param newAddress New address to be created
     */
    @Transactional
    public Address editAddress(final User user, final Address editAddress, Address newAddress) {

        newAddress.setCreateDate(new Date());

        newAddress = add(user, newAddress);

        Order order = getOrderManager().getOrCreateOrder(user);

        if (order.getAddress() != null && order.getAddress().equals(editAddress)) { // check if to be edited address is
            // in any of the addresses in cart
            // order
            order.setAddress(newAddress);
        }

        remove(user, editAddress);
        return newAddress;
    }

    @Transactional
    public Address add(User user, Address address) {
        address.setUser(user);
        boolean isDuplicateAddress = addressMatchScoreCalculator.isDuplicateAddress(address);
        if (!isDuplicateAddress) {
            address = getAddressService().save(address);
            user.getAddresses().add(address);
            getUserService().save(user);
            return address;
        }
        return null;
    }

    @Transactional
    public void remove(User user, Address address) {
        user.getAddresses().remove(address);
        getUserService().save(user);
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

	public AddressDao getAddressDao() {
		return addressDao;
	}

	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public AddressMatchScoreCalculator getAddressMatchScoreCalculator() {
        return addressMatchScoreCalculator;
    }

    public void setAddressMatchScoreCalculator(AddressMatchScoreCalculator addressMatchScoreCalculator) {
        this.addressMatchScoreCalculator = addressMatchScoreCalculator;
    }

}
