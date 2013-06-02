package com.hk.rest.mobile.service.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.akube.framework.gson.JsonUtils;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.rest.mobile.service.model.MCatalogJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.HKImageUtils;
import com.hk.web.HealthkartResponse;

@Path("/mOffers")
    @Component
    public class MOfferAction extends MBaseAction{
        List<SuperSaverImage> superSaverImages;
        List<String> categories;
        List<String> brands;
        private Integer defaultPerPage = 10;
        Page superSaverPage;

        @Autowired
        private SuperSaverImageService superSaverImageService;

        @DefaultHandler
        @GET
        @Path("/offers/")
        @Produces("application/json")
        public String offers(@Context HttpServletResponse response) {
            List<MCatalogJSONResponse> catalogList = new ArrayList<MCatalogJSONResponse>();
            HealthkartResponse healthkartResponse;
            String jsonBuilder = "";
            String message = MHKConstants.STATUS_DONE;
            String status = MHKConstants.STATUS_OK;
            Map statusMap = new HashMap<String,Object>();

            superSaverImages = getSuperSaverImageService().getSuperSaverImages();
           // superSaverImages = superSaverPage.getList();
            //return new ForwardResolution("/pages/superSavers.jsp");
            MCatalogJSONResponse catalogResponse = new MCatalogJSONResponse();
            ArrayList <String> paths = new ArrayList<String>();
            for(SuperSaverImage image:superSaverImages){
                Product product = image.getProduct();
                paths.add(HKImageUtils.getS3SuperSaverImageUrl(EnumImageSize.Original, image.getId()));
                if(null!=product){
                catalogResponse = populateCatalogResponse(product, catalogResponse);
                catalogResponse.setProductURL(product.getProductURL());
                catalogList.add(catalogResponse);
                }
            }
           // statusMap.put("offers",catalogList);
            healthkartResponse = new HealthkartResponse(status, message, paths);
            jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

            addHeaderAttributes(response);

            return jsonBuilder;
        }

        public List<SuperSaverImage> getSuperSaverImages() {
            return superSaverImages;
        }

        public void setSuperSaverImages(List<SuperSaverImage> superSaverImages) {
            this.superSaverImages = superSaverImages;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public List<String> getBrands() {
            return brands;
        }

        public void setBrands(List<String> brands) {
            this.brands = brands;
        }

        public int getPerPageDefault() {
            return defaultPerPage;
        }

        public int getPageCount() {
            return superSaverPage == null ? 0 : superSaverPage.getTotalPages();
        }

        public int getResultCount() {
            return superSaverPage == null ? 0 : superSaverPage.getTotalResults();
        }

        public Set<String> getParamSet() {
            HashSet<String> params = new HashSet<String>();
            params.add("categories");
            params.add("brands");
            return params;
        }

        public SuperSaverImageService getSuperSaverImageService() {
            return superSaverImageService;
        }
    private MCatalogJSONResponse populateCatalogResponse(Product product, MCatalogJSONResponse catalogJSONResponse) {
        if(null!=product.getProductURL())
            catalogJSONResponse.setProductURL(product.getProductURL());
        if(null!=product.getSlug())
            catalogJSONResponse.setProductSlug(product.getSlug());
        if (null != product.getId ()){
            if(null!=product.getMainImageId())
                catalogJSONResponse.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize,product.getMainImageId()));
            else
                catalogJSONResponse.setImageUrl(getImageUrl()+product.getId()+MHKConstants.IMAGETYPE);
        }
        if (null != product.getManufacturer())
            catalogJSONResponse.setManufacturer(product.getManufacturer().getName());
        if (null != product.getBrand())
            catalogJSONResponse.setBrand(product.getBrand());
        if (null != product.isCodAllowed())
            catalogJSONResponse.setCodAllowed(product.isCodAllowed());
        if (null != product.getDeleted())
            catalogJSONResponse.setDeleted(product.getDeleted());
        if (null != product.getDescription())
            catalogJSONResponse.setDescription(product.getDescription());
        catalogJSONResponse.setDropShipping(product.getDropShipping());
        if (null != product.getGoogleAdDisallowed())
            catalogJSONResponse.setGoogleAdDisallowed(product.getGoogleAdDisallowed());
        if (null != product.getId())
            catalogJSONResponse.setId(product.getId());
        if (null != product.getJit())
            catalogJSONResponse.setJit(product.getJit());
        if (null != product.getMaxDays())
            catalogJSONResponse.setMaxDays(product.getMaxDays());
        if (null != product.getMinDays())
            catalogJSONResponse.setMinDays(product.getMinDays());
        if (null != product.getName())
            catalogJSONResponse.setName(product.getName());
        if (null != product.getOrderRanking())
            catalogJSONResponse.setOrderRanking(product.getOrderRanking());
        if (null != product.isOutOfStock())
            catalogJSONResponse.setOutOfStock(product.isOutOfStock());
        if (null != product.getOverview())
            catalogJSONResponse.setOverview(product.getOverview());
        if (null != product.getProductHaveColorOptions())
            catalogJSONResponse.setProductHaveColorOptions(product.getProductHaveColorOptions());
        if (null != product.getService())
            catalogJSONResponse.setService(product.getService());
        if (null != product.getThumbUrl())
            catalogJSONResponse.setThumbUrl(product.getThumbUrl());
        if (null != product.getMinimumMRPProducVariant().getHkPrice())
            catalogJSONResponse.setHkPrice(priceFormat.format(product.getMinimumMRPProducVariant().getHkPrice()));
        if (null != product.getMinimumMRPProducVariant().getMarkedPrice())
            catalogJSONResponse.setMarkedPrice(priceFormat.format(product.getMinimumMRPProducVariant().getMarkedPrice()));
        if (null != product.getMinimumMRPProducVariant().getDiscountPercent())
            catalogJSONResponse.setDiscountPercentage(Double.valueOf(decimalFormat.format(product.getMinimumMRPProducVariant().getDiscountPercent()*100)));
        if(null!=product.getMainImageId())
            catalogJSONResponse.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.Original,product.getMainImageId()));
        else
            catalogJSONResponse.setImageUrl(getImageUrl()+product.getId()+MHKConstants.IMAGETYPE);
        return catalogJSONResponse;
    }
        
    }
