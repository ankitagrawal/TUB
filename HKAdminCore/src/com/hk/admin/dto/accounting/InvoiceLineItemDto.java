package com.hk.admin.dto.accounting;

import java.util.List;
import java.util.Set;

import com.hk.constants.core.TaxConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.VariantConfigOptionParam;
import com.hk.domain.order.CartLineItemConfigValues;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.shippingOrder.LineItem;

/**
 * Created by IntelliJ IDEA. User: Rahul Date: Mar 14, 2012 Time: 6:10:49 PM To change this template use File | Settings |
 * File Templates.
 */
public class InvoiceLineItemDto {

    private LineItem                      productLineItem;
    private List<Category>                productCategories;
    private List<ProductOption>           productOptions;
    private List<CartLineItemExtraOption> cartLineItemExtraOptions;
    private Set<CartLineItemConfigValues> cartLineItemConfigValues;
    private String                        productName;
    private String                        variantName;
    private long                          qty;
    private double                        hkPrice;
    private double                        lineItemTotal;
    private double                        costPrice;
    private double                        orderLevelDiscount;
    private double                        itemLevelDiscount;
    private double                        rewardPointDiscount;
    private double                        totalDiscountOnLineItem;
    private double                        rate;
    private String                        taxRate;
    private double                        taxValue;
    private double                        taxable;
    private double                        tax;
    private double                        surcharge;

    public InvoiceLineItemDto(LineItem productLineItem) {
        productCategories = productLineItem.getSku().getProductVariant().getProduct().getCategories();
        productName = productLineItem.getSku().getProductVariant().getProduct().getName();
        variantName = productLineItem.getSku().getProductVariant().getVariantName();
        productOptions = productLineItem.getSku().getProductVariant().getProductOptions();
        cartLineItemExtraOptions = productLineItem.getCartLineItem().getCartLineItemExtraOptions();
        if (productLineItem.getCartLineItem().getCartLineItemConfig() != null) {
            cartLineItemConfigValues = productLineItem.getCartLineItem().getCartLineItemConfig().getCartLineItemConfigValues();
        }

        qty = productLineItem.getQty();
        hkPrice = productLineItem.getHkPrice();
        lineItemTotal = hkPrice * qty;
        costPrice = productLineItem.getCostPrice();
        taxValue = productLineItem.getTax().getValue();

        orderLevelDiscount = productLineItem.getOrderLevelDiscount();
        rewardPointDiscount = productLineItem.getRewardPoints();
        itemLevelDiscount = productLineItem.getDiscountOnHkPrice();

        totalDiscountOnLineItem = orderLevelDiscount + rewardPointDiscount + itemLevelDiscount;
        rate = hkPrice - totalDiscountOnLineItem / qty;
        taxRate = (taxValue * 100) + "%";

        if (productLineItem.getSku().getWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)) {
            taxable = (lineItemTotal - totalDiscountOnLineItem) / (1 + taxValue + (taxValue * TaxConstants.SURCHARGE));
            surcharge = taxable * (taxValue * TaxConstants.SURCHARGE);

        } else {
            taxable = (lineItemTotal - totalDiscountOnLineItem) / (1 + taxValue);
            // surcharge is zero in case of maharashtra
            surcharge = 0.0;

        }
        tax = taxable * taxValue;

    }

    public LineItem getProductLineItem() {
        return productLineItem;
    }

    public long getQty() {
        return qty;
    }

    public double getHkPrice() {
        return hkPrice;
    }

    public double getLineItemTotal() {
        return lineItemTotal;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getOrderLevelDiscount() {
        return orderLevelDiscount;
    }

    public double getItemLevelDiscount() {
        return itemLevelDiscount;
    }

    public double getRewardPointDiscount() {
        return rewardPointDiscount;
    }

    public double getTotalDiscountOnLineItem() {
        return totalDiscountOnLineItem;
    }

    public double getRate() {
        return rate;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public double getTaxable() {
        if (taxable < 0.0) {
            return 0.0;
        }
        return taxable;
    }

    public double getTax() {
        if (tax < 0.0) {
            return 0.0;
        }
        return tax;
    }

    public double getSurcharge() {
        if (surcharge < 0.0) {
            return 0.0;
        }
        return surcharge;
    }

    public List<Category> getProductCategories() {
        return productCategories;
    }

    public String getProductName() {
        return productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public List<ProductOption> getProductOptions() {
        return productOptions;
    }

    public List<CartLineItemExtraOption> getCartLineItemExtraOptions() {
        return cartLineItemExtraOptions;
    }

    public Set<CartLineItemConfigValues> getCartLineItemConfigValues() {
        return cartLineItemConfigValues;
    }

    public String getExtraOptionsPipeSeparated() {
        StringBuffer stringBuffer = new StringBuffer();
        if (cartLineItemExtraOptions != null) {
            for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItemExtraOptions) {
                stringBuffer.append(cartLineItemExtraOption.getName()).append(":").append(cartLineItemExtraOption.getValue());
                stringBuffer.append(" |");
            }
        }
        return stringBuffer.toString();

    }

    public String getProductOptionsPipeSeparated() {
        StringBuffer stringBuffer = new StringBuffer();
        if (productOptions != null) {
            for (ProductOption productOption : productOptions) {
                stringBuffer.append(productOption.getName()).append(" ").append(productOption.getValue());
                stringBuffer.append(" |");
            }
        }
        return stringBuffer.toString();

    }

    public String getConfigOptionsPipeSeparated() {
        StringBuffer stringBuffer = new StringBuffer();
        if (cartLineItemConfigValues != null) {
            for (CartLineItemConfigValues cartLineItemConfigValue : cartLineItemConfigValues) {
                stringBuffer.append(cartLineItemConfigValue.getVariantConfigOption().getDisplayName()).append(":").append(cartLineItemConfigValue.getValue());
                String additionalParam = cartLineItemConfigValue.getVariantConfigOption().getAdditionalParam();
                if (!(additionalParam.equals(VariantConfigOptionParam.THICKNESS.param()) || additionalParam.equals(VariantConfigOptionParam.BFTHICKNESS.param())
                        || additionalParam.equals(VariantConfigOptionParam.COATING.param()) || additionalParam.equals(VariantConfigOptionParam.BFCOATING.param()))) {
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

}
