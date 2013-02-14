package com.hk.admin.pact.service.inventory;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.akube.framework.dao.Page;

import java.util.List;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 13, 2013
 * Time: 2:12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CycleCountService {

	//CycleCount methods

	public CycleCount save(CycleCount cycleCount);

    //CycleCountItem methods

	public CycleCountItem save(CycleCountItem cycleCountItem);

	public CycleCountItem getCycleCountItem(CycleCount cycleCount, SkuGroup skuGroup, SkuItem skuItem);

	public Page searchCycleList(String auditBy,Long cycleCountStatus, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage);

	public List<CycleCount> getCycleCountInProgress(List<BrandsToAudit> brandsToAuditList , Product product , ProductVariant productVariant, Warehouse warehouse);

    public CycleCountItem createCycleCountItem(SkuGroup validSkuGroup, SkuItem skuItem ,CycleCount cycleCount, Integer qty);

}
