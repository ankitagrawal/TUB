package com.hk.admin.pact.service.inventory;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;

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

}
