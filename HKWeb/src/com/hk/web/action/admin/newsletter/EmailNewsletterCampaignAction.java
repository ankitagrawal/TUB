package com.hk.web.action.admin.newsletter;

import java.io.*;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.solr.common.util.FileUtils;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.constants.core.Keys;
import com.hk.util.HKFileUtils;
import com.hk.util.FtlUtils;

@Component
public class EmailNewsletterCampaignAction extends BaseAction {

  @ValidateNestedProperties({
      @Validate(field = "name", required = true),
      @Validate(field = "subject", required = true),
      @Validate(field = "minDayGap", required = true)
  })

  private static Logger logger = LoggerFactory.getLogger(EmailNewsletterCampaignAction.class);
  EmailCampaign emailCampaign;

  public static String awsBucket = "healthkart-pratham";

  @Autowired
  EmailCampaignDao emailCampaignDao;

  @Autowired
  EmailCampaignService emailCampaignService;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String adminUploadsPath;
  String appBasePath;
  FileBean contentBean;
  String ftlContents;
  String name;
  String contentFolderName;
  Boolean ftlGenerated = Boolean.FALSE;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/createEmailNewsletterAmazon.jsp");
  }

  public Resolution create() throws Exception {
    emailCampaign.setHtmlPath(getBasicAmazonS3Path(contentFolderName));
    emailCampaign.setTemplateFtl(ftlContents);

    emailCampaign = emailCampaignDao.save(emailCampaign);
    addRedirectAlertMessage(new SimpleMessage("Email campaign has been saved"));
    return new ForwardResolution(EmailNewsletterCampaignAction.class, "pre");
  }

  public Resolution generateFtlAndUploadData() throws Exception {
    String contentZipPath = adminUploadsPath + "/emailContentFiles/" + contentBean.getFileName();
    File contentZipFolder = new File(contentZipPath);
    contentZipFolder.getParentFile().mkdirs();
    contentBean.save(contentZipFolder);

    String contentPath = contentZipPath.replaceAll("\\.zip", "");
    File contentFolder = HKFileUtils.unzipFolder(contentZipPath, contentPath);

    logger.info("trying to generate a .ftl file from the .html file to be uploaded.");
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".html");
      }
    };
    File[] htmlFile = new File(contentPath).listFiles(filter);
    if (htmlFile != null) {
      if (htmlFile.length == 1) {
        logger.debug(htmlFile[0].getAbsolutePath());
      } else if (htmlFile.length > 1) {
        addRedirectAlertMessage(new SimpleMessage("The zip folder contains multiple .html files for the email campaign. Kindly check into the same"));
        return new ForwardResolution(EmailNewsletterCampaignAction.class);
      } else {
        addRedirectAlertMessage(new SimpleMessage("The zip folder does not contain any .html file for the email campaign. Kindly check into the same"));
        return new ForwardResolution(EmailNewsletterCampaignAction.class);
      }
    }

    String ftlPath = contentPath + "/" + name + ".ftl";
    File ftlFile = FtlUtils.generateFtlFromHtml(htmlFile[0],ftlPath,getBasicAmazonS3Path(contentFolder.getName()));

    //making changes in the .html file also
    FileUtils.copyFile(ftlFile, htmlFile[0]);
    ftlContents = HKFileUtils.fileToString(ftlFile);
    ftlGenerated = Boolean.TRUE;
    logger.info("ftl generated");

    emailCampaignService.uploadEmailContent(contentFolder);
    logger.info("uploaded email content to s3.");

    return new ForwardResolution(EmailNewsletterCampaignAction.class, "pre");
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

  public String getContentFolderName() {
    return contentFolderName;
  }

  public void setContentFolderName(String contentFolderName) {
    this.contentFolderName = contentFolderName;
  }

  private static String getBasicAmazonS3Path(String contentFolder) {
    return "http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder + "/";
  }
}
