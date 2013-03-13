package com.hk.admin.pact.dao.inventory;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
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

    public CycleCountItem getCycleCountItem(CycleCount cycleCount, SkuGroup skuGroup, SkuItem skuItem);

    public List<CycleCount> cycleCountInProgress(List<BrandsToAudit> brandsToAuditList, Product product, ProductVariant productVariant, Warehouse warehouse);

    public Page searchCycleList(String auditBy, Long cycleCountStatus, List<BrandsToAudit> brandsToAuditList, Product product, ProductVariant productVariant, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage);

    public List<SkuItem> getScannedSkuItems(Long skuGroupId, Long cycleCountId);

    public void removeScannedSkuItemFromCycleCountItem(CycleCount cycleCount, SkuItem skuItem);


}
