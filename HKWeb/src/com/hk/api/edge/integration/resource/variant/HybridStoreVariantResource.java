package com.hk.api.edge.integration.resource.variant;

import com.hk.api.edge.integration.pact.service.variant.HybridStoreVariantServiceFromHKR;
import com.hk.api.edge.integration.response.variant.FreeVariantResponseFromHKR;
import com.hk.util.json.JSONResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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
    public String getUserCartSummaryFromHKR(@PathParam("id") String productVariantId) {
        FreeVariantResponseFromHKR freeVariantResponseFromHKR = getHybridStoreVariantServiceFromHKR().getFreeVariantForProductVariant(productVariantId);
        return new JSONResponseBuilder().addField("results", freeVariantResponseFromHKR).build();
    }


    public HybridStoreVariantServiceFromHKR getHybridStoreVariantServiceFromHKR() {
        return hybridStoreVariantServiceFromHKR;
    }
}
