package com.hk.impl.service.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.review.ReviewDao;
import com.hk.pact.service.review.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao userReviewDao;
    
    
    @Override
    public Page getProductReviews(Product product, List<Long> reviewStatusList, int page, int perPage) {
        return userReviewDao.getProductReviews(product, userReviewDao.getReviewStatusList(reviewStatusList), page, perPage);
    }

    @Override
    public Double getProductStarRating(Product product) {
        return userReviewDao.getStarRating(product);
    }

}
