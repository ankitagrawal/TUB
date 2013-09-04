package com.hk.api.edge.ext.resource.cart;

import com.hk.api.edge.ext.pact.service.cart.HKCatalogCartService;
import com.hk.api.edge.ext.response.cart.CartSummaryApiResponse;
import com.hk.util.json.JSONResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Rimal
 */
@Component
@Path("/cart/")
public class HKCatalogCartResource {

    @Autowired
    private HKCatalogCartService hkCatalogCartService;

    @GET
    @Path("/summary")
    @Produces("application/json")
    public String getCartSummary() {
        CartSummaryApiResponse cartSummaryApiResponse = getHkCatalogCartService().getCartSummary();
        return new JSONResponseBuilder().addField("results", cartSummaryApiResponse).build();
    }


    public HKCatalogCartService getHkCatalogCartService() {
        return hkCatalogCartService;
    }
}