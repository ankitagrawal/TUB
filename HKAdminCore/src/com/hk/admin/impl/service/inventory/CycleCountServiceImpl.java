package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.pact.dao.inventory.CycleCountDao;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

	public CycleCountItem getCycleCountItem(SkuGroup skuGroup){
	  return  cycleCountDao.getCycleCountItem(skuGroup);
	}

	public List<CycleCountItem> getAllCycleCountItem(){
		return getBaseDao().getAll(CycleCountItem.class);
	}
}
