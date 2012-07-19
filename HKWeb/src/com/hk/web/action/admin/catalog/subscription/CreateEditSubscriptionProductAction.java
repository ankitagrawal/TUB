package com.hk.web.action.admin.catalog.subscription;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/15/12
 * Time: 5:21 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class CreateEditSubscriptionProductAction extends BaseAction {
    @Autowired
    SubscriptionProductService subscriptionProductService;

    private static final String SHEET_NAME_SUBSCRIPTION_PRODUCTS = "subscriptionProducts";

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                      adminUploadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                             adminDownloads;
    private File xlsFile;


    @ValidateNestedProperties( {
            @Validate(field = "maxQtyPerDelivery", required = true, on = {"createSubscriptionProduct","saveSubscriptionProduct"}),
            @Validate(field = "minFrequencyDays", required = true, on = {"createSubscriptionProduct","saveSubscriptionProduct"}),
            @Validate(field = "maxFrequencyDays", required = true, on = {"createSubscriptionProduct","saveSubscriptionProduct"}),
            @Validate(field = "subscriptionDiscount180Days", required = true, on = {"createSubscriptionProduct","saveSubscriptionProduct"}),
            @Validate(field = "subscriptionDiscount360Days", required = true, on = {"createSubscriptionProduct","saveSubscriptionProduct"})
    })
    private SubscriptionProduct      subscriptionProduct;

    @Validate(required = true, on={"createSubscriptionProduct","editSubscriptionProduct","saveSubscriptionProduct"})
    private Product product;

    @Validate(required = true, on="downloadSubscriptionProductsExcel")
    private Category category;

    boolean editSubscription=false;

    FileBean fileBean;

    private static Logger logger                    = LoggerFactory.getLogger(CreateEditSubscriptionProductAction.class);

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
    }

    public Resolution createSubscriptionProduct(){
        SubscriptionProduct priorSubscriptionProduct=subscriptionProductService.findByProduct(product);
        editSubscription=false;
        if(priorSubscriptionProduct !=null){
            addRedirectAlertMessage(new SimpleMessage("subscription product already exists!!"));
            return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
        } else {
            subscriptionProduct.setProduct(product);
            subscriptionProductService.save(subscriptionProduct);
            addRedirectAlertMessage(new SimpleMessage("subscription product saved successfully."));
            return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
        }
    }

    public Resolution editSubscriptionProduct(){
        editSubscription=true;
        subscriptionProduct = subscriptionProductService.findByProduct(product);
        if(subscriptionProduct==null){
            editSubscription=false;
            addRedirectAlertMessage(new SimpleMessage("Subscription Product doesn't exist"));
        }
        return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
    }

    public Resolution saveSubscriptionProduct(){
        editSubscription=false;
        subscriptionProductService.save(subscriptionProduct);
        addRedirectAlertMessage(new SimpleMessage("subscription product saved successfully."));
        return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
    }

    public Resolution uploadSubscriptionProductsExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/storeFiles/subscriptionProducts"+ System.currentTimeMillis() +".xls";
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
            ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_SUBSCRIPTION_PRODUCTS);
            Iterator<HKRow> rowIterator = parser.parse();
            Set<SubscriptionProduct> storeProductSet = new HashSet<SubscriptionProduct>();

            while (null != rowIterator && rowIterator.hasNext()) {
                HKRow curHkRow = rowIterator.next();
                SubscriptionProduct storeProduct;

                int i = 0;
                while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
                   /* storeProduct = getStoreService().getStoreProductByHKVariantIDAndStoreId(StringUtils.trim(curHkRow.getColumnValue(i)),store.getId());
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
                    storeProductSet.add(storeProduct);*/
                }
            }

           /* for (StoreProduct storeProduct : storeProductSet) {
                if (storeProduct != null) {
                    getStoreService().saveStoreProduct(storeProduct);
                }
                logger.info("inserting or updating into variantId:" + storeProduct.getProductVariant().getId() + " store: " + storeProduct.getStore().getName().toString());
            }*/
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
        }

    }

    public Resolution downloadSubscriptionProductsExcel() throws Exception {
        String excelFilePath = adminDownloads + "/storeFiles/"+category.getDisplayName()+"_SubscriptionProducts"+System.currentTimeMillis()+".xls";
        xlsFile = new File(excelFilePath);
        xlsFile.getParentFile().mkdirs();

       /* List<SubscriptionProduct> storeProductList= storeService.getProductListForStore(store);
        createSubscriptionProductsExcel(subscriptionProductList);*/

        return new HTTPResponseResolution();

    }

    private void createSubscriptionProductsExcel(List<SubscriptionProduct> subscriptionProductList){
        try{
            HkXlsWriter xlsWriter = new HkXlsWriter();

            xlsWriter.addHeader("variantId", "variantId");
            xlsWriter.addHeader("storePrice", "storePrice");
            xlsWriter.addHeader("hidden", "hidden");

            int i=1;
            /*for(SubscriptionProduct subscriptionProduct : subscriptionProductList){
                xlsWriter.addCell(i,storeProduct.getProductVariant().getId().toString());
                xlsWriter.addCell(i,storeProduct.getStorePrice());
                xlsWriter.addCell(i,storeProduct.isHidden());
                i++;
            }*/

            xlsWriter.writeData(xlsFile, "subscriptionProducts");
        } catch (Exception e) {
            logger.error("Exception while generating excel ", e);
            addRedirectAlertMessage(new SimpleMessage("excel generation failed " + e.getMessage()));
        }
    }


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

    public SubscriptionProduct getSubscriptionProduct() {
        return subscriptionProduct;
    }

    public void setSubscriptionProduct(SubscriptionProduct subscriptionProduct) {
        this.subscriptionProduct = subscriptionProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public boolean isEditSubscription() {
        return editSubscription;
    }

    public void setEditSubscription(boolean editSubscription) {
        this.editSubscription = editSubscription;
    }
}
