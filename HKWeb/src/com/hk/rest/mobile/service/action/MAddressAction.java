package com.hk.rest.mobile.service.action;

import com.akube.framework.gson.JsonUtils;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.core.Country;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.AddressBookManager;
import com.hk.manager.OrderManager;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.dao.core.AddressDao;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.order.OrderSummaryAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Satish Date: Oct 2, 2012 Time: 9:36:13 PM To
 * change this template use File | Settings | File Templates.
 */
@Path("/mAddress")
@Component
public class MAddressAction extends MBaseAction {

	@Autowired
	OrderManager orderManager;
	@Autowired
	OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	AddressBookManager addressManager;
	@Autowired
	private PincodeCourierService pincodeCourierService;
  @Autowired
  PincodeService pincodeService;
  @Autowired
  AddressDao addressDao;

	private List<Address> addresses = new ArrayList<Address>(1);

	// @Validate(on = "checkout", required = true)
	Address selectedAddress;

	// @Validate(converter = EmailTypeConverter.class)
	private String email;

	// @Validate(required = true)
	private Order order;

	@DefaultHandler
	@GET
	@Path("/addAddress/")
	@Produces("application/json")
	public String addAddress(@Context HttpServletResponse response,
			@QueryParam("city") String city, @QueryParam("state") String state,
			@QueryParam("line1") String line1,
			@QueryParam("line2") String line2, @QueryParam("name") String name,
			@QueryParam("phone") String phone, @QueryParam("pin") String pin,
			@QueryParam("addressId") String addressId) {
		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;

        Pincode pincode = pincodeService.getByPincode(pin);

        if(pincode == null){
            //todo courier refactor handle this
        }

		try {
			User user = getUserService().getUserById(getPrincipal().getId());
			if (null != getUserService().getLoggedInUser()) {
				if (null != addressId) {
					selectedAddress = addressManager.getAddressDao().get(Address.class, new Long(addressId));
				} else {
					Address address = new Address();
					address.setCity(city);
					address.setState(state);
					address.setLine1(line1);
					address.setLine2(line2);
					address.setName(name);
					address.setPhone(phone);
          Country country = addressDao.getCountry(80L);
          address.setCountry(country);
					if (pincodeCourierService.isCodAllowed(pin))
						address.setPincode(pincode);
					else {
						message = MHKConstants.COD_NOT_IN_PINCODE + pin;
						status = MHKConstants.STATUS_ERROR;
						return JsonUtils.getGsonDefault()
								.toJson(new HealthkartResponse(status, message,
										message));
					}
					// selectedAddress = addressDao.save(address);
					selectedAddress = addressManager.add(user, address);

				}
				Order order = orderManager.getOrCreateOrder(user);
				order.setAddress(selectedAddress);
				orderService.save(order);

			} else {
				message = MHKConstants.PLEASE_LOGIN;
				status = MHKConstants.NOLOGIN_NOADDRESS;
				return JsonUtils.getGsonDefault().toJson(
						new HealthkartResponse(status, message, message));
			}
		} catch (Exception e) {
			message = MHKConstants.NO_RESULTS;
			status = MHKConstants.STATUS_ERROR;
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(status, message, status));
		}
		healthkartResponse = new HealthkartResponse(status, message, status);
		jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

		addHeaderAttributes(response);

		return jsonBuilder;

	}

	@GET
	@Path("/removeAddress/")
	@Produces("application/json")
	public String removeAddress(@Context HttpServletResponse response,
			@QueryParam("addressId") Long addressId) {
		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;

		try {
			User user = getUserService().getUserById(getPrincipal().getId());
			Address deleteAddress = null;
			Order order = orderManager.getOrCreateOrder(user);
			for (Address addr : getUserService().getLoggedInUser()
					.getAddresses()) {
				if (addressId.compareTo(addr.getId()) == 0) {
					deleteAddress = addr;
					break;
				}
			}
			if(null==deleteAddress)
			 {
				 message = MHKConstants.INVALID_ADDRESS;
				 status = MHKConstants.STATUS_ERROR;
				return JsonUtils.getGsonDefault().toJson(
						new HealthkartResponse(status, message, status));

				 }

			deleteAddress.setDeleted(true);
			addressManager.getAddressDao().save(deleteAddress);

			order.setAddress(null);
			orderService.save(order);

		} catch (Exception e) {
			message = MHKConstants.NO_RESULTS;
			status = MHKConstants.STATUS_ERROR;
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(status, message, status));
		}
		healthkartResponse = new HealthkartResponse(status, message, status);
		jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

		addHeaderAttributes(response);

		return jsonBuilder;

	}

	@SuppressWarnings("unchecked")
    @DefaultHandler
	@GET
	@Path("/addresses/")
	@Produces("application/json")
	public String userAddressList(@Context HttpServletResponse response) {
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;
		List<Address> addressList = new ArrayList<Address>();
		try {
			if (null != getUserService().getLoggedInUser()) {
				addressList = getUserService().getLoggedInUser().getAddresses();
				if (!addressList.isEmpty()) {
					jsonBuilder = JsonUtils.getGsonDefault()
							.toJson(new HealthkartResponse(status, message,
									addressList));
				} else {
					message = MHKConstants.NO_ADDRESS_AVAILABLE;
					status = MHKConstants.STATUS_ERROR;
					jsonBuilder = JsonUtils.getGsonDefault().toJson(
							new HealthkartResponse(status, message,
									new ArrayList()));
				}
			} else {
				message = MHKConstants.PLEASE_LOGIN;
				status = MHKConstants.STATUS_ERROR;
				jsonBuilder = JsonUtils.getGsonDefault()
						.toJson(new HealthkartResponse(status, message,
								new ArrayList()));

			}
		} catch (Exception e) {
			message = MHKConstants.NO_RESULTS;
			status = MHKConstants.STATUS_ERROR;
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(status, message, status));
		}

		addHeaderAttributes(response);

		return jsonBuilder;

	}

	// @ValidationMethod(on = "checkout")
	public void validate() {
		Role tempUserRole = getRoleService().getRoleByName(
				RoleConstants.TEMP_USER);
		User user = getUserService().getUserById(getPrincipal().getId());
		if (user.getRoles().contains(tempUserRole)) {
			if (StringUtils.isBlank(email)) {
				// addValidationError("email", new
				// SimpleError("Email is required for Guest checkout."));
			}
		}
	}

	/*
	 * @DefaultHandler
	 * 
	 * @DontValidate public Resolution pre() { User user =
	 * getUserService().getUserById(getPrincipal().getId()); email =
	 * user.getEmail();
	 * 
	 * addresses = addressDao.getVisibleAddresses(user); Order order =
	 * orderManager.getOrCreateOrder(user); selectedAddress =
	 * order.getAddress(); if (selectedAddress == null) { // get the last order
	 * address? for not selecting just first non deleted one. if (addresses !=
	 * null && addresses.size() > 0) { selectedAddress = addresses.get(0); } }
	 * 
	 * return new ForwardResolution("/pages/addressBook.jsp"); }
	 */

	public Resolution checkout() {
		Role tempUserRole = getRoleService().getRoleByName(
				RoleConstants.TEMP_USER);
		User user = getUserService().getUserById(getPrincipal().getId());
		if (user.getRoles().contains(tempUserRole)) {
			user.setEmail(email);
			user = getUserService().save(user);
		}

		Order order = orderManager.getOrCreateOrder(user);
		order.setAddress(selectedAddress);
		orderService.save(order);

		return new RedirectResolution(OrderSummaryAction.class);
	}

	public Address getSelectedAddress() {
		return selectedAddress;
	}

	public void setSelectedAddress(Address selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	/*
	 * public Address getDeleteAddress() { return deleteAddress; }
	 * 
	 * public void setDeleteAddress(Address deleteAddress) { this.deleteAddress
	 * = deleteAddress; }
	 */
}