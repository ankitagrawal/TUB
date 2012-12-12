package com.hk.impl.service.catalog.combo;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.pact.dao.catalog.combo.ComboDao;
import com.hk.pact.service.combo.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 5, 2012
 * Time: 1:58:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ComboServiceImpl implements ComboService{

  @Autowired
  ComboDao comboDao;

    @Async
    public void markRelatedCombosOutOfStock(ProductVariant productVariant){

      //setting all combos in a List just to save from concurrentException
      List<ComboProduct> comboProducts = new ArrayList<ComboProduct>();
      for(ComboProduct comboProduct : productVariant.getComboProducts()){
        comboProducts.add(comboProduct);
      }
      //getting all combos in set so that combo value doesn't repeat
      Set<Combo> combos = new HashSet<Combo>();
      for(ComboProduct comboProduct : comboProducts){
        combos.add(comboProduct.getCombo());
      }
      for(Combo combo : combos){
        boolean isComboInStock = true;
        for(ComboProduct comboProduct1 : combo.getComboProducts()) {
          List<ProductVariant> productAllowedVariants = comboProduct1.getAllowedProductVariants();
          boolean isComboOutOfStock = true;
          //checking allowed variants for outOfStock of a combo product
          for(ProductVariant productVariant1 : productAllowedVariants){
            if(!productVariant1.isOutOfStock()){
              isComboOutOfStock = false;
              break;
            }
          }
          //checking if combo product allowed variants is outOfStock then set combo as outOfStock if it's  inStock
          if(!combo.isOutOfStock() && isComboOutOfStock){
            isComboInStock = false;
            break;
          }
        }
        //setting combo in stock if it's outOfStock or vice-versa
        if((!combo.isOutOfStock() && !isComboInStock) || (isComboInStock && combo.isOutOfStock())){
          Combo combo1 = getComboDao().getComboById(combo.getId());
          combo1.setOutOfStock(!isComboInStock);
          getComboDao().save(combo1);
        }
      }
    }

  public ComboDao getComboDao() {
    return comboDao;
  }
}
