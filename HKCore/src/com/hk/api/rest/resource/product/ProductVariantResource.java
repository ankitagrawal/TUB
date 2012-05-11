package com.hk.api.rest.resource.product;


import com.hk.api.rest.utils.JSONResponseBuilder;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author vaibhav.adlakha
 */

@Path ("/product/variant")
public class ProductVariantResource {

  @Autowired
  private ProductVariantService productVariantService;
  @Autowired
  private InventoryService inventoryService;
  @Autowired
  private SkuService skuService;


  @GET
  @Path ("/{variantId}/price")
  @Produces ("application/json")
  public String variantPrice(@PathParam ("variantId") String variantId) {
    if (StringUtils.isBlank(variantId)) {
      return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant Id is required").build();
    }

    ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

    if (productVariant == null) {
      return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant does not exist").build();
    }

    //boolean isVisible = !(productVariant.getDeleted() || productVariant.isOutOfStock());
    boolean isVisible = false;
    Long unbookedInventory = 0L;
    List<Sku> skuList = getSkuService().getSKUsForProductVariant(productVariant);
    if (skuList != null && !skuList.isEmpty()) {
      unbookedInventory = getInventoryService().getAvailableUnbookedInventory(skuList);
    }
    if (unbookedInventory > 0) {
      isVisible = true;
    }
    int visibleNumber = isVisible ? 1 : 0;

    return new JSONResponseBuilder().addField("hkPrice", productVariant.getHkPrice()).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible", visibleNumber).build();
  }

  public ProductVariantService getProductVariantService() {
    return productVariantService;
  }

  public void setProductVariantService(ProductVariantService productVariantService) {
    this.productVariantService = productVariantService;
  }

  public InventoryService getInventoryService() {
    return inventoryService;
  }

  public void setInventoryService(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  public SkuService getSkuService() {
    return skuService;
  }

  public void setSkuService(SkuService skuService) {
    this.skuService = skuService;
  }
}
