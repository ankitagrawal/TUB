package com.hk.rest.impl.service;

import com.hk.rest.pact.service.DemandForecastService;
import com.hk.domain.warehouse.DemandForecast;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.inventory.DemandForecastDao;
import com.hk.pact.service.core.WarehouseService;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 16, 2012
 * Time: 1:40:58 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    private static Logger logger = LoggerFactory.getLogger(DemandForecastServiceImpl.class);

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private DemandForecastDao demandforecastDao;

    public BaseDao getBaseDao(){
        return this.baseDao;
    }

    public void SaveOrUpdateForecastInDB (List<DemandForecast> excelInput, Date minForecastDate){
        /***
         * A) sort dfList on forecastDate
         * b) get minForecastDate nad maxForecastDate from A
         * c) get list of exisiting rows between max and min date, this is input list as passed currently
         * build input list
         */
        Collection<DemandForecast> CollectionToUpdate = new ArrayList<DemandForecast>();
        boolean set=false;
        try{
        List<DemandForecast> existingInDB = demandforecastDao.findDemandforecastByDate(minForecastDate);
        
        for (DemandForecast dfExcel : excelInput){
            set=false;
            Date forecast_date = dfExcel.getforecastDate();
            String prod_variantId = dfExcel.getProductVariant().getId();
            Long warehouseId = dfExcel.getWarehouse().getId();
            Double forecastVal = dfExcel.getforecastValue();

            if (existingInDB.size() != 0){
             for (DemandForecast df : existingInDB ){
              if (df.getforecastDate().equals(forecast_date) && df.getProductVariant().getId().equals(prod_variantId) && df.getWarehouse().getId().equals(warehouseId)){
                df.setforecastValue(forecastVal);
                CollectionToUpdate.add(df);
                set=true;
                break;
                }
              }
            }
                 
            if (set == false){
                //getBaseDao().save(dForecast);
                CollectionToUpdate.add(dfExcel);
                }
            }

        getBaseDao().saveOrUpdate(CollectionToUpdate);
        }
         catch(Exception e){
                logger.error(e.getMessage());
            }
    }

    public boolean doesProductVariantExist(String variantId){
         return demandforecastDao.doesProductVariantExist(variantId);
    }

}
