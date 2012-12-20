package com.hk.api.resource;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.store.StoreProduct;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.store.StoreService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author vaibhav.adlakha
 */

@Path("/product/variant")
@Component
public class ProductVariantResource {

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String                      adminUploadsPath;

  private ProductVariantService productVariantService;
  private InventoryService inventoryService;
  private SkuService skuService;
  private StoreService storeService;

  @SuppressWarnings("unused")
private static Logger logger                    = LoggerFactory.getLogger(ProductVariantResource.class);



  @GET
  @Path("/{variantId}/price/{storeId}")
  @Produces("application/json")
  public String variantPrice(@PathParam("variantId") String variantId, @PathParam("storeId") Long storeId) {
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
      StoreProduct storeProduct;
    if(storeId !=null ){
        storeProduct=getStoreService().getStoreProductByHKVariantAndStore(getProductVariantService().getVariantById(StringUtils.trim(variantId)), getStoreService().getStoreById(storeId));
    }else{
        storeProduct=getStoreService().getStoreProductByHKVariantAndStore(getProductVariantService().getVariantById(StringUtils.trim(variantId)), getStoreService().getDefaultStore());
    }
    if(storeProduct !=null && !storeProduct.isHidden()){
        //don't get confused hkPrice field is actually the store price
      return new JSONResponseBuilder().addField("hkPrice", storeProduct.getStorePrice()).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible",visibleNumber).build();
    }else{
      return new JSONResponseBuilder().addField("hkPrice", productVariant.getHkPrice()).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible",visibleNumber).build();
    }

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

    public StoreService getStoreService() {
        if (storeService == null) {
            storeService = ServiceLocatorFactory.getService(StoreService.class);
        }
        return storeService;
    }

  public SkuService getSkuService() {
    if (skuService == null) {
      skuService = ServiceLocatorFactory.getService(SkuService.class);
    }
    return skuService;
  }

}
