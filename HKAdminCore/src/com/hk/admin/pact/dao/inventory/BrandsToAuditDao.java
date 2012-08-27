package com.hk.admin.pact.dao.inventory;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface BrandsToAuditDao extends BaseDao {

    public Page searchAuditList(String brand, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage);
    public List<String> brandsToBeAudited(Warehouse warehouse);
    public boolean isBrandAudited(String brand, Warehouse warehouse);

}