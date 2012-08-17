package com.hk.web.action.admin.store;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.domain.store.Store;
import com.hk.domain.store.StoreProduct;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.store.StoreService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.util.io.HkXlsWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 11:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class StorePricingAction extends BaseAction {

    private static final String SHEET_NAME_STORE_PRICING = "storePricing";

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                      adminUploadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                             adminDownloads;
    private File                               xlsFile;

    @Validate(required = true)
    private Store store;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductVariantService productVariantService;

    FileBean fileBean;

    private static Logger logger                    = LoggerFactory.getLogger(StorePricingAction.class);

    @DefaultHandler
    @DontValidate
    public Resolution pre() {

        return new ForwardResolution("/pages/admin/store/storepricing.jsp");
    }

    public Resolution uploadStorePricingExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/storeFiles/"+store.getPrefix()+"Pricing"+ System.currentTimeMillis() +".xls";
        //String excelFilePath ="E:\\mih\\mihpricing"+System.currentTimeMillis() +".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        uploadInDB(excelFilePath);

        excelFile.delete();

        addRedirectAlertMessage(new SimpleMessage("File Uploaded"));
        return new ForwardResolution("/pages/admin/store/storepricing.jsp");
    }

    public void uploadInDB(String excelFilePath) {
        try {
            ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_STORE_PRICING);
            Iterator<HKRow> rowIterator = parser.parse();
            Set<StoreProduct> storeProductSet = new HashSet<StoreProduct>();

            while (null != rowIterator && rowIterator.hasNext()) {
                HKRow curHkRow = rowIterator.next();
                StoreProduct storeProduct;

                int i = 0;
                while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
                    storeProduct = getStoreService().getStoreProductByHKVariantIDAndStoreId(StringUtils.trim(curHkRow.getColumnValue(i)),store.getId());
                    if (storeProduct == null) {
                        storeProduct = new StoreProduct();
                        storeProduct.setProductVariant(getProductVariantService().getVariantById(StringUtils.trim(curHkRow.getColumnValue(i))));
                        i++;
                        storeProduct.setStore(store);
                    }
                    if(i<1){
                        i++;
                    }
                    storeProduct.setStorePrice(Double.parseDouble(curHkRow.getColumnValue(i)));
                    i++;
                    storeProduct.setHidden(Boolean.parseBoolean(curHkRow.getColumnValue(i)));
                    i++;
                    storeProductSet.add(storeProduct);
                }
            }

            for (StoreProduct storeProduct : storeProductSet) {
                if (storeProduct != null) {
                    getStoreService().saveStoreProduct(storeProduct);
                }
                logger.info("inserting or updating into variantId:" + storeProduct.getProductVariant().getId() + " store: " + storeProduct.getStore().getName().toString());
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
        }

    }

    public Resolution downloadStorePricingExcel() throws Exception {
        String excelFilePath = adminDownloads + "/storeFiles/"+store.getPrefix()+"Pricing"+System.currentTimeMillis()+".xls";
        //String excelFilePath ="E:\\mih\\mihpricingTEST1.xls";
        xlsFile = new File(excelFilePath);
        xlsFile.getParentFile().mkdirs();

        List<StoreProduct> storeProductList= storeService.getProductListForStore(store);
        createPricingExcel(storeProductList);

        return new HTTPResponseResolution();

    }

    private void createPricingExcel(List<StoreProduct> storeProductList){
        try{
            HkXlsWriter xlsWriter = new HkXlsWriter();

            xlsWriter.addHeader("variantId", "variantId");
            xlsWriter.addHeader("storePrice", "storePrice");
            xlsWriter.addHeader("hidden", "hidden");

            int i=1;
            for(StoreProduct storeProduct : storeProductList){
                xlsWriter.addCell(i,storeProduct.getProductVariant().getId().toString());
                xlsWriter.addCell(i,storeProduct.getStorePrice());
                xlsWriter.addCell(i,storeProduct.isHidden());
                i++;
            }

            xlsWriter.writeData(xlsFile, "storePricing");
        } catch (Exception e) {
            logger.error("Exception while generating excel ", e);
            addRedirectAlertMessage(new SimpleMessage("excel generation failed " + e.getMessage()));
        }
    }

    /**
     * Custom resolution for HTTP response. The resolution will write the output file in response
     */


    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            OutputStream out = null;
            InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
            res.setContentLength((int) xlsFile.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
            out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        }
    }



    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}