/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.user.User;
import com.hk.loyaltypg.dao.LoyaltyProductDao;
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
	@Autowired
	private LoyaltyProductDao loyaltyProductDao;
	
	private String prodVariantId;
	private ProductVariant prodVariant;
	private LoyaltyProduct loyaltyProduct;
	private Product product;

	@DefaultHandler
	public Resolution pre() {
		if (prodVariantId != null && !prodVariantId.isEmpty()) {
			loyaltyProduct = loyaltyProductDao.getProductbyVariantId(prodVariantId);
			prodVariant = loyaltyProduct.getVariant();
			product = prodVariant.getProduct();
			// To handle proxy instances from hibernate
			if (product instanceof HibernateProxy) {
				HibernateProxy proxy = (HibernateProxy)product;                                                          
				product = (Product)proxy.getHibernateLazyInitializer().getImplementation();
			}
		} 
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

	/**
	 * @return the prodVariantId
	 */
	public String getProdVariantId() {
		return prodVariantId;
	}

	/**
	 * @param prodVariantId the prodVariantId to set
	 */
	public void setProdVariantId(String prodVariantId) {
		this.prodVariantId = prodVariantId;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

}
