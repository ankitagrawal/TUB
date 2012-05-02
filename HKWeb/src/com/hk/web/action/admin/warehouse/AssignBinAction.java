package com.hk.web.action.admin.warehouse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.warehouse.BinDao;
import com.hk.admin.manager.BinManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.Bin;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 1/16/12
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.ASSIGN_BIN}, authActionBean = AdminPermissionAction.class)
@Component
public class AssignBinAction extends BaseAction {

  Category category;
  String brand;
  Product product;
  ProductVariant productVariant;
  Boolean override;
  Bin bin;
   BinManager binManager;
   BinDao binDao;
  
  UserService userService;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/assignBin.jsp");
  }

  public Resolution assignBinByCategory() {
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    bin = binDao.getOrCreateBin(bin,warehouse);
    binManager.assignBinToCategory(category,bin,override);
    addRedirectAlertMessage(new SimpleMessage("bin assigned to all the child inStock inventory"));
    return new ForwardResolution("/pages/admin/assignBin.jsp");
  }

  public Resolution assignBinByBrand() {
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    bin = binDao.getOrCreateBin(bin,warehouse);
    binManager.assignBinToBrand(brand, bin, override);
    addRedirectAlertMessage(new SimpleMessage("bin assigned to all the child inStock inventory"));
    return new ForwardResolution("/pages/admin/assignBin.jsp");
  }

  public Resolution assignBinToProduct() {
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    bin = binDao.getOrCreateBin(bin,warehouse);
    binManager.assignBinToProduct(product, bin, override);
    addRedirectAlertMessage(new SimpleMessage("bin assigned to all the child inStock inventory"));
    return new ForwardResolution("/pages/admin/assignBin.jsp");
  }

  public Resolution assignBinToVariant(){
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    bin = binDao.getOrCreateBin(bin,warehouse);
    binManager.assignBinToVariant(productVariant, bin, override);
    addRedirectAlertMessage(new SimpleMessage("bin assigned to all the child inStock inventory"));
    return new ForwardResolution("/pages/admin/assignBin.jsp");
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Boolean getOverride() {
    return override;
  }

  public void setOverride(Boolean override) {
    this.override = override;
  }

  public Bin getBin() {
    return bin;
  }

  public void setBin(Bin bin) {
    this.bin = bin;
  }
}
