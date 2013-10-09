package com.hk.api.edge.integration.impl.service.variant;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.hk.pact.service.inventory.InventoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.api.edge.constants.ServiceEndPoints;
import com.hk.api.edge.http.HkHttpClient;
import com.hk.api.edge.http.URIBuilder;
import com.hk.api.edge.integration.pact.service.variant.HybridStoreVariantServiceFromHKR;
import com.hk.api.edge.integration.response.variant.ComboHKR;
import com.hk.api.edge.integration.response.variant.response.ComboResponseFromHKR;
import com.hk.api.edge.integration.response.variant.response.FreeVariantResponseFromHKR;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.edge.pact.service.HybridStoreVariantService;
import com.hk.edge.request.VariantPricingSyncRequest;
import com.hk.edge.request.VariantSavedSyncRequest;
import com.hk.edge.request.VariantStockSyncRequest;
import com.hk.edge.response.variant.StoreVariantBasicResponse;
import com.hk.edge.response.variant.StoreVariantBasicResponseWrapper;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.taglibs.Functions;
import com.hk.util.HKImageUtils;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HybridStoreVariantServiceImpl implements HybridStoreVariantService, HybridStoreVariantServiceFromHKR {

    private static final String   BASIC_STORE_VARIANT_SUFFIX = "oldVariant/";
    private static final String   VARIANT_STOCK              = "variant/stock/";

    @Autowired
    private LinkManager           linkManager;

    @Autowired
    private ProductService        productService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private BaseDao               baseDao;
    @Autowired
    private InventoryService inventoryService;

    @Override
    public StoreVariantBasicResponse getStoreVariantBasicDetailsFromEdge(String oldVariantId) {
        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.STORE_VARIANT + BASIC_STORE_VARIANT_SUFFIX + oldVariantId);
        StoreVariantBasicResponseWrapper variantBasicResponseWrapper = (StoreVariantBasicResponseWrapper) HkHttpClient.executeGet(builder.getWebServiceUrl(),
                StoreVariantBasicResponseWrapper.class);
        if (variantBasicResponseWrapper != null) {
            return variantBasicResponseWrapper.getStoreVariantBasic();
        }

        return null;
    }

    @Override
    public void syncStockOnEdge(VariantStockSyncRequest variantStockSyncRequest) {
        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.SYNC + VARIANT_STOCK);

        HkHttpClient.executePostForObject(builder.getWebServiceUrl(), variantStockSyncRequest.getParameters(), null);
    }

    @Override
    @Transactional
    public void syncVariantSaveFromEdge(VariantSavedSyncRequest variantSavedSyncRequest) {
        ProductVariant productVariant = getProductVariantService().getVariantById(variantSavedSyncRequest.getOldVariantId());

        if (productVariant != null) {
            productVariant.setHkPrice(Double.valueOf(((Integer) variantSavedSyncRequest.getOfferPrice()).toString()));
            productVariant.setDiscountPercent(variantSavedSyncRequest.getDiscount());


            Product product = getProductService().getProductById(variantSavedSyncRequest.getOldProductId());
            product.setCodAllowed(variantSavedSyncRequest.isCodAllowed());
            product.setJit(variantSavedSyncRequest.isJit());
            product.setMaxDays(variantSavedSyncRequest.getMaxDispatchDays());
            product.setMinDays(variantSavedSyncRequest.getMinDispatchDays());

            if (variantSavedSyncRequest.isJit()) {
                productVariant.setMarkedPrice(variantSavedSyncRequest.getMrp());
            }

            getBaseDao().save(product);
            getBaseDao().save(productVariant);
        }

    }

    @Override
    @Transactional
    public void syncPricingFromEdge(VariantPricingSyncRequest variantPricingSyncRequest) {
        ProductVariant productVariant = getProductVariantService().getVariantById(variantPricingSyncRequest.getOldVariantId());

        if (productVariant != null) {
            productVariant.setHkPrice(Double.valueOf(((Integer) variantPricingSyncRequest.getOfferPrice()).toString()));
            productVariant.setDiscountPercent(variantPricingSyncRequest.getDiscount());
            getBaseDao().save(productVariant);
        }

    }

    @Override
    public FreeVariantResponseFromHKR getFreeVariantForProductVariant(String productVariantId) {
        if (StringUtils.isBlank(productVariantId)) {
            throw new InvalidParameterException("PRODUCT_VARIANT_ID_CANNOT_BE_BLANK");
        }

        ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);
        if (productVariant != null) {
            Product freebie = Functions.showFreebieForVariant(productVariant);
            if (freebie != null) {
                FreeVariantResponseFromHKR freeVariantResponseFromHKR = new FreeVariantResponseFromHKR();

                freeVariantResponseFromHKR.setVariantId(productVariantId);
                freeVariantResponseFromHKR.setFreebieProductId(freebie.getId());
                freeVariantResponseFromHKR.setFreebieName(freebie.getName());

                return freeVariantResponseFromHKR;
            }
        }
        return null;
    }

    @Override
    public ComboResponseFromHKR getCombosForProductVariant(String productVariantId, int maxResults) {
        if (StringUtils.isBlank(productVariantId)) {
            throw new InvalidParameterException("PRODUCT_VARIANT_ID_CANNOT_BE_BLANK");
        }

        ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);
        if (productVariant != null) {
            ComboResponseFromHKR comboResponseFromHKR = new ComboResponseFromHKR();
            String param = HealthkartConstants.URL.productReferrerId;
            Product product = productVariant.getProduct();

            List<ComboHKR> comboHKRList = new ArrayList<ComboHKR>();
            List<Combo> relatedCombosForProduct = getProductService().getRelatedCombos(product);
            for (Combo relatedCombo : relatedCombosForProduct) {
                if (getProductService().isComboInStock(relatedCombo) && !relatedCombo.isDeleted() && !relatedCombo.isHidden() && !relatedCombo.isGoogleAdDisallowed()) {
                    String baseUrl = getLinkManager().getRelativeProductURL(relatedCombo, EnumProductReferrer.relatedProductsPage.getId());
                    baseUrl = baseUrl.replaceAll("/product/", "/combo/");

                    String productUrl = getProductService().getAppendedProductURL(baseUrl, param, null);

                    String sImageUrl = HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, relatedCombo.getMainImageId());
                    String mImageUrl = HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, relatedCombo.getMainImageId());

                    ComboHKR comboHKR = new ComboHKR(relatedCombo);
                    comboHKR.setUrl(productUrl);
                    comboHKR.setSLinkForImage(sImageUrl);
                    comboHKR.setMLinkForImage(mImageUrl);

                    comboHKRList.add(comboHKR);

                    if (comboHKRList.size() == maxResults) {
                        break;
                    }
                }
            }
            comboResponseFromHKR.setComboHKRList(comboHKRList);
            return comboResponseFromHKR;
        }
        return null;
    }

  @Override
  public Long getUnbookedInventoryForProductVariant(String productVariantId){
    ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);
    if(productVariant == null){
      throw new InvalidParameterException("INVALID PRODUCT VARIANT ID");
    }
     return getInventoryService().getAvailableUnBookedInventory(productVariant);
  }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public ProductService getProductService() {
        return productService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

  public InventoryService getInventoryService() {
    return inventoryService;
  }
}
