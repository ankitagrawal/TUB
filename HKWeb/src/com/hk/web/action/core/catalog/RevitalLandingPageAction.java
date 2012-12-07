package com.hk.web.action.core.catalog;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.action.core.cart.CartAction;

@UrlBinding("/revital")
public class RevitalLandingPageAction extends BaseAction {

    @Autowired
    private OrderManager          orderManager;
    @Autowired
    private UserManager           userManager;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private BaseDao               baseDao;

    Long                          productReferrerId;

    @DefaultHandler
    public Resolution revital() {
        return new ForwardResolution("/pages/lp/revital/index.jsp");
    }

    public Resolution addRevitalProductsAndGoToCart() {
        ProductReferrer productReferrer = null;
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            // user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        List<ProductVariant> productVariantList = new ArrayList<ProductVariant>();
        productVariantList.add(getProductVariantService().getVariantById("NUT410-01"));// Revital Daily Health
                                                                                        // Supplement
        productVariantList.add(getProductVariantService().getVariantById("NUT411-01"));// Revital For Women
        productVariantList.add(getProductVariantService().getVariantById("NUT412-01"));// Revital Form Seniors
        productVariantList.add(getProductVariantService().getVariantById("NUT598-01"));// Ranbaxy Revitalite Powder

        Order order = orderManager.getOrCreateOrder(user);
        try {
            if (productReferrerId != null) {
                productReferrer = getBaseDao().get(ProductReferrer.class, productReferrerId);
            }
            if (productVariantList != null && productVariantList.size() > 0) {
                orderManager.createLineItems(productVariantList, order, null, null, productReferrer);
            }
        } catch (Exception e) {
            //
        }
        return new RedirectResolution(CartAction.class);
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public Long getProductReferrerId() {
        return productReferrerId;
    }

    public void setProductReferrerId(Long productReferrerId) {
        this.productReferrerId = productReferrerId;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}