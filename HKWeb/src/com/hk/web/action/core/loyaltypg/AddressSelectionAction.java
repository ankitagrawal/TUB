package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.core.Pincode;
import com.hk.domain.user.Address;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.courier.PincodeDao;

public class AddressSelectionAction extends AbstractLoyaltyAction {
	
	private List<Address> addressList = new ArrayList<Address>();
	private Address address;
	private String pincode;
	private Long selectedAddressId;
	
	@Autowired AddressDao addressDao;
	@Autowired PincodeDao pincodeDao;
	
	@DefaultHandler
	public Resolution viewAddressList() {
		addressList = getProcessor().getUserAddresses(getPrincipal().getId());
		return new ForwardResolution("/pages/loyalty/address.jsp"); 
	}
	
	
	public Resolution confirm() {
		if(address == null) {
			address = addressDao.get(Address.class, selectedAddressId);
		} else {
			Pincode pin = pincodeDao.getByPincode(pincode);
			address.setPincode(pin);
		}
		Long orderId = getProcessor().getOrder(getPrincipal().getId()).getId();
		getProcessor().setShipmentAddress(orderId, address);
		
		return new RedirectResolution(PlaceOrderAction.class);
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
	
	public String getPincode() {
		return pincode;
	}
	
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
}
