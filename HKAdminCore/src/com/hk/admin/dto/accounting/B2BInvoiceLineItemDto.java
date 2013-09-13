package com.hk.admin.dto.accounting;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.hk.constants.core.TaxConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.VariantConfigOptionParam;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.CartLineItemConfigValues;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItemLineItem;

public class B2BInvoiceLineItemDto extends InvoiceLineItemDto {

	private LineItem productLineItem;
	private List<Category> productCategories;
	private List<ProductOption> productOptions;
	private List<CartLineItemExtraOption> cartLineItemExtraOptions;
	private Set<CartLineItemConfigValues> cartLineItemConfigValues;
    private List<SkuItemLineItem>         skuItemLineItems;
	private String productName;
	private String variantName;
    private String variantId;
	private long qty;
	private double markedPrice;
	private double hkPrice;
	private double lineItemTotal;
	private double costPrice;
	private double orderLevelDiscount;
	private double itemLevelDiscount;
	private double rewardPointDiscount;
	private double totalDiscountOnLineItem;
	private double rate;
	private String taxRate;
	private double taxValue;
	private double taxable;
	private double tax;
	private double surcharge;
    Map<String, QtyExp> skuGroupLongMap;
    DateFormat dateFormat = new SimpleDateFormat("MM/yy");
    private QtyExp qtyExp;
	

	public B2BInvoiceLineItemDto(LineItem productLineItem, Boolean sameState, boolean cFormAvailable) {
		super();
		productCategories = productLineItem.getSku().getProductVariant().getProduct().getCategories();
		productName = productLineItem.getSku().getProductVariant().getProduct().getName();
		variantName = productLineItem.getSku().getProductVariant().getVariantName();
        variantId   = productLineItem.getSku().getProductVariant().getId();
		productOptions = productLineItem.getSku().getProductVariant().getProductOptions();
		cartLineItemExtraOptions = productLineItem.getCartLineItem().getCartLineItemExtraOptions();
        skuItemLineItems = productLineItem.getSkuItemLineItems();
		CartLineItemConfig cartLineItemConfig = productLineItem.getCartLineItem().getCartLineItemConfig();
		if (cartLineItemConfig != null) {
			cartLineItemConfigValues = cartLineItemConfig.getCartLineItemConfigValues();
		}
        skuGroupLongMap = new HashMap<String, QtyExp>();
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
            Date expiry = skuItemLineItem.getSkuItem().getSkuGroup().getExpiryDate();
            String expiryDt;
            if(expiry!=null) {
                expiryDt = dateFormat.format(expiry);
            }
            else expiryDt = "N/A";
            String batchNumber = skuItemLineItem.getSkuItem().getSkuGroup().getBatchNumber();
            if(skuGroupLongMap.get(batchNumber)!=null){
                QtyExp qtyExp =  skuGroupLongMap.get(batchNumber);
                Long qty = skuGroupLongMap.get(batchNumber).getQty();
                ++qty;
                qtyExp.setQty(qty);
                skuGroupLongMap.put(batchNumber, qtyExp);
            }
            else
            {
                QtyExp qtyExp = new QtyExp();
                qtyExp.setExpiry(expiryDt);
                qtyExp.setQty(1l);
                skuGroupLongMap.put(batchNumber, qtyExp);

            }
        }

        qty = productLineItem.getQty();

		markedPrice = productLineItem.getMarkedPrice();
		hkPrice = productLineItem.getHkPrice();

		lineItemTotal = hkPrice * qty;
		costPrice = productLineItem.getCostPrice();
		taxValue = productLineItem.getTax().getValue();

		orderLevelDiscount = productLineItem.getOrderLevelDiscount();
		rewardPointDiscount = productLineItem.getRewardPoints();
		itemLevelDiscount = productLineItem.getDiscountOnHkPrice();

		totalDiscountOnLineItem = orderLevelDiscount + rewardPointDiscount + itemLevelDiscount;
		rate = hkPrice - totalDiscountOnLineItem / qty;
		taxRate = Double.parseDouble(new DecimalFormat("#.##").format(taxValue * 100)) + "%";


    Double surchargeRate= 0.0;
    if (productLineItem.getSku().getWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)){
      surchargeRate = TaxConstants.SURCHARGE;
    }
    else {
      surchargeRate = 0.0;
    }

    if (sameState) {
      taxable = (lineItemTotal - totalDiscountOnLineItem);
      tax = lineItemTotal * taxValue;
      surcharge = taxable * (taxValue * surchargeRate);
    } else {
      if(cFormAvailable){
        taxable = (lineItemTotal - totalDiscountOnLineItem);
        taxValue = 0.02;
        taxRate = Double.parseDouble(new DecimalFormat("#.##").format(taxValue * 100)) + "%";
        surcharge = 0.0;
        tax = lineItemTotal * taxValue;
      }
      else{
        taxable = (lineItemTotal - totalDiscountOnLineItem);
        tax = lineItemTotal * taxValue;
        surcharge = taxable * (taxValue * surchargeRate);  //Sale from haryana to other state without C form
        //	surcharge = 0.0;
      }
    }

/*

      if (productLineItem.getSku().getWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)) {
			if (sameState) {
				taxable = (lineItemTotal - totalDiscountOnLineItem);
				tax = lineItemTotal * taxValue;
				surcharge = taxable * (taxValue * surchargeRate);
			} else {
				if(cFormAvailable){
				taxable = (lineItemTotal - totalDiscountOnLineItem);
				taxValue = 0.02;
				taxRate = Double.parseDouble(new DecimalFormat("#.##").format(taxValue * 100)) + "%";
				surcharge = 0.0;
				tax = lineItemTotal * taxValue;
				}
				else{
					taxable = (lineItemTotal - totalDiscountOnLineItem);
					tax = lineItemTotal * taxValue;
          surcharge = taxable * (taxValue * surchargeRate);  //Sale from haryana to other state without C form
				//	surcharge = 0.0;
				}
			}

		} else
		{
			if (state.equalsIgnoreCase("Maharastra")) {
				taxable = (lineItemTotal - totalDiscountOnLineItem);
				tax = lineItemTotal * taxValue;
				surcharge = 0.0;
			} else {
				if(cFormAvailable){
				taxable = (lineItemTotal - totalDiscountOnLineItem);
				surcharge = 0.0;
				taxValue = 0.02;
				taxRate = Double.parseDouble(new DecimalFormat("#.##").format(taxValue * 100)) + "%";
				tax = lineItemTotal * taxValue;
				}
				else{
					taxable = (lineItemTotal - totalDiscountOnLineItem);
					tax = lineItemTotal * taxValue;
					surcharge = 0.0;
				}
			}

		}

*/
	}

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public LineItem getProductLineItem() {
		return productLineItem;
	}

	public long getQty() {
		return qty;
	}

	public double getMarkedPrice() {
		return markedPrice;
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

    public List<SkuItemLineItem> getSkuItemLineItems() {
        return skuItemLineItems;
    }

    public String getExtraOptionsPipeSeparated() {
		StringBuffer stringBuffer = new StringBuffer();
		if (cartLineItemExtraOptions != null) {
			for (CartLineItemExtraOption cartLineItemExtraOption : cartLineItemExtraOptions) {
				stringBuffer.append(cartLineItemExtraOption.getName()).append(":")
						.append(cartLineItemExtraOption.getValue());
				stringBuffer.append(" |");
			}
			if (stringBuffer.length() > 0 && stringBuffer.charAt(stringBuffer.length() - 1) == '|') {
				return stringBuffer.substring(0, stringBuffer.length() - 1);
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
			if (stringBuffer.length() > 0 && stringBuffer.charAt(stringBuffer.length() - 1) == '|') {
				return stringBuffer.substring(0, stringBuffer.length() - 1);
			}
		}

		return stringBuffer.toString();
	}

	public String getConfigOptionsPipeSeparated() {
		StringBuffer stringBuffer = new StringBuffer();
		if (cartLineItemConfigValues != null) {
			for (CartLineItemConfigValues cartLineItemConfigValue : cartLineItemConfigValues) {
				stringBuffer.append(cartLineItemConfigValue.getVariantConfigOption().getDisplayName()).append(":")
						.append(cartLineItemConfigValue.getValue());
				String additionalParam = cartLineItemConfigValue.getVariantConfigOption().getAdditionalParam();
				if (!(additionalParam.equals(VariantConfigOptionParam.THICKNESS.param())
						|| additionalParam.equals(VariantConfigOptionParam.BFTHICKNESS.param())
						|| additionalParam.equals(VariantConfigOptionParam.COATING.param()) || additionalParam
							.equals(VariantConfigOptionParam.BFCOATING.param()))) {
					String configName = cartLineItemConfigValue.getVariantConfigOption().getName();
					if (configName.startsWith("R"))
						stringBuffer.append("(R) ");
					if (configName.startsWith("L"))
						stringBuffer.append("(L) ");
				}
				if (additionalParam.equals(VariantConfigOptionParam.ENGRAVING.param())) {
					stringBuffer.append(" <b>+Rs. ").append(cartLineItemConfigValue.getAdditionalPrice())
							.append("</b>");
				}
				stringBuffer.append(" |");
			}
			if (stringBuffer.length() > 0 && stringBuffer.charAt(stringBuffer.length() - 1) == '|') {
				return stringBuffer.substring(0, stringBuffer.length() - 1);
			}
		}
		return stringBuffer.toString();
	}


    public Map<String, QtyExp> getSkuGroupLongMap() {
        return skuGroupLongMap;
    }

    public void setSkuGroupLongMap(Map<String, QtyExp> skuGroupLongMap) {
        this.skuGroupLongMap = skuGroupLongMap;
    }

    public QtyExp getQtyExp() {
        return qtyExp;
    }

    public void setQtyExp(QtyExp qtyExp) {
        this.qtyExp = qtyExp;
    }
}

 class QtyExp{
    Long qty;
    String expiry;

    String getExpiry() {
        return expiry;
    }

    void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    Long getQty() {
        return qty;
    }

    void setQty(Long qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        String val = "["+expiry+"]"+" - "+qty.toString();
        return val;
    }
}
