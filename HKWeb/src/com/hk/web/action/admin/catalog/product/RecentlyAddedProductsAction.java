package com.hk.web.action.admin.catalog.product;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_CATALOG}, authActionBean = AdminPermissionAction.class)
@Component
public class RecentlyAddedProductsAction extends BaseAction {
    @Autowired
   ProductDao productDao;

  List<Product> productList;


  @DefaultHandler
  public Resolution pre() {
    productList = productDao.getRecentlyAddedProducts();
    return new ForwardResolution("/pages/recentlyAddedProducts.jsp");
  }

  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }
}