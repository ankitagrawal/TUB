package com.hk.pact.dao.pos;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.dto.pos.POSSalesDto;
import com.hk.dto.pos.POSReturnItemDto;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

public interface POSDao extends BaseDao {

  public List findSaleForTimeFrame(Long storeId, Date startDate, Date endDate, List<OrderStatus> orderStatusList);

  public List<ReverseOrder> findReturnItemForTimeFrame(Long storeId, Date startDate, Date endDate);
}
