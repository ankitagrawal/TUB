package com.hk.pact.service.review;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
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
import com.hk.pact.service.UserService;
import com.hk.pact.service.review.ProductReviewMailService;
import com.hk.pact.service.review.ReviewService;
import com.hk.pact.service.review.UserReviewMailService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Component
public class ReviewCollectionFrameworkService{
    private static Logger logger = LoggerFactory.getLogger(ReviewCollectionFrameworkService.class);

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

    public boolean testEmail(User user, Product product){
        productReviewMail = productReviewMailService.getProductReviewMailByProduct(product);
        if(productReviewMail.getIsEnabled()){
            return getEmailManager().sendProductReviewEmail(user, product, productReviewMail.getMail());
        }else{
            logger.error("Review for this product is not enabled");
        }
        return false;
    }


    public void sendEmails(){
        List<UserReviewMail> userList = userReviewMailService.getAllUserReviewMailByDueDate(new Date());
        UserReviewMail userReviewMail;
        UserReview userReview;
        boolean isMailSent;
        if(userList != null){
            Iterator<UserReviewMail> listIterator = userList.iterator();
            while(listIterator.hasNext()){
                userReviewMail = listIterator.next();
                if(emailRecepientDao.getOrCreateEmailRecepient(userReviewMail.getUser().getEmail()).isSubscribed()){
                    //if(true){
                    productReviewMail = productReviewMailService.getProductReviewMailByProduct(userReviewMail.getProductVariant().getProduct());
                    userReview = reviewService.getReviewByUserAndProduct(userReviewMail.getUser(), userReviewMail.getProductVariant().getProduct());
                    if(userReview == null){
                        if(productReviewMail.getIsEnabled()){
                            Mail mail = productReviewMail.getMail();
                            isMailSent = getEmailManager().sendProductReviewEmail(userReviewMail.getUser(), userReviewMail.getProductVariant().getProduct(), mail);
                            if(isMailSent){
                                userReviewMail.setSentDate(BaseUtils.getCurrentTimestamp());
                                userReviewMail.setIsMailSent(true);
                                //setdueDate here to email again in case user does not review the product
                                userReviewMailService.save(userReviewMail);
                            }
                        }else{
                            logger.error("Emails not enabled for" + productReviewMail.getProduct());
                        }
                    }
                }else{
                    logger.error(userReviewMail.getUser() +"is not subscribed");
                }
            }
        }else{
            logger.error("No user to email today");
        }
    }


    public void userEntry(Order order){
        User user = order.getUser();
        userReviewMail = new UserReviewMail();
        boolean isUserSubscribed = emailRecepientDao.getOrCreateEmailRecepient(user.getEmail()).isSubscribed();
        //boolean isUserSubscribed = true;
        if(isUserSubscribed){
            Set cartLine = order.getCartLineItems();
            if(cartLine != null){
                Iterator<CartLineItem> cartLineIterator = cartLine.iterator();
                ProductVariant productVariant;
                while(cartLineIterator.hasNext()){
                    productVariant = cartLineIterator.next().getProductVariant();
                    if(productVariant != null){
                        productReviewMail = productReviewMailService.getProductReviewMailByProduct(productVariant.getProduct());
                        userReview = reviewService.getReviewByUserAndProduct(user, productVariant.getProduct());
                        if(userReview == null){
                            UserReviewMail priorUserReviewMail = userReviewMailService.getByUserAndProduct(user, productVariant);
                            if(priorUserReviewMail == null){
                                userReviewMail.setDueDate(BaseUtils.getFutureTimestamp( 24*3600*1000*productReviewMail.getTimeWindowDays()));
                                userReviewMail.setIsMailSent(false);
                                userReviewMail.setUser(user);
                                userReviewMail.setProductVariant(productVariant);
                                userReviewMail.setBaseOrder(order);
                                userReviewMail.setCreateDt(BaseUtils.getCurrentTimestamp());
                                userReviewMail.setUpdateDt(BaseUtils.getCurrentTimestamp());
                                userReviewMailService.save(userReviewMail);
                            }else{
                                logger.info("Entry for this user and product variant already exist");
                            }
                        } else
                            logger.info("User has already reviewed this product");
                        /*else{
                           userReviewMail.setDueDate(new Date(userReview.getReviewDate().getTime() + 24*3600*1000*productReviewMail.getDaysToReviewAgain()));
                        }*/
                    }
                }
            }
        }else{
            logger.info("User not Subscribed");

        }
    }


    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }


}
