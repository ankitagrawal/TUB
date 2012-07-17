package com.hk.rest.impl.service;

import com.hk.rest.pact.service.DemandForcastService;
import com.hk.domain.warehouse.DemandForcast;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;
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

    public BaseDao getBaseDao()
    {
        return this.baseDao;
    }

    public void InsertInDB(List<String> input, List<DemandForcast> dfList)
    {
        boolean set=false;
        DemandForcast dForcast = new DemandForcast();

        String forcast_date = input.get(0);
        String prod_varientId = input.get(1);
        String warehouseId = input.get(2);
        String forcastVal = input.get(3);

        try{
        //DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)formatter.parse(forcast_date);

        if (dfList.size() != 0)
                {
                    for (DemandForcast df : dfList )
                    {                          
                        //formatter = new SimpleDateFormat("yyyy-MM-dd");
                        //Date test=  (Date)formatter.parse(df.getForcastDate().toString());                        
                        if (df.getForcastDate().equals(date) && df.getProductVariant().getId().equals(prod_varientId))
                        {
                            df.setForcastValue(Double.parseDouble(forcastVal));
                            getBaseDao().save(df);
                            set=true;
                            break;
                        }
                    }
                }
                 
                if (set == false)
                {
                    dForcast.setForcastDate(date);
                    dForcast.setProductVariant(getBaseDao().get(ProductVariant.class,prod_varientId));
                    dForcast.setWarehouse(getBaseDao().get(Warehouse.class,Long.parseLong(warehouseId)));
                    dForcast.setForcastValue(Double.parseDouble(forcastVal));
                    getBaseDao().save(dForcast);
                    set = false;
                }
                  //getBaseDao().update(ob);            // needs Id value also
                 
              }
        catch (java.text.ParseException pe){
           logger.error(pe.getMessage(),0);
        }
        catch(Exception e){      
            logger.error(e.getMessage());
        }
  }
}
