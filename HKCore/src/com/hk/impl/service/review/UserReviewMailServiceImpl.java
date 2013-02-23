package com.hk.impl.service.review;


import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.review.ProductReviewMail;
import com.hk.domain.review.UserReview;
import com.hk.domain.review.UserReviewMail;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.review.UserReviewMailDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.review.ProductReviewMailService;
import com.hk.pact.service.review.ReviewService;
import com.hk.pact.service.review.UserReviewMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Service
public class UserReviewMailServiceImpl implements UserReviewMailService {

    @Autowired
    private UserReviewMailDao userReviewMailDao;



    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date){
        return userReviewMailDao.getAllUserReviewMailByDueDate(date);
    }

    public UserReviewMail getUserReviewMailById(Long id){
           return userReviewMailDao.getUserReviewMailById(id);
    }

    public UserReviewMail getLatestUserReviewMailBySentDate(User user,ProductVariant productVariant){
        return userReviewMailDao.getLatestUserReviewMailBySentDate(user, productVariant);
    }

    public UserReviewMail save(UserReviewMail userReviewMail){
        userReviewMail.setUpdateDt(BaseUtils.getCurrentTimestamp());
        return userReviewMailDao.save(userReviewMail);
    }

    public UserReviewMail getByUserAndProduct(User user, Product product){
        return userReviewMailDao.getByUserAndProduct(user, product);
    }
}
