
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    @Validate(required = true, on = {"createMailTemplate"})
    private FileBean contentBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;


    private boolean editTemplate = false, contentUploaded = false;
    String htmlPath,ftlContents;

    private String starImageBinary = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAK8AAACvABQqw0mAAAABx0RVh0U29mdHdhcmUAQWRvYmUgRmlyZXdvcmtzIENTNAay06AAAAInSURBVDiNnZI/TBNhGMZ/vV7va3vXu4otYCSNxYIJhBhDAiZUEjdlcXJhAMKkCXFycyNOmJgYZiMDAWTBxdGlaU10hiJaSgKKloZE24Y7Kr3PgT8BQ5vos715/rxvvueDBsikU5cz6VRPI43aiASmgBhwq55AabA9GQqZw5FINJlJp4b/KSCTTnmB6eaWFqLRKMBUJp0yztN6/jL2Aq3AndbWSxPhC2GkdCmVynzf3p4DngOVgeTg2klAJp1KAO+AmK4b+DQflhVGDwbZrzpIKdE0DcepUtzZwXVr2LYN8AO4pwwkB3P5/MZ0KBSi81onbW1tBAICZ98GQEqJbduoqpf2q+10dfdgmibZ1ezrgeTgRy/A0tKb9zf7+2JCiBuqquI4NlJyElCrHeA4NgcHv9n+9pXl5ZW3o2PjI3CqxpXV1QemZcWFJm77g36qVQePRwEktVoNVfVRKe+Rz298yOXW75/7iDOvXsbj8Sv5REeCSqWConhwXYmULoYRIvdlnc3NrdjI6NjWuTU2NTUlgvphW16vghCCQCCAqh4equs6VtjqOO05EyCE6DF0AyECBIMG5fIev36WEELH7w9iWiZC0/pOe858ZVX1deuGQaFQYKdQyDmO80xKKsVi8Ulzc0vXxUgEn0/rrxsgpXv989qn/O7u7ovJyaczK9ls6YiaW5iffRiJRB8ritJLPczPzY7XJY+wuLjwaGjobvh49sjjwv8TfwCXtcZoRHu4ugAAAABJRU5ErkJggg==";
    private String reviewLink = "<a href=\"${review_Link}&amp;starRating=1\" style=\"text-decoration:none;\"><img style =\"font-size:20px; font-weight:bold; vertical-align:middle;\" title=\"Bad\" alt=\"*\" src=\""+starImageBinary+"\" /></a> <a href=\"${review_Link}&amp;starRating=2\" style=\"text-decoration:none;\"><img style =\"font-size:20px; font-weight:bold; vertical-align:middle;\" title=\"Poor\" alt=\"*\" src=\""+starImageBinary+"\" /></a> <a href=\"${review_Link}&amp;starRating=3\" style=\"text-decoration:none;\"><img style =\"font-size:20px; font-weight:bold; vertical-align:middle;\" title=\"Regular\" alt=\"*\" src=\""+starImageBinary+"\" /></a> <a href=\"${review_Link}&amp;starRating=4\" style=\"text-decoration:none;\"><img style =\"font-size:20px; font-weight:bold; vertical-align:middle;\" title=\"Good\" alt=\"*\" src=\""+starImageBinary+"\" /></a> <a href=\"${review_Link}&amp;starRating=5\" style=\"text-decoration:none;\"><img style =\"font-size:20px; font-weight:bold; vertical-align:middle;\" title=\"Excellent\" alt=\"*\" src=\""+starImageBinary+"\" /></a>";


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
        Mail priorMail = mailService.getMailByName(mail.getName());
        if(priorMail != null){
            if(!editTemplate){
                addRedirectAlertMessage(new SimpleMessage(mail.getName() +" Template already  exist."));
                return new RedirectResolution(CreateMailTemplateAction.class, "pre");
            }else
                mail = priorMail;
        }
        File contentZipFolder=null;
        File contentFolder=null;
        try {
            if (contentBean != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String contentZipPath = adminUploadsPath + "/emailContentFiles/reviewCollection/" + mail.getName() + "-"+ sdf.format(new Date()) + ".zip";
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
                            logger.info(htmlFiles[0].getAbsolutePath());
                        } else if (htmlFiles.length > 1) {
                            addRedirectAlertMessage(new SimpleMessage("The zip folder contains multiple .html files for the email campaign. Kindly check into the same"));
                            return new ForwardResolution(CreateMailTemplateAction.class,"pre");
                        } else {
                            addRedirectAlertMessage(new SimpleMessage("The zip folder does not contain any .html file for the email campaign. Kindly check into the same"));
                            return new ForwardResolution(CreateMailTemplateAction.class,"pre");
                        }
                    }

                    String ftlPath = adminUploadsPath + "/emailContentFiles/reviewCollection/" + mail.getName() + ".ftl";
                    File ftlFile = FtlUtils.generateFtlFromHtml(htmlFiles[0], ftlPath,contentFolder.getName());

                    String htmlContent = HKFileUtils.fileToString(ftlFile);
                    htmlContent = StringUtils.replace(htmlContent, "${reviewLink}",reviewLink);
                    FileUtils.writeStringToFile(ftlFile,htmlContent);


                    //making changes in the .html file also
                    FileUtils.copyFile(ftlFile, htmlFiles[0]);
                    ftlContents = HKFileUtils.fileToString(ftlFile);
                    logger.info("ftl generated");

                    contentUploaded = adminEmailCampaignService.uploadEmailContent(contentFolder);
//                  contentUploaded = true;

                    FileUtils.deleteQuietly(ftlFile);
                    if(contentUploaded){
                        logger.info("uploaded email content to s3.");
                        mail.setContent(ftlContents);
                        mail.setAmazonFilePath(FtlUtils.getBasicAmazonS3Path() + HKFileUtils.getPathAfterSubstring(htmlPath, "emailContentFiles"));
                        mailService.save(mail);
                        addRedirectAlertMessage(new SimpleMessage("ftl generated and upload to s3"));
                        return new RedirectResolution(ReviewMailSettingsAction.class, "pre");
                    }
                    addRedirectAlertMessage(new SimpleMessage("error in  uploading to s3"));
                    return new ForwardResolution(CreateMailTemplateAction.class, "pre");
                }
                addRedirectAlertMessage(new SimpleMessage("Error while unzipping folder: " + contentBean.getFileName()));
            } else {
                addRedirectAlertMessage(new SimpleMessage("Please select a .zip folder to be uploaded!"));
            }
            return new ForwardResolution(CreateMailTemplateAction.class, "pre");
        } catch (IOException ioe) {
            logger.info("ftl generation failed: " + ioe);
        }finally{
            if(contentFolder != null || contentZipFolder != null){
                try{
                    FileUtils.deleteDirectory(contentFolder);
                }catch (IOException ioe){
                    logger.info("Error in deleting file" + ioe);
                }
                FileUtils.deleteQuietly(contentZipFolder);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Error generating ftl"));
        return new ForwardResolution(CreateMailTemplateAction.class, "pre");
    }

    public Resolution saveMailTemplate(){
        Mail priorMail = mailService.getMailById(mail.getId());
        mail.setAmazonFilePath(priorMail.getAmazonFilePath());

        if(contentBean != null)
            return new ForwardResolution(CreateMailTemplateAction.class, "createMailTemplate");

        String amazonHtmlPath = mail.getAmazonFilePath();
        String htmlKey = amazonHtmlPath.replaceAll("(.*\\.s3\\.amazonaws\\.com/)", "");
        File htmlFile = new File(adminUploadsPath + "/emailContentFiles/emailer.html");
        editTemplate = false;
        try {
            String htmlcontent = mail.getContent();
            htmlcontent = StringUtils.replace(htmlcontent, "${reviewLink}",reviewLink);
            FileUtils.writeStringToFile(htmlFile, htmlcontent);
            mail.setContent(htmlcontent);
            adminEmailCampaignService.uploadHtml(htmlFile, htmlKey);
            logger.info("uploaded changed html to s3 for mail Template: " + mail.getName());

            mail = mailService.save(mail);
            addRedirectAlertMessage(new SimpleMessage("changes to the mail template: " + mail.getName() + " have been saved."));
            return new RedirectResolution(ReviewMailSettingsAction.class);
        } catch (IOException ioe) {
            logger.info("Error writing contents to html file: " + ioe);
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
