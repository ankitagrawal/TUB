package com.hk.web.action.core.loyaltypg;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.core.Country;
import com.hk.domain.core.Pincode;
import com.hk.domain.user.Address;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.courier.PincodeDao;

@Secure(hasAnyRoles = {RoleConstants.HK_LOYALTY_USER}, authActionBean=JoinLoyaltyProgramAction.class)
public class AddressSelectionAction extends AbstractLoyaltyAction {
	
	private List<Address> addressList;
	
	private Address address;
	private Address deleteAddress;
	private String pincode;
	private Long selectedAddressId;
	private boolean groundShippingAllowed;
	@Autowired private PincodeCourierService pincodeCourierService;
	@Autowired AddressDao addressDao;
	@Autowired PincodeDao pincodeDao;
	
	@DefaultHandler
	public Resolution viewAddressList() {
		this.addressList = this.getProcessor().getUserAddresses(this.getPrincipal().getId());
		return new ForwardResolution("/pages/loyalty/address.jsp"); 
	}
	
	
	public Resolution confirm() {
		if(this.address == null) {
			this.address = this.addressDao.get(Address.class, this.selectedAddressId);
		} else {
			Pincode pin = this.pincodeDao.getByPincode(this.pincode);
			this.address.setPincode(pin);
			
			groundShippingAllowed = pincodeCourierService.isGroundShippingAllowed(pin.getPincode());
			Country country = this.addressDao.get(Country.class, 80l);
			this.address.setCountry(country);
		}
		if (this.getProcessor().getCart(this.getPrincipal().getId()) != null ) {
			Long orderId = this.getProcessor().getCart(this.getPrincipal().getId()).getId();
			this.getProcessor().setShipmentAddress(orderId, this.address);
		} else {
			return new RedirectResolution(CartAction.class);
		}
		return new RedirectResolution(PlaceOrderAction.class);
	}
	
	
    public Resolution remove() {
        this.deleteAddress.setDeleted(true);
        this.addressDao.save(this.deleteAddress);
        return new RedirectResolution(AddressSelectionAction.class);
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


	/**
	 * @return the deleteAddress
	 */
	public Address getDeleteAddress() {
		return this.deleteAddress;
	}


	/**
	 * @param deleteAddress the deleteAddress to set
	 */
	public void setDeleteAddress(Address deleteAddress) {
		this.deleteAddress = deleteAddress;
	}


	/**
	 * @return the groundShippingAllowed
	 */
	public boolean isGroundShippingAllowed() {
		return groundShippingAllowed;
	}


	/**
	 * @param groundShippingAllowed the groundShippingAllowed to set
	 */
	public void setGroundShippingAllowed(boolean groundShippingAllowed) {
		this.groundShippingAllowed = groundShippingAllowed;
	}
}
