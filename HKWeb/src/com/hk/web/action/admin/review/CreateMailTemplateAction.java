package com.hk.web.action.admin.review;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.review.Mail;
import com.hk.pact.service.review.MailService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CreateMailTemplateAction extends BaseAction {

    @Autowired
    MailService mailService;

    @ValidateNestedProperties( {
            @Validate(field = "timeWindowDays", required = true, on = {"createMailTemplate","saveMailTemplate"}),
            @Validate(field = "daysToReviewAgain", required = true, on = {"createProductSettings","saveProductSettings"}),
    })
    private Mail mail;
    private boolean editTemplate = false;

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }

    public Resolution createMailTemplate(){
        editTemplate = false;
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }
    public Resolution editMailTemplate(){
        editTemplate = true;
        //Mail = mailService.getMailById();
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }
    public Resolution saveMailTemplate(){
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }

    public boolean isEditTemplate() {
        return editTemplate;
    }

    public void setEditTemplate(boolean editTemplate) {
        this.editTemplate = editTemplate;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }
}
