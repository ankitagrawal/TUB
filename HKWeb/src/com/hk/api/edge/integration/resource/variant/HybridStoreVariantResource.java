package com.hk.api.edge.integration.resource.variant;

import com.hk.api.edge.integration.pact.service.variant.HybridStoreVariantServiceFromHKR;
import com.hk.api.edge.integration.response.variant.response.FreeVariantResponseFromHKR;
import com.hk.api.edge.integration.response.variant.response.ComboResponseFromHKR;
import com.hk.api.edge.integration.response.variant.response.InventoryResponseFromHkr;
import com.hk.util.json.JSONResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.security.InvalidParameterException;

/**
 * @author Rimal
 */
@Component
@Path("/variant/")
public class HybridStoreVariantResource {

    @Autowired
    private HybridStoreVariantServiceFromHKR hybridStoreVariantServiceFromHKR;


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
    @Path("{id}/inventory")
    @Produces("application/json")
    public String getInventoryForProductVariant(@PathParam("id") String productVariantId){
      InventoryResponseFromHkr inventoryResponseFromHkr = new InventoryResponseFromHkr();
      Long unbookedInventory = 0L;
      try{
        unbookedInventory = getHybridStoreVariantServiceFromHKR().getUnbookedInventoryForProductVariant(productVariantId);
      }catch (InvalidParameterException ipe){
        inventoryResponseFromHkr.addMessage("Invalid Product Variant Id");
        return new JSONResponseBuilder().addField("results",inventoryResponseFromHkr).build();
      }
      inventoryResponseFromHkr.addMessage("Inventory available");
      inventoryResponseFromHkr.setUnbookedInventory(unbookedInventory);
      return new JSONResponseBuilder().addField("results",inventoryResponseFromHkr).build();
    }


    public HybridStoreVariantServiceFromHKR getHybridStoreVariantServiceFromHKR() {
        return hybridStoreVariantServiceFromHKR;
    }
}
