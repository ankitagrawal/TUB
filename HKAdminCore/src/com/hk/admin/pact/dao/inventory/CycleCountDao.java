package com.hk.admin.pact.dao.inventory;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.akube.framework.dao.Page;

import java.util.List;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 14, 2013
 * Time: 3:54:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CycleCountDao {


	public CycleCountItem getCycleCountItem(CycleCount cycleCount ,SkuGroup skuGroup);

	public Page searchCycleList(String brand, Long warehouseId, User auditor, Date startDate, Date endDate, int pageNo, int perPage);


}
