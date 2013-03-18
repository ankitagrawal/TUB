package com.hk.pact.dao.catalog.combo;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.order.Order;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;

public interface ComboDao extends BaseDao {

  public Combo getComboById(String id);

  public ComboProduct getComboProduct(Product product, Combo combo);

  public Page getComboByCategoryAndBrand(List<Category> categories, String brand, int page, int perPage);

  public List<Combo> getCombos();

  public List<Combo> getCombos(Product product);

  public List<Combo> getCombos(ProductVariant productVariant);

}