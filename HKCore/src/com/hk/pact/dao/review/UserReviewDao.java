package com.hk.pact.dao.review;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.pact.dao.BaseDao;

public interface UserReviewDao extends BaseDao{

    public List<ReviewStatus> getReviewStatusList(List<Long> reviewStatusIdList);

    public Page getProductReviews(Product product, List<ReviewStatus> reviewStatusList, int page, int perPage);

    public Double getStarRating(Product product);
}
