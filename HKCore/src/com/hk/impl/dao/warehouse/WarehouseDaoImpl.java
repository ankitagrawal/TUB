package com.hk.impl.dao.warehouse;

import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.warehouse.WarehouseDao;
import com.hk.pact.service.core.WarehouseService;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseDaoImpl extends BaseDaoImpl implements WarehouseDao {

  public List<Warehouse> getAllWarehouses() {
    return getAll(Warehouse.class);
  }

  public List<Warehouse> getAllWarehouses(Long warehouseType, Boolean honoringB2COrders, Boolean active) {
    DetachedCriteria criteria = DetachedCriteria.forClass(Warehouse.class);
    if (warehouseType != null)
      criteria.add(Restrictions.eq("warehouseType", warehouseType));
    if (honoringB2COrders != null)
      criteria.add(Restrictions.eq("honoringB2COrders", honoringB2COrders));
    if (active != null)
      criteria.add(Restrictions.eq("active", active));
    return (List<Warehouse>) findByCriteria(criteria);
  }

  public Warehouse getWarehouseById(Long warehouseId) {
    return get(Warehouse.class, warehouseId);
  }

  public List<Warehouse> findByIds(List<Long> warehouseIds) {
    List<Warehouse> warehouses = getSession().createQuery("from Warehouse w where w.id in (:warehouseIds)").setParameterList("warehouseIds", warehouseIds).list();
    return warehouses;

  }

  /**
   * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
   * two warehouseses.
   *
   * @param currentWarehouseForSO
   * @return
   */
  public List<Warehouse> getWarehoueForFlipping(Warehouse currentWarehouseForSO) {
    List<Long> warehouseIdList = new ArrayList<Long>();
    // Add all Bright WH for B2B+B2C
    warehouseIdList.add(WarehouseService.GGN_BRIGHT_WH_ID);
    warehouseIdList.add(WarehouseService.MUM_BRIGHT_WH_ID);
    // Remove the base WH
    warehouseIdList.remove(currentWarehouseForSO.getId());

    return (List<Warehouse>) getSession().createQuery("from Warehouse w where w.id  in ( :warehouseIdList)").setParameterList("warehouseIdList", warehouseIdList).list();

  }

  public Warehouse findByIdentifier(String identifier) {
    return (Warehouse) getSession().createQuery("from Warehouse wh where wh.identifier = :identifier").setString("identifier", identifier).uniqueResult();
  }

}
