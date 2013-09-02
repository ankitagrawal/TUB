package com.hk.impl.service.catalog.combo;

import com.hk.cache.ProductCache;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.combo.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 5, 2012
 * Time: 1:58:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ComboServiceImpl implements ComboService {

  @Autowired
  ComboDao comboDao;
  @Autowired
  ProductService productService;

  //@Async
  public void markRelatedCombosOutOfStock(ProductVariant productVariant) {
    List<Combo> combos = getComboDao().getCombos(productVariant);
    for (Combo combo : combos) {
      if (getProductService().isComboInStock(combo) && combo.isOutOfStock()) {
        combo.setOutOfStock(false);
        getComboDao().save(combo);
      } else if (!getProductService().isComboInStock(combo) && !combo.isOutOfStock()) {
        combo.setOutOfStock(true);
        getComboDao().save(combo);
      }
    }
  }

  @Async
  public void markProductOutOfStock(ProductVariant productVariant) {
    Product product = productVariant.getProduct();
    if (product.getInStockVariants().isEmpty()) {
      product.setOutOfStock(true);
      getProductService().save(product);
    }
  }

    @Override
    public void recacheFreebieProducts(ProductVariant productVariant) {
        List<Product> products = productService.getProductListWithFreebie(productVariant);
        for (Product product : products) {
            ProductCache.getInstance().refreshCache(product);
        }
    }

    public ComboDao getComboDao() {
    return comboDao;
  }

  public ProductService getProductService() {
    return productService;
  }
}
