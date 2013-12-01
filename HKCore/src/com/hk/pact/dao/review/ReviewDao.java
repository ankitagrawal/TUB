package com.hk.pact.dao.review;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.review.UserReview;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface ReviewDao extends BaseDao{

    public List<ReviewStatus> getReviewStatusList(List<Long> reviewStatusIdList);

    public Long getAllReviews(Product product, List<Long> reviewStatusList);

    public Page getProductReviewsForCustomer(Product product, List<Long> reviewStatusList, int page, int perPage);

    public Double getAverageRating(Product product);

    Page getProductReviewsForAdmin(Product product, List<Long> reviewStatusList, int page, int perPage);

    public UserReview getReviewByUserAndProduct(User user, Product product);
}
