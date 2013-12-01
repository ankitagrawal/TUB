package com.hk.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfigValues;
import com.hk.domain.order.CartLineItemExtraOption;

public class CartLineItemUtil {

    public static String getExtraOptionsAsString(CartLineItem cartLineItem, String seperator) {
        StringBuffer stringBuffer = new StringBuffer();
        List<CartLineItemExtraOption> cartLineItemExtraOptions = cartLineItem.getCartLineItemExtraOptions();
        if (cartLineItemExtraOptions != null) {
            for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItemExtraOptions) {
                stringBuffer.append(cartLineItemExtraOption.getName()).append(":").append(cartLineItemExtraOption.getValue());
                stringBuffer.append(" ").append(seperator);
            }
        }
        return stringBuffer.toString();
    }

    public static String getConfigOptionsAsString(CartLineItem cartLineItem, String seperator) {
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
                stringBuffer.append(" ").append(seperator);
            }
        }
        return stringBuffer.toString();
    }
}
