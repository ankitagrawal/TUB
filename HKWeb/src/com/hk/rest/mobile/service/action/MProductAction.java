package com.hk.rest.mobile.service.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.gson.JsonUtils;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.domain.MapIndia;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.content.SeoData;
import com.hk.domain.review.UserReview;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.AddressDistanceDto;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.rest.mobile.service.model.MProductJSONResponse;
import com.hk.rest.mobile.service.model.MProductVariantJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.HKImageUtils;
import com.hk.web.HealthkartResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 5, 2012
 * Time: 4:11:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mProduct")
@Component
public class MProductAction extends MBaseAction{

        @SuppressWarnings("unused")
        private static Logger logger = Logger.getLogger(MProductAction.class);

        Long superSaverImageId;
//        String productId;
        Product product;
//        String productSlug;
        List<ProductImage> productImages;
        private List<AddressDistanceDto> addressDistanceDtos;
        SeoData seoData;
        String topCategoryUrlSlug;
        String allCategories;
        Affiliate affiliate;
        Combo combo;
        String feed;
        String affid;
        Double averageRating;
        List<UserReview> userReviews = new ArrayList<UserReview>();
        Long totalReviews = 0L;
        List<Combo> relatedCombos = new ArrayList<Combo>();
        String renderComboUI = "false";
        SubscriptionProduct subscriptionProduct;
        Long productReferrerId;

        @Session(key = HealthkartConstants.Cookie.preferredZone)
        private String preferredZone;
        @Autowired
        private AffiliateDao affiliateDao;

        @Autowired
        private MapIndiaDao mapIndiaDao;
        @Autowired
        private LocalityMapDao localityMapDao;
        @Autowired
        private AddressDao addressDao;
        @Autowired
        private ProductService productService;
        @Autowired
        private SuperSaverImageService superSaverImageService;
        @Autowired
        private SubscriptionProductService subscriptionProductService;
        @Autowired
        private LinkManager linkManager;

        @DefaultHandler
        @DontValidate
        @GET
        @Path("/productDetail/")
        @Produces("application/json")

        public String productDetail(@QueryParam("productId") String productId,
                                    @Context HttpServletResponse response) {
            // getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + (300*1000)); // 5 min in future.
            User user = null;
            HealthkartResponse healthkartResponse;
            String jsonBuilder = "";
            String message = MHKConstants.STATUS_DONE;
            String status = MHKConstants.STATUS_OK;
            MProductJSONResponse productJSON = new MProductJSONResponse();
            if (productId == null || StringUtils.isBlank(productId)) {
                    message = MHKConstants.NO_SUCH_PRDCT;
                    status = MHKConstants.STATUS_ERROR;
                    logger.error( MHKConstants.NO_SUCH_PRDCT);
            }
/*
            try {
                combo = baseDao.get(Combo.class, productId);
            } catch (Exception e) {

            }
*/          try{

            product = getProductService().getProductById(productId);
            if (product == null) {
                message =  MHKConstants.NO_SUCH_PRDCT;
                status = MHKConstants.STATUS_ERROR;
            }

/*
            if (getPrincipal() != null) {
                user = getUserService().getUserById(getPrincipal().getId());
                if (user != null) {
                    userProductHistoryDao.addToUserProductHistory(product, user);
                    affiliate = affiliateDao.getAffilateByUser(user);
                }
            }
*/

            boolean isUserHkEmployee = user !=null ? user.isHKEmployee() : false;

/*
            if(!isUserHkEmployee && product.isDeleted()!=null && product.isDeleted() == true ){
                message = "No such product found";
                status = MHKConstants.STATUS_ERROR;
            }
*/

            List<Product> relatedProducts = product.getRelatedProducts();
            if (relatedProducts == null || relatedProducts.size() == 0) {
                relatedProducts = getProductService().getRelatedProducts(product);
                product.setRelatedProducts(relatedProducts);
            }
            for (Product product : relatedProducts) {
                product.setProductURL(linkManager.getRelativeProductURL(product, EnumProductReferrer.relatedProductsPage.getId()));
            }
            if (product.isProductHaveColorOptions()) {
                Integer outOfStockOrDeletedCtr = 0;
                for (ProductVariant productVariant : product.getProductVariants()) {
                    if (productVariant.isOutOfStock() || productVariant.isDeleted()) {
                        outOfStockOrDeletedCtr++;
                    }
                }
                if (product.getProductVariants().size() == outOfStockOrDeletedCtr) {
                    product.setOutOfStock(true);
                }
            }
            if (product.isService() != null && product.isService() && preferredZone != null) {
                MapIndia zone = mapIndiaDao.findByCity(preferredZone);
                Manufacturer manufacturer = product.getManufacturer();
                if (manufacturer != null) {
                    List<Address> manufacturerAddresses = addressDao.getVisibleAddresses(manufacturer.getAddresses());
                    if (!preferredZone.equals("All") && zone != null && manufacturerAddresses != null && manufacturerAddresses.size() > 0) {
                        addressDistanceDtos = localityMapDao.getClosestAddressList(zone.getLattitude(), zone.getLongitude(), 60D, 15, manufacturerAddresses);
                        if (addressDistanceDtos != null) addressDistanceDtos = getSortedByDistanceList(addressDistanceDtos);
                    }
                }
            }
            //urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
            productImages = getProductService().getImagesByProductForProductMainPage(product);
            //seoData = seoManager.generateSeo(productId);
/*
            String breadcrumbUrlFragment = menuHelper.getUrlFragementFromProduct(product);
            MenuNode breadcrumbMenuNode = menuHelper.getMenuNode(breadcrumbUrlFragment);
            topCategoryUrlSlug = menuHelper.getTopCategorySlug(breadcrumbMenuNode);
            allCategories = menuHelper.getAllCategoriesString(breadcrumbMenuNode);
*/

/*
            if (StringUtils.isNotBlank(feed)) {
                if (feed.equals("xml")) {
                    return new ForwardResolution("/pages/productFeedXml.jsp");
                }
            }

            if(product.isSubscribable()){
                subscriptionProduct= subscriptionProductService.findByProduct(product);
            }

            //User Reviews
            totalReviews = productService.getAllReviews(product, Arrays.asList(EnumReviewStatus.Published.getId()));
            if (totalReviews != null && totalReviews > 0) {
                averageRating = getProductService().getAverageRating(product);
                Page userReviewPage = getProductService().getProductReviewsForCustomer(product, Arrays.asList(EnumReviewStatus.Published.getId()), 1, 5);
                if (userReviewPage != null) {
                    userReviews = userReviewPage.getList();
                }
            }

            //Related Combos
            List<Combo> relatedCombosForProduct = getProductService().getRelatedCombos(product);
            for (Combo relatedCombo : relatedCombosForProduct) {
                if (getProductService().isComboInStock(relatedCombo)) {
                    relatedCombos.add(relatedCombo);
                    relatedCombo.setProductURL(linkManager.getRelativeProductURL(relatedCombo, ProductReferrerMapper.getProductReferrerid(EnumProductReferrer.relatedProductsPage.getName())));
                    if (relatedCombos.size() == 6) {
                        break;
                    }
                }

            }

            if (combo != null) {
                List<SuperSaverImage> superSaverImages = getSuperSaverImageService().getSuperSaverImages(product, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
                String directTo;
                if (superSaverImages == null || superSaverImages.isEmpty()) {
                    directTo = "product.jsp";
                } else {
                    SuperSaverImage latestSuperSaverImage = superSaverImages.get(superSaverImages.size() - 1);
                    superSaverImageId = latestSuperSaverImage.getId();
                    directTo = "combo.jsp";
                }
            }
*/
           
                if(null!=product.getMainImageId())
                	productJSON.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize,product.getMainImageId(),false));
                else
                	productJSON.setImageUrl(getImageUrl()+product.getId()+MHKConstants.IMAGETYPE);
                productJSON.setAmazonProduct(product.getAmazonProduct());
                productJSON.setBrand(product.getBrand());
                productJSON.setCategoriesPipeSeparated(product.getCategoriesPipeSeparated());
                productJSON.setCodAllowed(product.getCodAllowed());
                productJSON.setDeleted(product.getDeleted());
                productJSON.setDescription(product.getDescription());
                productJSON.setDropShipping(product.getDropShipping());
                productJSON.setGoogleAdDisallowed(product.getGoogleAdDisallowed());
                productJSON.setHidden(product.getHidden());
                productJSON.setId(product.getId());
                productJSON.setJit(product.getJit());
                productJSON.setKeywords(product.getKeywords());
                productJSON.setMainImageId(product.getMainImageId());
                productJSON.setMaxDays(product.getMaxDays());
                productJSON.setMinDays(product.getMinDays());
                productJSON.setOrderRanking(product.getOrderRanking());
                productJSON.setOutOfStock(product.getOutOfStock());
                productJSON.setOverview(product.getOverview());
                productJSON.setPackType(product.getPackType());
                productJSON.setName(product.getName());
              //productJSON.setPrimaryCategory(product.getPrimaryCategory());
                productJSON.setProductHaveColorOptions(product.getProductHaveColorOptions());
                productJSON.setProductURL(product.getProductURL());
                productJSON.setService(product.getService());
                productJSON.setSupplier(product.getSupplier().getName());
                productJSON.setThumbUrl(product.getThumbUrl());
                productJSON.setVideoEmbedCode(product.getVideoEmbedCode());

                MProductVariantJSONResponse variantJSONResponse;
                List<MProductVariantJSONResponse> productVariantJSONResponseList = new ArrayList<MProductVariantJSONResponse>();
                if(null!=product.getProductVariants()&&!product.getProductVariants().isEmpty()){
                    for(ProductVariant productChoice:product.getProductVariants()){
                        variantJSONResponse = new MProductVariantJSONResponse();
                        if(null!=productChoice.getAffiliateCategory())
                        variantJSONResponse.setAffiliateCategory(productChoice.getAffiliateCategory().getAffiliateCategoryName());
                        variantJSONResponse.setProductId(product.getId());
                        if(null!=productChoice.getB2bPrice())
                        variantJSONResponse.setB2bPrice(priceFormat.format(productChoice.getB2bPrice()));
                        variantJSONResponse.setBreadth(productChoice.getBreadth());
                        variantJSONResponse.setBufferTime(productChoice.getBufferTime());
                        variantJSONResponse.setClearanceSale(productChoice.getClearanceSale());
                        variantJSONResponse.setColorHex(productChoice.getColorHex());
                        variantJSONResponse.setConsumptionTime(productChoice.getConsumptionTime());
                        if(null!=productChoice.getCostPrice())
                        variantJSONResponse.setCostPrice(priceFormat.format(productChoice.getCostPrice()));
                        variantJSONResponse.setCutOffInventory(productChoice.getCutOffInventory());
                        variantJSONResponse.setDeleted(productChoice.getDeleted());
	                    variantJSONResponse.setDiscountPercent(Double.valueOf(decimalFormat.format(productChoice.getDiscountPercent()*100)));
                        variantJSONResponse.setFollowingAvailableDate(productChoice.getFollowingAvailableDate());
                        variantJSONResponse.setHeight(productChoice.getHeight());
                        if(null!=productChoice.getHkPrice())
                        variantJSONResponse.setHkPrice(priceFormat.format(productChoice.getHkPrice()));
                        variantJSONResponse.setId(productChoice.getId());
                        variantJSONResponse.setLeadTime(productChoice.getLeadTime());
                        variantJSONResponse.setLeadTimeFactor(productChoice.getLeadTimeFactor());
                        variantJSONResponse.setLength(productChoice.getLength());
                        variantJSONResponse.setMainImageId(productChoice.getMainImageId());
                        if(null!=productChoice.getMarkedPrice())
                        variantJSONResponse.setMarkedPrice(priceFormat.format(productChoice.getMarkedPrice()));
                        variantJSONResponse.setOrderRanking(productChoice.getOrderRanking());
                        variantJSONResponse.setOtherRemark(productChoice.getOtherRemark());
                        variantJSONResponse.setOutOfStock(productChoice.getOutOfStock());
                        if(null!=productChoice.getPaymentType())
                        variantJSONResponse.setPaymentType(productChoice.getPaymentType().getName());
                        variantJSONResponse.setPostpaidAmount(productChoice.getPostpaidAmount());
                        variantJSONResponse.setQty(productChoice.getQty());
                        if(null!=productChoice.getServiceType())
                        variantJSONResponse.setServiceType(productChoice.getServiceType().getName());
                        if(null!=productChoice.getShippingAddPrice())
                        variantJSONResponse.setShippingAddPrice(priceFormat.format(productChoice.getShippingAddPrice()));
                        variantJSONResponse.setShippingAddQty(productChoice.getShippingAddQty());
                        if(null!=productChoice.getShippingBasePrice())
                        variantJSONResponse.setShippingBasePrice(priceFormat.format(productChoice.getShippingBasePrice()));
                        variantJSONResponse.setShippingBaseQty(productChoice.getShippingBaseQty());
                        variantJSONResponse.setSupplierCode(productChoice.getSupplierCode());
                        variantJSONResponse.setUpc(productChoice.getUpc());
                        if(null!=productChoice.getVariantConfig())
                        variantJSONResponse.setVariantConfig(productChoice.getVariantConfig().getName());
                        variantJSONResponse.setVariantName(productChoice.getVariantName());
                        variantJSONResponse.setWeight(productChoice.getWeight());
                        variantJSONResponse.setExtraOptions(productChoice.getExtraOptionsPipeSeparated());
                        variantJSONResponse.setColourOptions(productChoice.getColorOptionsValue());
                        variantJSONResponse.setImageUrl(getImageUrl()+product.getId()+MHKConstants.IMAGETYPE);
                        variantJSONResponse.setProductOptions(productChoice.getProductOptions());
                        productVariantJSONResponseList.add(variantJSONResponse);
                    }
                }
                productJSON.setProductVariants(productVariantJSONResponseList);


            }catch(Exception e) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, productJSON);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);
        addHeaderAttributes(response);

        return jsonBuilder;

        }

        public Resolution productBanner() {
            affiliate = affiliateDao.getAffiliateByCode(affid);
            return new ForwardResolution("/pages/affiliate/productBanner.jsp");
        }

        public List<AddressDistanceDto> getSortedByDistanceList(List<AddressDistanceDto> addressDistanceDtos) {
            Collections.sort(addressDistanceDtos, new DistanceComparator());
            return addressDistanceDtos;
        }

        public class DistanceComparator implements Comparator<AddressDistanceDto> {
            public int compare(AddressDistanceDto dist1, AddressDistanceDto dist2) {
                if (dist1.getDistance() != null && dist2.getDistance() != null) {
                    return dist1.getDistance().compareTo(dist2.getDistance());
                }
                return -1;
            }
        }

/*
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
*/

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

/*
        public String getProductSlug() {
            return productSlug;
        }

        public void setProductSlug(String productSlug) {
            this.productSlug = productSlug;
        }
*/

        public List<ProductImage> getProductImages() {
            return productImages;
        }

        public void setProductImages(List<ProductImage> productImages) {
            this.productImages = productImages;
        }

        public SeoData getSeoData() {
            return seoData;
        }

        public void setSeoData(SeoData seoData) {
            this.seoData = seoData;
        }

        public String getTopCategoryUrlSlug() {
            return topCategoryUrlSlug;
        }

        public void setTopCategoryUrlSlug(String topCategoryUrlSlug) {
            this.topCategoryUrlSlug = topCategoryUrlSlug;
        }

        public String getAllCategories() {
            return allCategories;
        }

        public void setAllCategories(String allCategories) {
            this.allCategories = allCategories;
        }

        public Combo getCombo() {
            return combo;
        }

        public void setCombo(Combo combo) {
            this.combo = combo;
        }

        public String getFeed() {
            return feed;
        }

        public void setFeed(String feed) {
            this.feed = feed;
        }

        public Affiliate getAffiliate() {
            return affiliate;
        }

        public void setAffiliate(Affiliate affiliate) {
            this.affiliate = affiliate;
        }

        public List<AddressDistanceDto> getAddressDistanceDtos() {
            return addressDistanceDtos;
        }

        public void setAddressDistanceDtos(List<AddressDistanceDto> addressDistanceDtos) {
            this.addressDistanceDtos = addressDistanceDtos;
        }

        public String getPreferredZone() {
            return preferredZone;
        }

        public void setPreferredZone(String preferredZone) {
            this.preferredZone = preferredZone;
        }

        public String getAffid() {
            return affid;
        }

        public void setAffid(String affid) {
            this.affid = affid;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

        public List<UserReview> getUserReviews() {
            return userReviews;
        }

        public ProductService getProductService() {
            return productService;
        }

        public void setProductService(ProductService productService) {
            this.productService = productService;
        }

      public SubscriptionProductService getSubscriptionProductService() {
        return subscriptionProductService;
      }

      public void setSubscriptionProductService(SubscriptionProductService subscriptionProductService) {
        this.subscriptionProductService = subscriptionProductService;
      }

      public SubscriptionProduct getSubscriptionProduct() {
        return subscriptionProduct;
      }

      public void setSubscriptionProduct(SubscriptionProduct subscriptionProduct) {
        this.subscriptionProduct = subscriptionProduct;
      }

        public Long getTotalReviews() {
            return totalReviews;
        }

        public List<Combo> getRelatedCombos() {
            return relatedCombos;
        }

        public void setRelatedCombos(List<Combo> relatedCombos) {
            this.relatedCombos = relatedCombos;
        }

        public Long getSuperSaverImageId() {
            return superSaverImageId;
        }

        public void setSuperSaverImageId(Long superSaverImageId) {
            this.superSaverImageId = superSaverImageId;
        }

        public Long getProductReferrerId() {
            return productReferrerId;
        }

        public void setProductReferrerId(Long productReferrerId) {
            this.productReferrerId = productReferrerId;
        }

        public SuperSaverImageService getSuperSaverImageService() {
            return superSaverImageService;
        }
    }