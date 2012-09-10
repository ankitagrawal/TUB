package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.util.ReconciliationVoucherParser;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.web.action.error.AdminPermissionAction;
import com.restfb.util.StringUtils;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.RECON_VOUCHER_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class ReconciliationVoucherAction extends BasePaginatedAction {

    private static Logger logger = Logger.getLogger(ReconciliationVoucherAction.class);
    @Autowired
    ReconciliationVoucherDao reconciliationVoucherDao;

    @Autowired
    ProductVariantDao productVariantDao;
    @Autowired
    private ReconciliationVoucherParser rvParser;
    @Autowired
    ReconciliationVoucherService reconciliationVoucherService;

    @Autowired
    UserDao userDao;
    @Autowired
    SkuGroupDao skuGroupDao;
    @Autowired
    AdminSkuItemDao adminSkuItemDao;
    @Autowired
    AdminInventoryService adminInventoryService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    AdminProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    SkuService skuService;
    @Autowired
    private ProductVariantService productVariantService;

    private ReconciliationVoucher reconciliationVoucher;
    private List<ReconciliationVoucher> reconciliationVouchers = new ArrayList<ReconciliationVoucher>();
    private List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
    public String productVariantId;
    Page reconciliationVoucherPage;
    private Integer defaultPerPage = 30;
    private Date createDate;
    private String userLogin;
    private Warehouse warehouse;

    @Validate(required = true, on = "parse")
    private FileBean fileBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @DefaultHandler
    public Resolution pre() {
        if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
            warehouse = getPrincipalUser().getSelectedWarehouse();
        }
        reconciliationVoucherPage = reconciliationVoucherDao.searchReconciliationVoucher(createDate, userLogin, warehouse, getPageNo(), getPerPage());
        reconciliationVouchers = reconciliationVoucherPage.getList();
        return new ForwardResolution("/pages/admin/reconciliationVoucherList.jsp");
    }

    public Resolution view() {
        if (reconciliationVoucher != null) {
            logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
        }
        return new ForwardResolution("/pages/admin/reconciliationVoucher.jsp");
    }

    public Resolution save() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        reconciliationVoucherService.save(loggedOnUser, rvLineItems, reconciliationVoucher);

        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(ReconciliationVoucherAction.class);
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            rvParser.setReconciliationVoucher(reconciliationVoucher); //has warehouse and reconciliation date            
            rvLineItems = rvParser.readAndCreateRVLineItems(excelFilePath, "Sheet1");
            save();
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(ReconciliationVoucherAction.class);
    }

    public ReconciliationVoucher getReconciliationVoucher() {
        return reconciliationVoucher;
    }

    public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
        this.reconciliationVoucher = reconciliationVoucher;
    }

    public List<ReconciliationVoucher> getReconciliationVouchers() {
        return reconciliationVouchers;
    }

    public void setReconciliationVouchers(List<ReconciliationVoucher> reconciliationVouchers) {
        this.reconciliationVouchers = reconciliationVouchers;
    }

    public List<RvLineItem> getRvLineItems() {
        return rvLineItems;
    }

    public void setRvLineItems(List<RvLineItem> rvLineItems) {
        this.rvLineItems = rvLineItems;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return reconciliationVoucherPage == null ? 0 : reconciliationVoucherPage.getTotalPages();
    }

    public int getResultCount() {
        return reconciliationVoucherPage == null ? 0 : reconciliationVoucherPage.getTotalResults();
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("createDate");
        params.add("userLogin");
        params.add("warehouse");
        return params;
    }

}