package com.hk.web.action.admin.inventory;

import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.constants.XslConstants;
import com.hk.constants.core.Keys;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.warehouse.DemandForcast;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.rest.pact.service.DemandForcastService;
import com.akube.framework.stripes.action.BaseAction;

import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.SimpleError;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 10, 2012
 * Time: 11:33:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForcastExcelAction extends BaseAction {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ForcastExcelAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                          adminUploadsPath;

    //@Validate(required = true)
    FileBean fileBean;

    @Autowired
    DemandForcastService domainForcastService;

    @Autowired
    private WarehouseService warehouseService;

    @DefaultHandler
    public Resolution pre() {
           return new ForwardResolution("/pages/admin/uploadForcast.jsp");
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public DemandForcastService getDomainForcastService (){
        return domainForcastService;
    }

    public Resolution parse() throws Exception
    {           
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/ForcastFiles/" + sdf.format(new Date()) + "/" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        logger.debug("parsing Demand forcast info : " + excelFile.getAbsolutePath());
        ExcelSheetParser excel = new ExcelSheetParser(excelFile.getAbsolutePath(), "Sheet1");
        Iterator<HKRow> rowiterator = excel.parse();
        int rowCount=1;
        String forecast_date = null;
        Date min_forecastDate = null;
        List<DemandForcast> excelInputList = new ArrayList<DemandForcast>();
        //List<DemandForcast> dfList = getBaseDao().getAll(DemandForcast.class);

        try {
              while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();

                forecast_date = row.getColumnValue("Forecast Date");
                String prod_variantId = row.getColumnValue("Product Variant");
                String warehouseId = row.getColumnValue("Warehouse ID");
                String forecastVal = row.getColumnValue("Forecast Value");
                //error handling for null values

                if(StringUtils.isBlank(forecast_date) || StringUtils.isBlank(prod_variantId) ||
                        StringUtils.isBlank(warehouseId) || StringUtils.isBlank(forecastVal)){
                getContext().getValidationErrors().add("1", new SimpleError("Please correct the excel.Value(s) empty at row "+rowCount+""));
                    /**
                     * TODO:
                     * a) throw an error message blank required field : fail entire upload here
                     * b) validate that variant id does exist
                     * 
                     */
                    //throw new ExcelBlankFieldException("value(s) empty at" + "    ", rowCount);
                }


               /*
                excelInputList.add(forecast_date);
                excelInputList.add(prod_variantId);
                excelInputList.add(warehouseId);
                excelInputList.add(forecastVal);

                 */
                try{
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = (Date)formatter.parse(forecast_date);

                    if(rowCount == 2){
                   min_forecastDate = date;
                }
                DemandForcast dForecast = new DemandForcast();
                dForecast.setForcastDate(date);
                dForecast.setProductVariant(getBaseDao().get(ProductVariant.class,prod_variantId));
                dForecast.setWarehouse(warehouseService.getWarehouseById(Long.parseLong(warehouseId)));  //ask - long operation
                dForecast.setForcastValue(Double.parseDouble(forecastVal));

                excelInputList.add(dForecast);
                }
                catch (java.text.ParseException pe){
                    logger.error(pe.getMessage(),0);
                    }
              }

              //String max_forecastDate = forecast_date;

              getDomainForcastService().SaveOrUpdateForecastInDB(excelInputList,min_forecastDate);
            }
        catch (ExcelBlankFieldException e) {
                logger.debug(e.getMessage());
              //throw new ExcelBlankFieldException(e.getMessage());
        }
        return new RedirectResolution(AdminHomeAction.class);
        }     

    }