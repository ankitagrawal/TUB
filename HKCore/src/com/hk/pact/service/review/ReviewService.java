package com.hk.pact.service.review;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;

public interface ReviewService {

    public Page getProductReviewsForCustomer(Product product, List<Long> reviewStatusList, int page, int perPage);

    public Page getProductReviewsForAdmin(Product product, List<Long> reviewStatusList, int page, int perPage);

    public Long getAllReviews(Product product, List<Long> reviewStatusList);

    public Double getProductStarRating(Product product);
    
    public ReviewStatus getReviewStatus(Long reviewStatusId);
}
