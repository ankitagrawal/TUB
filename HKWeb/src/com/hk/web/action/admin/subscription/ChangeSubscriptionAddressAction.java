package com.hk.web.action.admin.subscription;

import com.hk.domain.core.Country;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.manager.AddressBookManager;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.util.AddressMatchScoreCalculator;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/18/12
 * Time: 3:26 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SUBSCRIPTION })
@Component
public class ChangeSubscriptionAddressAction extends BaseAction {

    @Autowired
    AddressBookManager addressBookManager;
    @Autowired
    AddressService addressDao;
    @Autowired
    AddressMatchScoreCalculator addressMatchScoreCalculator;

    @Validate(required = true)
    private Subscription subscription;

    private Address newAddress;

    @ValidateNestedProperties( {
            @Validate(field = "name", maxlength = 80, required = true, on = { "save" }),
            @Validate(field = "line1", maxlength = 120, required = true, on = { "save" }),
            @Validate(field = "line2", maxlength = 120, on = { "save" }),
            @Validate(field = "city", maxlength = 60, required = true, on = { "save" }),
            @Validate(field = "state", maxlength = 50, required = true, on = { "save" }),
            @Validate(field = "pincode", maxlength = 10, required = true, on = { "save" }),
            @Validate(field = "phone", maxlength = 25, required = true, on = { "save" }) })
    private Address             address;

    private boolean             copyToUserAddressBook;

    private Long countryId;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionLoggingService subscriptionLoggingService;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/subscription/changeSubscriptionAddress.jsp");
    }

    public Resolution replace() {
        if (!subscription.getUser().getAddresses().contains(address)) {
            throw new RuntimeException("Address invalid. Selected address does not belong to the specified user");
        }
        subscription.setAddress(address);
        subscriptionService.save(subscription);
        addRedirectAlertMessage(new SimpleMessage("address replaced successfully"));
        subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.AddressChanged);
        return new RedirectResolution(ChangeSubscriptionAddressAction.class).addParameter("subscription", subscription.getId());
    }

    public Resolution edit() {
        address = subscription.getAddress();
        return new ForwardResolution("/pages/admin/subscription/editSubscriptionAddress.jsp");
    }

    public Resolution save() {
      if(address.getPincode()==null){
        address = subscription.getAddress();
        addRedirectAlertMessage(new SimpleMessage("We don't serve to this pincode !!!!"));
        return new ForwardResolution("/pages/admin/subscription/editSubscriptionAddress.jsp");
      }
      Country country = addressDao.getCountry(countryId);
      address.setCountry(country);
        if (copyToUserAddressBook) {
            address = addressBookManager.editAddress(subscription.getUser(), subscription.getAddress(), address); // here
            // edited
            // address
            // is
            // the
            // new
            // address
            // which
            // is to
            // be
            // saved
        } else {
            if (address == null) {
                address = subscription.getAddress();
            }
            boolean isDuplicateAddress = addressMatchScoreCalculator.isDuplicateAddress(address);
            if (!isDuplicateAddress) {
                address = addressDao.save(address);
            }
        }
        if (address != null) {
            subscription.setAddress(address);
            subscriptionService.save(subscription);
            addRedirectAlertMessage(new SimpleMessage("address edited successfully"));
            subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.AddressChanged);
        } else {
            addRedirectAlertMessage(new SimpleMessage("Duplicate address, it has been used in a different HK account too."));
        }
        return new RedirectResolution(ChangeSubscriptionAddressAction.class).addParameter("subscription", subscription.getId());
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(Address newAddress) {
        this.newAddress = newAddress;
    }

    public boolean isCopyToUserAddressBook() {
        return copyToUserAddressBook;
    }

    public void setCopyToUserAddressBook(boolean copyToUserAddressBook) {
        this.copyToUserAddressBook = copyToUserAddressBook;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public SubscriptionLoggingService getSubscriptionLoggingService() {
        return subscriptionLoggingService;
    }

    public void setSubscriptionLoggingService(SubscriptionLoggingService subscriptionLoggingService) {
        this.subscriptionLoggingService = subscriptionLoggingService;
    }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }
}
