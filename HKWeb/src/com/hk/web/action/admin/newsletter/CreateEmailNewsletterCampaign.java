package com.hk.web.action.admin.newsletter;

import java.io.*;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.NoSuchAlgorithmException;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.jets3t.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.solr.common.util.FileUtils;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.constants.core.Keys;

@Component
public class CreateEmailNewsletterCampaign extends BaseAction {

  //  @ValidateNestedProperties({@Validate(field = "name", required = true), @Validate(field = "template", required = true), @Validate(field = "minDayGap", required = true)})
  @ValidateNestedProperties({
      @Validate(field = "name", required = true),
      @Validate(field = "minDayGap", required = true)
  })

  private static Logger logger = LoggerFactory.getLogger(CreateEmailNewsletterCampaign.class);
  EmailCampaign emailCampaign;

  public static String awsBucket = "healthkart-rimal";

  @Autowired
  EmailCampaignDao emailCampaignDao;

  @Autowired
  EmailCampaignService emailCampaignService;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String adminUploadsPath;
  String appBasePath;
  FileBean contentBean;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/createEmailNewsletterAmazon.jsp");
  }

  public Resolution create() throws Exception {
//    String contentZipPath = adminUploadsPath + "/emailContentFiles/" + emailCampaign.getName() + ".zip";
    String contentZipPath = adminUploadsPath + "/emailContentFiles/" + contentBean.getFileName();
    File contentZipFolder = new File(contentZipPath);
    contentZipFolder.getParentFile().mkdirs();
    contentBean.save(contentZipFolder);

    int buffer = 90000;
    byte data[] = new byte[buffer];
//    String contentPath = adminUploadsPath + "/emailContentFiles/" + emailCampaign.getName();
    String contentPath = contentZipPath.replaceAll("\\.zip", "");
    File contentFolder = new File(contentPath);
    if (!contentFolder.exists()) {
      contentFolder.mkdir();
    }

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
    if (htmlFile != null && htmlFile.length > 0) {
      logger.debug(htmlFile[0].getAbsolutePath());
    } else {
      addRedirectAlertMessage(new SimpleMessage("The zip folder does not contain any .html file for the email campaign. Kindly check into the same"));
      return new ForwardResolution(CreateEmailNewsletterCampaign.class);
    }

    String line;
    String lineSeperator = System.getProperty("line.separator");
    File ftlFile = new File(contentPath + "/" + emailCampaign.getName() + ".ftl");
    BufferedReader br = new BufferedReader(new FileReader(htmlFile[0]));
    PrintWriter out = new PrintWriter(new FileWriter(ftlFile));
    while ((line = br.readLine()) != null) {
      //replacing relative paths with absolute paths
//      line = line.replaceAll("(<img src=\")(.*\")", "$1http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder.getName() + "/$2");
//      line = line.replaceAll("(<img .*)(src=\")(.*\")", "$1$2http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder.getName() + "/$3");
      line = line.replaceAll("(<img .*)(src=\"(?!http))(.*\")", "$1$2http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder.getName() + "/$3");
      line = line.replaceAll("(background-image:url\\((?!http))(.*)\"", "$1http://" + awsBucket + ".s3.amazonaws.com/" + contentFolder.getName() + "/$2\"");

      //adding "can't view this email and unsubsribe link" div to the ftl
      if (line.matches("<body .*>")) {
        out.write(line + lineSeperator);
        out.write("<div style=\"font-size:11px; text-align:center; color:#000000; padding:15px\">Can't view this email? <a href=\"emailer.html\">Click here</a> to view a web version.</div>" + lineSeperator);
      } else if (line.matches("</body>")) {
        out.write("<div align=\"center\" valign=\"middle\" style=\"border-top: solid #97b8ca 1px; font-size:11px; text-align:center; color:#666666; padding:10px\"> If you prefer not to receive HealthKart.com email, <a href=\"${unsubscribeLink}\">click here to Unsubscribe</a><br />\n" +
            "      Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br />\n" +
            "    © 2011 HealthKart.com. All Rights Reserved. </div>" + lineSeperator);
        out.write(line + lineSeperator);
      } else {
        out.write(line + lineSeperator);
      }
    }

    br.close();
    out.close();

    //making changes in the .html file also
    FileUtils.copyFile(ftlFile, htmlFile[0]);

    emailCampaignService.uploadEmailContent(contentFolder, emailCampaign);
    logger.info("uploaded email content to s3.");
    return null;
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
}
