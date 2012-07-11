package com.hk.admin.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

public interface BrandsToAuditDao extends BaseDao {

    public Page searchAuditList(String brand, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage);
    public List<String> brandsToBeAudited(Warehouse warehouse);

}