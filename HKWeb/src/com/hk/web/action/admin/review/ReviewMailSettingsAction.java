package com.hk.web.action.admin.review;

import com.akube.framework.stripes.population.CustomPopulationStrategy;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.Order;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ProductReviewMail;
import com.hk.domain.review.UserReviewMail;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.service.review.ProductReviewMailService;
import com.hk.pact.service.review.UserReviewMailService;
import net.sourceforge.stripes.action.*;
import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/8/13
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
@CustomPopulationStrategy(BeanFirstPopulationStrategy.class)
@Component
public class ReviewMailSettingsAction extends BaseAction {


   private static Logger logger = LoggerFactory.getLogger(ReviewMailSettingsAction.class);

   @Autowired
   ProductReviewMailService productReviewMailService;

    @Autowired
    UserReviewMailService userReviewMailService;

    @Autowired
    EmailManager emailManager;

    @Autowired
    EmailRecepientDao emailRecepientDao;

    private Order order;

   @ValidateNestedProperties( {
            @Validate(field = "timeWindowDays", required = true, on = {"createProductSettings","saveProductSettings"}),
            @Validate(field = "daysToReviewAgain", required = true, on = {"createProductSettings","saveProductSettings"}),
            @Validate(field = "mail", required = true, on = {"createProductSettings","saveProductSettings"}),
            @Validate(field = "testEmailId", required = true, on = {"createProductSettings","saveProductSettings"}),
    })
   private ProductReviewMail productReviewMail;

   @Validate(required = true, on={"editProductSettings","createProductSettings","saveProductSettings"})
   private Product product;

   private boolean editSettings = false;

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }

    public Resolution editProductSettings(){
        editSettings=true;
        productReviewMail = productReviewMailService.getProductReviewMailByProduct(product);
        if(productReviewMail==null){
            editSettings=false;
            addRedirectAlertMessage(new SimpleMessage("Review Mailing Product doesn't exist"));
        }
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }

    public Resolution createProductSettings(){
        ProductReviewMail priorProductReviewMail=productReviewMailService.getProductReviewMailByProduct(product);
        editSettings = false;

        if(priorProductReviewMail !=null){
            addRedirectAlertMessage(new SimpleMessage("Review Mailing product already exists!!"));
        } else {
            productReviewMail.setCreatedBy(getPrincipalUser());
            productReviewMail.setLastUpdatedBy(getPrincipalUser());
            productReviewMail.setProduct(product);
            productReviewMailService.save(productReviewMail);
            addRedirectAlertMessage(new SimpleMessage("Review Mailing product saved successfully."));
        }
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }
    public Resolution saveProductSettings(){
        editSettings = false;
        productReviewMail.setCreatedBy(getPrincipalUser());
        productReviewMail.setLastUpdatedBy(getPrincipalUser());
        productReviewMail.setProduct(product);
        productReviewMailService.save(productReviewMail);
        addRedirectAlertMessage(new SimpleMessage("Review Mailing product saved successfully."));
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }

    public Resolution test(){
        if(order!=null){
            userReviewMailService.userEntry(order);
        }else{
            addRedirectAlertMessage(new SimpleMessage("order doesn't exist"));
        }
        editSettings = false;
        return new ForwardResolution("/pages/admin/review/settings.jsp");
    }




    public boolean isEditSettings() {
        return editSettings;
    }

    public void setEditSettings(boolean editSettings) {
        this.editSettings = editSettings;
    }
    public ProductReviewMail getProductReviewMail() {
        return productReviewMail;
    }

    public void setProductReviewMail(ProductReviewMail productReviewMail) {
        this.productReviewMail = productReviewMail;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
