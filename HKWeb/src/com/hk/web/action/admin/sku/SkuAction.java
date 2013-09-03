package com.hk.web.action.admin.sku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Mar 5, 2012
 * Time: 4:26:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.CREATE_EDIT_SKU}, authActionBean = AdminPermissionAction.class)
@Component
public class SkuAction extends BaseAction {

  private static Logger logger = Logger.getLogger(SkuAction.class);
  @Autowired
  SkuService skuService;
  private Warehouse warehouse;
  private ProductVariant productVariant;
  private String category;
  private Sku sku;
  private String brand;
  private String productId;


  private List<Sku> skuList = new ArrayList<Sku>();

  @DefaultHandler
  public Resolution pre() {

    if (productVariant != null) {
      skuList = skuService.getSKUsForProductVariant(productVariant);
    }
    return new ForwardResolution("/pages/admin/skuList.jsp");
  }

  public Resolution save() {
    if (sku != null && sku.getId() != null) {
      logger.debug("skuList@Save: " + skuList.size());
    }
    for (Sku sku : skuList) {
      skuService.saveSku(sku);
    }
    addRedirectAlertMessage(new SimpleMessage("Changes saved."));
    return new RedirectResolution(SkuAction.class);
  }

  public Resolution addSku() {
    return new ForwardResolution("/pages/admin/addSku.jsp");
  }

  public Resolution saveNewSku() {
    if (sku.getProductVariant() != null) {
      Sku skuInDB = skuService.findSKU(sku.getProductVariant(), sku.getWarehouse());
      if (skuInDB != null) {
        addRedirectAlertMessage(new SimpleMessage("This Product Variant in this warehouse already exists."));
        return new RedirectResolution(SkuAction.class);
      }
      skuService.saveSku(sku);

      ProductVariant productVariant = sku.getProductVariant();
      if (productVariant.getWarehouse() == null) {
        productVariant.setWarehouse(sku.getWarehouse());
        getBaseDao().save(productVariant);

      }
      addRedirectAlertMessage(new SimpleMessage("New SKU saved successfully."));
      return new RedirectResolution(SkuAction.class);
    }
    addRedirectAlertMessage(new SimpleMessage("Invalid Product Variant Id."));
    return new RedirectResolution(SkuAction.class);
  }

  public Resolution searchSKUs() {

    if (category != null || brand != null || productId != null) {
      String categoryName = null;
      if (category != null) {
        categoryName = Category.getNameFromDisplayName(category);
      }
      skuList = skuService.getSKUs(categoryName, brand, productId);
    } else {
      skuList = skuService.getSKUsForProductVariant(productVariant);
    }
    return new ForwardResolution("/pages/admin/skuList.jsp");
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("productVariant");
    return params;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public List<Sku> getSkuList() {
    return skuList;
  }

  public void setSkuList(List<Sku> skuList) {
    this.skuList = skuList;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Sku getSku() {
    return sku;
  }

  public void setSku(Sku sku) {
    this.sku = sku;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

}
