package com.hk.web.action.admin.review;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.population.CustomPopulationStrategy;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.Order;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ProductReviewMail;
import com.hk.manager.EmailManager;
import com.hk.pact.service.review.MailService;
import com.hk.pact.service.review.ProductReviewMailService;
import com.hk.pact.service.review.ReviewCollectionFrameworkService;
import com.hk.pact.service.review.UserReviewMailService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.hk.web.validation.MailTypeConverter;


@CustomPopulationStrategy(BeanFirstPopulationStrategy.class)
@Component
@Secure(hasAnyRoles = {RoleConstants.MARKETING}, authActionBean = AdminPermissionAction.class)
public class ReviewMailSettingsAction extends BasePaginatedAction {


    private static Logger logger = LoggerFactory.getLogger(ReviewMailSettingsAction.class);

    @Autowired
    ProductReviewMailService productReviewMailService;

    @Autowired
    UserReviewMailService userReviewMailService;

    @Autowired
    MailService mailService;

    @Autowired
    EmailManager emailManager;

    @Autowired
    ReviewCollectionFrameworkService reviewCollectionFrameworkService;

    private Order order;

    @ValidateNestedProperties( {
            @Validate(field = "timeWindowDays", required = true, on = {"createProductSettings","saveProductSettings"}),
            @Validate(field = "daysToSendReviewMailAgain", required = true, on = {"createProductSettings","saveProductSettings"}),
            @Validate(field = "mail", required = true, on = {"createProductSettings","saveProductSettings"}),
            @Validate(field = "testEmailId", required = true, on = {"createProductSettings","saveProductSettings"})
    })
    private ProductReviewMail productReviewMail;
    private List<ProductReviewMail> productReviewMailList = new ArrayList<ProductReviewMail>();
    Page reviewCollectionPage;

    @Validate(required = true, on={"createProductSettings", "sendTestEmail","saveProductSettings"})
    private Product product;

    private Mail mail;

    private Integer defaultPerPage = 20;

    private boolean editSettings = false;

    @DefaultHandler
    public Resolution pre(){
//        if(product != null){
//            productReviewMail = productReviewMailService.getProductReviewMailByProduct(product);
//            productReviewMailList.add(productReviewMail);
//        }else{
            reviewCollectionPage = productReviewMailService.searchAllProductReviewMail(product,mail, getPageNo(), getPerPage());
            productReviewMailList = reviewCollectionPage.getList();
//        }
        return new ForwardResolution("/pages/admin/review/reviewCollectionList.jsp");
    }

    public Resolution editProductSettings(){
        editSettings=true;
        product = productReviewMail.getProduct();
        if(productReviewMail==null){
            editSettings=false;
            addRedirectAlertMessage(new SimpleMessage("Review Mailing Product doesn't exist"));
        }
        return new ForwardResolution("/pages/admin/review/productReviewMailSettings.jsp");
    }

    public Resolution deleteProductSettings(){
        productReviewMailService.delete(productReviewMail);
        return new RedirectResolution(ReviewMailSettingsAction.class);
    }

    public Resolution create(){
        if(mailService.getAllMailType() == null || mailService.getAllMailType().size() == 0 ){
            addRedirectAlertMessage(new SimpleMessage("You need to create atleast one mail template before creating any product's settings"));
            return new RedirectResolution(ReviewMailSettingsAction.class);
        }
        return new ForwardResolution("/pages/admin/review/productReviewMailSettings.jsp");
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
        return new RedirectResolution(ReviewMailSettingsAction.class);
    }
    public Resolution saveProductSettings(){
        editSettings = false;
        productReviewMail.setLastUpdatedBy(getPrincipalUser());
        //productReviewMail.setProduct(product);
        productReviewMailService.save(productReviewMail);
        addRedirectAlertMessage(new SimpleMessage("Review Mailing product saved successfully."));
        return new RedirectResolution(ReviewMailSettingsAction.class);
    }

    public void validateProductSettings(){
        if (product == null) {
            editSettings = true;
            getContext().getValidationErrors().add("1", new SimpleError("Please Enter Product"));
        }
        if (productReviewMail == null || productReviewMail.getTimeWindowDays() == null) {
            editSettings = true;
            getContext().getValidationErrors().add("2", new SimpleError("Please Enter Days to Mail after"));
        }
        if (productReviewMail.getDaysToSendReviewMailAgain() == null) {
            editSettings = true;
            getContext().getValidationErrors().add("3", new SimpleError("Please Enter Days to Send Review Mail again"));
        }
        if (productReviewMail.getMail() == null) {
            editSettings = true;
            getContext().getValidationErrors().add("4", new SimpleError("Please Select a Mail Type"));
        }
        if (productReviewMail.getTestEmailId() == null) {
            editSettings = true;
            getContext().getValidationErrors().add("5", new SimpleError("Please Enter Test EMail Id"));
        }
    }

    /*public Resolution test(){
        if(order!=null){
            reviewCollectionFrameworkService.doUserEntryForReviewMail(order);
        }else{
            addRedirectAlertMessage(new SimpleMessage("order doesn't exist"));
        }
        editSettings = false;
        return new ForwardResolution("/pages/admin/review/productReviewMailSettings.jsp");
    } */

    public Resolution sendTestEmail(){
        boolean  result = reviewCollectionFrameworkService.sendTestEmail(getPrincipalUser(), product);
        if(result)
            addRedirectAlertMessage(new SimpleMessage("Test Email Sent successfully."));
        else
            addRedirectAlertMessage(new SimpleMessage("Error in sending the test email."));

        return new RedirectResolution(ReviewMailSettingsAction.class);

    }

    public Resolution sendDueEmail(){
        int noOfEmailsSent = reviewCollectionFrameworkService.sendDueEmails();
        String message = "";
        if(noOfEmailsSent == 0)
            message = "No Email sent.";
        else
            message = noOfEmailsSent + " emails sent";

        addRedirectAlertMessage(new SimpleMessage(message));
        return new RedirectResolution(ReviewMailSettingsAction.class);
    }


    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("product");
        params.add("mail");
        return params;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getResultCount() {
        return reviewCollectionPage == null ? 0 : reviewCollectionPage.getTotalResults();
    }

    public int getPageCount() {
        return reviewCollectionPage == null ? 0 : reviewCollectionPage.getTotalPages();
    }

    public boolean isEditSettings() {
        return editSettings;
    }

    public void setEditSettings(boolean editSettings) {
        this.editSettings = editSettings;
    }

    public List<ProductReviewMail> getProductReviewMailList() {
        return productReviewMailList;
    }

    public void setProductReviewMailList(List<ProductReviewMail> productReviewMailList) {
        this.productReviewMailList = productReviewMailList;
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
