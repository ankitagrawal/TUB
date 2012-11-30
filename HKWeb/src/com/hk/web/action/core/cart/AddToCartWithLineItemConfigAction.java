package com.hk.web.action.core.cart;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.VariantConfigOption;
import com.hk.domain.catalog.product.VariantConfigOptionParam;
import com.hk.domain.catalog.product.VariantConfigValues;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.CartLineItemConfigValues;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.LinkManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.report.dto.order.LineItemConfigValuesDTO;
import com.hk.web.HealthkartResponse;

/**
 * Created by IntelliJ IDEA. User: AdminUser Date: Feb 14, 2012 Time: 11:35:48 AM To change this template use File |
 * Settings | File Templates.
 */

@Component
public class AddToCartWithLineItemConfigAction extends BaseAction {
    @SuppressWarnings("unused")
    private static Logger                 logger       = Logger.getLogger(AddToCartWithLineItemConfigAction.class);

    private String                        variantId;

    private String                        nameToBeEngraved;

    private List<LineItemConfigValuesDTO> configValues = new ArrayList<LineItemConfigValuesDTO>();

    private String                        jsonConfigValues;

    private Long                          productReferrerId;

    @Autowired
    private ProductVariantService         productVariantService;

    @Autowired
    private UserService                   userService;
    @Autowired
    private UserManager                   userManager;
    @Autowired
    private OrderManager                  orderManager;

    @Autowired
    private LinkManager                   linkManager;

    @Autowired
    private BaseDao                       baseDao;

    @Autowired
    UserProductHistoryDao                 userProductHistoryDao;

    @SuppressWarnings("unchecked")
    @JsonHandler
    @DefaultHandler
    public Resolution buyNow() {
        getConfigValuesFromJson(jsonConfigValues);

        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
        User user = null;
        ProductReferrer productReferrer = null;

        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            // user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        Order order = orderManager.getOrCreateOrder(user);

        CartLineItemConfig lineItemConfig = new CartLineItemConfig();
        boolean isLineItemCreated = false;
        try {
            for (LineItemConfigValuesDTO dto : configValues) {
                CartLineItemConfigValues configValue = new CartLineItemConfigValues();
                configValue.setLineItemConfig(lineItemConfig);
                VariantConfigOption configOption = getBaseDao().get(VariantConfigOption.class, dto.getOptionId());
                VariantConfigValues selectedConfigValue = getBaseDao().get(VariantConfigValues.class, dto.getValueId());

                if (configOption != null && selectedConfigValue != null) {
                    if (configOption.getAdditionalParam().equalsIgnoreCase(VariantConfigOptionParam.ENGRAVING.param())) {
                        configValue.setValue(getNameToBeEngraved());
                    } else {
                        configValue.setValue(selectedConfigValue.getValue());
                    }
                }
                if (VariantConfigOptionParam.shouldPriceBeDoubledForParam(configOption.getAdditionalParam())) {
                    configValue.setAdditionalPrice(selectedConfigValue.getAdditonalPrice() * 2);
                } else {
                    configValue.setAdditionalPrice(selectedConfigValue.getAdditonalPrice());
                }

                if (VariantConfigOptionParam.shouldPriceBeDoubledForParam(configOption.getAdditionalParam())) {
                    configValue.setCostPrice(selectedConfigValue.getCostPrice() * 2);
                } else {
                    configValue.setCostPrice(selectedConfigValue.getCostPrice());
                }
                configValue.setVariantConfigOption(configOption);

                lineItemConfig.getCartLineItemConfigValues().add(configValue);
            }
            productVariant.setQty(new Long(1));
            if (productReferrerId != null) {
                productReferrer = getBaseDao().get(ProductReferrer.class, productReferrerId);
            }
            isLineItemCreated = orderManager.createLineItems(productVariant, lineItemConfig, order, productReferrer);
            userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);

        } catch (OutOfStockException e) {
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }

        Map dataMap = new HashMap();
        String cartUrl = linkManager.getCartUrl();
        dataMap.put("url", cartUrl);
        dataMap.put("name", productVariant.getProduct().getName());
        dataMap.put("qty", productVariant.getQty());
        Long itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
        dataMap.put("itemsInCart", isLineItemCreated ? itemsInCart + 1L : itemsInCart);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been added to cart", dataMap);

        noCache();
        return new JsonResolution(healthkartResponse);
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public List<LineItemConfigValuesDTO> getConfigValues() {
        return configValues;
    }

    public void setConfigValues(List<LineItemConfigValuesDTO> configValues) {
        this.configValues = configValues;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public String getJsonConfigValues() {
        return jsonConfigValues;
    }

    public void setJsonConfigValues(String jsonConfigValues) {
        this.jsonConfigValues = jsonConfigValues;
    }

    public String getNameToBeEngraved() {
        return nameToBeEngraved;
    }

    public void setNameToBeEngraved(String nameToBeEngraved) {
        this.nameToBeEngraved = nameToBeEngraved;
    }

    private void getConfigValuesFromJson(String jsonConfigValue) {
        if (jsonConfigValue != null && !jsonConfigValue.equalsIgnoreCase("")) {
            Type listType = new TypeToken<List<LineItemConfigValuesDTO>>() {
            }.getType();
            Object obj = new Gson().fromJson(jsonConfigValue, listType);
            List<LineItemConfigValuesDTO> lineItemConfigValuesDTOs = (List<LineItemConfigValuesDTO>) obj;
            setConfigValues(lineItemConfigValuesDTOs);
        }
    }

    public Long getProductReferrerId() {
        return productReferrerId;
    }

    public void setProductReferrerId(Long productReferrerId) {
        this.productReferrerId = productReferrerId;
    }
}
