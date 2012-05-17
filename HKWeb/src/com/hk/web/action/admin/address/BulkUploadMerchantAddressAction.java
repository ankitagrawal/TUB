package com.hk.web.action.admin.address;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.pact.dao.core.ManufacturerDao;
import com.hk.util.LatLongGenerator;
import com.hk.web.BatchProcessWorkManager;
import com.hk.web.action.admin.catalog.ParseExcelAction;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Nov 10, 2011
 * Time: 11:07:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class BulkUploadMerchantAddressAction extends BaseAction {
  private static Logger logger = LoggerFactory.getLogger(ParseExcelAction.class);

  @Autowired
   BatchProcessWorkManager batchProcessWorkManager;
  @Autowired 
  LatLongGenerator latLongGenerator;
  @Autowired 
  ManufacturerDao manufacturerDao;

   @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
   String adminUploadsPath;

  @Validate(required = true)
  FileBean fileBean;

  @Validate(required = true)
  String manufacturerName;

  public void setFileBean(FileBean fileBean) {
    this.fileBean = fileBean;
  }

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/bulkUploadMerchantAddress.jsp");
  }

  public Resolution parse() throws Exception {
    String excelFilePath = adminUploadsPath + "/merchantAddressFile/" + System.currentTimeMillis() + ".xls";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();
    fileBean.save(excelFile);

    try {
      latLongGenerator.readCsvFile(excelFilePath,manufacturerDao.findByName(manufacturerName));
    } catch (Exception e) {
      logger.error("Exception while reading excel sheet.", e);
      addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
      return new ForwardResolution("/pages/admin/bulkUploadMerchantAddress.jsp");
    }
    excelFile.delete();
    addRedirectAlertMessage(new SimpleMessage("Database Updated"));
    return new ForwardResolution("/pages/admin/bulkUploadMerchantAddress.jsp");
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public void setManufacturerName(String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }
}
