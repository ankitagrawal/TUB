package com.hk.web.action.admin.review;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.population.CustomPopulationStrategy;
import com.hk.domain.review.Mail;
import com.hk.pact.service.review.MailService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
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
@CustomPopulationStrategy(BeanFirstPopulationStrategy.class)
@Component
public class CreateMailTemplateAction extends BaseAction {

    @Autowired
    MailService mailService;

    @ValidateNestedProperties( {
            @Validate(field = "name", required = true, on = {"createMailTemplate","saveMailTemplate","editMailTemplate"}),
            @Validate(field = "content", required = true, on = {"createMailTemplate","saveMailTemplate"}),
            @Validate(field = "amazonFileName", required = true, on = {"createMailTemplate","saveMailTemplate"}),
    })
    private Mail mail;

    private boolean editTemplate = false;

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }

    public Resolution searchMail(){
        mail = mailService.getMailByName(mail.getName());
        if(mail != null){
            editTemplate = true;
        } else
            addRedirectAlertMessage(new SimpleMessage("Mail Template doesnt  exist."));
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }
    public Resolution createMailTemplate(){
        Mail priorMailTemplate=mailService.getMailByName(mail.getName());
        editTemplate = false;
        if(priorMailTemplate !=null){
            addRedirectAlertMessage(new SimpleMessage("Mail Template already exists!!"));
        } else {
            mailService.save(mail);
            addRedirectAlertMessage(new SimpleMessage("Mail Template saved successfully."));
        }

        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }

    public Resolution editMailTemplate(){
        editTemplate = true;
        mail = mailService.getMailByName(mail.getName());
        if(mail == null){
            editTemplate=false;
            addRedirectAlertMessage(new SimpleMessage("Mail Template doesn't exist"));
        }
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }

    public Resolution saveMailTemplate(){
        editTemplate = false;
        mailService.save(mail);
        addRedirectAlertMessage(new SimpleMessage("Mail Template saved successfully"));
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
