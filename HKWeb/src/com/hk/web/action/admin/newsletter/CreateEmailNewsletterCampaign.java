package com.hk.web.action.admin.newsletter;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.security.NoSuchAlgorithmException;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.jets3t.service.ServiceException;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.constants.core.Keys;
import com.hk.constants.EnumS3UploadStatus;

@Component
public class CreateEmailNewsletterCampaign extends BaseAction {

  //  @ValidateNestedProperties({@Validate(field = "name", required = true), @Validate(field = "template", required = true), @Validate(field = "minDayGap", required = true)})
  @ValidateNestedProperties({
      @Validate(field = "name", required = true),
      @Validate(field = "minDayGap", required = true)
  })

  EmailCampaign emailCampaign;

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
    EnumS3UploadStatus uploadStatus;
    String contentZipPath = adminUploadsPath + "/emailContentFiles/" + emailCampaign.getName() + ".zip";
    File contentZipFolder = new File(contentZipPath);
    contentZipFolder.getParentFile().mkdirs();
    contentBean.save(contentZipFolder);

    int buffer = 90000;
    byte data[] = new byte[buffer];
    String contentPath = adminUploadsPath + "/emailContentFiles/" + emailCampaign.getName();
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

    uploadStatus = emailCampaignService.uploadEmailContent(contentFolder, emailCampaign);
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
