package com.hk.web.action.core.cart;

import java.util.ArrayList;
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
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.UserService;
import com.hk.web.HealthkartResponse;

@Component
public class AddToCartAction extends BaseAction implements ValidationErrorHandler {

    @SuppressWarnings("unused")
    private static Logger         logger = Logger.getLogger(AddToCartAction.class);

    List<ProductVariant>          productVariantList;
    Combo                         combo;

    @Autowired
    private UserService           userService;
    @Autowired
    private UserManager           userManager;
    @Autowired
    private OrderManager          orderManager;
    @Autowired
    private BaseDao               baseDao;
    @Autowired
    private UserCartDao           userCartDao;
    @Autowired
    private UserProductHistoryDao userProductHistoryDao;

    @SuppressWarnings( { "unchecked", "deprecation" })
    @DefaultHandler
    @JsonHandler
    public Resolution addToCart() {
        // I need to pass product info
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            if (user == null) {
                user = getUserManager().createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = getUserManager().createAndLoginAsGuestUser(null, null);
        }

        Order order = getOrderManager().getOrCreateOrder(user);
        List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
        try {
            if (productVariantList != null && productVariantList.size() > 0) {
                for (ProductVariant productVariant : productVariantList) {
                    if (productVariant != null && productVariant.isSelected() != null && productVariant.isSelected()) {
                        selectedProductVariants.add(productVariant);
                        getUserCartDao().addToCartHistory(productVariant.getProduct(), user);
                        getUserProductHistoryDao().updateIsAddedToCart(productVariant.getProduct(), user);
                    }
                }
            }
            ComboInstance comboInstance = null;
            if (combo != null) {
                if (combo != null && combo.getId() != null) {
                    Long maxQty = 0L;
                    Long netQty = 0L;
                    for (ComboProduct comboProduct : combo.getComboProducts()) {
                        maxQty += comboProduct.getQty();
                    }
                    for (ProductVariant productVariant : productVariantList) {
                        netQty += productVariant.getQty();
                    }
                    if (netQty != maxQty) {
                        addValidationError("Combo product variant qty are not in accordance to offer", new SimpleError("Combo product variant qty are not in accordance to offer"));
                        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Combo product variant qty are not in accordance to offer",
                                new HashMap());
                        noCache();
                        return new JsonResolution(healthkartResponse);
                    }
                } else {
                    if (productVariantList != null && productVariantList.size() > 0) {
                        for (ProductVariant productVariant : productVariantList) {
                            // can pv be null here? have to check, putting a null check --> still null pointer on above
                            // line --> null check for PVlist
                            if (productVariant != null && productVariant.getQty() != null && productVariant.getQty() < 1) {
                                addValidationError("Min Qty should be 1", new SimpleError("Min Qty should be 1"));
                                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Min Qty should be 1", new HashMap());
                                noCache();
                                return new JsonResolution(healthkartResponse);
                            }
                        }
                    }
                }

                comboInstance = new ComboInstance();
                comboInstance.setCombo(combo);
                comboInstance = (ComboInstance) getBaseDao().save(comboInstance);
            }

            if (comboInstance != null) {
                if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
                    for (ProductVariant variant : selectedProductVariants) {
                        ComboInstanceHasProductVariant comboInstanceProductVariant = new ComboInstanceHasProductVariant();
                        if (variant.getQty() != 0) {
                            comboInstanceProductVariant.setComboInstance(comboInstance);
                            comboInstanceProductVariant.setProductVariant(variant);
                            comboInstanceProductVariant.setQty(variant.getQty());
                            comboInstanceProductVariant = (ComboInstanceHasProductVariant) getBaseDao().save(comboInstanceProductVariant);
                        }
                    }
                }
            }
            getOrderManager().createLineItems(selectedProductVariants, order, combo, comboInstance);
        } catch (OutOfStockException e) {
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }

        Map dataMap = new HashMap();
        // null pointer here --> putting a null check
        if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
            if (combo != null) {
                dataMap.put("name", combo.getName());
                dataMap.put("addedProducts", combo.getName());
            } else {
                dataMap.put("name", selectedProductVariants.get(0).getProduct().getName());
                String addedProducts = "";
                for (ProductVariant selectedProductVariant : selectedProductVariants) {
                    addedProducts = addedProducts.concat(selectedProductVariant.getProduct().getName()).concat("  |");
                }
                dataMap.put("addedProducts", addedProducts);
            }
            
            dataMap.put("options", selectedProductVariants.get(0).getOptionsCommaSeparated());
            dataMap.put("qty", selectedProductVariants.get(0).getQty());
            dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + 1L);
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been added to cart", dataMap);
            noCache();
            return new JsonResolution(healthkartResponse);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Product has not been added to cart", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }

    public List<ProductVariant> getProductVariantList() {
        return productVariantList;
    }

    public void setProductVariantList(List<ProductVariant> productVariantList) {
        this.productVariantList = productVariantList;
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
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

    public UserCartDao getUserCartDao() {
        return userCartDao;
    }

    public void setUserCartDao(UserCartDao userCartDao) {
        this.userCartDao = userCartDao;
    }

    public UserProductHistoryDao getUserProductHistoryDao() {
        return userProductHistoryDao;
    }

    public void setUserProductHistoryDao(UserProductHistoryDao userProductHistoryDao) {
        this.userProductHistoryDao = userProductHistoryDao;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}