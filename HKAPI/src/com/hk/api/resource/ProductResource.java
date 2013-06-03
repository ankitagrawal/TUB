package com.hk.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.pact.service.HKAPIProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.ProductService;
import com.hk.service.ServiceLocatorFactory;

@Path ("/product")
@Component
public class ProductResource {

	@Autowired
	private ProductService productService;
	@Autowired
	private HKAPIProductService hkapiProductService;

	@GET
	@Path ("/all")
	@Produces ("application/json")
	public String test() {

		@SuppressWarnings("unused")
        ProductService testService = ServiceLocatorFactory.getService(ProductService.class);
		Product product = getProductService().getProductById("NUT304");
		return new String(product.getName() + ":" + product.getBrand());
	}

	@GET
	@Path ("/{productId}")
	@Produces (MediaType.APPLICATION_JSON)
	public String product(@PathParam ("productId") String productId) {

		Product product = getProductService().getProductById(productId);
		product.getProductVariants();
		Gson gson = JsonUtils.getGsonDefault();
		return gson.toJson(product);
	}

    @GET
    @Path ("/{productId}/info")
    @Produces (MediaType.APPLICATION_JSON)
    public HKAPIBaseDTO productInfo(@PathParam ("productId") String productId) {

        return hkapiProductService.getProductDetails(productId);
    }

	@GET
	@Path ("/sync")
	@Produces (MediaType.APPLICATION_JSON)
	public String product() {
		return getHkapiProductService().syncContentAndDescription();
	}

	@GET
	@Path ("/sync/images")
	@Produces (MediaType.APPLICATION_JSON)
	public String product_images() {
		return getHkapiProductService().syncProductImages();
	}

    @GET
    @Path("/ooshiddendeleted/all")
    @Produces (MediaType.APPLICATION_JSON)
    public HKAPIBaseDTO getOOSHiddenDeletedProducts() {
        return getHkapiProductService().getOOSHiddenDeletedProducts();
    }

	@GET
	@Path ("/lowresolutionimage")
	@Produces (MediaType.APPLICATION_JSON)
	public String lowResolutionImage() {
		return getHkapiProductService().getProductsWithLowResolutionImages();
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public HKAPIProductService getHkapiProductService() {
		return hkapiProductService;
	}

	public void setHkapiProductService(HKAPIProductService hkapiProductService) {
		this.hkapiProductService = hkapiProductService;
	}
}
