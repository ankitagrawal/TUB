package com.hk.web.action.admin.inventory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.PurchaseOrderManager;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.PurchaseOrderPDFGenerator;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.accounting.PoLineItem;
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

@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class POAction extends BasePaginatedAction {

    private static Logger        logger            = Logger.getLogger(POAction.class);
    @Autowired
    private PurchaseOrderDao     purchaseOrderDao;
    @Autowired
    private GoodsReceivedNoteDao goodsReceivedNoteDao;
    @Autowired
    private GrnLineItemDao       grnLineItemDao;
    @Autowired
    private UserService          userService;
    @Autowired
    private SkuService           skuService;
    @Autowired
    private AdminEmailManager    adminEmailManager;
    @Autowired
    private PurchaseOrderManager purchaseOrderManager;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                       adminDownloads;

    @Autowired
    XslGenerator xslGenerator;
    @Autowired
    PurchaseOrderPDFGenerator purchaseOrderPDFGenerator;

    private File                 xlsFile;
    Page                         purchaseOrderPage;
    private List<PurchaseOrder>  purchaseOrderList = new ArrayList<PurchaseOrder>();
    private PurchaseOrder        purchaseOrder;
    private PurchaseOrderDto     purchaseOrderDto;

    private Date                 startDate;
    private Date                 endDate;
    private String               tinNumber;
    private String               invoiceNumber;
    private String               supplierName;
    private ProductVariant       productVariant;
    private Warehouse            warehouse;
    private PurchaseOrderStatus  purchaseOrderStatus;
    private User                 approvedBy;
    private User                 createdBy;

    private Integer              defaultPerPage    = 20;

    @DefaultHandler
    public Resolution pre() {
        if (productVariant != null) {
            purchaseOrderList = getPurchaseOrderDao().listPurchaseOrdersWithProductVariant(productVariant);
        } else {
            if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
                warehouse = getPrincipalUser().getSelectedWarehouse();
            }
            purchaseOrderPage = getPurchaseOrderDao().searchPO(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber, tinNumber, supplierName, warehouse,
                    getPageNo(), getPerPage());
            purchaseOrderList = purchaseOrderPage.getList();
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
            purchaseOrderList = getPurchaseOrderDao().searchPO(purchaseOrder, purchaseOrderStatus, approvedBy, createdBy, invoiceNumber, tinNumber, supplierName, warehouse);
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
        grn.setInvoiceNumber("-");
        grn.setReceivedBy(loggedOnUser);
        grn.setGrnStatus(getGoodsReceivedNoteDao().get(GrnStatus.class, EnumGrnStatus.GoodsReceived.getId()));
        grn.setWarehouse(warehouse);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(grn.getGrnDate());
	    calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
	    grn.setEstPaymentDate(calendar.getTime());
	    grn = (GoodsReceivedNote) getGoodsReceivedNoteDao().save(grn);
        for (PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
            ProductVariant productVariant = poLineItem.getSku().getProductVariant();
            Sku sku = getSkuService().getSKU(productVariant, warehouse);

            GrnLineItem grnLineItem = new GrnLineItem();
            grnLineItem.setGoodsReceivedNote(grn);
            grnLineItem.setProductVariant(productVariant);
            grnLineItem.setSku(poLineItem.getSku());
            grnLineItem.setQty(-1L); // Negative so that while entring GRN they know what they have filled and what
            // they have not.
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

        getAdminEmailManager().sendGRNEmail(grn);

        addRedirectAlertMessage(new SimpleMessage("GRN generated from PO. Please adjust it according to invoice. Also email set to po creator."));
        return new RedirectResolution(GRNAction.class).addParameter("view").addParameter("grn", grn.getId());

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
            String logoImagePath = getContext().getServletContext().getRealPath("/") + "/images/logo/HealthKartLogo.png";
            purchaseOrderDto = getPurchaseOrderManager().generatePurchaseOrderDto(purchaseOrder);
            getPurchaseOrderPDFGenerator().generatePurchaseOrderPdf(xlsFile.getPath(), purchaseOrderDto, logoImagePath);
            addRedirectAlertMessage(new SimpleMessage("Purchase Order successfully generated"));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("PurchaseOrder generation failed"));
        }
        return new HTTPResponseResolution();
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
}