package com.hk.web.action.core.catalog.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.shiro.PrincipalImpl;
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

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.constants.review.EnumReviewStatus;
import com.hk.domain.MapIndia;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.domain.content.SeoData;
import com.hk.domain.review.UserReview;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.AddressDistanceDto;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.pact.service.analytics.TrafficAndUserBrowsingService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;
import com.hk.pact.service.image.ProductImageService;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.service.search.ProductSearchService;
import com.hk.util.SeoManager;
import com.hk.web.action.core.search.SearchAction;
import com.hk.web.filter.WebContext;

@UrlBinding("/product/{productSlug}/{productId}")
@Component
public class ProductAction extends BaseAction {
    @SuppressWarnings("unused")
    private static Logger              logger        = Logger.getLogger(ProductAction.class);

    Long                               superSaverImageId;
    String                             productId;
    Product                            product;
    String                             productSlug;
    List<ProductImage>                 productImages;
    private List<AddressDistanceDto>   addressDistanceDtos;
    SeoData                            seoData;
    String                             topCategoryUrlSlug;
    String                             allCategories;
    Affiliate                          affiliate;
    Combo                              combo;
    String                             feed;
    String                             affid;
    Double                             averageRating;
    List<UserReview>                   userReviews   = new ArrayList<UserReview>();
    Long                               totalReviews  = 0L;
    List<Combo>                        relatedCombos = new ArrayList<Combo>();
    String                             renderComboUI = "false";
    SubscriptionProduct                subscriptionProduct;

    Long                               productReferrerId;  // Same as HealthkartConstants.URL.productReferrerId
    String                             productPosition;  // Same as HealthkartConstants.URL.productPosition
    boolean                            isOutOfStockPage = false;
    String                             menuNodeUrlFragment;

    @Session(key = HealthkartConstants.Cookie.preferredZone)
    private String                     preferredZone;
    private String                     urlFragment;

    @Autowired
    private SeoManager                 seoManager;

    @Autowired
    private MenuHelper                 menuHelper;
    @Autowired
    private AffiliateDao               affiliateDao;

    @Autowired
    private MapIndiaDao                mapIndiaDao;
    @Autowired
    private LocalityMapDao             localityMapDao;
    @Autowired
    private AddressDao                 addressDao;
    @Autowired
    private ProductService             productService;
    @Autowired
    private SuperSaverImageService     superSaverImageService;
    @Autowired
    private SubscriptionProductService subscriptionProductService;
    @Autowired
    private LinkManager                linkManager;
    @Autowired
    ProductImageService                productImageService;
    private ProductVariant             validTryOnProductVariant;
    @Autowired
    TrafficAndUserBrowsingService      trafficAndUserBrowsingService;

   @Autowired
   ProductSearchService productSearchService;

  @Override
    public PrincipalImpl getPrincipal() {
        return super.getPrincipal();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        // getContext().getResponse().setDateHeader("Expires", System.currentTimeMillis() + (300*1000)); // 5 min in
        // future.
        User user = null;
        if (productId == null || StringUtils.isBlank(productId)) {
            WebContext.getResponse().setStatus(310); // redirection
            return new ForwardResolution(SearchAction.class).addParameter("query", productSlug);
        }

        product = getProductService().getProductById(productId);

        if (product != null) {
            if (product instanceof Combo) {
                combo = (Combo) product;
            }
            // Save Browsing
            trafficAndUserBrowsingService.saveBrowsingHistory(product, WebContext.getRequest(), productReferrerId, productPosition);
            // In case of search log the first clicked position in search log table
            if (productReferrerId != null && productReferrerId.equals(EnumProductReferrer.searchPage.getId())) {
              productSearchService.updatePositionInSearchLog(productPosition);
            }
        } else {
            WebContext.getResponse().setStatus(310); // redirection
            return new ForwardResolution(SearchAction.class).addParameter("query", productSlug);
        }

      // code to check if this page has all variants out of stock. this is used to fire a GA custom event
      if (!product.isDeleted()) {
        if (product.getProductVariants() != null && product.getProductVariants().size() > 1) {
          // multiple products
          for (ProductVariant productVariant: product.getProductVariants()) {
            if (!productVariant.isDeleted() && productVariant.isOutOfStock()) {
              isOutOfStockPage = true;
              break;
            }
          }
        } else if (product.getProductVariants() != null) {
          ProductVariant productVariant = product.getProductVariants().get(0);
          if (!productVariant.isDeleted() && productVariant.isOutOfStock()) {
            isOutOfStockPage = true;
          }
        }
      }

        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            if (user != null) {
                affiliate = affiliateDao.getAffilateByUser(user);
            }
        }

        boolean isUserHkEmployee = user != null ? user.isHKEmployee() : false;

        if (!isUserHkEmployee && product.isDeleted() != null && product.isDeleted() == true) {
            WebContext.getResponse().setStatus(404); // redirection
            return new ForwardResolution("/pages/error/noPage.html");
        }

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
                    if (addressDistanceDtos != null)
                        addressDistanceDtos = getSortedByDistanceList(addressDistanceDtos);
                }
            }
        }
        urlFragment = getContext().getRequest().getRequestURI().replaceAll(getContext().getRequest().getContextPath(), "");
        productImages = productImageService.searchProductImages(null, product, null, true, false);
        seoData = seoManager.generateSeo(productId);
        menuNodeUrlFragment = menuHelper.getUrlFragementFromProduct(product);
        MenuNode breadcrumbMenuNode = menuHelper.getMenuNode(menuNodeUrlFragment);
        topCategoryUrlSlug = menuHelper.getTopCategorySlug(breadcrumbMenuNode);
        allCategories = menuHelper.getAllCategoriesString(breadcrumbMenuNode);

        if (StringUtils.isNotBlank(feed)) {
            if (feed.equals("xml")) {
                return new ForwardResolution("/pages/productFeedXml.jsp");
            }
        }

        if (product.isSubscribable()) {
            subscriptionProduct = subscriptionProductService.findByProduct(product);
        }

        validTryOnProductVariant = productService.validTryOnProductVariant(product);

        // User Reviews
        totalReviews = productService.getAllReviews(product, Arrays.asList(EnumReviewStatus.Published.getId()));
        if (totalReviews != null && totalReviews > 0) {
            averageRating = getProductService().getAverageRating(product);
            Page userReviewPage = getProductService().getProductReviewsForCustomer(product, Arrays.asList(EnumReviewStatus.Published.getId()), 1, 5);
            if (userReviewPage != null) {
                userReviews = userReviewPage.getList();
            }
        }

        // Related Combos
        List<Combo> relatedCombosForProduct = getProductService().getRelatedCombos(product);
        for (Combo relatedCombo : relatedCombosForProduct) {
            if (getProductService().isComboInStock(relatedCombo)) {
                relatedCombos.add(relatedCombo);
                relatedCombo.setProductURL(linkManager.getRelativeProductURL(relatedCombo,
                        EnumProductReferrer.relatedProductsPage.getId()));
                if (relatedCombos.size() == 6) {
                    break;
                }
            }

        }

        if (combo == null) {
            return new ForwardResolution("/pages/product.jsp");
        } else {
            List<SuperSaverImage> superSaverImages = getSuperSaverImageService().getSuperSaverImages(product, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
            String directTo;
            if (superSaverImages == null || superSaverImages.isEmpty()) {
                directTo = "product.jsp";
            } else {
                SuperSaverImage latestSuperSaverImage = superSaverImages.get(superSaverImages.size() - 1);
                superSaverImageId = latestSuperSaverImage.getId();
                directTo = "combo.jsp";
            }

            return new ForwardResolution("/pages/" + directTo);
        }
    }

    public Resolution productBanner() {
        if (product != null) {
            if (product instanceof Combo) {
                combo = (Combo) product;
            }
        }
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

    public String getUrlFragment() {
        return urlFragment;
    }

    public void setUrlFragment(String urlFragment) {
        this.urlFragment = urlFragment;
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

    public String getProductPosition() {
      return productPosition;
    }

    public void setProductPosition(String productPosition) {
      this.productPosition = productPosition;
    }

    public SuperSaverImageService getSuperSaverImageService() {
        return superSaverImageService;
    }

    public ProductVariant getValidTryOnProductVariant() {
        return validTryOnProductVariant;
    }

    public void setValidTryOnProductVariant(ProductVariant validTryOnProductVariant) {
        this.validTryOnProductVariant = validTryOnProductVariant;
    }

  public boolean isOutOfStockPage() {
    return isOutOfStockPage;
  }

  public void setOutOfStockPage(boolean outOfStockPage) {
    isOutOfStockPage = outOfStockPage;
  }

  public String getMenuNodeUrlFragment() {
    return menuNodeUrlFragment;
  }

  public void setMenuNodeUrlFragment(String menuNodeUrlFragment) {
    this.menuNodeUrlFragment = menuNodeUrlFragment;
  }
}
