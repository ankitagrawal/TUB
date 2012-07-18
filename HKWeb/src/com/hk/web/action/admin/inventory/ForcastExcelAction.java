package com.hk.web.action.admin.inventory;

import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.constants.XslConstants;
import com.hk.constants.core.Keys;
import com.hk.pact.dao.BaseDao;
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
    DemandForcastService dfServ;

    @DefaultHandler
    public Resolution pre() {
           return new ForwardResolution("/pages/admin/uploadForcast.jsp");
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public DemandForcastService getDfService (){
        return dfServ;
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
        List<DemandForcast> dfList = getBaseDao().getAll(DemandForcast.class);
        try {
              while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();

                String forcast_date = row.getColumnValue("Date");
                String prod_varientId = row.getColumnValue("Product Varient");
                String warehouseId = row.getColumnValue("Warehouse ID");
                String forcastVal = row.getColumnValue("Forcast");
                //error handling for null values

                if(StringUtils.isBlank(forcast_date) || StringUtils.isBlank(prod_varientId) ||
                        StringUtils.isBlank(warehouseId) || StringUtils.isBlank(forcastVal)){
                    continue;
                    /**
                     * TODO:
                     * a) throw an error message blank required field : fail entire upload here
                     * b) validate that variant id does exist
                     * 
                     */
                    //throw new ExcelBlankFieldException("value(s) empty at" + "    ", rowCount);
                }

                List<String> ll = new ArrayList<String>();
                ll.add(forcast_date);
                ll.add(prod_varientId);
                ll.add(warehouseId);
                ll.add(forcastVal);

                getDfService().InsertInDB(ll,dfList);
             
              }
            }
        catch (ExcelBlankFieldException e) {
                logger.debug(e.getMessage());
              //throw new ExcelBlankFieldException(e.getMessage());
        }
        return new RedirectResolution(AdminHomeAction.class);
        }     

    }