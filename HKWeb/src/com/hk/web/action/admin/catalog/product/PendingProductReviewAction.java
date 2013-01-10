package com.hk.web.action.admin.catalog.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.review.EnumReviewStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.review.UserReview;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.review.ReviewDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.review.ReviewService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure (hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS}, authActionBean = AdminPermissionAction.class)
public class PendingProductReviewAction extends BasePaginatedAction {
    private static Logger    logger               = LoggerFactory.getLogger(PendingProductReviewAction.class);
    private String           product;
    private Boolean          saveReview;
    private Long             reviewId;
    private Page             productReviewPage;
    private List<UserReview> productReviews       = new ArrayList<UserReview>();
    private Integer          defaultPerPage       = 10;
    private UserReview       review;
    private ReviewStatus     reviewStatus;
    @Autowired
    private ReviewService    reviewService;
    @Autowired
    private ReviewDao        reviewDao;
    @Autowired
    private ProductDao       productDao;
    @Autowired
    private UserService userService;

    @DefaultHandler
    public Resolution pre() {
      Product productObj = null;
      List<Long> reviewStatusList = new ArrayList<Long>();
      try {
        if (reviewStatus != null) {
          reviewStatusList.add(reviewStatus.getId());
        } else {
          reviewStatusList.addAll(EnumReviewStatus.getAllStatus());
        }

        //Checking if entered ProductId is valid or not.
        if (product != null) {
          productObj = productDao.get(Product.class,product);
          if (productObj == null) {
            addRedirectAlertMessage(new SimpleMessage("Entered product id is invalid."));
          }
        }

        productReviewPage = reviewService.getProductReviewsForAdmin(productObj, reviewStatusList, getPageNo(), getPerPage());
        if (productReviewPage != null) {
          productReviews = productReviewPage.getList();
        }

        //Check for an empty list.
        if (productReviews.size() == 0) {
          addRedirectAlertMessage(new SimpleMessage("Sorry ! No review exists."));
        }
      }
      catch (Exception ex) {
        logger.error("Exception Occurred in pre() of PendingProductReviewAction " + ex.getMessage());
      }
      return new ForwardResolution("/pages/admin/pendingReviewQueue.jsp");
    }

    public Resolution saveReviews() {
      if (saveReview) {
        review.setPublishDate(new Date());
        review.setPublishedBy(userService.getLoggedInUser());
        reviewDao.save(review);
        addRedirectAlertMessage(new SimpleMessage("Review Updated."));
        return new RedirectResolution(PendingProductReviewAction.class).addParameter("searchReviews");
      } else {
        review = reviewDao.get(UserReview.class, reviewId);
        return new ForwardResolution("/pages/admin/editProductReview.jsp");
      }
    }

   public String getProduct() {
    return product;
   }

   public void setProduct(String product) {
    this.product = product;
   }

   public Boolean isSaveReview() {
    return saveReview;
   }

   public void setSaveReview(Boolean saveReview) {
    this.saveReview = saveReview;
   }

   public Long getReviewId() {
    return reviewId;
   }

   public void setReviewId(Long reviewId) {
    this.reviewId = reviewId;
   }

   public List<UserReview> getProductReviews() {
    return productReviews;
   }

   public void setProductReviews(List<UserReview> productReviews) {
    this.productReviews = productReviews;
   }

   public UserReview getReview() {
        return review;
    }

    public void setReview(UserReview review) {
        this.review = review;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return productReviewPage == null ? 0 : productReviewPage.getTotalPages();
    }

    public int getResultCount() {
        return productReviewPage == null ? 0 : productReviewPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("product");
        params.add("reviewStatus");
        return params;
    }

}