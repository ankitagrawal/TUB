package com.hk.api.resource;


import com.google.gson.Gson;
import com.hk.api.pact.service.HKAPIProductService;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumUnitProcessedStatus;
import com.hk.domain.api.HKAPIForeignBookingResponseInfo;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.ForeignSkuItemCLI;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.store.StoreProduct;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.service.core.WarehouseService ;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vaibhav.adlakha
 */

@Path("/product/variant")
@Component
public class ProductVariantResource {

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    private ProductVariantService productVariantService;
    private InventoryService inventoryService;
    private SkuService skuService;
    private StoreService storeService;
    private WarehouseService warehouseService;
    private InventoryHealthService inventoryHealthService;
    private SkuItemLineItemService skuItemLineItemService;

    @Autowired
    private HKAPIProductService hkapiProductService;

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(ProductVariantResource.class);


    @GET
    @Path("/{variantId}/price/{storeId}")
    @Produces("application/json")
    public String variantPrice(@PathParam("variantId") String variantId, @PathParam("storeId") Long storeId) {
        if (StringUtils.isBlank(variantId)) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant Id is required").build();
        }

        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

        if (productVariant == null) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant does not exist").build();
        }

        //boolean isVisible = !(productVariant.getDeleted() || productVariant.isOutOfStock());
        boolean isVisible = false;
        Long unbookedInventory = 0L;
        unbookedInventory = getInventoryService().getAllowedStepUpInventory(productVariant);

        if (unbookedInventory > 0) {
            isVisible = true;
        }
        int visibleNumber = isVisible ? 1 : 0;
        StoreProduct storeProduct;
        if (storeId != null) {
            storeProduct = getStoreService().getStoreProductByHKVariantAndStore(getProductVariantService().getVariantById(StringUtils.trim(variantId)), getStoreService().getStoreById(storeId));
        } else {
            storeProduct = getStoreService().getStoreProductByHKVariantAndStore(getProductVariantService().getVariantById(StringUtils.trim(variantId)), getStoreService().getDefaultStore());
        }
        if (storeProduct != null && !storeProduct.isHidden()) {
            //don't get confused hkPrice field is actually the store price
            return new JSONResponseBuilder().addField("hkPrice", storeProduct.getStorePrice()).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible", visibleNumber).build();
        } else {
            return new JSONResponseBuilder().addField("hkPrice", productVariant.getHkPrice()).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible", visibleNumber).build();
        }

    }


    @GET
    @Path("/unbookedInventory/{variantId}")
    @Produces("application/json")
    public String getUnbookedInventory(@PathParam("variantId") String variantId) {
        if (StringUtils.isBlank(variantId)) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant Id is required").build();
        }

        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

        if (productVariant == null) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant does not exist").build();
        }
        Long unbookedQtyAqua = getInventoryService().getAvailableUnBookedInventory(productVariant);

        return new JSONResponseBuilder().addField("variantId", variantId).addField("qty", unbookedQtyAqua).build();

    }


    @GET
    @Path("/{variantId}/inventoryhealthcheck")
    @Produces("application/json")
    public String variantPrice(@PathParam("variantId") String variantId) {
        if (StringUtils.isBlank(variantId)) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant Id is required").build();
        }
        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
        if (productVariant == null) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant does not exist").build();
        }
        logger.debug("Aqua inventoryhealthcheck called by Bright for Variant = " + variantId);
        getInventoryService().checkInventoryHealth(productVariant);

        return new JSONResponseBuilder().addField("outofstock", productVariant.isOutOfStock()).build();

    }


    @GET
    @Path("/skuInfo/{variantId}/{tinPrefix}")
    @Produces("application/json")
    public String getSKUInfo(@PathParam("variantId") String variantId, @PathParam("tinPrefix") String tinPrefix) {
        if (StringUtils.isBlank(variantId)) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant Id is required").build();
        }

        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

        if (productVariant == null) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Variant does not exist").build();
        }

        List<Warehouse> whList = getWarehouseService().findWarehousesByPrefix(tinPrefix);
        Sku aquaSku = getSkuService().getSKU(productVariant,whList.get(0)) ;

        return new JSONResponseBuilder().addField("variantId", variantId).addField("warehouseId", aquaSku.getId()).build();

    }


  @POST
  @Path("/updateFreezedInventory/")
  @Produces("application/json")
  public String updateFreezedInventory(List<HKAPIForeignBookingResponseInfo> hKAPIForeignBookingResponseInfos) {
    Boolean inventoryUpdated = Boolean.FALSE;
    Gson gson = new Gson();
    Long baseOrderId = null;

    if (hKAPIForeignBookingResponseInfos != null && hKAPIForeignBookingResponseInfos.size() > 0) {
      baseOrderId = hKAPIForeignBookingResponseInfos.get(0).getFboId();
      for (HKAPIForeignBookingResponseInfo info : hKAPIForeignBookingResponseInfos) {
        getInventoryHealthService().freezeInventoryForAB(info);
      }
      getSkuItemLineItemService().removeRefusedFsicli(hKAPIForeignBookingResponseInfos);
      inventoryUpdated = Boolean.TRUE;
    }
    String returnVal = gson.toJson(inventoryUpdated);
    return returnVal;

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

    public WarehouseService getWarehouseService() {
        if (warehouseService == null) {
            warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        }
        return warehouseService;
    }

  public InventoryHealthService getInventoryHealthService() {
    if (inventoryHealthService == null) {
      inventoryHealthService = ServiceLocatorFactory.getService(InventoryHealthService.class);
    }
    return inventoryHealthService;
  }

  public SkuItemLineItemService getSkuItemLineItemService() {
    if (skuItemLineItemService == null) {
      skuItemLineItemService = ServiceLocatorFactory.getService(SkuItemLineItemService.class);
    }
    return skuItemLineItemService;
  }
}
