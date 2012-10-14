package com.hk.rest.mobile.service.action;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.AddressBookManager;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.web.action.core.order.OrderSummaryAction;
import com.hk.web.HealthkartResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.dto.menu.MenuNode;
import com.akube.framework.gson.JsonUtils;
import net.sourceforge.stripes.action.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 2, 2012
 * Time: 9:36:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mAddress")
@Component
public class MAddressAction extends MBaseAction {
    //private static Logger logger    = LoggerFactory.getLogger(SelectAddressAction.class);

    @Autowired
    AddressDao addressDao;
    @Autowired
    OrderManager orderManager;
    @Autowired
    OrderDao orderDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    AddressBookManager addressManager;
    private List<Address> addresses = new ArrayList<Address>(1);


    //@Validate(converter = EmailTypeConverter.class)
    private String email;

    // @Validate(required = true)
    private Order order;

    //@ValidationMethod(on = "checkout")
    public void validate() {
        Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
        User user = getUserService().getUserById(getPrincipal().getId());
        if (user.getRoles().contains(tempUserRole)) {
            if (StringUtils.isBlank(email)) {
                //addValidationError("email", new SimpleError("Email is required for Guest checkout."));
            }
        }
    }

/*
    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        email = user.getEmail();

        addresses = addressDao.getVisibleAddresses(user);
        Order order = orderManager.getOrCreateOrder(user);
        selectedAddress = order.getAddress();
        if (selectedAddress == null) {
            // get the last order address? for not selecting just first non deleted one.
            if (addresses != null && addresses.size() > 0) {
                selectedAddress = addresses.get(0);
            }
        }

        return new ForwardResolution("/pages/addressBook.jsp");
    }

*/
    //@ValidationMethod(on = "remove")
    public void validateDelete() {
        User user = getUserService().getUserById(getPrincipal().getId());
    //    if (!user.getAddresses().contains(deleteAddress)) {
            //getContext().getValidationErrors().add("invalid address id", new LocalizableError("/EditAddress.action.invalid.address"));
  //      }
    }


    //    @Validate(on = "checkout", required = true)
    Address selectedAddress;

    public Resolution checkout() {
        Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
        User user = getUserService().getUserById(getPrincipal().getId());
        if (user.getRoles().contains(tempUserRole)) {
            user.setEmail(email);
            user = getUserService().save(user);
        }

        Order order = orderManager.getOrCreateOrder(user);
        order.setAddress(selectedAddress);
        orderDao.save(order);

        return new RedirectResolution(OrderSummaryAction.class);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

/*
    public Address getDeleteAddress() {
        return deleteAddress;
    }

    public void setDeleteAddress(Address deleteAddress) {
        this.deleteAddress = deleteAddress;
    }

*/
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

    @DefaultHandler
    @GET
    @Path("/addAddress/")
    @Produces("application/json")

    public String addAddress(@Context HttpServletResponse response,
                                      @QueryParam("city") String city,
                                      @QueryParam("state") String state,
                                      @QueryParam("line1") String line1,
                                      @QueryParam("line2") String line2,
                                      @QueryParam("name") String name,
                                      @QueryParam("phone") String phone,
                                      @QueryParam("pin") String pin,
                                      @QueryParam("addressId") String addressId
                                      ) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;

        try {
            User user = getUserService().getUserById(getPrincipal().getId());
            if(null!=getUserService().getLoggedInUser()){
                if(null!=addressId){
                    selectedAddress = addressDao.get(Address.class,new Long(addressId));
                }else{
            Address address = new Address();
            address.setCity(city);
            address.setState(state);
            address.setLine1(line1);
            address.setLine2(line2);
            address.setName(name);
            address.setPhone(phone);
            address.setPin(pin);
            selectedAddress = addressDao.save(address);
            
             }
            Order order = orderManager.getOrCreateOrder(user);
            order.setAddress(selectedAddress);
            orderDao.save(order);

            }else{
                message = "User not logged in";
                status = MHKConstants.NOLOGIN_NOADDRESS;
            }
        } catch (Exception e) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
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
            Address deleteAddress = new Address();
            Order order = orderManager.getOrCreateOrder(user);
            for(Address addr:getUserService().getLoggedInUser().getAddresses()){
                            if(addressId.compareTo(addr.getId())==0){
                                deleteAddress = addr;
                                break;
                            }
            }

                deleteAddress.setDeleted(true);
                addressDao.save(deleteAddress);

                order.setAddress(null);
                orderDao.save(order);

        } catch (Exception e) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, status);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;

    }
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
             if(null!=getUserService().getLoggedInUser()){
             addressList = getUserService().getLoggedInUser().getAddresses();
              if(!addressList.isEmpty()){
                  jsonBuilder = JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, addressList));
              }
                 else {
                 message = "No address available";
                 status = MHKConstants.STATUS_ERROR;
                  jsonBuilder = JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message,new ArrayList()));
         }
             }
             else{
                message = "Please login";
                 status = MHKConstants.STATUS_ERROR;
                 jsonBuilder = JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, new ArrayList()));

             }
         } catch (Exception e) {
             message = MHKConstants.NO_RESULTS;
             status = MHKConstants.STATUS_ERROR;
         }

         addHeaderAttributes(response);

         return jsonBuilder;

     }
}