package com.hk.core.fliter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;


public class CartLineItemFilter {

  private Set<CartLineItem> cartLineItems;
  private Set<EnumCartLineItemType> cartLineItemTypes = new HashSet<EnumCartLineItemType>();

  /*private boolean onlyComboLineItems = false;
  private boolean onlyProductLineItems = false;*/
  private boolean onlyServiceLineItems = false;
  private boolean onlyGroundShippedItems = false;

  private String productVariantId;
  private Long cartLineItemConfigId;
  private String categoryName;


  public CartLineItemFilter(Set<CartLineItem> cartLineItems) {
    this.cartLineItems = cartLineItems;
  }

  public CartLineItemFilter addCartLineItemType(EnumCartLineItemType cartLineItemType) {
    this.cartLineItemTypes.add(cartLineItemType);
    return this;
  }

  public CartLineItemFilter hasOnlyServiceLineItems(boolean onlyServiceLineItems) {
    this.onlyServiceLineItems = onlyServiceLineItems;
    return this;
  }

    public CartLineItemFilter hasOnlyGroundShippedItems(boolean onlyGroundShippedItems) {
    this.onlyGroundShippedItems = onlyGroundShippedItems;
    return this;
  }


  public CartLineItemFilter setCategoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

  /*public CartLineItemFilter hasOnlyComboLineItems(boolean onlyComboLineItems) {
    this.onlyComboLineItems = onlyComboLineItems;
    return this;
  }

  public CartLineItemFilter hasOnlyProductLineItems(boolean onlyProductLineItems) {
    this.onlyProductLineItems = onlyProductLineItems;
    return this;
  }*/

  public CartLineItemFilter setProductVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
    return this;
  }

  public CartLineItemFilter setCartLineItemConfigId(Long cartLineItemConfigId) {
    this.cartLineItemConfigId = cartLineItemConfigId;
    return this;
  }

  public Set<CartLineItem> filter() {

    Set<CartLineItem> filteredCartLineItems = new HashSet<CartLineItem>();
    Set<CartLineItem> currentLineItems = new HashSet<CartLineItem>(cartLineItems);

    if (cartLineItemTypes != null && cartLineItemTypes.size() > 0) {
      for (CartLineItem cartLineItem : cartLineItems) {
        List<Long> selectedCartLineItemTypeIDs = EnumCartLineItemType.getCartLineItemTypeIDs(cartLineItemTypes);
        if (!selectedCartLineItemTypeIDs.contains(cartLineItem.getLineItemType().getId())) {
          currentLineItems.remove(cartLineItem);
        }
      }
    }

    filteredCartLineItems.addAll(currentLineItems);

    currentLineItems.clear();
    currentLineItems.addAll(filteredCartLineItems);

    if (!StringUtils.isBlank(categoryName)) {
      for (CartLineItem cartLineItem : currentLineItems) {
        if (!categoryName.equals(cartLineItem.getProductVariant().getProduct().getPrimaryCategory().getName())) {
          filteredCartLineItems.remove(cartLineItem);
        }
      }
    }

    currentLineItems.clear();
    currentLineItems.addAll(filteredCartLineItems);

    if (StringUtils.isNotBlank(productVariantId)) {
      for (CartLineItem cartLineItem : currentLineItems) {
        if (!productVariantId.equals(cartLineItem.getProductVariant().getId())) {
          filteredCartLineItems.remove(cartLineItem);
        }
      }
    }

//    filteredCartLineItems.addAll(currentLineItems);

    currentLineItems.clear();
    currentLineItems.addAll(filteredCartLineItems);

    if (cartLineItemConfigId != null) {
      for (CartLineItem cartLineItem : currentLineItems) {
        if (cartLineItem != null && cartLineItem.getCartLineItemConfig() != null
            && !cartLineItemConfigId.equals(cartLineItem.getCartLineItemConfig().getId())) {
          filteredCartLineItems.remove(cartLineItem);
        }
      }
    }

    currentLineItems.clear();
    currentLineItems.addAll(filteredCartLineItems);


    if (onlyGroundShippedItems) {
        for (CartLineItem cartLineItem : currentLineItems) {
        if (cartLineItem != null ) {
          ProductVariant productVariant = cartLineItem.getProductVariant();
          if(productVariant !=null){
            Product product = productVariant.getProduct();
            if(product !=null && !product.isGroundShipping()){
               filteredCartLineItems.remove(cartLineItem);
            }
          }
        }
      }   
    }
      currentLineItems.clear();
       currentLineItems.addAll(filteredCartLineItems);


     if (onlyServiceLineItems) {
        for (CartLineItem cartLineItem : currentLineItems) {
        if (cartLineItem != null ) {
          ProductVariant productVariant = cartLineItem.getProductVariant();
          if(productVariant !=null){
            Product product = productVariant.getProduct();
            if(product !=null && !product.isService()){
               filteredCartLineItems.remove(cartLineItem);
            }
          }
        }
      }
    }

    currentLineItems.clear();
		currentLineItems.addAll(filteredCartLineItems);

     

    /*

    if(onlyComboLineItems) {
      for (CartLineItem cartLineItem : currentLineItems) {
				if (cartLineItem.getComboInstance() == null) {
					filteredCartLineItems.remove(cartLineItem);
				}
			}
    }


    filteredCartLineItems.addAll(currentLineItems);

    currentLineItems.clear();
		currentLineItems.addAll(filteredCartLineItems);

    if(onlyProductLineItems) {
      for (CartLineItem cartLineItem : currentLineItems) {
				if (cartLineItem.getProductVariant() == null) {
					filteredCartLineItems.remove(cartLineItem);
				}
			}
    }*/

    return filteredCartLineItems;
  }
/*
	public Set<CartLineItem> filterCartLineItemsByType(EnumLineItemType lineItemType) {
		return filterCartLineItemsByType(Arrays.asList(lineItemType));
	}

	public Set<CartLineItem> filterCartLineItemsByType(Set<EnumLineItemType> selectedLineItemTypes) {
		Set<CartLineItem> filteredCartLineItems = new HashSet<CartLineItem>();
		List<Long> selectedCartLineItemTypeIDs = EnumLineItemType.getCartLineItemTypeIDs(selectedLineItemTypes);

		for (CartLineItem cartLineItem : cartLineItems) {
			if (selectedCartLineItemTypeIDs.contains(cartLineItem.getLineItemType().getId())) {
				filteredCartLineItems.add(cartLineItem);
			}
		}

		return filteredCartLineItems;
	}*/

}
