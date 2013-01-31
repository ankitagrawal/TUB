package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.pact.dao.inventory.CycleCountDao;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.akube.framework.dao.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 13, 2013
 * Time: 2:14:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CycleCountServiceImpl implements CycleCountService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private CycleCountDao cycleCountDao;
	@Autowired
	ProductService productService;
	@Autowired
	BrandsToAuditDao brandsToAuditDao;
	@Autowired
	ProductVariantService productVariantService;


	public CycleCount save(CycleCount cycleCount) {
		return (CycleCount) getBaseDao().save(cycleCount);
	}

	public CycleCountItem save(CycleCountItem cycleCountItem) {
		return (CycleCountItem) getBaseDao().save(cycleCountItem);
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public CycleCountItem getCycleCountItem(CycleCount cycleCount, SkuGroup skuGroup) {
		return cycleCountDao.getCycleCountItem(cycleCount, skuGroup);
	}

	public Page searchCycleList(String auditBy, Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage) {
		List<BrandsToAudit> brandsToAuditList = null;
		Product product = null;
		ProductVariant productVariant = null;
		if (auditBy != null) {
			brandsToAuditList = brandsToAuditDao.getBrandsToAudit(auditBy, null);
			if (brandsToAuditList.isEmpty()) {
				product = productService.getProductById(auditBy);
				if (product == null) {
					productVariant = productVariantService.getVariantById(auditBy);
				}
			}
		}

		return cycleCountDao.searchCycleList(auditBy, brandsToAuditList, product, productVariant, warehouse, auditor, startDate, endDate, pageNo, perPage);
	}

	public List<CycleCount> cycleCountInProgress(List<BrandsToAudit> brandsToAuditList ,Product product , ProductVariant productVariant, Warehouse warehouse){
		return cycleCountDao. cycleCountInProgress(brandsToAuditList ,product , productVariant,  warehouse);
	}
}
