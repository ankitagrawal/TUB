package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 13, 2013
 * Time: 2:14:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CycleCountServiceImpl implements CycleCountService {

	private BaseDao baseDao;

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
}
