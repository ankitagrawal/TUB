package com.hk.admin.impl.task.dbmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.task.TaskService;
import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.util.XslParser;
import com.hk.admin.util.SkuXslParser;
import com.hk.admin.util.ReconciliationVoucherParser;

import com.hk.domain.sku.Sku;
import com.hk.domain.core.Pincode;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.constants.core.Keys;
import com.hk.impl.service.inventory.SkuServiceImpl;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jun 12, 2012
 * Time: 5:26:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DBMasterServiceImpl implements TaskService{
    
    private static Logger logger                  = LoggerFactory.getLogger(DBMasterServiceImpl.class);

  @Autowired
  MasterDataService masterDataService;

  @Autowired
  private UserService userService;

  @Autowired
  private ProductCatalogServiceImpl productCatalogServiceImpl;

  @Autowired
  private SkuService skuService;

  @Autowired
  ReconciliationVoucherService reconciliationVoucherService;

  @Autowired
  PincodeDao pincodeDao;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String                          adminUploadsPath;

  @Autowired
  private XslParser xslParser;

  @Autowired
  private SkuXslParser                  skuXslParser;
  @Autowired
  ReconciliationVoucherParser rvParser;

  @Override
  public boolean execute(String masterData) {
    boolean isSuccessful = false;
    if (!(masterData.equals("static") || masterData.equals("both") || masterData.equals("catalog"))) {
      throw new IllegalArgumentException("acceptable values for argument 1 are static, catalog or both (defaults to static)");
    }

    try{
      if ("static".equals(masterData) || "both".equals(masterData)) {
        masterDataService.insert();
        isSuccessful = true;
      }

      if ("catalog".equals(masterData) || "both".equals(masterData)) {
          /*
           String excelFilePath;
           File excelFile;
           List<String> categoryList = new ArrayList<String>();
           categoryList.add("nutrition");
           //categoryList.add("beauty");
           for (String category : categoryList){
             excelFilePath = adminUploadsPath + "/DBDumpFiles/" + category + ".xls"; //change path to a dir with constant files
             excelFile = new File(excelFilePath);
             getProductManager().insertCatalogue(excelFile, null);
           }
            */
        String catalogFiles;
	    String skuFiles;
        String catalogPath = adminUploadsPath + "/DBDumpCatalogFiles/"; //change path to a dir with constant files
	    String skuPath = adminUploadsPath + "/DBDumpSkuFiles/";
        String pincodePath = adminUploadsPath + "/DBDumpPincodeFiles/";
        String reconciliationVoucherPath = adminUploadsPath + "/DBDumpRVFiles/";
        File catalogFolder = new File(catalogPath);
	    File skuFolder = new File(skuPath);
	    File[] listOfCatalogExcels = catalogFolder.listFiles();
	    File[] listOfSkuExcels = skuFolder.listFiles();
        File pincodeExcel = new File(pincodePath);
        File reconciliationVoucherExcel = new File(reconciliationVoucherPath);

	    for (int i = 0; i < listOfCatalogExcels.length; i++) {

		    if (listOfCatalogExcels[i].isFile()) {
			    catalogFiles = listOfCatalogExcels[i].getName();
			    if (catalogFiles.endsWith(".xls") || catalogFiles.endsWith(".XLS")) {

				    getProductManager().insertCatalogue(listOfCatalogExcels[i], null);

			    }
		    }
	    }
	    for (int i = 0; i < listOfSkuExcels.length; i++) {

		    if (listOfSkuExcels[i].isFile()) {
			    skuFiles = listOfSkuExcels[i].getName();
			    if (skuFiles.endsWith(".xls") || skuFiles.endsWith(".XLS")) {
                    Set<Sku>skuSet = skuXslParser.readSKUCatalog(listOfSkuExcels[i]);
				    getSkuServiceImpl().insertSKUs(skuSet);
			    }
		    }
	    }

		String pincodeFile = pincodeExcel.getName();
	    if (pincodeFile.endsWith(".xls") || pincodeFile.endsWith(".XLS")) {
         try {
           Set<Pincode> pincodeSet = xslParser.readPincodeList(pincodeExcel);
           for (Pincode pincode : pincodeSet) {
            if (pincode != null)
               pincodeDao.save(pincode);   //avoided changing the service class
            logger.info("inserting or updating:" + pincode.getPincode());
            }
          } catch (Exception e) {
            logger.error("Exception while reading pincode excel sheet.", e);            
            }
	    }

        String reconciliationVoucherFile = reconciliationVoucherExcel.getName();
	    if (reconciliationVoucherFile.endsWith(".xls") || reconciliationVoucherFile.endsWith(".XLS")) {
         try {
           List<RvLineItem> rvLineItems = rvParser.readAndCreateRVLineItems(reconciliationVoucherPath, "Sheet1");
           ReconciliationVoucher reconciliationVoucher = new ReconciliationVoucher() ;
           reconciliationVoucherService.save(null , rvLineItems, reconciliationVoucher);   

          } catch (Exception e) {
            logger.error("Exception while reading reconciliationVoucher excel sheet.", e);            
            }
	    }
          isSuccessful = true;
         
      }

/* DB CATALOG on hold. Excel files cannot be push to the build

      if ("catalog".equals(masterData) || "both".equals(masterData)) {
        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_baby, null);
        batchProcessWorkManager.endWork();

        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_beauty, null);
        batchProcessWorkManager.endWork();

        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_diabetes, null);
        batchProcessWorkManager.endWork();
      
*/

    }catch (Exception e){
      logger.debug("Unable to run db master: ",e);
    }
    return isSuccessful;
  }

    public ProductCatalogServiceImpl getProductManager() {
        return productCatalogServiceImpl;
    }

    public XslParser getXslParser() {
        return xslParser;
    }

    public UserService getUserService() {
        return userService;
    }

    public SkuService getSkuServiceImpl() {
        return skuService;
    }
}
