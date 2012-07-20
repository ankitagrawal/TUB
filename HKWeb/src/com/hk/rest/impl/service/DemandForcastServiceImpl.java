package com.hk.rest.impl.service;

import com.hk.rest.pact.service.DemandForcastService;
import com.hk.domain.warehouse.DemandForcast;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.inventory.DemandForcastDao;
import com.hk.pact.service.core.WarehouseService;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

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
public class DemandForcastServiceImpl implements DemandForcastService {

    private static Logger logger = LoggerFactory.getLogger(DemandForcastServiceImpl.class);

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private DemandForcastDao demandforcastDao;

    public BaseDao getBaseDao(){
        return this.baseDao;
    }

    public void SaveOrUpdateForecastInDB (List<DemandForcast> excelInput, Date minForecastDate){
        /***
         * A) sort dfList on forecastDate
         * b) get minForecastDate nad maxForecastDate from A
         * c) get list of exisiting rows between max and min date, this is input list as passed currently
         * build input list
         */
        Collection<DemandForcast> CollectionToUpdate = new ArrayList<DemandForcast>();
        boolean set=false;

        List<DemandForcast> existingInDB = demandforcastDao.findByDate(minForecastDate);
        //DemandForcast dForecast = new DemandForcast();
        for (DemandForcast dfExcel : excelInput){
            Date forcast_date = dfExcel.getForcastDate();
            String prod_variantId = dfExcel.getProductVariant().getId();
            Long warehouseId = dfExcel.getWarehouse().getId();
            Double forcastVal = dfExcel.getForcastValue();

            try{
            //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //Date date = (Date)formatter.parse(forcast_date);

            if (existingInDB.size() != 0){
             for (DemandForcast df : existingInDB ){
              if (df.getForcastDate().equals(forcast_date) && df.getProductVariant().getId().equals(prod_variantId) && df.getWarehouse().getId().equals(warehouseId)){
                df.setForcastValue(forcastVal);
                CollectionToUpdate.add(df);
                  //getBaseDao().save(df);
                set=true;
                break;
                }
              }
            }
                 
            if (set == false){
                /*
                dForecast.setForcastDate(date);
                dForecast.setProductVariant(getBaseDao().get(ProductVariant.class,prod_variantId));
                dForecast.setWarehouse(warehouseService.getWarehouseById(warehouseId));  //ask - long operation
                dForecast.setForcastValue(forcastVal);
                */
                //getBaseDao().save(dForecast);
                CollectionToUpdate.add(dfExcel);
                set = false;
                }
            }
                  //getBaseDao().update(ob);            // needs Id value also
            catch(Exception e){
                logger.error(e.getMessage());
            }
        }
        getBaseDao().saveOrUpdate(CollectionToUpdate);
            /*
            catch (java.text.ParseException pe){
           logger.error(pe.getMessage(),0);
        }
        */
    }
}
