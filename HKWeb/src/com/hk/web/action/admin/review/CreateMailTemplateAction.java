package com.hk.web.action.admin.review;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.population.CustomPopulationStrategy;
import com.hk.domain.review.Mail;
import com.hk.pact.service.review.MailService;
import com.hk.util.FtlUtils;
import com.hk.util.HKFileUtils;
import com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@CustomPopulationStrategy(BeanFirstPopulationStrategy.class)
@Component
public class CreateMailTemplateAction extends BaseAction {

    @Autowired
    MailService mailService;

    @ValidateNestedProperties( {
            @Validate(field= "name", required = true, on = {"searchMail","generateFtl"}),
            @Validate(field= "subject", required = true, on = {"generateFtl"})
    })
    Mail mail;

    @Validate(required = true, on = {"generateFtl"})
    private FileBean contentBean;


    private boolean editTemplate = false;

    private EmailNewsletterCampaignAction emailNewsletterCampaignAction = new EmailNewsletterCampaignAction();

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
    }

    public Resolution searchMail(){
        mail = mailService.getMailByName(mail.getName());
        if(mail != null){
            editTemplate = true;
            return new ForwardResolution("/pages/admin/review/mailTemplate.jsp");
        } else{
            addRedirectAlertMessage(new SimpleMessage("Mail Template doesnt  exist."));
            return new RedirectResolution(CreateMailTemplateAction.class);
        }
    }

    public Resolution generateFtl(){
        Mail priorMail = mailService.getMailByName(mail.getName());
        if(priorMail != null && !editTemplate)
            mail = priorMail;//edit existing template
        emailNewsletterCampaignAction.setName(mail.getName());
        emailNewsletterCampaignAction.setContentBean(contentBean);
        Resolution resolution = emailNewsletterCampaignAction.generateFtlAndUploadData();
        if(emailNewsletterCampaignAction.getContentUploaded()){
          mail.setContent(emailNewsletterCampaignAction.getFtlContents());
          mail.setAmazonFileName(FtlUtils.getBasicAmazonS3Path() + HKFileUtils.getPathAfterSubstring(emailNewsletterCampaignAction.getHtmlPath(), "emailContentFiles"));
          mailService.save(mail);
        }
        return new RedirectResolution(CreateMailTemplateAction.class);
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

    public FileBean getContentBean() {
        return contentBean;
    }

    public void setContentBean(FileBean contentBean) {
        this.contentBean = contentBean;
    }
}
