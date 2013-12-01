package com.hk.web.action.core.user;

import com.hk.domain.core.Country;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Modal;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.LinkManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AddressService;
import com.hk.util.AddressMatchScoreCalculator;
import com.hk.web.action.core.affiliate.AffiliateAccountAction;
import com.hk.web.action.core.order.OrderSummaryAction;

@Secure
@Modal
@Component
public class NewAddressAction extends BaseAction implements ValidationErrorHandler {

    @Autowired
    AddressService              addressDao;
    @Autowired
    LinkManager                 linkManager;
    @Autowired
    OrderManager                orderManager;
    @Autowired
    OrderDao                    orderDao;
    @Autowired
    AddressMatchScoreCalculator addressMatchScoreCalculator;
    @Autowired
    private UserService         userService;

    @ValidateNestedProperties( {
            @Validate(field = "name", required = true, maxlength = 80),
            @Validate(field = "line1", required = true, maxlength = 120),
            @Validate(field = "line2", maxlength = 120),
            @Validate(field = "city", required = true, maxlength = 60),
            @Validate(field = "state", required = true, maxlength = 50),
            @Validate(field = "pincode", required = true, maxlength = 6),
            @Validate(field = "phone", required = true, maxlength = 25) })
    private Address             address;
    private User                user;
    private Long                countryId;

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new ForwardResolution(SelectAddressAction.class);
    }

    @DontValidate
    public Resolution show() {
        user = getUserService().getUserById(getPrincipal().getId());
        return new ForwardResolution("/pages/newAddress.jsp");
    }

    @DefaultHandler
    /*
     * @JsonHandler public Resolution create() { user = getUserService().getUserById(getPrincipal().getId());
     * address.setUser(user); addressDao.save(address); addRedirectAlertMessage(new
     * LocalizableMessage("/NewAddress.action.success")); Map<String, String> data = ImmutableMap.of("url",
     * linkManager.getSelectAddressUrl()); return new JsonResolution(new
     * HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, "New address added!", data)); }
     */
    public Resolution create() {
      if(address!=null){
        if(address.getPincode()==null){
          addRedirectAlertMessage(new SimpleMessage("We don't Service in this Pincode, please enter valid one or Call Customer Care"));
          return new RedirectResolution(SelectAddressAction.class).addParameter("printAlert",true);
        }
      }
        user = getUserService().getUserById(getPrincipal().getId());
        address.setUser(user);

        boolean isDuplicateAddress = addressMatchScoreCalculator.isDuplicateAddress(address);
        if (!isDuplicateAddress) {
            Country country = addressDao.getCountry(countryId);
            address.setCountry(country);
            address = addressDao.save(address);
            Order order = orderManager.getOrCreateOrder(user);
            order.setAddress(address);
            orderDao.save(order);
            return new RedirectResolution(OrderSummaryAction.class);
        } else {
            addRedirectAlertMessage(new SimpleMessage("Duplicate Address. It has been used in a different HK account."));
            return new RedirectResolution(SelectAddressAction.class);
        }
    }

    public Resolution saveAffiliateAddress() {
        user = getUserService().getUserById(getPrincipal().getId());
        address.setUser(user);
        address = addressDao.save(address);

        Order order = orderManager.getOrCreateOrder(user);
        order.setAddress(address);
        orderDao.save(order);

        return new RedirectResolution(AffiliateAccountAction.class);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }
}
