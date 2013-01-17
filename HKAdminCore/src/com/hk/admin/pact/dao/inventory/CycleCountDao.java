package com.hk.admin.pact.dao.inventory;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.SkuGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jan 14, 2013
 * Time: 3:54:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CycleCountDao {


	public CycleCountItem getCycleCountItem(CycleCount cycleCount ,SkuGroup skuGroup);


}
