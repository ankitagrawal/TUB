package com.hk.impl.service.review;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.user.EnumEmailSubscriptions;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.email.EmailRecepient;
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

import java.util.*;


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
            Integer urm = -1;
            return getEmailManager().sendProductReviewEmail(user, product.getProductVariants().get(0), productReviewMail.getMail(),productReviewMail.getTestEmailId(),urm);
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
                    //UserReviewMail latestUserReviewMail = userReviewMailService.getLatestUserReviewMailBySentDate(userReviewMail.getUser(),userReviewMail.getProductVariant());
                    productReviewMail = productReviewMailService.getProductReviewMailByProduct(userReviewMail.getProductVariant().getProduct());
                    userReview = reviewService.getReviewByUserAndProduct(userReviewMail.getUser(), userReviewMail.getProductVariant().getProduct());
                    if(productReviewMail != null && userReview == null){
                        if(productReviewMail.getIsEnabled()){
                            Mail mail = productReviewMail.getMail();
                            //if(latestUserReviewMail == null || (userReviewMail.getDueDate().getTime()-latestUserReviewMail.getSentDate().getTime() >= 24*3600*1000*productReviewMail.getDaysToSendReviewMailAgain())){
                                mailSent = getEmailManager().sendProductReviewEmail(userReviewMail.getUser(), userReviewMail.getProductVariant(), mail, null,userReviewMail.getId());
                                if(mailSent){
                                    userReviewMail.setSentDate(BaseUtils.getCurrentTimestamp());
                                    userReviewMail.setIsMailSent(true);
                                    userReviewMailService.save(userReviewMail);
                                }
//                            }else{
//                                userReviewMail.setDueDate(BaseUtils.getFutureTimestamp(userReviewMail.getDueDate().getTime()-latestUserReviewMail.getSentDate().getTime()));
//                                userReviewMailService.save(userReviewMail);
//
//                            }
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

    private Set<CartLineItem> keepOneVariantOfProduct(Set<CartLineItem> cartLineItems){
        Set<CartLineItem> currentLineItems = new HashSet<CartLineItem>(cartLineItems);
        for(CartLineItem cartLineItem : cartLineItems){
            boolean firstTime = true;
            for(CartLineItem cartLineItem1 : cartLineItems){
                if(cartLineItem.getProductVariant().getProduct().equals(cartLineItem1.getProductVariant().getProduct())){
                    if(firstTime)
                        firstTime = false;
                    else
                        currentLineItems.remove(cartLineItem1);
                }
            }
        }
        return currentLineItems;
    }
    public void doUserEntryForReviewMail(Order order){
        User user = order.getUser();
        userReviewMail = new UserReviewMail();
        boolean isUserUnsubscribed = EnumEmailSubscriptions.isSubscribed(EnumEmailSubscriptions.UNSUBSCRIBED , user.getSubscribedMask());
        if(!isUserUnsubscribed){
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            if(productCartLineItems != null){
                //productCartLineItems = keepOneVariantOfProduct(productCartLineItems);
                ProductVariant productVariant;
                for(CartLineItem cartLineItem : productCartLineItems){
                    productVariant = cartLineItem.getProductVariant();
                    if(productVariant != null){
                        productReviewMail = productReviewMailService.getProductReviewMailByProduct(productVariant.getProduct());
                        userReview = reviewService.getReviewByUserAndProduct(user, productVariant.getProduct());
                        if(productReviewMail != null && userReview == null && productReviewMail.getIsEnabled() ){
                            UserReviewMail priorUserReviewMail = userReviewMailService.getByUserAndProduct(user, productVariant.getProduct());
                            userReviewMail.setIsMailSent(false);
                            userReviewMail.setUser(user);
                            userReviewMail.setProductVariant(productVariant);
                            userReviewMail.setBaseOrder(order);
                            userReviewMail.setCreateDt(BaseUtils.getCurrentTimestamp());
                            if(priorUserReviewMail == null){
                                userReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()));
                                userReviewMailService.save(userReviewMail);
                            }else if(priorUserReviewMail.getIsMailSent()){
                                    if(priorUserReviewMail.getSentDate().getTime() + 24*3600*1000*productReviewMail.getDaysToSendReviewMailAgain() <= BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()).getTime() ){
                                        userReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()));
                                    }
                                    else{
                                        userReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getDaysToSendReviewMailAgain()-(BaseUtils.getCurrentTimestamp().getTime()-priorUserReviewMail.getSentDate().getTime())));
                                    }
                                    userReviewMailService.save(userReviewMail);
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
