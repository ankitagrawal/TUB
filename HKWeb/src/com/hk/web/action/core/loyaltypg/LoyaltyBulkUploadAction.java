/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * @author Ankit Chhabra
 *
 */

@Secure(hasAnyPermissions = { PermissionConstants.UPLOAD_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class LoyaltyBulkUploadAction extends AbstractLoyaltyAction {

    
    @Autowired
    private LoyaltyProgramService loyaltyProgramService;
    
    private List<String> errorMessages;
    private String successMessage;
    private FileBean csvFileBean;
    private FileBean badgeCsvFileBean;
    
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/loyalty/bulkLoyaltyAdd.jsp");
    }

    public Resolution uploadPorducts() {
        this.errorMessages = new ArrayList<String>();
    	
    	User loggedOnUser = null;
        if (this.getPrincipal() != null) {
            loggedOnUser = this.getUserService().getUserById(this.getPrincipal().getId());
        }
        
        this.loyaltyProgramService.uploadLoyaltyProductsCSV(this.csvFileBean, this.errorMessages);

        if (this.errorMessages.size()> 0) {
        	return new ForwardResolution("/pages/loyalty/bulkLoyaltyAdd.jsp");
        }
        this.successMessage = "Database succesfully Updated";
        return new ForwardResolution("/pages/loyalty/bulkLoyaltyAdd.jsp");
    }

    public Resolution uploadBadges() throws Exception {
    	this.errorMessages = new ArrayList<String>();
    	User loggedOnUser = null;
        if (this.getPrincipal() != null) {
            loggedOnUser = this.getUserService().getUserById(this.getPrincipal().getId());
        }
        this.loyaltyProgramService.uploadBadgeInfoCSV(this.badgeCsvFileBean, this.errorMessages);

        if (this.errorMessages.size()> 0) {
        	return new ForwardResolution("/pages/loyalty/bulkLoyaltyAdd.jsp");
        }
        this.successMessage = "Database succesfully Updated.";
        return new ForwardResolution("/pages/loyalty/bulkLoyaltyAdd.jsp");
    }

	/**
	 * @return the errorMessages
	 */
	public List<String> getErrorMessages() {
		return this.errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * @return the successMessage
	 */
	public String getSuccessMessage() {
		return this.successMessage;
	}

	/**
	 * @param successMessage the successMessage to set
	 */
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	/**
	 * @return the csvFileBean
	 */
	public FileBean getCsvFileBean() {
		return this.csvFileBean;
	}

	/**
	 * @param csvFileBean the csvFileBean to set
	 */
	public void setCsvFileBean(FileBean csvFileBean) {
		this.csvFileBean = csvFileBean;
	}

	/**
	 * @return the badgeCsvFileBean
	 */
	public FileBean getBadgeCsvFileBean() {
		return this.badgeCsvFileBean;
	}

	/**
	 * @param badgeCsvFileBean the badgeCsvFileBean to set
	 */
	public void setBadgeCsvFileBean(FileBean badgeCsvFileBean) {
		this.badgeCsvFileBean = badgeCsvFileBean;
	}


}
