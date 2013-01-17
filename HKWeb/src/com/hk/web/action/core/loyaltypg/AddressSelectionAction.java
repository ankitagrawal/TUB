package com.hk.web.action.core.loyaltypg;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.user.Address;
import com.hk.store.StoreProcessor;

public class AddressSelectionAction extends BaseAction {
	
	@Qualifier("loyaltyStoreProcessor")
	@Autowired
	StoreProcessor processor;

	private List<Address> addressList;
	private Address address;
	
	@DefaultHandler
	public Resolution viewAddressList() {
		addressList = processor.getUserAddresses(getPrincipal().getId());
		return new ForwardResolution("/pages/loyalty/address.jsp"); 
	}
	
	public List<Address> getAddressList() {
		return addressList;
	}
	
	public Resolution selectAddress() {
		processor.shipmentAddress(processor.getOrder(getPrincipal().getId()).getId(), address);
		return new ForwardResolution("/pages/loyalty/address.jsp"); 
	}
	
	public Resolution viewOrder() {
		return new RedirectResolution(ConfirmOrderAction.class);
	}
}
