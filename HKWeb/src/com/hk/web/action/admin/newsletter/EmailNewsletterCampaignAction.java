package com.hk.web.action.admin.newsletter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hk.domain.review.Mail;
import com.hk.pact.service.review.MailService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.email.AdminEmailCampaignService;
import com.hk.constants.core.Keys;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.util.FtlUtils;
import com.hk.util.HKFileUtils;

@Component
public class EmailNewsletterCampaignAction extends BaseAction {

  @ValidateNestedProperties({
      @Validate(field = "name", required = true),
      @Validate(field = "subject", required = true),
      @Validate(field = "minDayGap", required = true)
  })

  private static Logger logger = LoggerFactory.getLogger(EmailNewsletterCampaignAction.class);
  EmailCampaign emailCampaign;

  @Autowired
  EmailCampaignDao emailCampaignDao;

  @Autowired
  AdminEmailCampaignService adminEmailCampaignService;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String adminUploadsPath;

  String appBasePath;
  FileBean contentBean;
  String ftlContents;
  String htmlContents;
  String htmlPath;
  String name;
//  String contentFolderName;
  Boolean ftlGenerated = Boolean.FALSE;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/createEmailNewsletterAmazon.jsp");
  }

  public Resolution create() {
    emailCampaign.setHtmlPath(FtlUtils.getBasicAmazonS3Path() + HKFileUtils.getPathAfterSubstring(htmlPath, "emailContentFiles"));
    emailCampaign.setTemplateFtl(ftlContents);

    emailCampaign = emailCampaignDao.save(emailCampaign);
    addRedirectAlertMessage(new SimpleMessage("Email campaign has been saved"));
    return new ForwardResolution(EmailNewsletterAdmin.class);
  }

  public Resolution generateFtlAndUploadData() {
    File contentZipFolder;
    File contentFolder;
    try {
      if (contentBean != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String contentZipPath = adminUploadsPath + "/emailContentFiles/" + name + "-"+ sdf.format(new Date()) + ".zip";
        contentZipFolder = new File(contentZipPath);
        contentZipFolder.getParentFile().mkdirs();
        contentBean.save(contentZipFolder);

        String contentPath = contentZipPath.replaceAll("\\.zip", "");
        contentFolder = HKFileUtils.unzipFolder(contentZipPath, contentPath);
        if (contentFolder != null) {
//          contentFolderName = contentFolder.getName();
          logger.info("trying to generate a .ftl file from the .html file to be uploaded.");
          String[] extensions= {"html","htm"};
          File[] htmlFiles = FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(contentFolder,extensions,true));
          if (htmlFiles != null) {
            if (htmlFiles.length == 1) {
              htmlPath = htmlFiles[0].getAbsolutePath();
              logger.debug(htmlFiles[0].getAbsolutePath());
            } else if (htmlFiles.length > 1) {
              addRedirectAlertMessage(new SimpleMessage("The zip folder contains multiple .html files for the email campaign. Kindly check into the same"));
              return new ForwardResolution(EmailNewsletterCampaignAction.class);
            } else {
              addRedirectAlertMessage(new SimpleMessage("The zip folder does not contain any .html file for the email campaign. Kindly check into the same"));
              return new ForwardResolution(EmailNewsletterCampaignAction.class);
            }
          }

          String ftlPath = adminUploadsPath + "/emailContentFiles/" + name + ".ftl";
          File ftlFile = FtlUtils.generateFtlFromHtml(htmlFiles[0], ftlPath,contentFolder.getName());
          //making changes in the .html file also
          FileUtils.copyFile(ftlFile, htmlFiles[0]);
          ftlContents = HKFileUtils.fileToString(ftlFile);
          ftlGenerated = Boolean.TRUE;
          logger.info("ftl generated");

          adminEmailCampaignService.uploadEmailContent(contentFolder);
          logger.info("uploaded email content to s3.");
          FileUtils.deleteDirectory(contentFolder);
          FileUtils.deleteQuietly(contentZipFolder);
          FileUtils.deleteQuietly(ftlFile);
          return new ForwardResolution(EmailNewsletterCampaignAction.class, "pre");
        }
        addRedirectAlertMessage(new SimpleMessage("Error while unzipping folder: " + contentBean.getFileName()));
      } else {
        addRedirectAlertMessage(new SimpleMessage("Please select a .zip folder to be uploaded!"));
      }
      return new ForwardResolution(EmailNewsletterCampaignAction.class, "pre");
    } catch (IOException ioe) {
      logger.error("ftl generation failed: " + ioe);
    }
    addRedirectAlertMessage(new SimpleMessage("Error generating ftl"));
    return new ForwardResolution(EmailNewsletterCampaignAction.class, "pre");
  }

  public Resolution editEmailCampaign() {
    File htmlFile = new File(adminUploadsPath + "/emailContentFiles/emailer.html");

    try {
      URL amazonUrl = new URL(emailCampaign.getHtmlPath());
      FileUtils.copyURLToFile(amazonUrl, htmlFile);
      htmlContents = HKFileUtils.fileToString(htmlFile);
      return new ForwardResolution("/pages/admin/newsletter/editEmailNewsletterAmazon.jsp");
    } catch (IOException ioe) {
      logger.error("error while fetching html file from amazon S3: " + ioe);
    } finally {
      FileUtils.deleteQuietly(htmlFile);
    }
    addRedirectAlertMessage(new SimpleMessage("Error while fetching html file from amazon S3. Please try again after some time."));
    return new ForwardResolution(SendEmailNewsletterCampaign.class);
  }

  public Resolution saveEmailCampaign() {
    String amazonHtmlPath = emailCampaign.getHtmlPath();
    String htmlKey = amazonHtmlPath.replaceAll("(.*\\.s3\\.amazonaws\\.com/)", "");
    File htmlFile = new File(adminUploadsPath + "/emailContentFiles/emailer.html");
    try {
      FileUtils.writeStringToFile(htmlFile, htmlContents);

      adminEmailCampaignService.uploadHtml(htmlFile, htmlKey);
      logger.info("uploaded changed html to s3 for email campaign: " + emailCampaign.getName());

      emailCampaign = emailCampaignDao.save(emailCampaign);
      addRedirectAlertMessage(new SimpleMessage("changes to the email campaign: " + emailCampaign.getName() + " have been saved."));
      return new ForwardResolution(SendEmailNewsletterCampaign.class);
    } catch (IOException ioe) {
      logger.error("Error writing contents to html file: " + ioe);
    } finally {
      FileUtils.deleteQuietly(htmlFile);
    }
    addRedirectAlertMessage(new SimpleMessage("Error writing contents to html file!"));
    return new ForwardResolution(EmailNewsletterCampaignAction.class, "editEmailCampaign");
  }


    public EmailCampaign getEmailCampaign() {
    return emailCampaign;
  }

  public void setEmailCampaign(EmailCampaign emailCampaign) {
    this.emailCampaign = emailCampaign;
  }

  public void setContentBean(FileBean contentBean) {
    this.contentBean = contentBean;
  }

  public String getFtlContents() {
    return ftlContents;
  }

  public void setFtlContents(String ftlContents) {
    this.ftlContents = ftlContents;
  }

  public Boolean isFtlGenerated() {
    return ftlGenerated;
  }

  public Boolean getFtlGenerated() {
    return ftlGenerated;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /*public String getContentFolderName() {
    return contentFolderName;
  }

  public void setContentFolderName(String contentFolderName) {
    this.contentFolderName = contentFolderName;
  }    */

  public String getHtmlContents() {
    return htmlContents;
  }

  public void setHtmlContents(String htmlContents) {
    this.htmlContents = htmlContents;
  }

  public String getHtmlPath() {
    return htmlPath;
  }

  public void setHtmlPath(String htmlPath) {
    this.htmlPath = htmlPath;
  }
}
