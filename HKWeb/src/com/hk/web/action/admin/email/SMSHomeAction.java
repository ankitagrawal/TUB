package com.hk.web.action.admin.email;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.manager.SMSManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.Address;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SEND_SMS }, authActionBean = AdminPermissionAction.class)
@Component
public class SMSHomeAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(SMSHomeAction.class);

    private boolean       bulkSMS;
    private String        name;
    private String        mobile;
    private String        mobiles;

    @Validate(required = true)
    private String        message;

    private Category      topLevelCategory;
    @Autowired
    SMSManager            smsManager;
    @Autowired
    AddressDao            addressDao;
    @Autowired
    RoleDao               roleDao;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/smsHome.jsp");
    }

    @ValidationMethod
    public void validateSMSRequiredFields() {
        if (!bulkSMS) {
            if (StringUtils.isBlank(mobile) && StringUtils.isBlank(mobiles)) {
                getContext().getValidationErrors().add("mobileNumber", new SimpleError("Mobile number(s) is a required field for non-bulk sms or send bulk sms"));
            }
        }
    }

    public Resolution sendSMS() {
        Set<String> mobileNumbers = new HashSet<String>();
        if (!bulkSMS) {
            if (StringUtils.isNotBlank(mobiles)) {
                String[] mobileArray = mobiles.split(",");
                for (String mobile : mobileArray) {
                    smsManager.sendSMS(message, StringUtils.trim(mobile));
                }
            } else if (StringUtils.isNotBlank(mobile)) {
                smsManager.sendSMS(message, mobile);
            }
        } else if (topLevelCategory != null) {
            List<Category> categories = new ArrayList<Category>();
            categories.add(topLevelCategory);
            List<Address> addresses = addressDao.getAllAddressesByCategories(categories);
            logger.info("addresses: " + addresses.size());
            for (Address address : addresses) {
                if (!address.getUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.UNSUBSCRIBED_USER))) {
                    if (!mobileNumbers.contains(address.getPhone())) {
                        smsManager.sendSMS(message, address.getPhone());
                        mobileNumbers.add(address.getPhone());
                    }
                }
            }
        } else {
            List<Address> addresses = addressDao.getAllAddresses();
            logger.info("addresses: " + addresses.size());
            for (Address address : addresses) {
                if (!address.getUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.UNSUBSCRIBED_USER))) {
                    if (!mobileNumbers.contains(address.getPhone())) {
                        smsManager.sendSMS(message, address.getPhone());
                        mobileNumbers.add(address.getPhone());
                    }
                }
            }
        }

        return new RedirectResolution(SMSHomeAction.class);
    }

    public boolean isBulkSMS() {
        return bulkSMS;
    }

    public void setBulkSMS(boolean bulkSMS) {
        this.bulkSMS = bulkSMS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Category getTopLevelCategory() {
        return topLevelCategory;
    }

    public void setTopLevelCategory(Category topLevelCategory) {
        this.topLevelCategory = topLevelCategory;
    }
}