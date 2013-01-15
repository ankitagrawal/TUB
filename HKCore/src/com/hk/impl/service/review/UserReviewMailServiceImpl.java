package com.hk.impl.service.review;


import com.akube.framework.util.BaseUtils;
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

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserReviewMailServiceImpl implements UserReviewMailService {

    private static Logger                     logger = LoggerFactory.getLogger(UserReviewMailServiceImpl.class);

    @Autowired
    private ProductReviewMailService productReviewMailService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserReviewMailDao userReviewMailDao;

    @Autowired
    private EmailRecepientDao  emailRecepientDao;

    private UserReviewMail userReviewMail = new UserReviewMail();
    private ProductReviewMail productReviewMail;
    private UserReview userReview;

    public List<UserReviewMail> getAllUserReviewMailByDueDate(Date date){
        return userReviewMailDao.getAllUserReviewMailByDueDate(date);
    }
    public UserReviewMail save(UserReviewMail userReviewMail){
        return userReviewMailDao.save(userReviewMail);
    }

    public void userEntry(Order order){
        User user = order.getUser();
        userReviewMail = userReviewMailDao.getByOrder(order);
        if(userReviewMail == null)
                userReviewMail = new UserReviewMail();
        //boolean isUserSubscribed = emailRecepientDao.getOrCreateEmailRecepient(user.getEmail()).isSubscribed();
        boolean isUserSubscribed = true;
        if(isUserSubscribed){
            Set cartLine = order.getCartLineItems();
            if(cartLine != null){
                Iterator<CartLineItem> cartLineIterator = cartLine.iterator();
                ProductVariant productVariant;
                while(cartLineIterator.hasNext()){
                    productVariant = cartLineIterator.next().getProductVariant();
                    if(productVariant != null){
                        productReviewMail = productReviewMailService.getProductReviewMailByProduct(productVariant.getProduct());
                        if(productReviewMail.isEnabled()){
                            userReview = reviewService.getReviewByUserAndProduct(user, productVariant.getProduct());
                            if(userReview == null){
                                userReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()));
                            }else{
                               userReviewMail.setDueDate(new Date(userReview.getReviewDate().getTime() + 24*3600*1000*productReviewMail.getDaysToReviewAgain()));
                            }
                            userReviewMail.setIsMailSent(false);
                            userReviewMail.setUser(user);
                            userReviewMail.setProductVariant(productVariant);
                            userReviewMail.setBaseOrder(order);
                            userReviewMail.setCreateDt(BaseUtils.getCurrentTimestamp());
                            userReviewMail.setUpdateDt(BaseUtils.getCurrentTimestamp());
                            userReviewMailDao.save(userReviewMail);
                        }
                    }
                }
            }
        }else{
            logger.info("User not Subscribed");

        }
    }
}
