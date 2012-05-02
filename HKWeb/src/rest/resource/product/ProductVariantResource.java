package rest.resource.product;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.service.InventoryService;
import com.hk.service.ProductVariantService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.service.SkuService;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author vaibhav.adlakha
 */

@Path("/product/variant")
public class ProductVariantResource {


  private ProductVariantService productVariantService;
  private InventoryService inventoryService;
  private SkuService skuService;


  @GET
  @Path("/{variantId}/price")
  @Produces("application/json")
  public String variantPrice(@PathParam("variantId") String variantId) {
    if (StringUtils.isBlank(variantId)) {
      return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant Id is required").build();
    }
    
    ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

    if(productVariant ==null){
       return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant does not exist").build();
    }

    //boolean isVisible = !(productVariant.getDeleted() || productVariant.isOutOfStock());
    boolean isVisible = false;
    Long unbookedInventory = 0L;
    List<Sku> skuList = getSkuService().getSKUsForProductVariant(productVariant);
    if (skuList != null && !skuList.isEmpty()) {
      unbookedInventory = getInventoryService().getAvailableUnbookedInventory(skuList);
    }
    if(unbookedInventory > 0){
      isVisible = true;
    }
    int visibleNumber = isVisible ? 1: 0;
    
    return new JSONResponseBuilder().addField("hkPrice", productVariant.getHkPrice()).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible",visibleNumber).build();
  }

  public ProductVariantService getProductVariantService() {
    if (productVariantService == null) {
      productVariantService = ServiceLocatorFactory.getService(ProductVariantService.class);
    }
    return productVariantService;
  }

   public InventoryService getInventoryService() {
    if (inventoryService == null) {
      inventoryService = ServiceLocatorFactory.getService(InventoryService.class);
    }
    return inventoryService;
  }

   public SkuService getSkuService() {
    if (skuService == null) {
      skuService = ServiceLocatorFactory.getService(SkuService.class);
    }
    return skuService;
  }
}
