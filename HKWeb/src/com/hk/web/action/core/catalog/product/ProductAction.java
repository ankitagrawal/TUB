package com.hk.web.action.core.catalog.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.MapIndia;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.content.SeoData;
import com.hk.domain.review.UserReview;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.AddressDistanceDto;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.catalog.product.ProductCountDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.SeoManager;
import com.hk.web.action.core.search.SearchAction;
import com.hk.web.filter.WebContext;

@UrlBinding("/product/{productSlug}/{productId}")
@Component
public class ProductAction extends BaseAction {

    @SuppressWarnings("unused")
    private static Logger            logger      = Logger.getLogger(ProductAction.class);

    String                           productId;
    Product                          product;
    String                           productSlug;
    List<ProductImage>               productImages;
    private List<AddressDistanceDto> addressDistanceDtos;
    SeoData                          seoData;
    String                           topCategoryUrlSlug;
    String                           allCategories;
    Affiliate                        affiliate;
    Combo                            combo;
    String                           feed;
    String                           affid;
    Double                           starRating;
    List<UserReview>                 userReviews = new ArrayList<UserReview>();

    @Session(key = HealthkartConstants.Cookie.preferredZone)
    private String                   preferredZone;
    private String                   urlFragment;

    @Autowired
    private SeoManager               seoManager;

    @Autowired
    private MenuHelper               menuHelper;
    @Autowired
    private AffiliateDao             affiliateDao;

    /*
     * @Autowired private ComboDao comboDao;
     */
    @Autowired
    private MapIndiaDao              mapIndiaDao;
    @Autowired
    private LocalityMapDao           localityMapDao;
    @Autowired
    private ProductCountDao          productCountDao;
    @Autowired
    private UserProductHistoryDao    userProductHistoryDao;
    @Autowired
    private AddressDao               addressDao;
    @Autowired
    private ProductService           productService;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + (300*1000)); // 5 min in future.
        User user = null;
        if (productId == null || StringUtils.isBlank(productId)) {
            WebContext.getResponse().setStatus(310); // redirection
            return new ForwardResolution(SearchAction.class).addParameter("query", productSlug);
        }
        try {
            combo = getBaseDao().get(Combo.class, productId);
        } catch (Exception e) {

        }
        product = getProductService().getProductById(productId);
        if (product == null) {
            WebContext.getResponse().setStatus(310); // redirection
            return new ForwardResolution(SearchAction.class).addParameter("query", productSlug);
        }

        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            if (user != null) {
                userProductHistoryDao.addToUserProductHistory(product, user);
                affiliate = affiliateDao.getAffilateByUser(user);
            }
        }

        List<Product> relatedProducts = product.getRelatedProducts();
        if (relatedProducts == null || relatedProducts.size() == 0) {
            relatedProducts = getProductService().getRelatedProducts(product);
            product.setRelatedProducts(relatedProducts);
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
                    if (addressDistanceDtos != null)
                        addressDistanceDtos = getSortedByDistanceList(addressDistanceDtos);
                }
            }
        }
        urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
        productImages = getProductService().getImagesByProductForProductMainPage(product);
        seoData = seoManager.generateSeo(productId);
        String breadcrumbUrlFragment = menuHelper.getUrlFragementFromProduct(product);
        MenuNode breadcrumbMenuNode = menuHelper.getMenuNode(breadcrumbUrlFragment);
        topCategoryUrlSlug = menuHelper.getTopCategorySlug(breadcrumbMenuNode);
        allCategories = menuHelper.getAllCategoriesString(breadcrumbMenuNode);

        if (StringUtils.isNotBlank(feed)) {
            if (feed.equals("xml")) {
                return new ForwardResolution("/pages/productFeedXml.jsp");
            }
        }
        
        
        return new ForwardResolution("/pages/product.jsp");
    }

    // User Reviews
    /*
     * starRating = productService.getStarRating(product); Page<UserReview> userReviewPage =
     * productService.getProductReviews(product, Arrays.asList(EnumReviewStatus.Published.getId()), 1, 5); if
     * (userReviewPage != null) { userReviews = userReviewPage.getList(); }
     */

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public void setProductSlug(String productSlug) {
        this.productSlug = productSlug;
    }

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

    public Double getStarRating() {
        return starRating;
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

    public String getUrlFragment() {
        return urlFragment;
    }

    public void setUrlFragment(String urlFragment) {
        this.urlFragment = urlFragment;
    }

}
