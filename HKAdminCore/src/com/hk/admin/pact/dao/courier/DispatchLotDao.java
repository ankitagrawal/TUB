package com.hk.admin.pact.dao.courier;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.pact.dao.BaseDao;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/5/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DispatchLotDao extends BaseDao {

	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, int pageNo, int perPage);

}
