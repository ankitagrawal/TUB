package com.hk.web.action.core.user;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;

@Secure(hasAnyRoles = { RoleConstants.HK_USER, RoleConstants.HK_UNVERIFIED, RoleConstants.ADMIN })
public class UserManageAddressAction extends BaseAction {
    private static Logger logger = Logger.getLogger(UserManageAddressAction.class);
    
    Address               address;
    List<Address>         addresses;
    String                mainAddressId;
    Boolean               selected;
    Affiliate             affiliate;
    User                  user;

    @ValidateNestedProperties( {
            @Validate(field = "name", required = true, on = "saveAddress"),
            @Validate(field = "line1", required = true, on = "saveAddress"),
            @Validate(field = "city", required = true, on = "saveAddress"),
            @Validate(field = "state", required = true, on = "saveAddress"),
            @Validate(field = "pin", required = true, on = "saveAddress"),
            @Validate(field = "phone", required = true, on = "saveAddress") })
    @Autowired
    AffiliateDao          affiliateDao;
    @Autowired
    AffiliateManager      affiliateManager;
    @Autowired
    UserDao               userDao;
    @Autowired
    AddressDao            addressDao;
    @Autowired
    UserService           userService;

    @DefaultHandler
    @DontValidate
    public Resolution showAddressBook() {
        // User user = userDao.find(getPrincipal().getId());
        if (user == null) {
            user = getUserService().getUserById(getPrincipal().getId());
        } else {
            userDao.refresh(user);
        }

        affiliate = affiliateDao.getAffilateByUser(user);
        // addresses = user.getAddresses();
        return new ForwardResolution("/pages/manageUserAddresses.jsp");
    }

    public Resolution editUserAddresses() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
        }
        // return new RedirectResolution("/pages/editUserAddresses.jsp").addParameter("address", address.getId());
        return new ForwardResolution("/pages/editUserAddresses.jsp");

    }

    /*
     * public Resolution addNewAddress() { if (getPrincipal() != null) { // User user =
     * userDao.find(getPrincipal().getId()); user = userDao.find(getPrincipal().getId()); affiliate =
     * affiliateDao.getAffilateByUser(user); } // return new ForwardResolution("/pages/addUserAddress.jsp"); return new
     * ForwardResolution("/pages/editUserAddresses.jsp"); } public Resolution editUserAddress() { if (getPrincipal() !=
     * null) { // User user = userDao.find(getPrincipal().getId()); user = userDao.find(getPrincipal().getId());
     * logger.debug("Editing address " + address.getId() + " for " + user.getName()); } return new
     * RedirectResolution("/pages/editUserAddresses.jsp").addParameter("address", address.getId()); }
     */

    public Resolution saveAddress() {
        user = userService.getLoggedInUser();
        if (user != null) {
            // if(address.getId() == null){
            address.setUser(user);
            address = addressDao.save(address);
            // }
        }
        addRedirectAlertMessage(new SimpleMessage("Your changes have been saved."));
        return showAddressBook();
    }

    /*
     * public Resolution saveEditedAddress() { if (getPrincipal() != null) { // User user =
     * userDao.find(getPrincipal().getId()); user = userDao.find(getPrincipal().getId()); affiliate =
     * affiliateDao.getAffilateByUser(user); if (affiliate != null) { //checking that mainAddressId should not be null ,
     * otherwise may throw exception. if (mainAddressId != null)
     * affiliate.setMainAddressId(Long.parseLong(mainAddressId)); affiliateDao.save(affiliate); } address.setUser(user);
     * address = addressDao.save(address); } addRedirectAlertMessage(new SimpleMessage("changes saved.")); return new
     * ForwardResolution(UserManageAddressAction.class, "showAddressBook"); } public Resolution saveNewAddress() { if
     * (getPrincipal() != null) { // User user = userDao.find(getPrincipal().getId()); user =
     * userDao.find(getPrincipal().getId()); address.setUser(user); address.setPhone("0".concat(address.getPhone()));
     * address = addressDao.save(address); affiliate = affiliateDao.getAffilateByUser(user); if (affiliate != null) {
     * mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : ""; }
     * addRedirectAlertMessage(new SimpleMessage("New Address successfully added.")); } // return new
     * ForwardResolution(UserManageAddressAction.class, "showAddressBook"); return showAddressBook(); }
     */

    public Resolution manageAddresses() {
        List<Address> addresses = new ArrayList<Address>();
        if (getPrincipal() != null) {
            // User user = userDao.find(getPrincipal().getId());
            user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            addresses = user.getAddresses();
            if (affiliate != null) {
                mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : "";
            }
        }
        if (addresses.size() > 0) {
            return new ForwardResolution("/pages/manageUserAddresses.jsp");
        } else {
            // return new ForwardResolution("/pages/addUserAddress.jsp");
            return new ForwardResolution("/pages/editUserAddresses.jsp");
        }
    }

    public Resolution remove() {
        if (getPrincipal() != null) {
            // User user = userDao.find(getPrincipal().getId());
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
        addRedirectAlertMessage(new LocalizableMessage("/SelectAddress.action.address.deleted"));
        return new ForwardResolution(UserManageAddressAction.class, "showAddressBook");
    }

    // for affiliates only

    public Resolution setAsDefaultAddress() {
        if (getPrincipal() != null) {
            // User user = userDao.find(getPrincipal().getId());
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
}