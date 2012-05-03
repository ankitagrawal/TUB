package com.hk.service.impl.order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.dao.order.cartLineItem.CartLineItemDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfigValues;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;
import com.hk.service.order.CartLineItemService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class CartLineItemServiceImpl implements CartLineItemService {

    @Autowired
    private CartLineItemDao cartLineItemDao;

    @Transactional
    public CartLineItem save(CartLineItem cartLineItem) {
        return getCartLineItemDao().save(cartLineItem);
    }

    @Override
    public CartLineItemType getCartLineItemType(EnumCartLineItemType enumCartLineItemType) {
        return getCartLineItemDao().get(CartLineItemType.class, enumCartLineItemType.getId());
    }

    /**
     * create a cart line item with basic attributes filled to be added to cart.
     * 
     * @param productVariant
     * @param order
     * @return
     */
    public CartLineItem createCartLineItemWithBasicDetails(ProductVariant productVariant, Order order) {
        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setProductVariant(productVariant);
        cartLineItem.setQty(productVariant.getQty());
        cartLineItem.setOrder(order);
        cartLineItem.setMarkedPrice(productVariant.getMarkedPrice());
        cartLineItem.setHkPrice(productVariant.getHkPrice(null));
        cartLineItem.setDiscountOnHkPrice(new Double(0));
        cartLineItem.setLineItemType(getCartLineItemType(EnumCartLineItemType.Product));

        return cartLineItem;
    }

    /*
     * @Transactional public void updateStatusForCartLineItems(List<CartLineItem> cartLineItems, EnumLineItemStatus
     * statusToSet) { for (CartLineItem cartLineItem : cartLineItems) { updateStatusForCartLineItem(cartLineItem,
     * statusToSet); } } @Transactional public CartLineItem updateStatusForCartLineItem(CartLineItem cartLineItem,
     * EnumLineItemStatus statusToSet) { LineItemStatus lineItemStatus =
     * lineItemStatusDaoProvider.get().find(statusToSet.getId()); cartLineItem.setLineItemStatus(lineItemStatus); return
     * cartLineItemDaoProvider.get().save(cartLineItem); }
     */

    public CartLineItem getMatchingCartLineItemFromOrder(Order order, CartLineItemMatcher cartLineItemMatcher) {

        return cartLineItemMatcher.match(order.getCartLineItems());
    }

    /*
     * public CartLineItem getCartLineItemFromOrder(Order order, ProductVariant productVariant, ComboInstance
     * comboInstance) { for (CartLineItem cartLineItem : order.getProductCartLineItems()) { if
     * (productVariant.equals(cartLineItem.getProductVariant()) &&
     * comboInstance.equals(cartLineItem.getComboInstance())) { return cartLineItem; } } return null; } public
     * CartLineItem getCartLineItemFromOrder(Order order, ProductVariant productVariant, CartLineItemConfig
     * lineItemConfig) { for (CartLineItem cartLineItem : order.getProductCartLineItems()) { if
     * (cartLineItem.getProductVariant().equals(productVariant)) { if
     * (lineItemConfig.equals(cartLineItem.getCartLineItemConfig())) { return cartLineItem; } } } return null; } public
     * CartLineItem getCartLineItemFromOrder(Order order, ProductVariant productVariant) { for (CartLineItem
     * cartLineItem : order.getProductCartLineItems()) { if (productVariant.equals(cartLineItem.getProductVariant())) {
     * return cartLineItem; } } return null; } public CartLineItem getCartLineItemFromOrder(Order order, ProductVariant
     * productVariant, List<CartLineItemExtraOption> extraOptions) { for (CartLineItem cartLineItem :
     * order.getProductCartLineItems()) { if (productVariant.equals(cartLineItem.getProductVariant())) { for
     * (CartLineItemExtraOption extraOption1 : extraOptions) { for (CartLineItemExtraOption extraOption2 :
     * cartLineItem.getCartLineItemExtraOptions()) { if (extraOption1.getName().equals(extraOption2.getName())) { if
     * (!extraOption1.getValue().equals(extraOption2.getValue())) { return null; } } } } return cartLineItem; } } return
     * null; }
     */

    public String getExtraOptionsPipeSeparated(CartLineItem cartLineItem) {
        StringBuffer stringBuffer = new StringBuffer();
        List<CartLineItemExtraOption> cartLineItemExtraOptions = cartLineItem.getCartLineItemExtraOptions();
        if (cartLineItemExtraOptions != null) {
            for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItemExtraOptions) {
                stringBuffer.append(cartLineItemExtraOption.getName()).append(":").append(cartLineItemExtraOption.getValue());
                stringBuffer.append(" |");
            }
        }
        return stringBuffer.toString();
    }

    public String getConfigOptionsPipeSeparated(CartLineItem cartLineItem) {
        StringBuffer stringBuffer = new StringBuffer();
        Set<CartLineItemConfigValues> cartLineItemConfigValues = new HashSet<CartLineItemConfigValues>();
        if (cartLineItem.getCartLineItemConfig() != null) {
            cartLineItemConfigValues = cartLineItem.getCartLineItemConfig().getCartLineItemConfigValues();
        }
        if (cartLineItemConfigValues != null) {
            for (CartLineItemConfigValues cartLineItemConfigValue : cartLineItemConfigValues) {
                stringBuffer.append(cartLineItemConfigValue.getVariantConfigOption().getDisplayName()).append(":").append(cartLineItemConfigValue.getValue());
                String additionalParam = cartLineItemConfigValue.getVariantConfigOption().getAdditionalParam();
                if (!(additionalParam.equals("TH") || additionalParam.equals("THBF") || additionalParam.equals("CO") || additionalParam.equals("COBF"))) {
                    String configName = cartLineItemConfigValue.getVariantConfigOption().getName();
                    if (configName.startsWith("R"))
                        stringBuffer.append("(R) ");
                    if (configName.startsWith("L"))
                        stringBuffer.append("(L) ");
                }
                stringBuffer.append(" |");
            }
        }
        return stringBuffer.toString();
    }

    public CartLineItemDao getCartLineItemDao() {
        return cartLineItemDao;
    }

    public void setCartLineItemDao(CartLineItemDao cartLineItemDao) {
        this.cartLineItemDao = cartLineItemDao;
    }

}
