package com.hk.api.resource;

import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.pact.service.HKAPIBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 5/14/13
 * Time: 2:35 PM
 */
@Path("/brand")
@Component
public class BrandResource {

    @Autowired
    HKAPIBrandService   hkapiBrandService;

    @GET
    @Path ("/{brandId}")
    @Produces(MediaType.APPLICATION_JSON)
    public HKAPIBaseDTO productInfo(@PathParam("brandId") String brand) {

        return hkapiBrandService.getAllProductsByBrand(brand);

    }
}
