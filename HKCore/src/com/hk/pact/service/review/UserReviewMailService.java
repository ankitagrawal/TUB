package com.hk.pact.service.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.review.UserReviewMail;
import com.hk.domain.user.User;

import java.util.Date;
import java.util.List;


public interface UserReviewMailService {

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date);

    public UserReviewMail getUserReviewMailById(Long id);

    public UserReviewMail getLatestUserReviewMailBySentDate(User user,ProductVariant productVariant);

    public UserReviewMail save(UserReviewMail userReviewMail);

    public UserReviewMail getByUserAndProduct(User user, Product product);


}
