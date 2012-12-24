package com.hk.web.action.core.subscription;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.subscription.SubscriptionProductService;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/4/12
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class SubscriptionAction extends BaseAction implements ValidationErrorHandler {

  SubscriptionProduct subscriptionProduct;
  @Validate(required = true)
  ProductVariant productVariant;
  Product product;
  private boolean fromCart=false;

  @Autowired
  SubscriptionProductService subscriptionProductService;
  @Autowired
  ProductVariantService productVariantService;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    product=productVariant.getProduct();
    subscriptionProduct = subscriptionProductService.findByProduct(productVariant.getProduct());
    return new ForwardResolution("/pages/modal/subscription.jsp");
  }

  public SubscriptionProduct getSubscriptionProduct() {
    return subscriptionProduct;
  }

  public void setSubscriptionProduct(SubscriptionProduct subscriptionProduct) {
    this.subscriptionProduct = subscriptionProduct;
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

    public boolean isFromCart() {
        return fromCart;
    }

    public void setFromCart(boolean fromCart) {
        this.fromCart = fromCart;
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
    return new JsonResolution(validationErrors, getContext().getLocale());
  }

}

