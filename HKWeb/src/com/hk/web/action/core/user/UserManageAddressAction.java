package com.hk.web.action.core.user;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.core.Country;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AddressService;

@Secure(hasAnyRoles = {RoleConstants.HK_USER, RoleConstants.HK_UNVERIFIED, RoleConstants.ADMIN})
@HttpCache(allow = false)
public class UserManageAddressAction extends BaseAction {
    // private static Logger logger = Logger.getLogger(UserManageAddressAction.class);

    Address address;
    List<Address> addresses;
    String mainAddressId;
    Boolean selected;
    Affiliate affiliate;
    User user;
    private Long countryId;

    @ValidateNestedProperties({
            @Validate(field = "name", required = true, on = "saveAddress"),
            @Validate(field = "line1", required = true, on = "saveAddress"),
            @Validate(field = "city", required = true, on = "saveAddress"),
            @Validate(field = "state", required = true, on = "saveAddress"),
            @Validate(field = "pincode", required = true, on = "saveAddress"),
            @Validate(field = "phone", required = true, on = "saveAddress")})
    @Autowired
    AffiliateDao affiliateDao;
    @Autowired
    AffiliateManager affiliateManager;
    @Autowired
    UserDao userDao;
    @Autowired
    AddressService addressDao;
    @Autowired
    UserService userService;

    @DefaultHandler
    @DontValidate
    public Resolution showAddressBook() {
        if (user == null) {
            user = getUserService().getUserById(getPrincipal().getId());
        } else {
            userDao.refresh(user);
        }
        addresses = addressDao.getVisibleAddresses(user);
        affiliate = affiliateDao.getAffilateByUser(user);

        if (isHybridRelease()) {
            return new ForwardResolution("/pages/manageUserAddressesBeta.jsp");
        }
        return new ForwardResolution("/pages/manageUserAddresses.jsp");
    }

    public Resolution editUserAddresses() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
        }
        if (isHybridRelease()) {
            return new ForwardResolution("/pages/editUserAddressesBeta.jsp");
        }
        return new ForwardResolution("/pages/editUserAddresses.jsp");

    }

    public Resolution saveAddress() {
        user = userService.getLoggedInUser();
        if (user != null) {
            if (address.getPincode() == null) {
                addRedirectAlertMessage(new SimpleMessage("<style=\"color:red;font-size:14px; font-weight:bold;\"> We don't Service to this Pincode, please enter again or Contact to Customer Care!!!</style>"));

                if (isHybridRelease()) {
                    return new ForwardResolution("/pages/editUserAddressesBeta.jsp");
                }
                return new ForwardResolution("/pages/editUserAddresses.jsp").addParameter("address", address.getId());
            }
            address.setUser(user);
            Country country = addressDao.getCountry(countryId);
            address.setCountry(country);
            address = addressDao.save(address);
        }
        addRedirectAlertMessage(new SimpleMessage("<div class=\"alert-cntnr\">" +
                                                    "<span class=\"icn-success mrgn-r-10\"></span>" +
                                                        "Your changes have been saved." +
                                                    "<span class=\"icn icn-close2 remove-success\"></span>" +
                                                  "</div>"));
        return showAddressBook();
    }

    public Resolution manageAddresses() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            addresses = addressDao.getVisibleAddresses(user);
            if (affiliate != null) {
                mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : "";
            }
        }
        if (addresses.size() > 0) {

            if (isHybridRelease()) {
                return new ForwardResolution("/pages/manageUserAddressesBeta.jsp");
            }
            return new ForwardResolution("/pages/manageUserAddresses.jsp");
        } else {
            if (isHybridRelease()) {
                return new ForwardResolution("/pages/editUserAddressesBeta.jsp");
            }
            return new ForwardResolution("/pages/editUserAddresses.jsp");
        }
    }

    public Resolution remove() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            if (affiliate != null && affiliate.getMainAddressId() != null) {
                if (affiliate.getMainAddressId().equals(address.getId())) {
                    addRedirectAlertMessage(new SimpleMessage("The address cannot be deleted as it is set as your default address!"));
                    return new ForwardResolution(UserManageAddressAction.class, "showAddressBook");
                }
            }
        }
        address.setDeleted(true);
        addressDao.save(address);
        addRedirectAlertMessage(new SimpleMessage("<div class=\"alert-cntnr\">" +
                                                            "<span class=\"icn-success mrgn-r-10\"></span>" +
                                                                "Address has been successfully deleted" +
                                                            "<span class=\"icn icn-close2 remove-success\"></span>" +
                                                        "</div>"));
        return new ForwardResolution(UserManageAddressAction.class, "showAddressBook");
    }

    // for affiliates only
    public Resolution setAsDefaultAddress() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            affiliate.setMainAddressId(address.getId());
            mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : "";
            affiliateDao.save(affiliate);
            addRedirectAlertMessage(new SimpleMessage("Default Set."));
        }
        return new ForwardResolution(UserManageAddressAction.class, "showAddressBook");
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getMainAddressId() {
        return mainAddressId;
    }

    public void setMainAddressId(String mainAddressId) {
        this.mainAddressId = mainAddressId;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}