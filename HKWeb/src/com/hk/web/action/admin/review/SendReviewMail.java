package com.hk.web.action.admin.review;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ankur
 * Date: 1/16/13
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class SendReviewMail extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(ReviewMailSettingsAction.class);

    @Autowired
    UserReviewMailService userReviewMailService;

    @Autowired
    EmailRecepientDao emailRecepientDao;

    @Autowired
    ProductReviewMailService productReviewMailService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    EmailManager emailManager;

    ProductReviewMail productReviewMail;

    @Validate(required = true, on = {"testEmail"})
    private Product product;

    public Resolution testEmail(){
        productReviewMail = productReviewMailService.getProductReviewMailByProduct(product);
        if(productReviewMail.getIsEnabled()){
            User user = getPrincipalUser();
            getEmailManager().sendProductReviewEmail(user, product, productReviewMail.getMail());
        }else{
            logger.error("Review for this product is not enabled");
        }
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }


    public Resolution sendEmails(){
        List<UserReviewMail> userList = userReviewMailService.getAllUserReviewMailByDueDate(new Date());
        UserReviewMail userReviewMail;
        UserReview userReview;
        if(userList != null){
            Iterator<UserReviewMail> listIterator = userList.iterator();
            while(listIterator.hasNext()){
                userReviewMail = listIterator.next();
                //if(emailRecepientDao.getOrCreateEmailRecepient(userReviewMail.getUser().getEmail()).isSubscribed()){
                if(true){
                    productReviewMail = productReviewMailService.getProductReviewMailByProduct(userReviewMail.getProductVariant().getProduct());
                    userReview = reviewService.getReviewByUserAndProduct(userReviewMail.getUser(), productReviewMail.getProduct());
                    if(userReview == null){
                        if(productReviewMail.getIsEnabled()){
                            Mail mail = productReviewMail.getMail();
                            getEmailManager().sendProductReviewEmail(userReviewMail.getUser(), userReviewMail.getProductVariant().getProduct(), mail);
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
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }


    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
