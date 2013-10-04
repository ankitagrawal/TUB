package com.hk.api.edge.integration.resource.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.edge.pact.service.HybridStoreVariantService;
import com.hk.edge.request.VariantPricingSyncRequest;
import com.hk.util.json.JSONResponseBuilder;

@Component
@Path("/hkr/sync/")
public class SyncResource {

    private ExecutorService           pricingSyncExecutorService = Executors.newFixedThreadPool(40);

    @Autowired
    private HybridStoreVariantService hybridStoreVariantService;

    @POST
    @Path("/variant/pricing")
    @Produces("application/json")
    public String syncVariantPricing(final VariantPricingSyncRequest variantPricingSyncRequest) {

        pricingSyncExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                getHybridStoreVariantService().syncPricingFromEdge(variantPricingSyncRequest);
            }
        });
        return new JSONResponseBuilder().addField("msg", "synced for" + variantPricingSyncRequest.getOldVariantId()).build();
    }

    public HybridStoreVariantService getHybridStoreVariantService() {
        return hybridStoreVariantService;
    }

}
