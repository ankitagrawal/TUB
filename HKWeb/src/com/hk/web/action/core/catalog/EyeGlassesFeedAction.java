package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 9/3/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EyeGlassesFeedAction extends BaseAction {
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductVariantService productVariantService;

	private List<Product> products;
	private List<ProductVariant> productVariants;

	private String category;

	public Resolution pre() {
//		products = getProductService().getProductByCategory(category);
		productVariants = getProductVariantService().getAllProductVariantsByCategory(category);
		return new ForwardResolution("/pages/eyeglassesXmlFeed.jsp");
	}

	public List<Product> getProducts() {
		return products;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public void setProductVariantService(ProductVariantService productVariantService) {
		this.productVariantService = productVariantService;
	}

	public List<ProductVariant> getProductVariants() {
		return productVariants;
	}

	public void setProductVariants(List<ProductVariant> productVariants) {
		this.productVariants = productVariants;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
