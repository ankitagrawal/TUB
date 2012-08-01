package com.hk.admin.impl.task.dbmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.task.TaskService;
import com.hk.admin.util.XslParser;
import com.hk.domain.user.User;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.UserService;
import com.hk.constants.core.Keys;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.ForwardResolution;


/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jun 12, 2012
 * Time: 5:26:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DBMasterServiceImpl implements TaskService{
    
    private static Logger Logger                  = LoggerFactory.getLogger(DBMasterServiceImpl.class);

  @Autowired
  MasterDataService masterDataService;

  @Autowired
  private UserService userService;

  @Autowired
  private ProductCatalogServiceImpl productCatalogServiceImpl;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String                          adminUploadsPath;

  @Autowired
  private XslParser xslParser;

  @Override
  public boolean execute(String masterData) {
    boolean isSuccessful = false;
    if (!(masterData.equals("static") || masterData.equals("both") || masterData.equals("catalog"))) {
      throw new IllegalArgumentException("acceptable values for argument 1 are static, catalog or both (defaults to static)");
    }


/*    BasicConfigurator.configure();
    Logger.getRootLogger().setLevel(Level.ERROR);
    Logger.getLogger("db").setLevel(Level.DEBUG);
    Logger.getLogger("mhc.util").setLevel(Level.INFO);
    Logger.getLogger("app.script").setLevel(Level.DEBUG);
    Logger.getLogger("mhc.service").setLevel(Level.INFO);*/




    try{
      if ("static".equals(masterData) || "both".equals(masterData)) {
        masterDataService.insert();
        isSuccessful = true;
      }

      if ("catalog".equals(masterData) || "both".equals(masterData)) {
           String excelFilePath;
           File excelFile;
           List<String> categoryList = new ArrayList<String>();
           categoryList.add("baby");
           categoryList.add("beauty");
           for (String category : categoryList){
             excelFilePath = adminUploadsPath + "/DBDumpFiles/" + category + ".xls"; //change path to a dir with constant files
             excelFile = new File(excelFilePath);
             getProductManager().insertCatalogue(excelFile, null);
           }
          /*
            User loggedOnUser = null;

            if (getPrincipal() != null) {
                loggedOnUser = getUserService().getUserById(getPrincipal().getId());
            }
            try {
                Set<Product> productSet = getXslParser().readProductList(excelFile, loggedOnUser);
            } catch (Exception e) {
                Logger.error("Exception while reading excel sheet.", e);
               }
               */

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

        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_eye, null);
        batchProcessWorkManager.endWork();

        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_homeHealthDevices, null);
        batchProcessWorkManager.endWork();


        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_nutrition, null);
        batchProcessWorkManager.endWork();


        batchProcessWorkManager.beginWork();
        productCatalogService.insertCatalogue(catalog_personalCare, null);
        batchProcessWorkManager.endWork();
      }
      
*/

    }catch (Exception e){
      Logger.debug("Unable to run db master: ",e);
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
}
