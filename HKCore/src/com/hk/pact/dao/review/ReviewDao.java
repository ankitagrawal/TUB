package com.hk.pact.dao.review;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.pact.dao.BaseDao;

public interface ReviewDao extends BaseDao{

    public List<ReviewStatus> getReviewStatusList(List<Long> reviewStatusIdList);

    public Long getAllReviews(Product product, List<Long> reviewStatusList);

    public Page getProductReviews(Product product, List<ReviewStatus> reviewStatusList, int page, int perPage);

    public Double getAverageRating(Product product);
}
