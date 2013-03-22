package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.pact.dao.inventory.CycleCountDao;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

	public CycleCountItem getCycleCountItem(CycleCount cycleCount, SkuGroup skuGroup,SkuItem skuItem) {
		return cycleCountDao.getCycleCountItem(cycleCount, skuGroup,skuItem);
	}

	public Page searchCycleList(String auditBy, Long cycleCountStatus ,Warehouse warehouse, User auditor, Date startDate, Date endDate, int pageNo, int perPage) {
		List<BrandsToAudit> brandsToAuditList = null;
		Product product = null;
		ProductVariant productVariant = null;
		if (auditBy != null) {
			brandsToAuditList = brandsToAuditDao.getBrandsToAudit(auditBy, null,null);
			if (brandsToAuditList.isEmpty()) {
				product = productService.getProductById(auditBy);
				if (product == null) {
					productVariant = productVariantService.getVariantById(auditBy);
				}
			}
		}

		return cycleCountDao.searchCycleList(auditBy,cycleCountStatus, brandsToAuditList, product, productVariant, warehouse, auditor, startDate, endDate, pageNo, perPage);
	}

	public List<CycleCount> getCycleCountInProgress(List<BrandsToAudit> brandsToAuditList ,Product product , ProductVariant productVariant, Warehouse warehouse){
		return cycleCountDao. cycleCountInProgress(brandsToAuditList ,product , productVariant,  warehouse);
	}

    public CycleCountItem createCycleCountItem(SkuGroup validSkuGroup, SkuItem skuItem ,CycleCount cycleCount, Integer qty) {
        CycleCountItem cycleCountItem = new CycleCountItem();        
        cycleCountItem.setSkuGroup(validSkuGroup);
        cycleCountItem.setSkuItem(skuItem);
        cycleCountItem.setCycleCount(cycleCount);
        cycleCountItem.setScannedQty(qty);
        return cycleCountItem;
    }


     public List<SkuItem> getScannedSkuItems (Long skuGroupId , Long cycleCountId){
           return cycleCountDao.getScannedSkuItems(skuGroupId,cycleCountId);
     }

    public void removeScannedSkuItemFromCycleCountItem (CycleCount cycleCount, SkuItem skuItem) {
        cycleCountDao.removeScannedSkuItemFromCycleCountItem(cycleCount,skuItem);
    }

    @Transactional
    public void deleteAllCycleCountItemsOfProductVariant(CycleCount cycleCount, ProductVariant productVariant) {
        List<CycleCountItem> deleteCycleCountItemList = new ArrayList<CycleCountItem>();
        List<CycleCountItem> cycleCountItemList = cycleCount.getCycleCountItems();
        for (CycleCountItem cycleCountItem : cycleCountItemList) {
            if (cycleCountItem.getSkuGroup() != null && cycleCountItem.getSkuGroup().getSku().getProductVariant().getId().equals(productVariant.getId())) {
                deleteCycleCountItemList.add(cycleCountItem);
            } else if (cycleCountItem.getSkuItem() != null && (cycleCountItem.getSkuItem().getSkuGroup().getSku().getProductVariant().getId().equals(productVariant.getId()))) {
                deleteCycleCountItemList.add(cycleCountItem);
            }
        }

        for (CycleCountItem cycleCountItem : deleteCycleCountItemList) {
            cycleCountDao.deleteCycleCountItem(cycleCountItem);
        }

    }

}
