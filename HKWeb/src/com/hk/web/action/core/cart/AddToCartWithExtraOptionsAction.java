package com.hk.web.action.core.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.LinkManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.UserService;
import com.hk.report.dto.order.ProductLineItemWithExtraOptionsDto;
import com.hk.web.HealthkartResponse;

@Component
public class AddToCartWithExtraOptionsAction extends BaseAction implements ValidationErrorHandler {

    @SuppressWarnings("unused")
    private static Logger                    logger = Logger.getLogger(AddToCartWithExtraOptionsAction.class);

    List<ProductLineItemWithExtraOptionsDto> productLineItemWithExtraOptionsDtos;
    private Long                             productReferrerId;

    @Autowired
    private UserService                      userService;
    @Autowired
    private UserManager                      userManager;
    @Autowired
    private OrderManager                     orderManager;
    @Autowired
    private LinkManager                      linkManager;
    @Autowired
    UserProductHistoryDao                    userProductHistoryDao;
    @Autowired
    BaseDao                                  baseDao;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    @JsonHandler
    public Resolution addToCart() {
        // I need to pass product info
        User user = null;
        ProductReferrer productReferrer = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            // user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
            if (user == null) {
                user = getUserManager().createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = getUserManager().createAndLoginAsGuestUser(null, null);
        }
        Order order = getOrderManager().getOrCreateOrder(user);
        try {
            for (ProductLineItemWithExtraOptionsDto dto : productLineItemWithExtraOptionsDtos) {
                boolean selected = dto.isSelected();
                ProductVariant productVariant = dto.getProductVariant();
                List<CartLineItemExtraOption> extraOptions = dto.getExtraOptions();
                if (selected) {
                    if (productReferrerId != null) {
                        productReferrer = getBaseDao().get(ProductReferrer.class, productReferrerId);
                    }
                    getOrderManager().createLineItems(productVariant, extraOptions, order, productReferrer);
                    userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);
                }
            }
        } catch (OutOfStockException e) {
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }

        Map dataMap = new HashMap();
        String cartUrl = getLinkManager().getCartUrl();
        dataMap.put("url", cartUrl);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, "Product has been added to cart", dataMap);

        noCache();
        return new JsonResolution(healthkartResponse);
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

    public List<ProductLineItemWithExtraOptionsDto> getProductLineItemWithExtraOptionsDtos() {
        return productLineItemWithExtraOptionsDtos;
    }

    public void setProductLineItemWithExtraOptionsDtos(List<ProductLineItemWithExtraOptionsDto> productLineItemWithExtraOptionsDtos) {
        this.productLineItemWithExtraOptionsDtos = productLineItemWithExtraOptionsDtos;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
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