package com.hk.web.action.admin.review;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.population.CustomPopulationStrategy;
import com.hk.admin.pact.service.email.AdminEmailCampaignService;
import com.hk.constants.core.Keys;
import com.hk.domain.review.Mail;
import com.hk.pact.service.review.MailService;
import com.hk.util.FtlUtils;
import com.hk.util.HKFileUtils;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@CustomPopulationStrategy(BeanFirstPopulationStrategy.class)
@Component
public class CreateMailTemplateAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(CreateMailTemplateAction.class);

    @Autowired
    MailService mailService;

    @Autowired
    AdminEmailCampaignService adminEmailCampaignService;

    @ValidateNestedProperties( {
            @Validate(field= "name", required = true, on = {"searchMail","createMailTemplate", "saveMailTemplate"}),
            @Validate(field= "subject", required = true, on = {"createMailTemplate", "saveMailTemplate"})
    })
    Mail mail;

    @Validate(required = true, on = {"generateFtl"})
    private FileBean contentBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;


    private boolean editTemplate = false, ftlGenerated =false, contentUploaded = false;
    String htmlPath,ftlContents;


    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/review/productReviewMailTemplate.jsp");
    }

    public Resolution searchMail(){
        mail = mailService.getMailByName(mail.getName());
        if(mail != null){
            editTemplate = true;
            return new ForwardResolution("/pages/admin/review/productReviewMailTemplate.jsp");
        } else{
            addRedirectAlertMessage(new SimpleMessage("Mail Template doesnt  exist."));
            return new ForwardResolution(CreateMailTemplateAction.class, "pre");
        }
    }


    public Resolution createMailTemplate(){
        File contentZipFolder;
        File contentFolder;
        try {
            if (contentBean != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String contentZipPath = adminUploadsPath + "/emailContentFiles/" + mail.getName() + "-"+ sdf.format(new Date()) + ".zip";
                contentZipFolder = new File(contentZipPath);
                contentZipFolder.getParentFile().mkdirs();
                contentBean.save(contentZipFolder);

                String contentPath = contentZipPath.replaceAll("\\.zip", "");
                contentFolder = HKFileUtils.unzipFolder(contentZipPath, contentPath);
                if (contentFolder != null) {
//          contentFolderName = contentFolder.getName();
                    logger.info("trying to generate a .ftl file from the .html file to be uploaded.");
                    String[] extensions= {"html","htm"};
                    File[] htmlFiles = FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(contentFolder, extensions, true));
                    if (htmlFiles != null) {
                        if (htmlFiles.length == 1) {
                            htmlPath = htmlFiles[0].getAbsolutePath();
                            logger.debug(htmlFiles[0].getAbsolutePath());
                        } else if (htmlFiles.length > 1) {
                            addRedirectAlertMessage(new SimpleMessage("The zip folder contains multiple .html files for the email campaign. Kindly check into the same"));
                            return new ForwardResolution(CreateMailTemplateAction.class,"pre");
                        } else {
                            addRedirectAlertMessage(new SimpleMessage("The zip folder does not contain any .html file for the email campaign. Kindly check into the same"));
                            return new ForwardResolution(CreateMailTemplateAction.class,"pre");
                        }
                    }

                    String ftlPath = adminUploadsPath + "/emailContentFiles/" + mail.getName() + ".ftl";
                    File ftlFile = FtlUtils.generateFtlFromHtml(htmlFiles[0], ftlPath,contentFolder.getName());
                    //making changes in the .html file also
                    FileUtils.copyFile(ftlFile, htmlFiles[0]);
                    ftlContents = HKFileUtils.fileToString(ftlFile);
                    ftlGenerated = Boolean.TRUE;
                    logger.info("ftl generated");

                    contentUploaded = adminEmailCampaignService.uploadEmailContent(contentFolder);
                    //contentUploaded = true;
                    logger.info("uploaded email content to s3.");
                    FileUtils.deleteDirectory(contentFolder);
                    FileUtils.deleteQuietly(contentZipFolder);
                    FileUtils.deleteQuietly(ftlFile);
                    if(contentUploaded){
                        mail.setContent(ftlContents);
                        mail.setAmazonFileName(FtlUtils.getBasicAmazonS3Path() + HKFileUtils.getPathAfterSubstring(htmlPath, "emailContentFiles"));
                        mailService.save(mail);
                    }
                    addRedirectAlertMessage(new SimpleMessage("ftl generated and upload to s3"));
                    return new ForwardResolution(CreateMailTemplateAction.class, "pre");
                }
                addRedirectAlertMessage(new SimpleMessage("Error while unzipping folder: " + contentBean.getFileName()));
            } else {
                addRedirectAlertMessage(new SimpleMessage("Please select a .zip folder to be uploaded!"));
            }
            return new ForwardResolution(CreateMailTemplateAction.class, "pre");
        } catch (IOException ioe) {
            logger.error("ftl generation failed: " + ioe);
        }
        addRedirectAlertMessage(new SimpleMessage("Error generating ftl"));
        return new ForwardResolution(CreateMailTemplateAction.class, "pre");
    }

    public Resolution saveMailTemplate(){
        editTemplate = false;
        Mail priorMail = mailService.getMailById(mail.getId());
        mail.setAmazonFileName(priorMail.getAmazonFileName());

        if(contentBean != null)
            return new ForwardResolution(CreateMailTemplateAction.class, "createMailTemplate");

        String amazonHtmlPath = mail.getAmazonFileName();
        String htmlKey = amazonHtmlPath.replaceAll("(.*\\.s3\\.amazonaws\\.com/)", "");
        File htmlFile = new File(adminUploadsPath + "/emailContentFiles/emailer.html");
        try {
            FileUtils.writeStringToFile(htmlFile, mail.getContent());
            adminEmailCampaignService.uploadHtml(htmlFile, htmlKey);
            logger.info("uploaded changed html to s3 for mail Template: " + mail.getName());

            mail = mailService.save(mail);
            addRedirectAlertMessage(new SimpleMessage("changes to the mail template: " + mail.getName() + " have been saved."));
            return new RedirectResolution(CreateMailTemplateAction.class);
        } catch (IOException ioe) {
            logger.error("Error writing contents to html file: " + ioe);
        } finally {
            FileUtils.deleteQuietly(htmlFile);
        }
        addRedirectAlertMessage(new SimpleMessage("Error writing contents to html file!"));
        return new ForwardResolution(CreateMailTemplateAction.class, "searchMail");
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
