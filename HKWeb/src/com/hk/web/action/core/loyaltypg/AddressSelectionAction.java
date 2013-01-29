package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.user.Address;
import com.hk.pact.dao.core.AddressDao;
import com.hk.store.InvalidOrderException;

public class AddressSelectionAction extends AbstractLoyaltyAction {
	
	private List<Address> addressList = new ArrayList<Address>();
	private Address address;
	private Long selectedAddressId;
	
	@Autowired AddressDao addressDao;
	
	@DefaultHandler
	public Resolution viewAddressList() {
		addressList = getProcessor().getUserAddresses(getPrincipal().getId());
		return new ForwardResolution("/pages/loyalty/address.jsp"); 
	}
	
	
	// TODO this action has so much responsibility. Need to split.
	public Resolution confirm() {
		if(address == null) {
			address = addressDao.get(Address.class, selectedAddressId);
		}
		Long orderId = getProcessor().getOrder(getPrincipal().getId()).getId();
		getProcessor().setShipmentAddress(orderId, address);
		getProcessor().makePayment(orderId, getRemoteHostAddr());
		try {
			getProcessor().escalateOrder(orderId);
		} catch (InvalidOrderException e) {
			return new RedirectResolution("/pages/loyalty/failure.jsp");
		}
		return new RedirectResolution("/pages/loyalty/success.jsp");
	}
	
	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getSelectedAddressId() {
		return selectedAddressId;
	}

	public void setSelectedAddressId(Long selectedAddressId) {
		this.selectedAddressId = selectedAddressId;
	}
}
