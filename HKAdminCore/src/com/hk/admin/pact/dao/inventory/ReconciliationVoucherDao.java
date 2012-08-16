package com.hk.admin.pact.dao.inventory;

import java.util.Date;

import com.akube.framework.dao.Page;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface ReconciliationVoucherDao extends BaseDao {

    public Page searchReconciliationVoucher(Date createDate, String userLogin, Warehouse warehouse, int pageNo, int perPage) ;

    public RvLineItem getRvLineItem(ReconciliationVoucher reconciliationVoucher , Sku sku);
}