package web.action.core.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.dao.BaseDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.VariantConfigOption;
import com.hk.domain.catalog.product.VariantConfigOptionParam;
import com.hk.domain.catalog.product.VariantConfigValues;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.CartLineItemConfigValues;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.LinkManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.report.dto.order.LineItemConfigValuesDTO;
import com.hk.service.ProductVariantService;
import com.hk.service.UserService;
import com.hk.web.HealthkartResponse;

/**
 * Created by IntelliJ IDEA. User: AdminUser Date: Feb 14, 2012 Time: 11:35:48 AM To change this template use File |
 * Settings | File Templates.
 */

@Component
public class AddToCartWithLineItemConfigAction extends BaseAction {
    private static Logger                 logger       = Logger.getLogger(AddToCartWithLineItemConfigAction.class);

    private String                        variantId;

    private List<LineItemConfigValuesDTO> configValues = new ArrayList<LineItemConfigValuesDTO>();

    @Autowired
    private ProductVariantService         productVariantService;

    @Autowired
    private UserService                   userService;

    private UserManager                   userManager;

    private OrderManager                  orderManager;

    @Autowired
    private LinkManager                   linkManager;

    @Autowired
    private BaseDao                       baseDao;

    @SuppressWarnings("unchecked")
    @JsonHandler
    public Resolution buyNow() {
        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
        User user = null;

        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
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
                    configValue.setValue(selectedConfigValue.getValue());
                }
                if (VariantConfigOptionParam.shouldPriceBeDoubledForParam(configOption.getAdditionalParam())) {
                    configValue.setAdditionalPrice(selectedConfigValue.getAdditonalPrice() * 2);
                } else {
                    configValue.setAdditionalPrice(selectedConfigValue.getAdditonalPrice() * 2);
                }
                configValue.setVariantConfigOption(configOption);

                lineItemConfig.getCartLineItemConfigValues().add(configValue);
            }
            productVariant.setQty(new Long(1));
            isLineItemCreated = orderManager.createLineItems(productVariant, lineItemConfig, order);

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

}
