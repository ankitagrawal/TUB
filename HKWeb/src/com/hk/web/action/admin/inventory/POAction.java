package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.PurchaseOrderManager;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.catalog.product.ProductVariantSupplierInfoService;
import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.util.PurchaseOrderPDFGenerator;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class POAction extends BasePaginatedAction {

    private static Logger                     logger            = Logger.getLogger(POAction.class);
    @Autowired
    private PurchaseOrderDao                  purchaseOrderDao;
    @Autowired
    private GoodsReceivedNoteDao              goodsReceivedNoteDao;
    @Autowired
    private GrnLineItemDao                    grnLineItemDao;
    @Autowired
    private UserService                       userService;
    @Autowired
    private SkuService                        skuService;
    @Autowired
    private AdminEmailManager                 adminEmailManager;
    @Autowired
    private PurchaseOrderManager              purchaseOrderManager;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                                    adminDownloads;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	  String adminUploadsPath;

    @Validate(required = true, on = "uploadExcelAndCreatePOs")
	  private FileBean fileBean;

    @Autowired
    XslParser xslParser;
    @Autowired
    XslGenerator                              xslGenerator;
    @Autowired
    PurchaseOrderPDFGenerator                 purchaseOrderPDFGenerator;
    @Autowired
    GrnLineItemService                        grnLineItemService;
    @Autowired
    private ProductVariantSupplierInfoService productVariantSupplierInfoService;
    @Autowired
    private PurchaseOrderService              purchaseOrderService;

    private File                              xlsFile;
    Page                                      purchaseOrderPage;
    private List<PurchaseOrder>               purchaseOrderList = new ArrayList<PurchaseOrder>();
    private PurchaseOrder                     purchaseOrder;
    private PurchaseOrderDto                  purchaseOrderDto;

    private Date                              startDate;
    private Date                              endDate;
    private String                            tinNumber;
    private String                            invoiceNumber;
    private String                            supplierName;
    private ProductVariant                    productVariant;
    private Warehouse                         warehouse;
    private PurchaseOrderStatus               purchaseOrderStatus;
    private User                              approvedBy;
    private User                              createdBy;
    private Boolean                           extraInventoryCreated;

    private Integer                           defaultPerPage    = 20;
	private String                            poType;
    private Long 															brightSoId;

    @DefaultHandler
    public Resolution pre() {
        if (productVariant != null) {
            purchaseOrderList = getPurchaseOrderDao().listPurchaseOrdersWithProductVariant(productVariant);
        } else {
            if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
                warehouse = getPrincipalUser().getSelectedWarehouse();
            }
            purchaseOrderPage = getPurchaseOrderDao().searchPO(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber, tinNumber, supplierName, warehouse,
                    extraInventoryCreated, brightSoId, startDate, endDate, poType, getPageNo(), getPerPage());
            purchaseOrderList = purchaseOrderPage.getList();
        }
        return new ForwardResolution("/pages/admin/poList.jsp");
    }

    public Resolution getExtraInventoryPO() {

        purchaseOrderList = getPurchaseOrderService().getAllPurchaseOrderByExtraInventory();
        if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
            warehouse = getPrincipalUser().getSelectedWarehouse();
        }
        return new ForwardResolution("/pages/admin/poList.jsp");

    }

    public Resolution generateExcelReport() {
        if (productVariant != null) {
            purchaseOrderList = getPurchaseOrderDao().listPurchaseOrdersWithProductVariant(productVariant);
        } else {
            if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
                warehouse = getPrincipalUser().getSelectedWarehouse();
            }
            purchaseOrderList = getPurchaseOrderDao().searchPO(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber, tinNumber, supplierName, warehouse, null, null, startDate, endDate, poType);
        }

        if (purchaseOrderList != null) {
            xlsFile = new File(adminDownloads + "/reports/POList.xls");
            xslGenerator.generatePOListExcel(xlsFile, purchaseOrderList);
            return new HTTPResponseResolution();
        }
        return new RedirectResolution(POAction.class);
    }

    public Resolution print() {
        logger.debug("purchaseOrder: " + purchaseOrder);
        purchaseOrderDto = getPurchaseOrderManager().generatePurchaseOrderDto(purchaseOrder);
        return new ForwardResolution("/pages/admin/poPrintView.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.GRN_CREATION }, authActionBean = AdminPermissionAction.class)
    public Resolution generateGRNCheck() {
        logger.debug("purchaseOrder: " + purchaseOrder);
        if (!purchaseOrder.getGoodsReceivedNotes().isEmpty()) {
            return new ForwardResolution("/pages/admin/grnForPOAlreadyExistCheck.jsp");
        }
        return new RedirectResolution(POAction.class).addParameter("generateGRN").addParameter("purchaseOrder", purchaseOrder.getId());
    }

    @Secure(hasAnyPermissions = { PermissionConstants.GRN_CREATION }, authActionBean = AdminPermissionAction.class)
    public Resolution generateGRN() {
        if (!(purchaseOrder.getPurchaseOrderStatus().equals(EnumPurchaseOrderStatus.SentToSupplier.getPurchaseOrderStatus()))
                && !(purchaseOrder.getPurchaseOrderStatus().equals(EnumPurchaseOrderStatus.Received.getPurchaseOrderStatus()))) {
            addRedirectAlertMessage(new SimpleMessage("Can't Create GRN, PO is not in Sent To Supplier, or Received state"));
            return new RedirectResolution(POAction.class);
        }

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        logger.debug("purchaseOrder: " + purchaseOrder);
        Warehouse warehouse = purchaseOrder.getWarehouse();
        GoodsReceivedNote grn = new GoodsReceivedNote();
        grn.setPurchaseOrder(purchaseOrder);
        grn.setGrnDate(new Date());
        grn.setInvoiceDate(new Date());
        if(purchaseOrder.getBrightSoId()!=null){
        	grn.setInvoiceNumber(purchaseOrder.getBrightSoId().toString());
        }else{
        	grn.setInvoiceNumber("-");
        }
        grn.setReceivedBy(loggedOnUser);
        grn.setGrnStatus(getGoodsReceivedNoteDao().get(GrnStatus.class, EnumGrnStatus.GoodsReceived.getId()));
        grn.setWarehouse(warehouse);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(grn.getGrnDate());
        if (purchaseOrder.getSupplier().getCreditDays() != null && purchaseOrder.getSupplier().getCreditDays() >= 0) {
            calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
            grn.setEstPaymentDate(calendar.getTime());
        } else {
            grn.setEstPaymentDate(purchaseOrder.getPoPlaceDate());
        }

        grn = (GoodsReceivedNote) getGoodsReceivedNoteDao().save(grn);
        editPVFillRate(purchaseOrder);
      
        boolean isInternalSupplier = grn.getPurchaseOrder().getSupplier().isInternalSupplier();

        for (PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
            ProductVariant productVariant = poLineItem.getSku().getProductVariant();
            Sku sku = getSkuService().getSKU(productVariant, warehouse);
            long existingGrnLineItemQty = grnLineItemService.getGrnLineItemQtyAlreadySet(grn, poLineItem.getSku());

            if (existingGrnLineItemQty >= poLineItem.getQty().longValue()) {
                continue;
            }
            GrnLineItem grnLineItem = new GrnLineItem();
            grnLineItem.setGoodsReceivedNote(grn);
            grnLineItem.setProductVariant(productVariant);
            grnLineItem.setSku(poLineItem.getSku());
            if(productVariant.getWeight()!=null){
            grnLineItem.setWeight(productVariant.getWeight());
            }
            else{
            	grnLineItem.setWeight(0.0);
            }
            if (isInternalSupplier) {
              grnLineItem.setQty(poLineItem.getQty());
            } else {
              grnLineItem.setQty(0L);
            }
            if (poLineItem.getCostPrice() != null) {
                grnLineItem.setCostPrice(poLineItem.getCostPrice());
            } else if (productVariant.getCostPrice() != null) {
                grnLineItem.setCostPrice(productVariant.getCostPrice());
            } else {
                grnLineItem.setCostPrice(0.0);
            }
            if (poLineItem.getMrp() != null) {
                grnLineItem.setMrp(poLineItem.getMrp());
            } else if (productVariant.getMarkedPrice() != null) {
                grnLineItem.setMrp(productVariant.getMarkedPrice());
            } else {
                grnLineItem.setMrp(0.0);
            }
            grnLineItem.setDiscountPercent(poLineItem.getDiscountPercent());
            getGoodsReceivedNoteDao().save(grnLineItem);
        }

        addRedirectAlertMessage(new SimpleMessage("GRN generated from PO. Please adjust it according to invoice."));
        return new RedirectResolution(GRNAction.class).addParameter("view").addParameter("grn", grn.getId());

    }

    // For the first GRN, update askedQty in ProductVariantSupplierInfo table
    private void editPVFillRate(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getGoodsReceivedNotes() != null && purchaseOrder.getGoodsReceivedNotes().size() == 1) {
            for (PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
                ProductVariantSupplierInfo productVariantSupplierInfo = productVariantSupplierInfoService.getOrCreatePVSupplierInfo(poLineItem.getSku().getProductVariant(),
                        purchaseOrder.getSupplier());
                productVariantSupplierInfoService.updatePVSupplierInfo(productVariantSupplierInfo, poLineItem.getQty(), null);
            }
        }
    }

    public Resolution delete() {
        logger.debug("purchaseOrder: " + purchaseOrder);
        purchaseOrder.setPurchaseOrderStatus(getPurchaseOrderDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Cancelled.getId()));
        purchaseOrder.setUpdateDate(new Date());
        getPurchaseOrderDao().save(purchaseOrder);
        addRedirectAlertMessage(new SimpleMessage("PO Deleted successfully."));
        return new RedirectResolution(POAction.class);
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

    public Resolution poInExcel() {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/PO-" + purchaseOrder.getId() + ".xls");
            xlsFile.getParentFile().mkdirs();
            xlsFile = getPurchaseOrderManager().generatePurchaseOrderXls(xlsFile.getPath(), purchaseOrder);
            addRedirectAlertMessage(new SimpleMessage("Purchase Order successfully generated"));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("PurchaseOrder generation failed"));
        }
        return new HTTPResponseResolution();

    }

    public Resolution poInPdf() {
        try {
            xlsFile = new File(adminDownloads + "/reports/PO-" + purchaseOrder.getId() + ".pdf");
            xlsFile.getParentFile().mkdirs();
            purchaseOrderDto = getPurchaseOrderManager().generatePurchaseOrderDto(purchaseOrder);
            getPurchaseOrderPDFGenerator().generatePurchaseOrderPdf(xlsFile.getPath(), purchaseOrderDto);
            addRedirectAlertMessage(new SimpleMessage("Purchase Order successfully generated"));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("PurchaseOrder generation failed"));
        }
        return new HTTPResponseResolution();
    }

  public Resolution uploadExcelAndCreatePOs() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      String excelFilePath = adminUploadsPath + "/poFiles/BrightPO" + sdf.format(new Date()) + ".xls";
      File excelFile = new File(excelFilePath);
      excelFile.getParentFile().mkdirs();
      fileBean.save(excelFile);
      Map<Warehouse, Set<ProductVariant>> whPVMap = xslParser.parseBrightPOFile(excelFile);
      if (!whPVMap.isEmpty()) {
        getPurchaseOrderService().createPOsForBright(whPVMap);
        addRedirectAlertMessage(new SimpleMessage("POs and their Line Items Created Successfully."));
      } else
        addRedirectAlertMessage(new SimpleMessage("POs and their Line Items Could not be Successfully."));
    } catch (Exception e) {
      logger.error("Exception while reading excel sheet.", e);
      addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
    }
    return new RedirectResolution(POAction.class);
  }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public PurchaseOrderDto getPurchaseOrderDto() {
        return purchaseOrderDto;
    }

    public void setPurchaseOrderDto(PurchaseOrderDto purchaseOrderDto) {
        this.purchaseOrderDto = purchaseOrderDto;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return purchaseOrderPage == null ? 0 : purchaseOrderPage.getTotalPages();
    }

    public int getResultCount() {
        return purchaseOrderPage == null ? 0 : purchaseOrderPage.getTotalResults();
    }

    public PurchaseOrderStatus getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("productVariant");
        params.add("tinNumber");
        params.add("supplierName");
        params.add("approvedBy");
        params.add("purchaseOrderStatus");
        params.add("createdBy");
        params.add("warehouse");
        params.add("extraInventoryCreated");
        params.add("poType");
        params.add("startDate");
        params.add("endDate");
        return params;
    }

    public PurchaseOrderDao getPurchaseOrderDao() {
        return purchaseOrderDao;
    }

    public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
        this.purchaseOrderDao = purchaseOrderDao;
    }

    public GoodsReceivedNoteDao getGoodsReceivedNoteDao() {
        return goodsReceivedNoteDao;
    }

    public void setGoodsReceivedNoteDao(GoodsReceivedNoteDao goodsReceivedNoteDao) {
        this.goodsReceivedNoteDao = goodsReceivedNoteDao;
    }

    public GrnLineItemDao getGrnLineItemDao() {
        return grnLineItemDao;
    }

    public void setGrnLineItemDao(GrnLineItemDao grnLineItemDao) {
        this.grnLineItemDao = grnLineItemDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public AdminEmailManager getAdminEmailManager() {
        return adminEmailManager;
    }

    public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
        this.adminEmailManager = adminEmailManager;
    }

    public PurchaseOrderManager getPurchaseOrderManager() {
        return purchaseOrderManager;
    }

    public void setPurchaseOrderManager(PurchaseOrderManager purchaseOrderManager) {
        this.purchaseOrderManager = purchaseOrderManager;
    }

    public PurchaseOrderPDFGenerator getPurchaseOrderPDFGenerator() {
        return purchaseOrderPDFGenerator;
    }

    public void setPurchaseOrderPDFGenerator(PurchaseOrderPDFGenerator purchaseOrderPDFGenerator) {
        this.purchaseOrderPDFGenerator = purchaseOrderPDFGenerator;
    }

    public PurchaseOrderService getPurchaseOrderService() {
        return purchaseOrderService;
    }

    public Boolean isExtraInventoryCreated() {
        return extraInventoryCreated;
    }

    public void setExtraInventoryCreated(Boolean extraInventoryCreated) {
        this.extraInventoryCreated = extraInventoryCreated;
    }

    public Boolean getExtraInventoryCreated() {
        return extraInventoryCreated;
    }

    public void setFileBean(FileBean fileBean) {
      this.fileBean = fileBean;
    }

		public Long getBrightSoId() {
			return brightSoId;
		}

		public void setBrightSoId(Long brightSoId) {
			this.brightSoId = brightSoId;
		}
		
		public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }
}