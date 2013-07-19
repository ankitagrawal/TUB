package com.hk.pact.dao.pos;

import com.hk.dto.pos.POSSalesDto;
import com.hk.dto.pos.POSReturnItemDto;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/18/13
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface POSDao extends BaseDao {

  public List<POSSalesDto> findSaleForTimeFrame(Long storeId, Date startDate, Date endDate);

  public List<POSReturnItemDto> findReturnItemForTimeFrame(Long storeId, Date startDate, Date endDate);
}
