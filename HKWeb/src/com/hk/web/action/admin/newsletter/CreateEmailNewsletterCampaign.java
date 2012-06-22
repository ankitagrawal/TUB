package com.hk.web.action.admin.newsletter;

import java.io.*;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

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

@Component
public class CreateEmailNewsletterCampaign extends BaseAction {

  @ValidateNestedProperties({
      @Validate(field = "name", required = true),
      @Validate(field = "subject", required = true),
      @Validate(field = "minDayGap", required = true)
  })

  private static Logger logger = LoggerFactory.getLogger(CreateEmailNewsletterCampaign.class);
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
    return new ForwardResolution(CreateEmailNewsletterCampaign.class,"pre");
  }

  public Resolution generateFtlAndUploadData() throws Exception {
    String contentZipPath = adminUploadsPath + "/emailContentFiles/" + contentBean.getFileName();
    File contentZipFolder = new File(contentZipPath);
    contentZipFolder.getParentFile().mkdirs();
    contentBean.save(contentZipFolder);

    int buffer = 90000;
    byte data[] = new byte[buffer];
    String contentPath = contentZipPath.replaceAll("\\.zip", "");
    File contentFolder = new File(contentPath);
    if (!contentFolder.exists()) {
      contentFolder.mkdir();
    }

    contentFolderName = contentFolder.getName();

    //get zip file content
    ZipInputStream zis = new ZipInputStream(new FileInputStream(contentZipPath));
    //get zipped file list entry
    ZipEntry ze = zis.getNextEntry();

    while (ze != null) {
      String fileName = ze.getName();
      File newFile = new File(contentPath + "/" + fileName);

      //checking is the zipped entry is a folder
      if (ze.isDirectory()) {
        (newFile).mkdir();
      } else {
        newFile.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(newFile);

        int len;
        while ((len = zis.read(data)) > 0) {
          fos.write(data, 0, len);
        }
        fos.close();
      }
      ze = zis.getNextEntry();
    }
    zis.closeEntry();
    zis.close();

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
        return new ForwardResolution(CreateEmailNewsletterCampaign.class);
      } else {
        addRedirectAlertMessage(new SimpleMessage("The zip folder does not contain any .html file for the email campaign. Kindly check into the same"));
        return new ForwardResolution(CreateEmailNewsletterCampaign.class);
      }
    }

    String line;
    String lineSeperator = System.getProperty("line.separator");
    File ftlFile = new File(contentPath + "/" + name + ".ftl");
    BufferedReader br = new BufferedReader(new FileReader(htmlFile[0]));
    PrintWriter out = new PrintWriter(new FileWriter(ftlFile));
    while ((line = br.readLine()) != null) {
      //replacing relative paths with absolute paths
      line = line.replaceAll("(<img .*)(src=\"(?!http))(.*\")", "$1$2" + getBasicAmazonS3Path(contentFolder.getName()) + "$3");
      line = line.replaceAll("(background-image:url\\((?!http))(.*)\"", "$1" + getBasicAmazonS3Path(contentFolder.getName()) + "$2\"");

      //adding "can't view this email and unsubsribe link" div to the ftl
      if (line.matches("<body .*>")) {
        out.write(line + lineSeperator);
        out.write(getCantViewEmailDiv(contentFolder.getName()) + lineSeperator);
      } else if (line.matches("</body>")) {
        out.write(getUnsubscribeEmailDiv() + lineSeperator);
        out.write(line + lineSeperator);
      } else {
        out.write(line + lineSeperator);
      }
    }

    br.close();
    out.close();

    //making changes in the .html file also
    FileUtils.copyFile(ftlFile, htmlFile[0]);
    ftlContents = fileToString(ftlFile);
    ftlGenerated = Boolean.TRUE;
    logger.info("ftl generated");

    emailCampaignService.uploadEmailContent(contentFolder);
    logger.info("uploaded email content to s3.");

    return new ForwardResolution(CreateEmailNewsletterCampaign.class, "pre");
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

  private static String fileToString(File file) {
    byte[] fileBytes = new byte[0];
    try {
      byte[] buffer = new byte[4096];
      ByteArrayOutputStream outs = new ByteArrayOutputStream();
      InputStream ins = new FileInputStream(file);

      int read = 0;
      while ((read = ins.read(buffer)) != -1) {
        outs.write(buffer, 0, read);
      }

      ins.close();
      outs.close();
      fileBytes = outs.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String(fileBytes);
  }

  private static String getBasicAmazonS3Path(String contentFolder) {
    return "http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder + "/";
  }

  private static String getCantViewEmailDiv(String contentFolder) {
    return "<div style=\"font-size:11px; text-align:center; color:#000000; padding:15px\">" +
        "Can't view this email? " +
        "<a href=\"http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder + "/emailer.html\">Click here</a> " +
        "to view a web version." +
        "</div>";
  }

  private static String getUnsubscribeEmailDiv() {
    return "<div align=\"center\" valign=\"middle\" style=\"border-top: solid #97b8ca 1px; font-size:11px; text-align:center; color:#666666; padding:10px\">" +
        " If you prefer not to receive HealthKart.com email, <a href=\"${unsubscribeLink}\">click here to Unsubscribe</a>" +
        "<br />  Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br />\n" +
        "    © 2011 HealthKart.com. All Rights Reserved. </div>";
  }
}
