package com.hk.web.action.admin.inventory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.domain.warehouse.DemandForecast;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.rest.pact.service.DemandForecastService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 10, 2012
 * Time: 11:33:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForecastExcelAction extends BaseAction {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ForecastExcelAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Validate(required = true)
    FileBean fileBean;

    @Autowired
    DemandForecastService demandForecastService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductVariantService productVariantService;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/uploadForecast.jsp");
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public DemandForecastService getDemandForecastService() {
        return demandForecastService;
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/forecastFiles/" + sdf.format(new Date()) + "/" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
        logger.debug("parsing Demand forecast info : " + excelFile.getAbsolutePath());

        Iterator<HKRow> rowiterator = null;
        try{
        ExcelSheetParser excel = new ExcelSheetParser(excelFile.getAbsolutePath(), "Sheet1");    
        rowiterator = excel.parse();
        }
        catch(Exception e){
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new ForwardResolution("/pages/admin/uploadForecast.jsp");
        }

        int rowCount = 1;

        List<DemandForecast> excelInputList = new ArrayList<DemandForecast>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();

                String forecastDate = row.getColumnValue("Forecast Date");
                String productVariantId = row.getColumnValue("Product Variant");
                String warehouseId = row.getColumnValue("Warehouse ID");
                String forecastVal = row.getColumnValue("Forecast Value");
                //error handling for null values

                if (StringUtils.isBlank(forecastDate) || StringUtils.isBlank(productVariantId) ||
                        StringUtils.isBlank(warehouseId) || StringUtils.isBlank(forecastVal)) {
                    getContext().getValidationErrors().add("1", new SimpleError("Please correct the excel. Value(s) empty at row " + rowCount + ""));
                    return new ForwardResolution("/pages/admin/uploadForecast.jsp");
                }

                if (productVariantService.getVariantById(productVariantId) == null) {
                    getContext().getValidationErrors().add("1", new SimpleError("No product variant exists with the entered Product Variant at row " + rowCount + ""));
                    return new ForwardResolution("/pages/admin/uploadForecast.jsp");
                }

                try {

                    Date date = (Date) formatter.parse(forecastDate);

                    DemandForecast dForecast = new DemandForecast();
                    dForecast.setforecastDate(date);
                    dForecast.setProductVariant(productVariantService.getVariantById(productVariantId));
                    dForecast.setWarehouse(warehouseService.getWarehouseById(Long.parseLong(warehouseId)));
                    dForecast.setforecastValue(Double.parseDouble(forecastVal));

                    excelInputList.add(dForecast);
                }
                catch (java.text.ParseException pe) {
                    logger.error(pe.getMessage(), 0);
                    getContext().getValidationErrors().add("1", new SimpleError("Please enter date in correct format at row " + rowCount + ""));
                    return new ForwardResolution("/pages/admin/uploadForecast.jsp");
                }
            }


            Collections.sort(excelInputList, new Comparator<DemandForecast>() {
                public int compare(DemandForecast df1, DemandForecast df2) {
                    return df1.getforecastDate().compareTo(df2.getforecastDate());
                }
            });
            Date minimumForecastDate = excelInputList.get(0).getforecastDate();

            getDemandForecastService().saveOrUpdateForecastInDB(excelInputList, minimumForecastDate);

        }
        catch (Exception e) {
            logger.debug(e.getMessage());
            getContext().getValidationErrors().add("1", new SimpleError("Please correct values at row " + rowCount + ""));
            return new ForwardResolution("/pages/admin/uploadForecast.jsp");
        }

        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/uploadForecast.jsp");
        //return new RedirectResolution(AdminHomeAction.class);
    }


}