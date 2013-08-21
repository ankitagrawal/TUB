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
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.catalog.category.CategoryDaoImpl;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class InventoryHealthStatusAction extends BasePaginatedAction {

    Page                          lowInventoryPage;
    List<LowInventory>            lowInventories;
    String                        primaryCategory;
    String                        subCategory;
    String                        brand;
    String                        brandName;
    Category                      categoryName;
    ProductVariant                productVariant;
    Product                       product;
    private Boolean               unbookedInventoryRequired;
    @Autowired
    LowInventoryDao               lowInventoryDao;
    @Autowired
    AdminInventoryService         adminInventoryService;

    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    CategoryDaoImpl               categoryDao;
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

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
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
    public static final String    UNBOOKED_INVENTORY =   "UNBOOKED_INVENTORY";
    public static final String    GGN_UNBOOKED_INVENTORY  = "GGN_UNBOOKED_INVENTORY";
    public static final String    MUM_UNBOOKED_INVENTORY  = "MUM_UNBOOKED_INVENTORY";

  @ValidationMethod(on = { "generateWHInventoryExcel" })
    public void validateCategoryAndBrand() {
        if (primaryCategory == null && subCategory == null && brand == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter Category/Brand or Both."));
        }
    }

    @ValidationMethod(on = { "generateWHInventoryExcel" })
    public void validateCategory() {
        if (primaryCategory != null & subCategory == null) {
            if (categoryDao.getCategoryByName(Category.getNameFromDisplayName(primaryCategory)) == null) {
                getContext().getValidationErrors().add("1", new SimpleError("Category not found.Enter valid category."));
            }
        } else if (subCategory != null & primaryCategory == null) {
            if (categoryDao.getCategoryByName(Category.getNameFromDisplayName(subCategory)) == null) {
                getContext().getValidationErrors().add("1", new SimpleError("Category not found.Enter valid category."));
            }
        } else if (primaryCategory != null & subCategory != null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter Primary Category/Sub Category,not both."));
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
        Long loggedInWHUnbookedInventory  =0L;
        Long ggnUnbookedInventory = 0L;
        Long mumUnbookedInventory = 0L;
        String categoryName = null;
        List<ProductVariant> variants = null;
        // Fetching all non-deleted product-variants.
        if (primaryCategory != null) {
            categoryName = Category.getNameFromDisplayName(primaryCategory);
            variants = productVariantService.getAllNonDeletedProductVariants(categoryName, brand, true);
        } else if (subCategory != null) {
            categoryName = Category.getNameFromDisplayName(subCategory);
            variants = productVariantService.getAllNonDeletedProductVariants(categoryName, brand, false);
        } else {
            variants = productVariantService.getAllNonDeletedProductVariants(categoryName, brand, false);
        }

        String excelFilePath = null;
        if (categoryName != null) {
            excelFilePath = adminDownloadsPath + "/categoryExcelFiles/" + categoryName + "_inv_" + sdf.format(new Date()) + ".xls";
        } else if (brand != null) {
            excelFilePath = adminDownloadsPath + "/categoryExcelFiles/" + brand + "_inv_" + sdf.format(new Date()) + ".xls";
        }
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
        if (unbookedInventoryRequired) {
            if (loggedInWarehouse != null ){
                xlsWriter.addHeader(UNBOOKED_INVENTORY, UNBOOKED_INVENTORY);
            } else {
             xlsWriter.addHeader (GGN_UNBOOKED_INVENTORY,GGN_UNBOOKED_INVENTORY );
             xlsWriter.addHeader (MUM_UNBOOKED_INVENTORY,MUM_UNBOOKED_INVENTORY );
            }
        }
        int row = 1;
        for (ProductVariant variant : variants) {
            Sku ggnSKU = skuService.findSKU(variant, whGurgaon);
            Sku mumSKU = skuService.findSKU(variant, whMumbai);
            Sku loggedInSKU = skuService.findSKU(variant, loggedInWarehouse);
            loggedInWHInventory = adminInventoryService.getNetInventory(loggedInSKU);
            ggnInventory = adminInventoryService.getNetInventory(ggnSKU);
            mumInventory = adminInventoryService.getNetInventory(mumSKU);
            if (unbookedInventoryRequired){
               if (loggedInSKU != null) {
                loggedInWHUnbookedInventory = loggedInWHInventory - adminInventoryService.getBookedInventory(loggedInSKU);
               }
                if (mumSKU != null) {
                   mumUnbookedInventory = mumInventory - adminInventoryService.getBookedInventory(mumSKU);
                }
                if (ggnSKU != null) {
                  ggnUnbookedInventory = ggnInventory - adminInventoryService.getBookedInventory(ggnSKU);
                }

            }
            if (loggedInWarehouse != null) {
                if (loggedInSKU != null) {
                    xlsWriter.addCell(row, variant.getId());
                    xlsWriter.addCell(row, variant.getProduct().getBrand());
                    xlsWriter.addCell(row, variant.getProduct().getName());
                    xlsWriter.addCell(row, variant.getOptionsCommaSeparated());
                    xlsWriter.addCell(row, loggedInWHInventory);
                    if (unbookedInventoryRequired) {
                        xlsWriter.addCell(row, loggedInWHUnbookedInventory);
                    }
                    row++;
                }
            } else {
                if (ggnSKU != null & mumSKU == null) {
                    xlsWriter.addCell(row, variant.getId());
                    xlsWriter.addCell(row, variant.getProduct().getBrand());
                    xlsWriter.addCell(row, variant.getProduct().getName());
                    xlsWriter.addCell(row, variant.getOptionsCommaSeparated());
                    xlsWriter.addCell(row, ggnInventory);
                    xlsWriter.addCell(row, NOT_APPLICABLE);
                    if (unbookedInventoryRequired)  {
                        xlsWriter.addCell(row, ggnUnbookedInventory);
                        xlsWriter.addCell(row, NOT_APPLICABLE);
                    }
                    row++;

                } else if (mumSKU != null & ggnSKU == null) {
                    xlsWriter.addCell(row, variant.getId());
                    xlsWriter.addCell(row, variant.getProduct().getBrand());
                    xlsWriter.addCell(row, variant.getProduct().getName());
                    xlsWriter.addCell(row, variant.getOptionsCommaSeparated());
                    xlsWriter.addCell(row, NOT_APPLICABLE);
                    xlsWriter.addCell(row, mumInventory);
                    if (unbookedInventoryRequired) {
                        xlsWriter.addCell(row, NOT_APPLICABLE);
                        xlsWriter.addCell(row, mumUnbookedInventory);
                    }
                    row++;
                } else if (ggnSKU != null & mumSKU != null) {
                    xlsWriter.addCell(row, variant.getId());
                    xlsWriter.addCell(row, variant.getProduct().getBrand());
                    xlsWriter.addCell(row, variant.getProduct().getName());
                    xlsWriter.addCell(row, variant.getOptionsCommaSeparated());
                    xlsWriter.addCell(row, ggnInventory);
                    xlsWriter.addCell(row, mumInventory);
                    if (unbookedInventoryRequired) {
                        xlsWriter.addCell(row, ggnUnbookedInventory);
                        xlsWriter.addCell(row, mumUnbookedInventory);
                    }
                    row++;
                }
            }
        }
        if (loggedInWarehouse != null) {
            if (categoryName != null) {
                xlsWriter.writeData(excelFile, categoryName + "_Inventory_" + loggedInWarehouse.getCity());
            } else if (brand != null) {
                xlsWriter.writeData(excelFile, brand + "_Inventory_" + loggedInWarehouse.getCity());
            }
        } else {
            if (categoryName != null) {
                xlsWriter.writeData(excelFile, categoryName + "_Inventory_All_Warehouse");
            } else if (brand != null) {
                xlsWriter.writeData(excelFile, brand + "_Inventory_All_Warehouse");
            }
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

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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

    public boolean isUnbookedInventoryRequired() {
        return unbookedInventoryRequired;
    }

    public void setUnbookedInventoryRequired(boolean unbookedInventoryRequired) {
        this.unbookedInventoryRequired = unbookedInventoryRequired;
    }

    public Boolean getUnbookedInventoryRequired() {
        return unbookedInventoryRequired;
    }

    public void setUnbookedInventoryRequired(Boolean unbookedInventoryRequired) {
        this.unbookedInventoryRequired = unbookedInventoryRequired;
    }
    
    
}