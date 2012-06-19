package com.hk.admin.impl.task.dbmaster;

import com.hk.admin.pact.task.TaskService;
import com.hk.web.BatchProcessWorkManager;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jun 12, 2012
 * Time: 5:26:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DBMasterService implements TaskService{
  @Autowired
  MasterDataService masterDataService;

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
}
