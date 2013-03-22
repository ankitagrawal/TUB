package com.hk.impl.service.review;

import java.util.List;

import com.hk.domain.review.UserReview;
import com.hk.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ReviewStatus;
import com.hk.pact.dao.review.ReviewDao;
import com.hk.pact.service.review.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;


  @Override
    public Page getProductReviewsForCustomer(Product product, List<Long> reviewStatusList, int page, int perPage) {
        return reviewDao.getProductReviewsForCustomer(product, reviewStatusList, page, perPage);
    }

    @Override
    public Page getProductReviewsForAdmin(Product product, List<Long> reviewStatusList, int page, int perPage) {
        return reviewDao.getProductReviewsForAdmin(product, reviewStatusList, page, perPage);
    }

    @Override
    public Long getAllReviews(Product product, List<Long> reviewStatusList) {
      return reviewDao.getAllReviews(product, reviewStatusList);
    }

    @Override
    public Double getProductStarRating(Product product) {
        return reviewDao.getAverageRating(product);
    }

    public UserReview getReviewByUserAndProduct(User user, Product product){
        return reviewDao.getReviewByUserAndProduct(user,product);
    }

    @Override
    public ReviewStatus getReviewStatus(Long reviewStatusId) {
        return reviewDao.get(ReviewStatus.class, reviewStatusId);
    }

}
