package com.hk.web.action.admin.sku;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.SkuXslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;



@Secure(hasAnyPermissions = { PermissionConstants.UPLOAD_PRODUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
@Component
public class SkuParseExcelAction extends BaseAction {

    private static Logger      logger                 = LoggerFactory.getLogger(SkuParseExcelAction.class);

    // constants for retrieving values from excel.
    public static final String WAREHOUSE_ID           = "WAREHOUSE_ID";
    public static final String COST_PRICE             = "COST_PRICE";
    public static final String CUT_OFF_INVENTORY      = "CUT_OFF_INVENTORY";
    public static final String FORECASTED_QUANTITY    = "FORECASTED_QUANTITY";
    public static final String TAX_ID                 = "TAX_ID";
    public static final String PRODUCT_VARIANT_ID     = "PRODUCT_VARIANT_ID";
    public static final String ID                     = "ID";
    public static final String PRODUCT_OPTIONS        = "PRODUCT_OPTIONS";
    public static final String NET_INVENTORY          = "NET_INVENTORY";
    public static final String BOOKED_QTY             = "BOOKED_QTY";
    public static final String NET_UNBOOKED_INVENTORY = "NET_UNBOOKED_INVENTORY";

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/bulkUploadSKU.jsp");
    }

    @Autowired
    SkuXslParser skuXslParser;
    @Autowired
    SkuService                    skuService;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductService        productService;

    @Autowired
    private SkuService            service;

    @Autowired
    private InventoryService      inventoryService;
    @Autowired
    private AdminInventoryService adminInventoryService;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                        adminUploadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                        adminDownloadsPath;

    FileBean                      fileBean;

    String                        category;
    String                        brand;

    @ValidationMethod(on = "parse")
    public void validateOnParse() {
        if (fileBean == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please select a file to upload."));
        }
        if (category == null) {
            getContext().getValidationErrors().add("2", new SimpleError("Please enter Category to upload."));
        }
    }

    @ValidationMethod(on = "generateSkuExcel")
    public void validateOnGenerateSkuExcel() {
        if (category == null & brand == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter Category/Brand or both to download excel."));
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public Resolution generateSkuExcel() {

        List<Sku> skuList = new ArrayList<Sku>();
        String excelFilePath = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        skuList = getSkuList(category, brand);
        if (category != null) {
            excelFilePath = adminDownloadsPath + "/SkuExcelFiles/" + category + "_sku_" + sdf.format(new Date()) + ".xls";
        } else {
            excelFilePath = adminDownloadsPath + "/SkuExcelFiles/" + brand + "_sku_" + sdf.format(new Date()) + ".xls";
        }
        final File excelFile = new File(excelFilePath);
        HkXlsWriter xlsWriter = null;

        xlsWriter = new HkXlsWriter();
        xlsWriter.addHeader(ID, ID);
        xlsWriter.addHeader(PRODUCT_VARIANT_ID, PRODUCT_VARIANT_ID);
        xlsWriter.addHeader(PRODUCT_OPTIONS, PRODUCT_OPTIONS);
        xlsWriter.addHeader(WAREHOUSE_ID, WAREHOUSE_ID);
        xlsWriter.addHeader(CUT_OFF_INVENTORY, CUT_OFF_INVENTORY);
        xlsWriter.addHeader(FORECASTED_QUANTITY, FORECASTED_QUANTITY);
        xlsWriter.addHeader(TAX_ID, TAX_ID);
        xlsWriter.addHeader(NET_INVENTORY, NET_INVENTORY);
        xlsWriter.addHeader(BOOKED_QTY, BOOKED_QTY);
        xlsWriter.addHeader(NET_UNBOOKED_INVENTORY, NET_UNBOOKED_INVENTORY);

        int row = 1;
        for (Sku skuObj : skuList) {
            xlsWriter.addCell(row, skuObj.getId());
            xlsWriter.addCell(row, skuObj.getProductVariant());
            xlsWriter.addCell(row, skuObj.getProductVariant().getProduct().getName() + skuObj.getProductVariant().getOptionsCommaSeparated());
            xlsWriter.addCell(row, skuObj.getWarehouse().getName());
            xlsWriter.addCell(row, skuObj.getCutOffInventory());
            xlsWriter.addCell(row, skuObj.getForecastedQuantity());
            xlsWriter.addCell(row, skuObj.getTax().getName());
            xlsWriter.addCell(row, getAdminInventoryService().getNetInventory(skuObj));
            xlsWriter.addCell(row, getAdminInventoryService().getBookedInventory(skuObj, null));
            xlsWriter.addCell(row, (getAdminInventoryService().getNetInventory(skuObj) - getAdminInventoryService().getBookedInventory(skuObj, null)));

            row++;
        }
        xlsWriter.writeData(excelFile, category + "_SKUs");

        addRedirectAlertMessage(new SimpleMessage("Download complete"));

        return new Resolution() {

            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
                res.setContentLength((int) excelFile.length());
                res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
                out = res.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[8192];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
        };
    }

    public Resolution parse() throws Exception {
        Set<Sku> skuSet = new HashSet<Sku>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/skuFiles/" + sdf.format(new Date()) + "/" + category + "_" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            skuSet = skuXslParser.readSKUCatalog(excelFile);
        } catch (Exception e) {
            logger.error("Upload failed.Exception while reading excel sheet.");
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new ForwardResolution("/pages/admin/bulkUploadSKU.jsp");
        }
        skuService.insertSKUs(skuSet);
        addRedirectAlertMessage(new SimpleMessage("Database Successfully Updated."));
        return new ForwardResolution("/pages/admin/bulkUploadSKU.jsp");
    }

    private List<Sku> getSkuList(String category, String brand) {
        List<Product> productList = new ArrayList<Product>();
        List<Sku> skuList = new ArrayList<Sku>();
        List<ProductVariant> superProductVariantList = new ArrayList<ProductVariant>();
        List<ProductVariant> subProductVariantList = new ArrayList<ProductVariant>();

        if (category != null && brand != null) {
            productList = getProductService().getProductByCategoryAndBrand(category, brand);
        } else if (category != null) {
            productList = getProductService().getAllProductBySubCategory(category);
        } else {
            productList = getProductService().getAllProductByBrand(brand);
        }

        if (productList.size() > 0) {
            for (Product product : productList) {
                subProductVariantList = getProductVariantService().getProductVariantsByProductId(product.getId());
                superProductVariantList.addAll(subProductVariantList);
            }
        }

        if (superProductVariantList.size() > 0) {
            for (ProductVariant productVariant : superProductVariantList) {
                skuList.addAll(getSkuService().getSKUsForProductVariant(productVariant));
            }
        }
        return skuList;
    }
    /*
    @Transactional
    private void insertSKUs(Set<Sku> skuSet) throws Exception {
        for (Sku skuObj : skuSet) {
            if (skuObj.getId() == null) {
                Sku sku = new Sku();
                sku.setProductVariant(skuObj.getProductVariant());
                sku.setCutOffInventory(skuObj.getCutOffInventory());
                sku.setForecastedQuantity(skuObj.getForecastedQuantity());
                sku.setTax(skuObj.getTax());
                sku.setWarehouse(skuObj.getWarehouse());
                skuService.saveSku(sku);
            }
        }
    }
      */
    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

}
