package com.hk.web.action.admin.inventory;

import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.constants.core.Keys;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.warehouse.DemandForecast;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.rest.pact.service.DemandForecastService;
import com.akube.framework.stripes.action.BaseAction;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 10, 2012
 * Time: 11:33:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForecastExcelAction extends BaseAction implements Comparator<DemandForecast> {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ForecastExcelAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                          adminUploadsPath;

    //@Validate(required = true)
    FileBean fileBean;

    @Autowired
    DemandForecastService domainForecastService;

    @Autowired
    private WarehouseService warehouseService;

    @DefaultHandler
    public Resolution pre() {
           return new ForwardResolution("/pages/admin/uploadForecast.jsp");
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public DemandForecastService getDomainForecastService (){
        return domainForecastService;
    }
    
    public Resolution parse() throws Exception
    {           
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/forecastFiles/" + sdf.format(new Date()) + "/" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        logger.debug("parsing Demand forecast info : " + excelFile.getAbsolutePath());
        ExcelSheetParser excel = new ExcelSheetParser(excelFile.getAbsolutePath(), "Sheet1");
        Iterator<HKRow> rowiterator = excel.parse();
        int rowCount=1;

        Date min_forecastDate = null;
        List<DemandForecast> excelInputList = new ArrayList<DemandForecast>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
              while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();

                String forecast_date = row.getColumnValue("Forecast Date");
                String prod_variantId = row.getColumnValue("Product Variant");
                String warehouseId = row.getColumnValue("Warehouse ID");
                String forecastVal = row.getColumnValue("Forecast Value");
                //error handling for null values

                if(StringUtils.isBlank(forecast_date) || StringUtils.isBlank(prod_variantId) ||
                        StringUtils.isBlank(warehouseId) || StringUtils.isBlank(forecastVal)){
                getContext().getValidationErrors().add("1", new SimpleError("Please correct the excel. Value(s) empty at row "+rowCount+""));
                return new ForwardResolution("/pages/admin/uploadForecast.jsp");
                }

                if (!domainForecastService.doesProductVariantExist(prod_variantId)) {
                    getContext().getValidationErrors().add("1", new SimpleError("No product variant exists with the entered Product Variant at row "+rowCount+""));
                    return new ForwardResolution("/pages/admin/uploadForecast.jsp");
                }
                    /**
                     * TODO:
                     * a) throw an error message blank required field : fail entire upload here
                     * b) validate that variant id does exist
                     * 
                     */
                   
                try{

                Date date = (Date)formatter.parse(forecast_date);

                DemandForecast dForecast = new DemandForecast();
                dForecast.setforecastDate(date);
                dForecast.setProductVariant(getBaseDao().get(ProductVariant.class,prod_variantId));
                dForecast.setWarehouse(warehouseService.getWarehouseById(Long.parseLong(warehouseId)));  //ask - long operation
                dForecast.setforecastValue(Double.parseDouble(forecastVal));

                excelInputList.add(dForecast);
                }
                catch (java.text.ParseException pe){
                    logger.error(pe.getMessage(),0);
                    }
              }
                            

              Collections.sort(excelInputList, this );
              min_forecastDate = excelInputList.get(0).getforecastDate();

              getDomainForecastService().SaveOrUpdateForecastInDB(excelInputList, min_forecastDate);

            }
            catch (Exception e) {
                logger.debug(e.getMessage());
            }

         return new RedirectResolution(AdminHomeAction.class);
        }     


        public int compare(DemandForecast df1 , DemandForecast df2){
            return df1.getforecastDate().compareTo(df2.getforecastDate());
        }

    }