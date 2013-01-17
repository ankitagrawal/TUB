package com.hk.pact.dao.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.review.UserReviewMail;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;


public interface UserReviewMailDao extends BaseDao {

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date);

    public UserReviewMail save(UserReviewMail userReviewMail);

    public UserReviewMail getByOrder(Order order);

    public UserReviewMail getByUserAndProduct(User user, ProductVariant productVariant);
}
