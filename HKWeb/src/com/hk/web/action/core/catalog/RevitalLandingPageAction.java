package com.hk.web.action.core.catalog;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.core.cart.CartAction;

@UrlBinding ("/revital")
public class RevitalLandingPageAction extends BaseAction {

  @Inject
  ProductVariantDao productVariantDao;
  @Inject
  OrderManager orderManager;
  @Inject
  UserManager userManager;
  @Inject
  UserDao userDao;

  @DefaultHandler
  public Resolution revital() {
    return new ForwardResolution("/pages/lp/revital/index.jsp");
  }

  public Resolution addRevitalProductsAndGoToCart() {
    User user = null;
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
      if (user == null) {
        user = userManager.createAndLoginAsGuestUser(null, null);
      }
    } else {
      user = userManager.createAndLoginAsGuestUser(null, null);
    }
    List<ProductVariant> productVariantList = new ArrayList<ProductVariant>();
    productVariantList.add(productVariantDao.find("NUT410-01"));//Revital Daily Health Supplement
    productVariantList.add(productVariantDao.find("NUT411-01"));//Revital For Women
    productVariantList.add(productVariantDao.find("NUT412-01"));//Revital Form Seniors
    productVariantList.add(productVariantDao.find("NUT598-01"));//Ranbaxy Revitalite Powder

    Order order = orderManager.getOrCreateOrder(user);
    try {
      if (productVariantList != null && productVariantList.size() > 0) {
        orderManager.createLineItems(productVariantList, order, null, null);
      }
    } catch (Exception e) {
      //
    }
    return new RedirectResolution(CartAction.class);
  }
}