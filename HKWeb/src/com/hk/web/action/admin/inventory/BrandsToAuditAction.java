//package com.hk.web.action.admin.inventory;
//
//import com.akube.framework.dao.Page;
//import com.akube.framework.stripes.action.BasePaginatedAction;
//import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
//import com.hk.constants.core.RoleConstants;
//import com.hk.constants.inventory.EnumAuditStatus;
//import com.hk.constants.inventory.EnumCycleCountStatus;
//import com.hk.domain.cycleCount.CycleCount;
//import com.hk.domain.inventory.BrandsToAudit;
//import com.hk.domain.user.User;
//import com.hk.domain.warehouse.Warehouse;
//import com.hk.pact.service.UserService;
//import com.hk.web.action.error.AdminPermissionAction;
//import com.hk.pact.service.catalog.ProductService;
//import net.sourceforge.stripes.action.*;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.stripesstuff.plugin.security.Secure;
//
//import java.util.*;
//
//@Secure(hasAnyRoles = {RoleConstants.WH_MANAGER}, authActionBean = AdminPermissionAction.class)
//public class BrandsToAuditAction extends BasePaginatedAction {
//
//    private static Logger logger = Logger.getLogger(BrandsToAuditAction.class);
//    private String brand;
//    private Long auditStatus;
//    private Warehouse warehouse;
//    private String auditorLogin;
//    private Date startDate;
//    private Date endDate;
//    private List<BrandsToAudit> brandsToAuditList = new ArrayList<BrandsToAudit>();
//    private Page brandsAuditPage;
//    private BrandsToAudit brandsToAudit;
//
//    @Autowired
//    private BrandsToAuditDao brandsToAuditDao;
//    @Autowired
//    ProductService productService;
//    @Autowired
//    UserService userService;
//
//    private Integer defaultPerPage = 20;
//
//    @DefaultHandler
//    @SuppressWarnings("unchecked")
//    public Resolution pre() {
//        User loggedOnUser = getPrincipalUser();
//        if (warehouse == null) warehouse = loggedOnUser.getSelectedWarehouse();
//        User auditor = null;
//
//        if (StringUtils.isNotBlank(auditorLogin)) {
//            auditor = getUserService().findByLogin(auditorLogin);
//        }
//        brandsAuditPage = getBrandsToAuditDao().searchAuditList(brand, warehouse, auditor, startDate, endDate, getPageNo(), getPerPage(), auditStatus);
//        if (brandsAuditPage != null) {
//            brandsToAuditList = brandsAuditPage.getList();
//        }
//        return new ForwardResolution("/pages/admin/brandAuditList.jsp");
//    }
//
//    public Resolution view() {
//        if (brandsToAudit == null) {
//            brandsToAudit = new BrandsToAudit();
//            brandsToAudit.setAuditStatus(0L);
//            brandsToAudit.setAuditDate(new Date());
//        }
//        return new ForwardResolution("/pages/admin/brandToAudit.jsp");
//    }
//
//    public Resolution save() {
//        if (getPrincipalUser() != null) {
//            Date auditDate = brandsToAudit.getAuditDate();
//            Date updateDate = brandsToAudit.getUpdateDate();
//            Date currentDate = new Date();
//            String brandName = brandsToAudit.getBrand();
//            Warehouse warehouse = userService.getWarehouseForLoggedInUser();
//            logger.debug("brand: " + brandName);
//
//            if (auditStatus == null){
//                auditStatus = EnumAuditStatus.Pending.getId();
//            }
//
//            /* check if brand name id valid */
//            boolean doesBrandExist = productService.doesBrandExist(brandName);
//            if (!doesBrandExist) {
//                addRedirectAlertMessage(new SimpleMessage("Invalid Brand Name"));
//                return new RedirectResolution(BrandsToAuditAction.class, "pre");
//            }
//
//            if (auditDate == null) {
//                brandsToAudit.setAuditDate(currentDate);
//                auditDate = currentDate;
//            }
//            if (updateDate == null) {
//                brandsToAudit.setUpdateDate(currentDate);
//            }
//            if (auditDate.compareTo(currentDate) > 0) {
//                addRedirectAlertMessage(new SimpleMessage("Invalid date"));
//                return new RedirectResolution(BrandsToAuditAction.class);
//            }
//
//            if (brandsToAudit.getId() == null) {
//                List<BrandsToAudit> brandsToAuditInDb = getBrandsToAuditDao().getBrandsToAudit(brandName, EnumAuditStatus.Pending.getId(), warehouse);
//                if (!brandsToAuditInDb.isEmpty()) {
//                    addRedirectAlertMessage(new SimpleMessage("Brand Already Exists"));
//                    return new RedirectResolution(BrandsToAuditAction.class);
//                }
//                brandsToAudit.setAuditStatus(EnumAuditStatus.Pending.getId());
//            } else {
//                if (!(brandsToAudit.getAuditStatus().equals(EnumAuditStatus.Pending.getId()))) {
//                    CycleCount cycleCount = brandsToAudit.getCycleCount();
//                    if (cycleCount != null) {
//                        if (!(cycleCount.getCycleStatus().equals(EnumCycleCountStatus.Closed.getId()))) {
//                            addRedirectAlertMessage(new SimpleMessage("Brand's Cycle Count Already In progress , First Close Cycle Count  " + cycleCount.getId()));
//                            return new RedirectResolution(BrandsToAuditAction.class);
//
//                        }
//
//                    }
//
//                }
//
//            }
//
//
//            User auditor = getPrincipalUser();
//            brandsToAudit.setAuditor(auditor);
//            brandsToAudit.setWarehouse(warehouse);
//
//
//            getBrandsToAuditDao().save(brandsToAudit);
//            addRedirectAlertMessage(new SimpleMessage("Changes made have been saved successfully"));
//        } else {
//            addRedirectAlertMessage(new SimpleMessage("Please Login First"));
//        }
//        return new RedirectResolution(BrandsToAuditAction.class);
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public Long getAuditStatus() {
//        return auditStatus;
//    }
//
//    public void setAuditStatus(Long auditStatus) {
//        this.auditStatus = auditStatus;
//    }
//
//    public Warehouse getWarehouse() {
//        return warehouse;
//    }
//
//    public void setWarehouse(Warehouse warehouse) {
//        this.warehouse = warehouse;
//    }
//
//    public String getAuditorLogin() {
//        return auditorLogin;
//    }
//
//    public void setAuditorLogin(String auditorLogin) {
//        this.auditorLogin = auditorLogin;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    public List<BrandsToAudit> getBrandsToAuditList() {
//        return brandsToAuditList;
//    }
//
//    public void setBrandsToAuditList(List<BrandsToAudit> brandsToAuditList) {
//        this.brandsToAuditList = brandsToAuditList;
//    }
//
//    public BrandsToAudit getBrandsToAudit() {
//        return brandsToAudit;
//    }
//
//    public void setBrandsToAudit(BrandsToAudit brandsToAudit) {
//        this.brandsToAudit = brandsToAudit;
//    }
//
//    public BrandsToAuditDao getBrandsToAuditDao() {
//        return brandsToAuditDao;
//    }
//
//    public void setBrandsToAuditDao(BrandsToAuditDao brandsToAuditDao) {
//        this.brandsToAuditDao = brandsToAuditDao;
//    }
//
//    public int getPerPageDefault() {
//        return defaultPerPage;
//    }
//
//    public int getPageCount() {
//        return brandsAuditPage == null ? 0 : brandsAuditPage.getTotalPages();
//    }
//
//    public int getResultCount() {
//        return brandsAuditPage == null ? 0 : brandsAuditPage.getTotalResults();
//    }
//
//    public Set<String> getParamSet() {
//        HashSet<String> params = new HashSet<String>();
//        params.add("brand");
//        params.add("warehouse");
//        params.add("auditorLogin");
//        params.add("startDate");
//        params.add("endDate");
//        return params;
//    }
//}