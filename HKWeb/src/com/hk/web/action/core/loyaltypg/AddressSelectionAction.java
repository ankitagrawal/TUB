package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.core.Country;
import com.hk.domain.core.Pincode;
import com.hk.domain.user.Address;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.courier.PincodeDao;

@Secure(hasAnyRoles = {RoleConstants.HK_USER}, authActionBean=SignInAction.class)
public class AddressSelectionAction extends AbstractLoyaltyAction {
	
	private List<Address> addressList = new ArrayList<Address>();
	
	@ValidateNestedProperties({
		@Validate(field="name" , required=true, on="confirm"),
		@Validate(field="line1" , required=true, on="confirm"),
		@Validate(field="city" , required=true, on="confirm"),
		@Validate(field="state" , required=true, on="confirm"),
		@Validate(field="pincode" , required=true, on="confirm"),
		@Validate(field="phone" , required=true, minlength=10, maxlength=16, on="confirm")		
		})
	private Address address;
	private String pincode;
	private Long selectedAddressId;
	
	@Autowired AddressDao addressDao;
	@Autowired PincodeDao pincodeDao;
	
	@DefaultHandler
	public Resolution viewAddressList() {
		this.addressList = this.getProcessor().getUserAddresses(this.getPrincipal().getId());
		return new ForwardResolution("/pages/loyalty/address.jsp"); 
	}
	
	
	public Resolution confirm() {
		if(this.address != null) {
			this.address = this.addressDao.get(Address.class, this.selectedAddressId);
		} else {
			Pincode pin = this.pincodeDao.getByPincode(this.pincode);
			this.address.setPincode(pin);
			Country country = this.addressDao.get(Country.class, 80l);
			this.address.setCountry(country);
		}
		Long orderId = this.getProcessor().getCart(this.getPrincipal().getId()).getId();
		this.getProcessor().setShipmentAddress(orderId, this.address);
		
		return new RedirectResolution(PlaceOrderAction.class);
	}
	
	@ValidationMethod(on="confirm" )
	public void validatePincode(ValidationErrors errors) {
		errors = this.getContext().getValidationErrors();
		Pincode pin = this.pincodeDao.getByPincode(this.pincode);
		if (pin == null ) {
			errors.add(this.pincode, new SimpleError(" The pincode {1} is not supported for delivery." ));
	}
	}
	
	public List<Address> getAddressList() {
		return this.addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public Address getAddress() {
		return this.address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getSelectedAddressId() {
		return this.selectedAddressId;
	}

	public void setSelectedAddressId(Long selectedAddressId) {
		this.selectedAddressId = selectedAddressId;
	}
	
	public String getPincode() {
		return this.pincode;
	}
	
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
}
