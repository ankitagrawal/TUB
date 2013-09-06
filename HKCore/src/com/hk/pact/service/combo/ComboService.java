package com.hk.pact.service.combo;

import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 5, 2012
 * Time: 1:57:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ComboService {

  public void markRelatedCombosOutOfStock(ProductVariant productVariant);

  public void markProductOutOfStock(ProductVariant productVariant);

    public void recacheFreebieProducts(ProductVariant productVariant);
  
}
