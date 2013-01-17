package com.hk.admin.impl.dao.inventory;

import com.hk.admin.pact.dao.inventory.CycleCountDao;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jan 14, 2013
 * Time: 3:54:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CycleCountDaoImpl extends BaseDaoImpl implements CycleCountDao {

	public CycleCountItem getCycleCountItem(CycleCount cycleCount,SkuGroup skuGroup) {
		DetachedCriteria cycleCountItemCriteria = getCycleCountItemCriteria(cycleCount ,skuGroup);
		List<CycleCountItem> cycleCountItems = findByCriteria(cycleCountItemCriteria);
		return cycleCountItems != null && cycleCountItems.size() > 0 ? cycleCountItems.get(0) : null;

	}

	private DetachedCriteria getCycleCountItemCriteria(CycleCount cycleCount,SkuGroup skuGroup) {
		DetachedCriteria cycleCountItemCriteria = DetachedCriteria.forClass(CycleCountItem.class);

		if (skuGroup != null) {
			cycleCountItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
		}
		if(cycleCount != null){
			cycleCountItemCriteria.add(Restrictions.eq("cycleCount", cycleCount));
		}
		return cycleCountItemCriteria;
	}

}
