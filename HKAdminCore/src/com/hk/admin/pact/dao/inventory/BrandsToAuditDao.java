//package com.hk.admin.pact.dao.inventory;
//
//import com.akube.framework.dao.Page;
//import com.hk.domain.inventory.BrandsToAudit;
//import com.hk.domain.user.User;
//import com.hk.domain.warehouse.Warehouse;
//import com.hk.pact.dao.BaseDao;
//
//import java.util.Date;
//import java.util.List;
//
//public interface BrandsToAuditDao extends BaseDao {
//
//    public Page searchAuditList(String brand, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage, Long auditStatus);
//
//    public List<String> brandsToBeAudited(Warehouse warehouse);
//
//    public boolean isBrandAudited(String brand, Warehouse warehouse);
//
//    public boolean isBrandAudited(String brand);
//
//    public List<BrandsToAudit> getBrandsToAudit(String brand, Long auditStatus,Warehouse warehouse);
//
//    public BrandsToAudit save(BrandsToAudit brandsToAudit);
//}