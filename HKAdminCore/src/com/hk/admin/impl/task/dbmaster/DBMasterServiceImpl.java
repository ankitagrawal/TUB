package com.hk.admin.impl.task.dbmaster;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.hk.admin.util.helper.XslPincodeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.pact.task.TaskService;
import com.hk.admin.util.ReconciliationVoucherParser;
import com.hk.admin.util.SkuXslParser;
import com.hk.constants.core.Keys;
import com.hk.domain.core.Pincode;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;

/**
 * Created by IntelliJ IDEA. User: user Date: Jun 12, 2012 Time: 5:26:52 PM To change this template use File | Settings |
 * File Templates.
 */
@Service
public class DBMasterServiceImpl implements TaskService {

    private static Logger               logger = LoggerFactory.getLogger(DBMasterServiceImpl.class);

    @Autowired
    MasterDataService                   masterDataService;

    @Autowired
    private UserService                 userService;

    @Autowired
    private ProductCatalogServiceImpl   productCatalogServiceImpl;

    @Autowired
    private SkuService                  skuService;

    @Autowired
    ReconciliationVoucherService        reconciliationVoucherService;

    @Autowired
    PincodeDao                          pincodeDao;

    @Autowired
    BaseDao                             baseDao;

    @Value("#{hkEnvProps['" + Keys.Env.testDataDump + "']}")
    String                              testDataDump;

    @Autowired
    private XslPincodeParser                   xslPincodeParser;

    @Autowired
    private SkuXslParser                skuXslParser;
    @Autowired
    private ReconciliationVoucherParser rvParser;

    @Override
    public boolean execute(String masterData) {
        boolean isSuccessful = false;
        if (!(masterData.equals("static") || masterData.equals("both") || masterData.equals("catalog"))) {
            throw new IllegalArgumentException("acceptable values for argument 1 are static, catalog or both (defaults to static)");
        }

        try {
            if ("static".equals(masterData)) { // || "both".equals(masterData)) {
                masterDataService.insert();
                isSuccessful = true;
            }

            if ("catalog".equals(masterData)) { // || "both".equals(masterData)) {

                String catalogFiles;
                String skuFiles;
                String catalogPath = testDataDump + "/DBDumpCatalogFiles/"; // change path to a dir with constant files
                String skuPath = testDataDump + "/DBDumpSkuFiles/";
                String pincodePath = testDataDump + "/DBDumpPincodeFiles/";
                String reconciliationVoucherPath = testDataDump + "/DBDumpRVFiles/";
                File catalogFolder = new File(catalogPath);
                File skuFolder = new File(skuPath);
                File pincodeFolder = new File(pincodePath);
                File rvFolder = new File(reconciliationVoucherPath);
                File[] listOfCatalogExcels = catalogFolder.listFiles();
                File[] listOfSkuExcels = skuFolder.listFiles();
                File[] listOfPincodeExcels = pincodeFolder.listFiles();
                File[] listOfRVExcels = rvFolder.listFiles();
                isSuccessful = true;

                /* Inserting product catalog for different categories */
                List<Long> a = baseDao.findByQuery("select count(*) from Product");
                if (a.get(0).intValue() == 1) {
                    for (int i = 0; i < listOfCatalogExcels.length; i++) {
                        if (listOfCatalogExcels[i].isFile()) {
                            catalogFiles = listOfCatalogExcels[i].getName();
                            if (catalogFiles.endsWith(".xls") || catalogFiles.endsWith(".XLS")) {
                                try {
                                    getProductManager().insertCatalogue(listOfCatalogExcels[i], null);
                                } catch (Exception e) {
                                    logger.error("Exception while reading catalog excel sheet.", e);
                                    isSuccessful = false;
                                }
                            }
                        }
                    }
                }

                /* Inserting Sku for different categories */
                a = baseDao.findByQuery("select count(*) from Sku");
                if (a.get(0).intValue() == 1) {
                    for (int i = 0; i < listOfSkuExcels.length; i++) {
                        if (listOfSkuExcels[i].isFile()) {
                            skuFiles = listOfSkuExcels[i].getName();
                            if (skuFiles.endsWith(".xls") || skuFiles.endsWith(".XLS")) {
                                try {
                                    Set<Sku> skuSet = skuXslParser.readSKUCatalog(listOfSkuExcels[i]); // doesnt insert
                                    // sku which
                                    // already
                                    // exists in DB
                                    // : fail excel
                                    getSkuServiceImpl().insertSKUs(skuSet);
                                } catch (Exception e) {
                                    logger.error("Exception while reading " + skuFiles + " Sku excel sheet.", e);
                                    isSuccessful = false;
                                }
                            }
                        }
                    }
                }

                /* adding pincodes */
                a = baseDao.findByQuery("select count(*) from Pincode");
                if (a.get(0).intValue() == 1) {
                    for (int i = 0; i < listOfPincodeExcels.length; i++) {
                        String pincodeFile = listOfPincodeExcels[i].getName();
                        if (pincodeFile.endsWith(".xls") || pincodeFile.endsWith(".XLS")) {
                            try {
                                Set<Pincode> pincodeSet = xslPincodeParser.readPincodeList(listOfPincodeExcels[i]);
                                for (Pincode pincode : pincodeSet) {
                                    if (pincode != null)
                                        pincodeDao.save(pincode); // avoided changing the service class
                                    logger.info("inserting or updating:" + pincode.getPincode());
                                }
                            } catch (Exception e) {
                                logger.error("Exception while reading pincode excel sheet.", e);
                                isSuccessful = false;
                            }
                        }
                    }
                }

                /* Creating inventory via reconciliation voucher */
                a = baseDao.findByQuery("select count(*) from ReconciliationVoucher");
                // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
                User loggedOnUser = userService.getLoggedInUser();

                if (a.get(0).intValue() == 0) {
                    for (int i = 0; i < listOfRVExcels.length; i++) {
                        String reconciliationVoucherFile = listOfRVExcels[i].getName();
                        if (reconciliationVoucherFile.endsWith(".xls") || reconciliationVoucherFile.endsWith(".XLS")) {
                            try {
                                ReconciliationVoucher reconciliationVoucher = new ReconciliationVoucher();
                                Warehouse testWarehouse = new Warehouse();
                                testWarehouse.setId(Long.parseLong("1"));
                                reconciliationVoucher.setWarehouse(testWarehouse);
                                // setWarehouse = userService.getWarehouseForLoggedInUser(); //can be null
                                // rvParser.setReconciliationVoucher(reconciliationVoucher); // required for sku table
                                List<RvLineItem> rvLineItems = rvParser.readAndCreateAddRVLineItems(listOfRVExcels[i].getAbsolutePath(), "Sheet1", reconciliationVoucher);
                                reconciliationVoucherService.reconcileAddRV(loggedOnUser, rvLineItems, reconciliationVoucher);

                            } catch (Exception e) {
                                logger.error("Exception while reading reconciliationVoucher excel sheet.", e);
                                isSuccessful = false;
                            }
                        }
                    }
                }
                // isSuccessful = true;

            }

            /*
             * DB CATALOG on hold. Excel files cannot be push to the build if ("catalog".equals(masterData) ||
             * "both".equals(masterData)) { batchProcessWorkManager.beginWork();
             * productCatalogService.insertCatalogue(catalog_baby, null); batchProcessWorkManager.endWork();
             */

        } catch (Exception e) {
            logger.debug("Unable to run db master: ", e);
        }
        return isSuccessful;
    }

    public ProductCatalogServiceImpl getProductManager() {
        return productCatalogServiceImpl;
    }

    public XslPincodeParser getXslPincodeParser() {
        return xslPincodeParser;
    }

    public UserService getUserService() {
        return userService;
    }

    public SkuService getSkuServiceImpl() {
        return skuService;
    }
}
