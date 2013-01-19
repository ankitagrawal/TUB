package com.hk.admin.impl.dao.inventory;

import com.hk.admin.pact.dao.inventory.CycleCountDao;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.akube.framework.dao.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 14, 2013
 * Time: 3:54:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CycleCountDaoImpl extends BaseDaoImpl implements CycleCountDao {

	public CycleCountItem getCycleCountItem(CycleCount cycleCount, SkuGroup skuGroup) {
		DetachedCriteria cycleCountItemCriteria = getCycleCountItemCriteria(cycleCount, skuGroup);
		List<CycleCountItem> cycleCountItems = findByCriteria(cycleCountItemCriteria);
		return cycleCountItems != null && cycleCountItems.size() > 0 ? cycleCountItems.get(0) : null;

	}

	private DetachedCriteria getCycleCountItemCriteria(CycleCount cycleCount, SkuGroup skuGroup) {
		DetachedCriteria cycleCountItemCriteria = DetachedCriteria.forClass(CycleCountItem.class);

		if (skuGroup != null) {
			cycleCountItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
		}
		DetachedCriteria cycleCountDetachedCriteria = null;
		if (cycleCount != null) {
			cycleCountDetachedCriteria = cycleCountItemCriteria.add(Restrictions.eq("cycleCount", cycleCount));
		}
		if (cycleCountDetachedCriteria != null) {


		}

		return cycleCountItemCriteria;
	}


	private DetachedCriteria getCycleCountCriteria(String brand, Long warehouseId, User auditor, Date startDate, Date endDate) {
		DetachedCriteria cycleCountDetachedCriteria = DetachedCriteria.forClass(CycleCount.class);
		DetachedCriteria brandToAuditDetachedCriteria = cycleCountDetachedCriteria.createCriteria("brandsToAudit");

		if (warehouseId != null) {
			brandToAuditDetachedCriteria.add(Restrictions.eq("warehouse.id", warehouseId));
		}
		if (brandToAuditDetachedCriteria != null) {
			if (brand != null) {
				brandToAuditDetachedCriteria.add(Restrictions.eq("brand", brand));
			}

			if (auditor != null) {
				brandToAuditDetachedCriteria.add(Restrictions.eq("auditor", auditor));
			}
		}
		if (startDate != null && endDate != null) {
			cycleCountDetachedCriteria.add(Restrictions.between("createDate", startDate, endDate));
		}
		return cycleCountDetachedCriteria;
	}


	public Page searchCycleList(String brand, Long warehouseId, User auditor, Date startDate, Date endDate, int pageNo, int perPage) {

		DetachedCriteria cyclecounDetachedCriteria = getCycleCountCriteria(brand, warehouseId, auditor, startDate, endDate);
		cyclecounDetachedCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
		return list(cyclecounDetachedCriteria, pageNo, perPage);


	}

}
