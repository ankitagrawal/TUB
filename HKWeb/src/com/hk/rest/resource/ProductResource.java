package com.hk.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.rest.pact.service.APIProductService;
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
	private APIProductService apiProductService;

	@GET
	@Path ("/all")
	@Produces ("application/json")
	public String test() {

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
	@Path ("/sync")
	@Produces (MediaType.APPLICATION_JSON)
	public String product() {
		return getApiProductService().syncContentAndDescription();
	}

	@GET
	@Path ("/sync/images")
	@Produces (MediaType.APPLICATION_JSON)
	public String product_images() {
		return getApiProductService().syncProductImages();
	}

	@GET
	@Path ("/lowresolutionimage")
	@Produces (MediaType.APPLICATION_JSON)
	public String lowResolutionImage() {
		return getApiProductService().getProductsWithLowResolutionImages();
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public APIProductService getApiProductService() {
		return apiProductService;
	}

	public void setApiProductService(APIProductService apiProductService) {
		this.apiProductService = apiProductService;
	}
}
