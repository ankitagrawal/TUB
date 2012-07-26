package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.admin.pact.dao.inventory.StockTransferDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.*;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.XslGenerator;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
@Component
public class InventoryCheckinAction extends BaseAction {

    private static Logger         logger    = Logger.getLogger(InventoryCheckinAction.class);
    @Autowired
    private InventoryService      inventoryService;
    @Autowired
    private AdminInventoryService adminInventoryService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private SkuService            skuService;
    @Autowired
    private UserService           userService;
    @Autowired
    private GrnLineItemDao        grnLineItemDao;
    @Autowired
    private GoodsReceivedNoteDao  goodsReceivedNoteDao;
    // private LowInventoryDao lowInventoryDao;
    @Autowired
    private XslGenerator          xslGenerator;
    @Autowired
    private XslParser             xslParser;
    @Autowired
    private StockTransferDao      stockTransferDao;

    // SkuGroupDao skuGroupDao;

    // SkuItemDao skuItemDao;

    @Validate(required = true, on = "save")
    private String                upc;
    @Validate(required = true, minvalue = 1.0, on = "save")
    private Long                  qty;
    @Validate(required = true, on = "save")
    private Double                costPrice;
    private Double                mrp;
    private String                batch;
    private Date                  mfgDate;
    private Date                  expiryDate;
    private GoodsReceivedNote     grn;
    private String                invoiceNumber;
    private Date                  invoiceDate;
    private StockTransfer         stockTransfer;
    int                           strLength = 20;
    File                          printBarcode;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                        adminUploadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                        adminDownloadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
    String                        barcodeGurgaon;

    @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
    String                        barcodeMumbai;

    @Validate(required = true, on = "parse")
    private FileBean              fileBean;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/inventoryCheckin.jsp");
    }

    public Resolution save() {
        User user = null;
        Warehouse userWarehouse = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(InventoryCheckinAction.class);
        }
        logger.debug("upc: " + upc);
        try {
            ProductVariant productVariant = getProductVariantService().findVariantFromUPC(upc);
            if (productVariant == null) {
                productVariant = getProductVariantService().getVariantById(upc);
            }
            if (productVariant != null) {
                Sku sku = getSkuService().findSKU(productVariant, grn.getWarehouse());
                Long askedQty = 0L;
                GrnLineItem grnLineItem = getGrnLineItemDao().getGrnLineItem(grn, productVariant);
                if (grnLineItem != null && sku != null) {
                    askedQty = grnLineItem.getQty();
                    Long alreadyCheckedInQty = getAdminInventoryService().countOfCheckedInUnitsForGrnLineItem(grnLineItem);
                    //logger.info("Inventory Checkin ->ProductVariant="+productVariant.getId()+";askedQty="+askedQty+";alreadyCheckedInQty="+alreadyCheckedInQty+";qty="+qty);
                    if (qty > (askedQty - alreadyCheckedInQty)) {
                        addRedirectAlertMessage(new SimpleMessage("Qty mentioned - " + qty + " is exceeding required checked in qty. Plz check."));
                        return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
                    }
	                if (StringUtils.isBlank(batch) || StringUtils.isEmpty(batch)) {
		                addRedirectAlertMessage(new SimpleMessage("Batch is required. Plz check."));
		                return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
	                }
	                if (mrp != null && mrp.equals(0.0D)) {
		                addRedirectAlertMessage(new SimpleMessage("MRP is required. Plz check."));
		                return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
	                }
                    SkuGroup skuGroup = getAdminInventoryService().createSkuGroup(batch, mfgDate, expiryDate, costPrice, mrp, grn, null,null, sku);
                    getAdminInventoryService().createSkuItemsAndCheckinInventory(skuGroup, qty, null, grnLineItem, null, null,
                            getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKIN), user);
                    getInventoryService().checkInventoryHealth(productVariant);

                    if (grn.getGrnStatus().getId() != EnumGrnStatus.InventoryCheckedIn.getId()) {
                        grn.setGrnStatus(getGoodsReceivedNoteDao().get(GrnStatus.class, EnumGrnStatus.InventoryCheckedIn.getId()));
                        getGoodsReceivedNoteDao().save(grn);
                    }
	                //Barcode File
                    try {
                        String productOptionStringBuffer = productVariant.getOptionsPipeSeparated();
                        String barcodeFilePath = null;
                        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
                            barcodeFilePath = barcodeGurgaon;
                        } else {
                            barcodeFilePath = barcodeMumbai;
                        }
                        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + user.getId() + "_" + user.getName() + "_"
                                + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
                        String date = "";
                        if (expiryDate == null) {
                            date = "NA";
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            date = sdf.format(expiryDate);
                        }
                        String data = skuGroup.getBarcode() + "\t" + StringUtils.substring(productVariant.getProduct().getName(), 0, strLength) + "\t"
                                + StringUtils.substring(productOptionStringBuffer.toString(), 0, strLength) + "\t" + date + "\t" + qty + "\t" + skuGroup.getMrp();

                        BarcodeUtil.createBarcodeFile(barcodeFilePath, data);

                    } catch (IOException e) {
                        logger.error("Exception while appending on barcode file", e);
                        ;
                    }
                }else{
                  addRedirectAlertMessage(new SimpleMessage("Error with either GrnLineItem->"+grnLineItem+" or Sku ->"+sku));
                  return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("No such UPC or Variant Id"));
                return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
            }
        } catch (Exception e) {
            logger.error("Error while checkin : ", e);
            addRedirectAlertMessage(new SimpleMessage("Duplicate UPC or Variants in PO - Please fix the same."));
            return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
        }

        addRedirectAlertMessage(new SimpleMessage("Inventory Checked in successfully. Qty = " + qty));
        return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
    }

    @Secure(hasAnyPermissions = { PermissionConstants.GRN_CREATION }, authActionBean = AdminPermissionAction.class)
    public Resolution saveInventoryAgainstStockTransfer() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }

        stockTransfer.setReceivedBy(user);
        logger.debug("upc: " + upc);
        try {
            ProductVariant productVariant = getProductVariantService().findVariantFromUPC(upc);
            if (productVariant == null) {
                productVariant = getProductVariantService().getVariantById(upc);
            }
            if (productVariant != null) {
                Sku sku = skuService.findSKU(productVariant, stockTransfer.getToWarehouse());
              if(sku == null){
                 addRedirectAlertMessage(new SimpleMessage("Sku doesn't exist for product  - " + productVariant.getId() + "Plz contact category manager."));
                        return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId()).addParameter(
                                "checkinInventoryAgainstStockTransfer", stockTransfer.getId());
              }
                Long askedQty = 0L;
                StockTransferLineItem stockTransferLineItem = stockTransferDao.getStockTransferLineItem(stockTransfer, productVariant, batch);
                if (stockTransferLineItem != null && sku != null) {
                    askedQty = stockTransferLineItem.getCheckedoutQty();
                    Long alreadyCheckedInQty = adminInventoryService.countOfCheckedInUnitsForStockTransferLineItem(stockTransferLineItem);
                    // logger.info("Inventory Checkin ->
                    // ProductVariant="+productVariant.getId()+";askedQty="+askedQty+";alreadyCheckedInQty="+alreadyCheckedInQty+";qty="+qty);
                    if (qty > (askedQty - alreadyCheckedInQty)) {
                        addRedirectAlertMessage(new SimpleMessage("Qty mentioned - " + qty + " is exceeding required checked in qty. Plz check."));
                        return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId()).addParameter(
                                "checkinInventoryAgainstStockTransfer", stockTransfer.getId());
                    }

                    SkuGroup skuGroup = adminInventoryService.createSkuGroup(batch, mfgDate, expiryDate, costPrice, mrp, null, null, stockTransfer, sku);
                    adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, qty, null, null, null, stockTransferLineItem,
                            inventoryService.getInventoryTxnType(EnumInvTxnType.STOCK_TRANSFER_CHECKIN), user);
                    inventoryService.checkInventoryHealth(productVariant);
                    stockTransferLineItem.setCheckedinQty(alreadyCheckedInQty + qty);
                    stockTransferLineItem.setBatchNumber(batch);
                    stockTransferLineItem.setMfgDate(mfgDate);
                    stockTransferLineItem.setExpiryDate(expiryDate);
                    stockTransferLineItem = (StockTransferLineItem) stockTransferDao.save(stockTransferLineItem);

	                try {
                        String productOptionStringBuffer = productVariant.getOptionsPipeSeparated();
                        String barcodeFilePath = null;
                        if (stockTransfer.getToWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)) {
                            barcodeFilePath = barcodeGurgaon;
                        } else {
                            barcodeFilePath = barcodeMumbai;
                        }
                        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + user.getId() + "_" + user.getName() + "_"
                                + StringUtils.substring(stockTransfer.getToWarehouse().getCity(), 0, 3) + ".txt";
                        String date = "";
                        if (expiryDate == null) {
                            date = "NA";
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            date = sdf.format(expiryDate);
                        }
                        String data = skuGroup.getBarcode() + "\t" + StringUtils.substring(productVariant.getProduct().getName(), 0, strLength) + "\t"
                                + StringUtils.substring(productOptionStringBuffer.toString(), 0, strLength) + "\t" + date + "\t" + qty + "\t" + skuGroup.getMrp();

                        BarcodeUtil.createBarcodeFile(barcodeFilePath, data);

                    } catch (IOException e) {
                        logger.error("Exception while appending on barcode file", e);
                        ;
                    }
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("No such UPC or Variant Id"));
                return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId()).addParameter("checkinInventoryAgainstStockTransfer",
                        stockTransfer.getId());
            }
        } catch (Exception e) {
            logger.error("Error while stock transfer checkin : ", e);
            addRedirectAlertMessage(new SimpleMessage("Duplicate UPC or Variants in Stock Transfer - Please fix the same."));
            return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId()).addParameter("checkinInventoryAgainstStockTransfer",
                    stockTransfer.getId());
        }
        addRedirectAlertMessage(new SimpleMessage("Inventory Checked in successfully. Qty = " + qty));
        return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId()).addParameter("checkinInventoryAgainstStockTransfer",
                stockTransfer.getId());

    }

    public Resolution generateGRNExcel() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/grnFiles/" + sdf.format(new Date()) + "/GRN-ID-" + grn.getId() + "-" + sdf.format(new Date()) + ".xls";
        final File excelFile = new File(excelFilePath);

        getXslGenerator().generateGRNXsl(grn, excelFilePath);
        addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

        return new Resolution() {

            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
                res.setContentLength((int) excelFile.length());
                res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
                out = res.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[4096];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
        };
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/poFiles/" + sdf.format(new Date()) + "/POID-" + grn.getId() + "-" + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            Set<SkuGroup> skuGroupSet = getXslParser().readAndBulkCheckinInventory(grn, excelFile);
            addRedirectAlertMessage(new SimpleMessage(skuGroupSet.size() + " SkuGroups Created Successfully."));
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(InventoryCheckinAction.class).addParameter("grn", grn.getId());
    }

    @Secure(hasAnyPermissions = { PermissionConstants.GRN_CREATION }, authActionBean = AdminPermissionAction.class)
    public Resolution clearPrintBarcodeFile() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = getUserService().getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        String barcodeFilePath = null;
        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
            barcodeFilePath = barcodeGurgaon;
        } else {
            barcodeFilePath = barcodeMumbai;
        }
        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + user.getId() + "_" + user.getName() + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
        File printBarcode = new File(barcodeFilePath);
        if (printBarcode.exists()) {
            printBarcode.delete();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no barcode file generated with your name."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        try {
            File newPrintBarcode = new File(barcodeFilePath);
            newPrintBarcode.createNewFile();
        } catch (IOException e) {
            logger.error("Exception while deleting " + barcodeFilePath, e);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcode deleted Successfully."));
        return new RedirectResolution(AdminHomeAction.class);
    }

    @Secure(hasAnyPermissions = { PermissionConstants.GRN_CREATION }, authActionBean = AdminPermissionAction.class)
    public Resolution downloadPrintBarcodeFile() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = getUserService().getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        String barcodeFilePath = null;
        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
            barcodeFilePath = barcodeGurgaon;
        } else {
            barcodeFilePath = barcodeMumbai;
        }
        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + user.getId() + "_" + user.getName() + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
        printBarcode = new File(barcodeFilePath);
        if (!printBarcode.exists()) {
            addRedirectAlertMessage(new SimpleMessage("There is no barcode file generated with your name."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcode downloaded Successfully."));
        return new HTTPResponseResolution();
    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            InputStream in = new BufferedInputStream(new FileInputStream(printBarcode));
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.setContentLength((int) printBarcode.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + printBarcode.getName() + "\";");
            OutputStream out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        }

    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Date getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
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

    public GrnLineItemDao getGrnLineItemDao() {
        return grnLineItemDao;
    }

    public void setGrnLineItemDao(GrnLineItemDao grnLineItemDao) {
        this.grnLineItemDao = grnLineItemDao;
    }

    public GoodsReceivedNoteDao getGoodsReceivedNoteDao() {
        return goodsReceivedNoteDao;
    }

    public void setGoodsReceivedNoteDao(GoodsReceivedNoteDao goodsReceivedNoteDao) {
        this.goodsReceivedNoteDao = goodsReceivedNoteDao;
    }

    public XslGenerator getXslGenerator() {
        return xslGenerator;
    }

    public void setXslGenerator(XslGenerator xslGenerator) {
        this.xslGenerator = xslGenerator;
    }

    public XslParser getXslParser() {
        return xslParser;
    }

    public void setXslParser(XslParser xslParser) {
        this.xslParser = xslParser;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }
    
    public StockTransfer getStockTransfer() {
        return stockTransfer;
    }

    public void setStockTransfer(StockTransfer stockTransfer) {
        this.stockTransfer = stockTransfer;
    }

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
}