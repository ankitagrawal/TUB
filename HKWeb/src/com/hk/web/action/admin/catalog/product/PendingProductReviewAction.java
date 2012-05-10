package com.hk.web.action.admin.catalog.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.review.EnumReviewStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.review.UserReview;
import com.hk.pact.dao.review.ReviewDao;
import com.hk.pact.service.review.ReviewService;

@Secure
public class PendingProductReviewAction extends BasePaginatedAction {

    private Product          product;
    private Page             productReviewPage;
    private List<UserReview> productReviews = new ArrayList<UserReview>();
    private Integer          defaultPerPage = 10;
    private UserReview       review;
    private ReviewStatus     reviewStatus;

    private ReviewService    reviewService;
    private ReviewDao        reviewDao;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    public Resolution pre() {
        List<Long> reviewStatusList = new ArrayList<Long>();
        if (reviewStatus != null) {
            reviewStatusList.add(reviewStatus.getId());
        } else {
            reviewStatusList.addAll(EnumReviewStatus.getAllStatus());
        }
        productReviewPage = reviewService.getProductReviews(product, reviewStatusList, getPageNo(), getPerPage());
        if (productReviewPage != null) {
            productReviews = productReviewPage.getList();
        }
        return new ForwardResolution("/pages/admin/pendingReviewQueue.jsp");
    }

    public Resolution saveReviews() {
        for (UserReview productReview : productReviews) {
            reviewDao.save(productReview);
        }
        addRedirectAlertMessage(new SimpleMessage("Reviews Updated."));
        return new RedirectResolution(PendingProductReviewAction.class).addParameter("searchReviews");
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        return params;
    }

}