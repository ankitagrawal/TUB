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
import com.akube.framework.stripes.action.BaseAction;

import java.util.Iterator;
import java.util.Date;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
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

    @DefaultHandler
    public Resolution pre() {
           return new ForwardResolution("/pages/admin/uploadForcast.jsp");
       }


    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public Resolution parse() throws Exception
    {
        //logger.debug("parsing Demand forcast info : " + fileBean.getAbsolutePath());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/ForcastFiles/" + sdf.format(new Date()) + "/" + "_" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);
               
        ExcelSheetParser excel = new ExcelSheetParser(excelFile.getAbsolutePath(), "Sheet1");
        Iterator<HKRow> rowiterator = excel.parse();
        int rowCount=1;
        DemandForcast ob= new DemandForcast();
        try {
              while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();

                String forcast_date = row.getColumnValue("Date");
                String prod_varientId=row.getColumnValue("Product Varient");
                String warehouseId = row.getColumnValue("Warehouse ID");
                String forcastVal = row.getColumnValue("Forcast");
                  //insertInDB

                DateFormat formatter = new SimpleDateFormat("mm/dd/yy");
                Date date = (Date)formatter.parse(forcast_date);

                ob.setForcastDate(date);
                ob.setProductVariant(getBaseDao().get(ProductVariant.class,prod_varientId));
                ob.setWarehouse(getBaseDao().get(Warehouse.class,Long.parseLong(warehouseId)));
                ob.setForcastValue(Double.parseDouble(forcastVal));
                getBaseDao().save(ob);

              }
            }
        catch (ExcelBlankFieldException e) {
              throw new ExcelBlankFieldException(e.getMessage());
        }
        return new RedirectResolution(AdminHomeAction.class);
        }

    }