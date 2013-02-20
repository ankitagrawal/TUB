package com.hk.impl.service.review;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.user.EnumEmailSubscriptions;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ProductReviewMail;
import com.hk.domain.review.UserReview;
import com.hk.domain.review.UserReviewMail;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.service.review.ProductReviewMailService;
import com.hk.pact.service.review.ReviewCollectionFrameworkService;
import com.hk.pact.service.review.ReviewService;
import com.hk.pact.service.review.UserReviewMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Service
public class ReviewCollectionFrameworkServiceImpl implements ReviewCollectionFrameworkService {
    private static Logger logger = LoggerFactory.getLogger(ReviewCollectionFrameworkServiceImpl.class);

    @Autowired
    UserReviewMailService userReviewMailService;

    @Autowired
    EmailRecepientDao emailRecepientDao;

    @Autowired
    ProductReviewMailService productReviewMailService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    EmailManager emailManager;

    private UserReviewMail userReviewMail = new UserReviewMail();
    private ProductReviewMail productReviewMail;
    private UserReview userReview;

    public boolean sendTestEmail(User user, Product product){
        productReviewMail = productReviewMailService.getProductReviewMailByProduct(product);
        if(productReviewMail.getIsEnabled()){
            return getEmailManager().sendProductReviewEmail(user, product, productReviewMail.getMail(),productReviewMail.getTestEmailId());
        }else{
            logger.info("Emails not enabled for" + productReviewMail.getProduct());
        }
        return false;
    }


    public void sendDueEmails(){
        List<UserReviewMail> userList = userReviewMailService.getAllUserReviewMailByDueDate(new Date());
        UserReview userReview;
        boolean mailSent;
        if(userList != null){
            for(UserReviewMail userReviewMail : userList ){
                boolean isUserUnsubscribed = EnumEmailSubscriptions.isSubscribed(EnumEmailSubscriptions.UNSUBSCRIBED , userReviewMail.getUser().getSubscribedMask());
                if(!isUserUnsubscribed){
                    productReviewMail = productReviewMailService.getProductReviewMailByProduct(userReviewMail.getProductVariant().getProduct());
                    userReview = reviewService.getReviewByUserAndProduct(userReviewMail.getUser(), userReviewMail.getProductVariant().getProduct());
                    if(userReview == null){
                        if(productReviewMail.getIsEnabled()){
                            Mail mail = productReviewMail.getMail();
                            mailSent = getEmailManager().sendProductReviewEmail(userReviewMail.getUser(), userReviewMail.getProductVariant().getProduct(), mail, null);
                            if(mailSent){
                                userReviewMail.setSentDate(BaseUtils.getCurrentTimestamp());
                                userReviewMail.setIsMailSent(true);
                                userReviewMailService.save(userReviewMail);
                            }
                        }else{
                            logger.info("Emails not enabled for" + productReviewMail.getProduct());
                        }
                    }
                }else{
                    logger.info(userReviewMail.getUser() + "is not subscribed");
                }
            }
        }else{
            logger.info("No user to email today");
        }
    }


    public void doUserEntryForReviewMail(Order order){
        User user = order.getUser();
        userReviewMail = new UserReviewMail();
        //boolean isUserSubscribed = emailRecepientDao.getOrCreateEmailRecepient(user.getEmail()).isSubscribed();
        boolean isUserUnsubscribed = EnumEmailSubscriptions.isSubscribed(EnumEmailSubscriptions.UNSUBSCRIBED , user.getSubscribedMask());
        if(!isUserUnsubscribed){
            Set<CartLineItem> cartLine = order.getCartLineItems();
            if(cartLine != null){
                ProductVariant productVariant;
                for(CartLineItem cartLineItem : cartLine){
                    productVariant = cartLineItem.getProductVariant();
                    if(productVariant != null){
                        productReviewMail = productReviewMailService.getProductReviewMailByProduct(productVariant.getProduct());
                        userReview = reviewService.getReviewByUserAndProduct(user, productVariant.getProduct());
                        if(productReviewMail != null && userReview == null ){
                            UserReviewMail priorUserReviewMail = userReviewMailService.getByUserAndProductVariant(user, productVariant);
                            if(priorUserReviewMail == null){
                                userReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()));
                                userReviewMail.setIsMailSent(false);
                                userReviewMail.setUser(user);
                                userReviewMail.setProductVariant(productVariant);
                                userReviewMail.setBaseOrder(order);
                                userReviewMail.setCreateDt(BaseUtils.getCurrentTimestamp());
                                userReviewMailService.save(userReviewMail);
                            }else if(priorUserReviewMail.getIsMailSent()){
                                    if(priorUserReviewMail.getSentDate().getTime() + 24*3600*1000*productReviewMail.getDaysToSendReviewMailAgain() <= BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()).getTime() ){
                                        priorUserReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()));
                                    }
                                    else{
                                        priorUserReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getDaysToSendReviewMailAgain()));
                                    }
                                    priorUserReviewMail.setIsMailSent(false);
                                    priorUserReviewMail.setBaseOrder(order);
                                    userReviewMailService.save(priorUserReviewMail);
                            }
                        }
                    }
                }
            }
        }
    }


    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }


}
