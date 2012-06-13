package com.hk.pact.service.task;

import java.util.List;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TaskService {
  static Logger Logger                  = LoggerFactory.getLogger(TaskService.class);
  static File catalog_baby              = new File("./data/catalog/ProductCatalog-Baby.xls");
  static File catalog_beauty            = new File("./data/catalog/ProductCatalog-Beauty.xls");
  static File catalog_diabetes          = new File("./data/catalog/ProductCatalog-Diabetes.xls");
  static File catalog_eye               = new File("./data/catalog/ProductCatalog-Eye.xls");
  static File catalog_homeHealthDevices = new File("./data/catalog/ProductCatalog-HomeHealthDevices.xls");
  static File catalog_nutrition         = new File("./data/catalog/ProductCatalog-Nutrition.xls");
  static File catalog_personalCare      = new File("./data/catalog/ProductCatalog-PersonalCare.xls");

    public boolean runDbMaster(String masterData) ;
}