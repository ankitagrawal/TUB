package com.hk.api.edge.integration.resource.variant;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.edge.integration.pact.service.variant.HybridStoreVariantServiceFromHKR;
import com.hk.api.edge.integration.response.variant.response.ComboResponseFromHKR;
import com.hk.api.edge.integration.response.variant.response.FreeVariantResponseFromHKR;
import com.hk.api.edge.integration.response.variant.response.InventoryResponseFromHkr;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author Rimal
 */
@Component
@Path("/variant/")
public class HybridStoreVariantResource {

  @Autowired
  private HybridStoreVariantServiceFromHKR hybridStoreVariantServiceFromHKR;
  @Autowired
  private ProductVariantService productVariantService;
  @Autowired
  private InventoryService inventoryService;

  @GET
  @Path("{id}/freeVariant/")
  @Produces("application/json")
  public String getFreeVariantsForProductVariantFromHKR(@PathParam("id") String productVariantId) {
    FreeVariantResponseFromHKR freeVariantResponseFromHKR = getHybridStoreVariantServiceFromHKR().getFreeVariantForProductVariant(productVariantId);
    return new JSONResponseBuilder().addField("results", freeVariantResponseFromHKR).build();
  }

  @GET
  @Path("{id}/combos/")
  @Produces("application/json")
  public String getCombosForProductVariantFromHKR(@PathParam("id") String productVariantId,
                                                  @QueryParam("noRs") @DefaultValue("6") int noOfResults) {
    ComboResponseFromHKR comboResponseFromHKR = getHybridStoreVariantServiceFromHKR().getCombosForProductVariant(productVariantId, noOfResults);
    return new JSONResponseBuilder().addField("results", comboResponseFromHKR).build();
  }

  @GET
  @Path("/inventory/{id}")
  @Produces("application/json")
  public String getInventoryForProductVariant(@PathParam("id") String productVariantId) {
    InventoryResponseFromHkr inventoryResponseFromHkr = new InventoryResponseFromHkr();
    ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);
    if (productVariant == null) {
      inventoryResponseFromHkr.addMessage("Invalid Product Variant Id");
      return new JSONResponseBuilder().addField("results", inventoryResponseFromHkr).build();
    }
    Long unbookedInventory = getInventoryService().getAvailableUnBookedInventory(productVariant);
    inventoryResponseFromHkr.addMessage("Inventory available");
    inventoryResponseFromHkr.setUnbookedInventory(unbookedInventory);
    return new JSONResponseBuilder().addField("results", inventoryResponseFromHkr).build();
  }


  public HybridStoreVariantServiceFromHKR getHybridStoreVariantServiceFromHKR() {
    return hybridStoreVariantServiceFromHKR;
  }

  public ProductVariantService getProductVariantService() {
    return productVariantService;
  }

  public InventoryService getInventoryService() {
    return inventoryService;
  }
}
