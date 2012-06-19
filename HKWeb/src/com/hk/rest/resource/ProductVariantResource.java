package com.hk.rest.resource;


import java.util.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.constants.core.Keys;

/**
 * @author vaibhav.adlakha
 */

@Path("/product/variant")
@Component
public class ProductVariantResource {

  private static final String SHEET_NAME_MIH_PRICING = "MIHPricing";

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String                      adminUploadsPath;

  private ProductVariantService productVariantService;
  private InventoryService inventoryService;
  private SkuService skuService;

  private static Logger logger                    = LoggerFactory.getLogger(ProductVariantResource.class);



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
    Map<String, Double> pricingMap=getMIHPricingMap();
    if(pricingMap.containsKey(variantId)){
      return new JSONResponseBuilder().addField("hkPrice", (Double)pricingMap.get(variantId)).addField("mrp", productVariant.getMarkedPrice()).addField("isVisible",visibleNumber).build();
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

  public SkuService getSkuService() {
    if (skuService == null) {
      skuService = ServiceLocatorFactory.getService(SkuService.class);
    }
    return skuService;
  }

  private Map<String,Double> getMIHPricingMap() {
    Map<String, Double> pricingMap=new HashMap<String,Double>();
    try {
        String excelFilePath = adminUploadsPath + "/mihFiles/mihPricing.xls";
      //String excelFilePath ="E:\\test\\mihpricing.xls";
      ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_MIH_PRICING);
      Iterator<HKRow> rowIterator = parser.parse();

      while (null != rowIterator && rowIterator.hasNext()) {
        HKRow curHkRow = rowIterator.next();

        int i = 0;
        while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
          pricingMap.put(StringUtils.trim(curHkRow.getColumnValue(i)), new Double(curHkRow.getColumnValue(i+1)));
          i+=2;
        }
      }
    } catch (Exception e) {
      logger.error("Exception while reading excel sheet.", e);
    }
     return pricingMap;
  }
}
