/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.PermissionConstants;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.user.User;
import com.hk.loyaltypg.dao.LoyaltyProductDao;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * @author Ankit Chhabra
 *
 */

@Secure(hasAnyPermissions = { PermissionConstants.UPLOAD_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class LoyaltyAdminAction extends AbstractLoyaltyAction {

    
    @Autowired
    private LoyaltyProgramService loyaltyProgramService;
    @Autowired
    private LoyaltyProductDao loyaltyProductDao;
    
    private List<String> errorMessages;
    private String successMessage;
    private FileBean csvFileBean;
    private FileBean badgeCsvFileBean;
    private String variantId;
    private String productId;
    private double points;
    private List<LoyaltyProduct> loyaltyProducts;
    
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/loyalty/loyaltyAdmin.jsp");
    }

    public Resolution uploadPorducts() {
        this.errorMessages = new ArrayList<String>();
    	
    	User loggedOnUser = null;
        if (this.getPrincipal() != null) {
            loggedOnUser = this.getUserService().getUserById(this.getPrincipal().getId());
        }
        
        this.loyaltyProgramService.uploadLoyaltyProductsCSV(this.csvFileBean, this.errorMessages);

        if (this.errorMessages.size()> 0) {
        	return new ForwardResolution("/pages/loyalty/loyaltyAdmin.jsp");
        }
        this.successMessage = "Database succesfully Updated";
        return new ForwardResolution("/pages/loyalty/loyaltyAdmin.jsp");
    }

    public Resolution uploadBadges() throws Exception {
    	this.errorMessages = new ArrayList<String>();
    	User loggedOnUser = null;
        if (this.getPrincipal() != null) {
            loggedOnUser = this.getUserService().getUserById(this.getPrincipal().getId());
        }
        this.loyaltyProgramService.uploadBadgeInfoCSV(this.badgeCsvFileBean, this.errorMessages);

        if (this.errorMessages.size()> 0) {
        	return new ForwardResolution("/pages/loyalty/loyaltyAdmin.jsp");
        }
        this.successMessage = "Database succesfully Updated.";
        return new ForwardResolution("/pages/loyalty/loyaltyAdmin.jsp");
    }
    
    public Resolution searchLoyaltyProducts() {
    	this.errorMessages = new ArrayList<String>();
    	Map<String, String> searchKeywordsMap = new HashMap<String, String>();
    	
    	// Add search parameters
    	if(variantId != null && !variantId.isEmpty()) {
    		searchKeywordsMap.put("variantId", variantId);
    	}
    	if(productId != null && !productId.isEmpty()) {
    		searchKeywordsMap.put("productId", productId);
    	}   	
    	
    	// If no parameters added return without any db call
    	if (searchKeywordsMap.size() > 0) {
    		loyaltyProducts = loyaltyProgramService.searchLoyaltyProducts(searchKeywordsMap);
        } else {
        	this.errorMessages.add("Invalid or no search parameters given.");
        }
    	
    	if (loyaltyProducts != null && !(loyaltyProducts.size() > 0)) {
    		this.errorMessages.add("No Products Found for the given productId and variant Id");
    	}
 
        return new ForwardResolution("/pages/loyalty/loyaltyAdmin.jsp");
    }

    public Resolution saveLoyaltyProduct() {
		HealthkartResponse healthkartResponse = null;

    	if (variantId !=null && !variantId.isEmpty()) {
    		LoyaltyProduct loyaltyProduct = this.loyaltyProgramService.getProductByVariantId(variantId);
    		if (loyaltyProduct != null) {
    			loyaltyProduct.setPoints(points);
    			loyaltyProductDao.save(loyaltyProduct);
    			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been updated successfully.", null);
    			this.noCache();
    			return new JsonResolution(healthkartResponse);
    		}
    	}
    	healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Could not update the product.", null);
		this.noCache();
		return new JsonResolution(healthkartResponse);

    }
    
    public Resolution removeLoyaltyProduct() {
		HealthkartResponse healthkartResponse = null;
    	if (variantId !=null && !variantId.isEmpty()) {
       		LoyaltyProduct loyaltyProduct = this.loyaltyProgramService.getProductByVariantId(variantId);
    		if (loyaltyProduct != null) {
    			loyaltyProductDao.delete(loyaltyProduct);
    			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been removed successfully.", null);
    			this.noCache();
    			return new JsonResolution(healthkartResponse);
    		}
    	}
    	healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Error in removing the product.", null);
		this.noCache();
		return new JsonResolution(healthkartResponse);
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

	/**
	 * @return the variantId
	 */
	public String getVariantId() {
		return variantId;
	}

	/**
	 * @param variantId the variantId to set
	 */
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}


	/**
	 * @return the loyaltyProducts
	 */
	public List<LoyaltyProduct> getLoyaltyProducts() {
		return loyaltyProducts;
	}

	/**
	 * @param loyaltyProducts the loyaltyProducts to set
	 */
	public void setLoyaltyProducts(List<LoyaltyProduct> loyaltyProducts) {
		this.loyaltyProducts = loyaltyProducts;
	}

	/**
	 * @return the points
	 */
	public double getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(double points) {
		this.points = points;
	}


}
