package com.hk.pact.service.review;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;

public interface ReviewService {

    public Page getProductReviews(Product product, List<Long> reviewStatusList, int page, int perPage);

    public Double getProductStarRating(Product product);
}
