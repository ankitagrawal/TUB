package com.hk.rest.impl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.warehouse.DemandForecast;
import com.hk.pact.dao.inventory.DemandForecastDao;
import com.hk.rest.pact.service.DemandForecastService;

/**
 * Created by IntelliJ IDEA. User: Neha Date: Jul 16, 2012 Time: 1:40:58 PM To change this template use File | Settings |
 * File Templates.
 */

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    private static Logger     logger = LoggerFactory.getLogger(DemandForecastServiceImpl.class);

    @Autowired
    private DemandForecastDao demandForecastDao;

    public DemandForecastDao getDemandForecastDao() {
        return demandForecastDao;
    }

    public void saveOrUpdateForecastInDB(List<DemandForecast> excelInput, Date minForecastDate) {

        Collection<DemandForecast> collectionToUpdate = new ArrayList<DemandForecast>();
        boolean set = false;
        try {
            List<DemandForecast> existingInDB = getDemandForecastDao().findDemandforecastByDate(minForecastDate);

            for (DemandForecast dfExcel : excelInput) {
                set = false;
                Date forecastDate = dfExcel.getforecastDate();
                String productVariantId = dfExcel.getProductVariant().getId();
                Long warehouseId = dfExcel.getWarehouse().getId();
                Double forecastVal = dfExcel.getforecastValue();

                if (existingInDB.size() != 0) {
                    for (DemandForecast df : existingInDB) {
                        if (df.getforecastDate().equals(forecastDate) && df.getProductVariant().getId().equals(productVariantId) && df.getWarehouse().getId().equals(warehouseId)) {
                            df.setforecastValue(forecastVal);
                            collectionToUpdate.add(df);
                            set = true;
                            break;
                        }
                    }
                }

                if (set == false) {
                    collectionToUpdate.add(dfExcel);
                }
            }

            getDemandForecastDao().saveOrUpdate(collectionToUpdate);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
