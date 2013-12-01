package com.hk.web.action.admin.catalog.subscription;

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
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.population.CustomPopulationStrategy;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/15/12
 * Time: 5:21 PM
 */

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@CustomPopulationStrategy(BeanFirstPopulationStrategy.class)
@Component
public class CreateEditSubscriptionProductAction extends BaseAction {
    @Autowired
    SubscriptionProductService subscriptionProductService;
    @Autowired
    ProductService productService;

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

    private boolean subscribable;

    @Validate(required = true, on={"createSubscriptionProduct","editSubscriptionProduct","saveSubscriptionProduct"})
    private Product product;

    @Validate(required = true, on="downloadSubscriptionProductsExcel")
    private Category category;

    boolean editSubscription=false;

    @Validate(required = true, on="uploadSubscriptionProductsExcel")
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
            product.setSubscribable(subscribable);
            productService.save(product);

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
        }else{
            subscribable=subscriptionProduct.getProduct().isSubscribable();
        }
        return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
    }

    public Resolution saveSubscriptionProduct(){
        editSubscription=false;
        subscriptionProduct.setProduct(product);

        product.setSubscribable(subscribable);
        productService.save(product);

        subscriptionProductService.save(subscriptionProduct);
        addRedirectAlertMessage(new SimpleMessage("subscription product saved successfully."));
        return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
    }

    public Resolution uploadSubscriptionProductsExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/subscriptionFiles/subscriptionProducts"+ System.currentTimeMillis() +".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        uploadInDB(excelFilePath);

        excelFile.delete();

        addRedirectAlertMessage(new SimpleMessage("File Uploaded"));
        return new ForwardResolution("/pages/admin/subscription/createSubscriptionProduct.jsp");
    }

    public void uploadInDB(String excelFilePath) {
        try {
            ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_SUBSCRIPTION_PRODUCTS);
            Iterator<HKRow> rowIterator = parser.parse();
            Set<SubscriptionProduct> subscriptionProductSet = new HashSet<SubscriptionProduct>();

            while (null != rowIterator && rowIterator.hasNext()) {
                HKRow curHkRow = rowIterator.next();
                SubscriptionProduct subscriptionProductItem;
                Product productItem;

                int i = 0;
                while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
                    productItem = productService.getProductById(StringUtils.trim(curHkRow.getColumnValue(i)));
                    subscriptionProductItem = subscriptionProductService.findByProduct(productItem);
                    if (subscriptionProductItem == null) {
                        subscriptionProductItem = new SubscriptionProduct();
                        subscriptionProductItem.setProduct(product);
                    }
                    i++;
                    subscriptionProductItem.setMaxQtyPerDelivery(Integer.parseInt(StringUtils.trim(curHkRow.getColumnValue(i))));
                    i++;
                    subscriptionProductItem.setMinFrequencyDays(Integer.parseInt(StringUtils.trim(curHkRow.getColumnValue(i))));
                    i++;
                    subscriptionProductItem.setMaxFrequencyDays(Integer.parseInt(StringUtils.trim(curHkRow.getColumnValue(i))));
                    i++;
                    subscriptionProductItem.setSubscriptionDiscount180Days(Double.parseDouble(StringUtils.trim(curHkRow.getColumnValue(i))));
                    i++;
                    subscriptionProductItem.setSubscriptionDiscount360Days(Double.parseDouble(StringUtils.trim(curHkRow.getColumnValue(i))));
                    i++;
                    productItem.setSubscribable(Boolean.parseBoolean(StringUtils.trim(curHkRow.getColumnValue(i))));
                    i++;
                    productService.save(productItem);
                    subscriptionProductSet.add(subscriptionProductItem);
                }
            }

            for (SubscriptionProduct subscriptionProduct1 : subscriptionProductSet) {
                if (subscriptionProduct1 != null) {
                    subscriptionProductService.save(subscriptionProduct1);
                }
                logger.info("inserting or updating subscription product for: " + subscriptionProduct1.getProduct().getId().toString());
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
        }

    }

    public Resolution downloadSubscriptionProductsExcel() throws Exception {
        String excelFilePath = adminDownloads + "/subscriptionFiles/"+category.getDisplayName()+"_SubscriptionProducts"+System.currentTimeMillis()+".xls";
        xlsFile = new File(excelFilePath);
        xlsFile.getParentFile().mkdirs();

        List<SubscriptionProduct> subscriptionProductList= subscriptionProductService.findByCategory(category);
        createSubscriptionProductsExcel(subscriptionProductList);

        return new HTTPResponseResolution();

    }

    private void createSubscriptionProductsExcel(List<SubscriptionProduct> subscriptionProductList){
        try{
            HkXlsWriter xlsWriter = new HkXlsWriter();

            xlsWriter.addHeader("productId", "productId");
            xlsWriter.addHeader("maxQtyPerDelivery", "maxQtyPerDelivery");
            xlsWriter.addHeader("minFrequencyDays", "minFrequencyDays");
            xlsWriter.addHeader("maxFrequencyDays", "maxFrequencyDays");
            xlsWriter.addHeader("DiscountPercent180Days", "DiscountPercent180Days");
            xlsWriter.addHeader("DiscountPercent360Days", "DiscountPercent360Days");
            xlsWriter.addHeader("isSubscribable", "isSubscribable");

            int i=1;
            for(SubscriptionProduct subscriptionProduct : subscriptionProductList){
                xlsWriter.addCell(i,subscriptionProduct.getProduct().getId());
                xlsWriter.addCell(i,subscriptionProduct.getMaxQtyPerDelivery());
                xlsWriter.addCell(i,subscriptionProduct.getMinFrequencyDays());
                xlsWriter.addCell(i,subscriptionProduct.getMaxFrequencyDays());
                xlsWriter.addCell(i,subscriptionProduct.getSubscriptionDiscount180Days());
                xlsWriter.addCell(i,subscriptionProduct.getSubscriptionDiscount360Days());
                xlsWriter.addCell(i,subscriptionProduct.getProduct().isSubscribable());
                i++;
            }

            xlsWriter.writeData(xlsFile, SHEET_NAME_SUBSCRIPTION_PRODUCTS);
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

    public boolean isSubscribable() {
        return subscribable;
    }

    public void setSubscribable(boolean subscribable) {
        this.subscribable = subscribable;
    }
}
