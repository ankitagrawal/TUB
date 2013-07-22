package com.hk.pact.dao.warehouse;

import java.util.List;

import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface WarehouseDao extends BaseDao {

  public List<Warehouse> getAllWarehouses();

  public List<Warehouse> getAllWarehouses(List<Long> warehouseTypeList, Boolean honoringB2COrders, Boolean active);

  public Warehouse getWarehouseById(Long warehouseId);

  /**
   * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
   * two warehouseses.
   *
   * @param currentWarehouseForSO
   * @return
   */
  public List<Warehouse> getWarehoueForFlipping(Warehouse currentWarehouseForSO);

  public Warehouse findByIdentifier(String identifier);

  public List<Warehouse> findWarehouses(String tinPrefix);

}
