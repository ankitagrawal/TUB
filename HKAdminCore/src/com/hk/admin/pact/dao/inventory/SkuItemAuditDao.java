package com.hk.admin.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.Date;

public interface SkuItemAuditDao extends BaseDao {

  public Page search(String brand, String variantId, String skuItemBarcode, String auditedBy,
                     Date startDate, Date endDate, Warehouse warehouse, int pageNo, int perPage);


}