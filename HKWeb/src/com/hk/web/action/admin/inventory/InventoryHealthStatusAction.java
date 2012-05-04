package com.hk.web.action.admin.inventory;

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
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.ProductManager;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.dao.inventory.LowInventoryDao;
import com.hk.dao.inventory.ProductVariantInventoryDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.InventoryService;
import com.hk.service.ProductService;
import com.hk.service.ProductVariantService;
import com.hk.service.SkuService;
import com.hk.service.UserService;
import com.hk.service.WarehouseService;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class InventoryHealthStatusAction extends BasePaginatedAction {

    Page                          lowInventoryPage;
    List<LowInventory>            lowInventories;
    String                        category;
    String                        brand;
    String                        brandName;
    Category                      categoryName;
    ProductVariant                productVariant;
    Product                       product;
    @Autowired
    ProductManager                productManager;
    @Autowired
    ProductVariantInventoryDao    productVariantInventoryDao;
    @Autowired
    LowInventoryDao               lowInventoryDao;
    @Autowired
    AdminInventoryService         adminInventoryService;

    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    CategoryDao                   categoryDao;
    @Autowired
    InventoryService              inventoryService;
    @Autowired
    SkuService                    skuService;
    @Autowired
    UserService                   userService;
    @Autowired
    WarehouseService              warehouseService;

    @Autowired
    private ProductService        productService;

    // @Named(Keys.Env.adminDownloads)
    @Value("#{hkEnvProps['adminDownloads']}")
    String                        adminDownloadsPath;

    private SimpleDateFormat      sdf            = new SimpleDateFormat("yyyy-MM-dd");
    public static final String    VARIANT_ID     = "VARIANT_ID";
    public static final String    BRAND          = "BRAND";
    public static final String    PRODUCT        = "PRODUCT";
    public static final String    INVENTORY      = "INVENTORY";
    public static final String    OPTIONS        = "OPTIONS";
    public static final String    GGN_INVENTORY  = "GGN_INVENTORY";
    public static final String    MUM_INVENTORY  = "MUM_INVENTORY";
    public static final String    NOT_APPLICABLE = "-NA-";

    @ValidationMethod(on = { "generateWHInventoryExcel" })
    public void validateCategoryAndBrand() {
        if (category == null & brand == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter Category/Brand or Both."));
        }
    }

    @ValidationMethod(on = { "generateWHInventoryExcel" })
    public void validateCategory() {
        if (category != null) {
            if (categoryDao.find(category) == null) {
                getContext().getValidationErrors().add("1", new SimpleError("Category not found.Enter valid category."));
            }
        }
    }

    @DefaultHandler
    @Secure(hasAnyPermissions = { PermissionConstants.DOWNLOAD_PRDOUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        lowInventoryPage = lowInventoryDao.findLowInventoryList(product, productVariant, categoryName, brandName, getPageNo(), getPerPage());
        lowInventories = lowInventoryPage.getList();
        return new ForwardResolution("/pages/lowInventoryList.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.DOWNLOAD_PRDOUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
    public Resolution listOutOfStock() {
        lowInventoryPage = lowInventoryDao.findOutOfstockInventory(product, productVariant, categoryName, brandName, getPageNo(), getPerPage());
        lowInventories = lowInventoryPage.getList();
        return new ForwardResolution("/pages/outOfStockInventoryList.jsp");
    }

    public Resolution downloadWHInventorySnapshot() {
        return new ForwardResolution("/pages/admin/downloadWHInventory.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.DOWNLOAD_PRDOUCT_CATALOG }, authActionBean = AdminPermissionAction.class)
    public Resolution generateWHInventoryExcel() {
        Warehouse loggedInWarehouse = userService.getWarehouseForLoggedInUser();
        Warehouse whGurgaon = warehouseService.getDefaultWarehouse();
        Warehouse whMumbai = warehouseService.getMumbaiWarehouse();
        Long ggnInventory = 0L;
        Long mumInventory = 0L;
        Long loggedInWHInventory = 0L;
        String categoryName = null;
        if (category != null) {
            categoryName = Category.getNameFromDisplayName(category);
        }

        // Fetching all non-deleted product-variants.
        List<ProductVariant> variants = getAllNonDeletedProductVariants(categoryName, brand);

        String excelFilePath = adminDownloadsPath + "/categoryExcelFiles/" + category + "_inv_" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);
        HkXlsWriter xlsWriter = null;

        xlsWriter = new HkXlsWriter();
        xlsWriter.addHeader(VARIANT_ID, VARIANT_ID);
        xlsWriter.addHeader(BRAND, BRAND);
        xlsWriter.addHeader(PRODUCT, PRODUCT);
        xlsWriter.addHeader(OPTIONS, OPTIONS);
        if (loggedInWarehouse != null) {
            xlsWriter.addHeader(INVENTORY, INVENTORY);
        } else {
            xlsWriter.addHeader(GGN_INVENTORY, GGN_INVENTORY);
            xlsWriter.addHeader(MUM_INVENTORY, MUM_INVENTORY);
        }
        int row = 1;
        for (ProductVariant variant : variants) {

            Sku ggnSKU = skuService.findSKU(variant, whGurgaon);
            Sku mumSKU = skuService.findSKU(variant, whMumbai);
            Sku loggedInSKU = skuService.findSKU(variant, loggedInWarehouse);
            loggedInWHInventory = adminInventoryService.getNetInventory(loggedInSKU);
            ggnInventory = adminInventoryService.getNetInventory(ggnSKU);
            mumInventory = adminInventoryService.getNetInventory(mumSKU);
            if (loggedInSKU != null || ggnSKU != null || mumSKU != null) {
                xlsWriter.addCell(row, variant.getId());
                xlsWriter.addCell(row, variant.getProduct().getBrand());
                xlsWriter.addCell(row, variant.getProduct().getName());
                xlsWriter.addCell(row, variant.getOptionsCommaSeparated());
                if (loggedInWarehouse != null) {
                    if (loggedInWHInventory != 0) {
                        xlsWriter.addCell(row, loggedInWHInventory);
                    } else {
                        xlsWriter.addCell(row, NOT_APPLICABLE);
                    }
                } else {
                    if (ggnInventory != 0) {
                        xlsWriter.addCell(row, ggnInventory);
                    } else {
                        xlsWriter.addCell(row, NOT_APPLICABLE);
                    }
                    // for mum inventory
                    if (mumInventory != 0) {
                        xlsWriter.addCell(row, mumInventory);
                    } else {
                        xlsWriter.addCell(row, NOT_APPLICABLE);
                    }
                }
                row++;
            }
        }

        if (loggedInWarehouse != null) {
            xlsWriter.writeData(excelFile, category + "_Inventory_" + loggedInWarehouse.getCity());

        } else {
            xlsWriter.writeData(excelFile, category + "_Inventory_All_Warehouse");
        }
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

    private List<ProductVariant> getAllNonDeletedProductVariants(String category, String brand) {
        List<ProductVariant> allProductVariantList = new ArrayList<ProductVariant>();
        List<ProductVariant> subProductVariantList = new ArrayList<ProductVariant>();
        List<Product> productList = new ArrayList<Product>();

        if (category != null && brand != null) {
            productList = getProductService().getProductByCategoryAndBrand(category, brand);
        } else if (category != null) {
            productList = getProductService().getAllProductBySubCategory(category);
        } else {
            productList = getProductService().getAllProductByBrand(brand);
        }

        if (productList.size() > 0) {
            for (Product productObj : productList) {
                subProductVariantList = getProductVariantService().getProductVariantsByProductId(productObj.getId());
                allProductVariantList.addAll(subProductVariantList);
            }
        }
        return allProductVariantList;
    }

    public Page getLowInventoryPage() {
        return lowInventoryPage;
    }

    public void setLowInventoryPage(Page lowInventoryPage) {
        this.lowInventoryPage = lowInventoryPage;
    }

    public List<LowInventory> getLowInventories() {
        return lowInventories;
    }

    public void setLowInventories(List<LowInventory> lowInventories) {
        this.lowInventories = lowInventories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("categoryName");
        params.add("productVariant");
        params.add("product");
        params.add("brandName");
        return params;
    }

    public int getPerPageDefault() {
        return 30;
    }

    public int getPageCount() {
        return lowInventoryPage == null ? 0 : lowInventoryPage.getTotalPages();
    }

    public int getResultCount() {
        return lowInventoryPage == null ? 0 : lowInventoryPage.getTotalResults();
    }

    public Category getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Category categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public WarehouseService getWarehouseService() {
        return warehouseService;
    }

    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}