package web.action.core.affiliate;

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
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.dao.affiliate.AffiliateDao;
import com.hk.dao.core.AddressDao;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.AffiliateManager;
import com.hk.service.UserService;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Sep 13, 2011 Time: 6:18:48 PM To change this template use File |
 * Settings | File Templates.
 */
@Secure(hasAnyRoles = { RoleConstants.HK_AFFILIATE, RoleConstants.HK_AFFILIATE_UNVERIFIED, RoleConstants.ADMIN })
@Component
public class AffiliateManageAddressAction extends BaseAction {
    private static Logger logger = Logger.getLogger(AffiliateManageAddressAction.class);
    Address               address;
    List<Address>         addresses;
    String                mainAddressId;
    Boolean               selected;
    Affiliate             affiliate;
    @ValidateNestedProperties( {
            @Validate(field = "name", required = true, on = "saveAddress"),
            @Validate(field = "line1", required = true, on = "saveAddress"),
            @Validate(field = "city", required = true, on = "saveAddress"),
            @Validate(field = "state", required = true, on = "saveAddress"),
            @Validate(field = "pin", required = true, on = "saveAddress"),
            @Validate(field = "phone", required = true, on = "saveAddress") })
    AffiliateDao          affiliateDao;
    AffiliateManager      affiliateManager;
    @Autowired
    private UserService   userService;
    AddressDao            addressDao;

    @DefaultHandler
    @DontValidate
    public Resolution showAddressBook() {
        User user = getUserService().getUserById(getPrincipal().getId());
        addresses = addressDao.getVisibleAddresses(user);
        return new ForwardResolution("/pages/affiliate/manageAffiliateAddresses.jsp");
    }

    public Resolution addNewAddress() {
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
        }
        return new ForwardResolution("/pages/affiliate/addAffiliateAddress.jsp");
    }

    public Resolution editAddresses() {
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);

            if (addresses != null) {
                for (Address address : addresses) {
                    addressDao.save(address);
                }
            }
            // checking that mainAddressId should not be null , otherwise may throw exception.
            if (mainAddressId != null)
                affiliate.setMainAddressId(Long.parseLong(mainAddressId));
            affiliateDao.save(affiliate);
        }
        addRedirectAlertMessage(new SimpleMessage("changes saved."));
        return new ForwardResolution("/pages/affiliate/manageAffiliateAddresses.jsp");
    }

    public Resolution manageAddresses() {
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            addresses = addressDao.getVisibleAddresses(affiliate.getUser());
            mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : "";
        }
        if (addresses.size() > 0) {
            return new ForwardResolution("/pages/affiliate/manageAffiliateAddresses.jsp");
        } else {
            return new ForwardResolution("/pages/affiliate/addAffiliateAddress.jsp");
        }
    }

    public Resolution saveAddress() {
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            address.setUser(user);
            address = addressDao.save(address);
            affiliate = affiliateDao.getAffilateByUser(user);
            addresses = addressDao.getVisibleAddresses(affiliate.getUser());
            mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : "";
            addRedirectAlertMessage(new SimpleMessage("New Address successfully added."));
        }
        return new ForwardResolution(AffiliateManageAddressAction.class, "showAddressBook");
    }

    public Resolution remove() {
        address.setDeleted(true);
        addressDao.save(address);
        addRedirectAlertMessage(new LocalizableMessage("/SelectAddress.action.address.deleted"));
        return new ForwardResolution(AffiliateManageAddressAction.class, "showAddressBook");
    }

    public Resolution setAsDefaultAddress() {
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            affiliate.setMainAddressId(address.getId());
            mainAddressId = affiliate.getMainAddressId() != null ? affiliate.getMainAddressId().toString() : "";
            affiliateDao.save(affiliate);
            addRedirectAlertMessage(new SimpleMessage("Default Set."));
        }
        return new ForwardResolution(AffiliateManageAddressAction.class, "showAddressBook");
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
