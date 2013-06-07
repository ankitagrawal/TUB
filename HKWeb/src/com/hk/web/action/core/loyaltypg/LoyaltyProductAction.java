/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.loyaltypg.service.LoyaltyProgramService;

/**
 * @author Ankit Chhabra
 *
 */
@Component
@Secure(hasAnyRoles = {RoleConstants.HK_LOYALTY_USER}, authActionBean=JoinLoyaltyProgramAction.class)
public class LoyaltyProductAction extends AbstractLoyaltyAction {

	@Autowired
	private LoyaltyProgramService loyaltyProgramService;

	private ProductVariant prodVariant;
	private LoyaltyProduct loyaltyProduct;
	
	@DefaultHandler
	public Resolution pre() {
	
		prodVariant = loyaltyProduct.getVariant();
		return new ForwardResolution("/pages/loyalty/productDescription.jsp");
	}

	/**
	 * @return the prodVariant
	 */
	public ProductVariant getProdVariant() {
		return prodVariant;
	}

	/**
	 * @param prodVariant the prodVariant to set
	 */
	public void setProdVariant(ProductVariant prodVariant) {
		this.prodVariant = prodVariant;
	}

	/**
	 * @return the loyaltyProduct
	 */
	public LoyaltyProduct getLoyaltyProduct() {
		return loyaltyProduct;
	}

	/**
	 * @param loyaltyProduct the loyaltyProduct to set
	 */
	public void setLoyaltyProduct(LoyaltyProduct loyaltyProduct) {
		this.loyaltyProduct = loyaltyProduct;
	}

}
