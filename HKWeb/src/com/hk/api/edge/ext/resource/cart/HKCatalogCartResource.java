package com.hk.api.edge.ext.resource.cart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.api.edge.ext.pact.service.cart.HKCatalogCartService;
import com.hk.api.edge.ext.response.cart.CartSummaryApiResponse;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.util.json.JSONResponseBuilder;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.cart.AddToCartWithLineItemConfigAction;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Rimal
 */
@Component
@Path("/cart/")
public class HKCatalogCartResource {

    @Autowired
    private HKCatalogCartService hkCatalogCartService;

    @GET
    @Path("/summary")
    @Produces("application/json")
    public String getCartSummary() {
        CartSummaryApiResponse cartSummaryApiResponse = getHkCatalogCartService().getCartSummary();
        return new JSONResponseBuilder().addField("results", cartSummaryApiResponse).build();
    }
    
    public String addVariantToCart(){
        return null;
        /*if (isVariantConfigProvided()) {
            if (productVariantList != null && productVariantList.size() > 0) {
                setVariantId(productVariantList.get(0).getId());
            }
            return new ForwardResolution(AddToCartWithLineItemConfigAction.class).addParameter("variantId", variantId);
        } else {
            // I need to pass product info
            User user = null;
            ProductReferrer productReferrer = null;
            if (getPrincipal() != null) {
                user = userDao.getUserById(getPrincipal().getId());
                // user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
                if (user == null) {
                    user = userManager.createAndLoginAsGuestUser(null, null);
                }
            } else {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }

            Order order = orderManager.getOrCreateOrder(user);
            List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
            try {
                if (productVariantList != null && productVariantList.size() > 0) {
                    for (ProductVariant productVariant : productVariantList) {
                        if (productVariant != null && productVariant.isSelected() != null && productVariant.isSelected() && productVariant.getQty() != null
                                && productVariant.getQty() > 0) {
                            selectedProductVariants.add(productVariant);
                            userCartDao.addToCartHistory(productVariant.getProduct(), user);
                            userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);
                        }
                    }
                }

                ComboInstance comboInstance = null;
                if (combo != null) {
                    *//**
                     * do combo specific validations
                     *//*
                    if (combo != null && combo.getId() != null) {
                        Long maxQty = 0L;
                        Long netQty = 0L;
                        for (ComboProduct comboProduct : combo.getComboProducts()) {
                            maxQty += comboProduct.getQty();
                        }
                        for (ProductVariant productVariant : productVariantList) {
                            if (productVariant.getQty() != null) {
                                netQty += productVariant.getQty();
                            } else {
                                logger.error("Null qty for Combo=" + combo.getId());
                            }
                        }
                        if (netQty != maxQty) {
                            addValidationError("Combo product variant qty are not in accordance to offer", new SimpleError(
                                    "Combo product variant qty are not in accordance to offer"));
                            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                                    "Combo product variant qty are not in accordance to offer", new HashMap());
                            noCache();
                            return new JsonResolution(healthkartResponse);
                        }
                    } else {
                        if (productVariantList != null && productVariantList.size() > 0) {
                            for (ProductVariant productVariant : productVariantList) {
                                // can pv be null here? have to check, putting a null check --> still null pointer on
                                // above line --> null check for PVlist
                                if (productVariant != null && productVariant.getQty() != null && productVariant.getQty() < 1) {
                                    addValidationError("Min Qty should be 1", new SimpleError("Min Qty should be 1"));
                                    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Min Qty should be 1", new HashMap());
                                    noCache();
                                    return new JsonResolution(healthkartResponse);
                                }
                            }
                        }
                    }

                    if (order != null && order.getCartLineItems() != null && order.getCartLineItems().size() > 0) {
                        Set<CartLineItem> existingProductCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
                        for (ProductVariant selectedProductVariant : selectedProductVariants) {

                            for (CartLineItem existingProductCartLineItem : existingProductCartLineItems) {
                                if (selectedProductVariant.equals(existingProductCartLineItem.getProductVariant())) {
                                    ComboInstance existingComboInstance = existingProductCartLineItem.getComboInstance();
                                    if (existingComboInstance != null) {

                                        if (existingComboInstance.getCombo().getId().equals(combo.getId())) {
                                            // same combo addition, check variants
                                            Set<ProductVariant> existingPVInCombo = new HashSet<ProductVariant>(existingComboInstance.getVariants());
                                            Set<ProductVariant> selectedPV = new HashSet<ProductVariant>(selectedProductVariants);

                                            Collection<ProductVariant> diffInPV = CollectionUtils.subtract(existingPVInCombo, selectedPV);
                                            if (diffInPV != null && diffInPV.size() > 0) {
                                                addValidationError("You can't add any combo whose product is already in the cart", new SimpleError(
                                                        "You can't add any combo whose product is already in the cart"));
                                                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                                                        "You can't add any combo whose product is already in the cart", new HashMap());
                                                noCache();
                                                return new JsonResolution(healthkartResponse);
                                            }
                                        } else if (!existingComboInstance.getCombo().getId().equals(combo.getId())) {
                                            addValidationError("You can't add any combo whose product is already in the cart", new SimpleError(
                                                    "You can't add any combo whose product is already in the cart"));
                                            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                                                    "You can't add any combo whose product is already in the cart", new HashMap());
                                            noCache();
                                            return new JsonResolution(healthkartResponse);
                                        }
                                    } else {
                                        addValidationError("You can't add any combo whose product is already in the cart", new SimpleError(
                                                "You can't add any combo whose product is already in the cart"));
                                        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                                                "You can't add any combo whose product is already in the cart", new HashMap());
                                        noCache();
                                        return new JsonResolution(healthkartResponse);
                                    }
                                }
                            }
                        }
                    }

                    comboInstance = new ComboInstance();
                    comboInstance.setCombo(combo);
                    comboInstance = (ComboInstance) comboInstanceDao.save(comboInstance);
                } else {
                    if (order != null && order.getCartLineItems() != null && order.getCartLineItems().size() > 0) {
                        Set<CartLineItem> existingProductCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
                        for (ProductVariant selectedProductVariant : selectedProductVariants) {
                            for (CartLineItem existingProductCartLineItem : existingProductCartLineItems) {
                                if (existingProductCartLineItem.getProductVariant().equals(selectedProductVariant) && existingProductCartLineItem.getComboInstance() != null) {
                                    addValidationError("You can't add any product whose combo is already added in the cart", new SimpleError(
                                            "You can't add any product whose combo is already added in the cart"));
                                    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
                                            "You can't add any product whose combo is already added in the cart", new HashMap());
                                    noCache();
                                    return new JsonResolution(healthkartResponse);
                                }
                            }
                        }
                    }
                }

                if (comboInstance != null) {
                    if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
                        for (ProductVariant variant : selectedProductVariants) {
                            ComboInstanceHasProductVariant comboInstanceProductVariant = new ComboInstanceHasProductVariant();
                            if (variant != null && variant.getQty() != null && variant.getQty() != 0) {
                                comboInstanceProductVariant.setComboInstance(comboInstance);
                                comboInstanceProductVariant.setProductVariant(variant);
                                comboInstanceProductVariant.setQty(variant.getQty());
                                comboInstanceProductVariant = (ComboInstanceHasProductVariant) comboInstanceHasProductVariantDao.save(comboInstanceProductVariant);
                            }
                        }
                    }
                }

                if (productReferrerId != null) {
                    productReferrer = userCartDao.get(ProductReferrer.class, productReferrerId);
                }
                orderManager.createLineItems(selectedProductVariants, order, combo, comboInstance, productReferrer);
            } catch (OutOfStockException e) {
                getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
                return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
            }

            Map dataMap = new HashMap();
            // null pointer here --> putting a null check
            if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
                if (combo != null) {
                    dataMap.put("name", combo.getName());
                } else {
                    dataMap.put("name", selectedProductVariants.get(0).getProduct().getName());
                }
                if (combo != null) {
                    dataMap.put("addedProducts", combo.getName());
                } else {
                    String addedProducts = "";
                    for (ProductVariant selectedProductVariant : selectedProductVariants) {
                        addedProducts = addedProducts.concat(selectedProductVariant.getProduct().getName()).concat("  |");
                    }
                    dataMap.put("addedProducts", addedProducts);
                }
                dataMap.put("options", selectedProductVariants.get(0).getOptionsCommaSeparated());
                dataMap.put("qty", selectedProductVariants.get(0).getQty());
                Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
                dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size())
                        + subscriptionCartLineItems.size() + 1L);
                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been added to cart", dataMap);
                noCache();
                // recomendationEngine.notifyAddToCart(user.getId(), productVariantList);
                return new JsonResolution(healthkartResponse);
            }
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Product has not been added to cart", dataMap);
            noCache();
            return new JsonResolution(healthkartResponse);
        }*/
    }


    public HKCatalogCartService getHkCatalogCartService() {
        return hkCatalogCartService;
    }
}