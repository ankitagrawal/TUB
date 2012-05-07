package com.hk.impl.dao.warehouse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.warehouse.WarehouseDao;
import com.hk.pact.service.core.WarehouseService;

@Repository
public class WarehouseDaoImpl extends BaseDaoImpl implements WarehouseDao {

    public List<Warehouse> getAllWarehouses() {
        return getAll(Warehouse.class);
    }

    public Warehouse getWarehouseById(Long warehouseId) {
        return get(Warehouse.class, warehouseId);
    }

    /**
     * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
     * two warehouseses.
     * 
     * @param currentWarehouseForSO
     * @return
     */
    public Warehouse getWarehoueForFlipping(Warehouse currentWarehouseForSO) {
        List<Long> warehouseIdList = new ArrayList<Long>();
        warehouseIdList.add(currentWarehouseForSO.getId());
        warehouseIdList.add(WarehouseService.CORPORATE_OFFICE_ID);
        return (Warehouse) getSession().createQuery("from Warehouse w where w.id  not in ( :warehouseIdList)").setParameterList("warehouseIdList", warehouseIdList).uniqueResult();

    }

    public Warehouse findByName(String name) {
        return (Warehouse) getSession().createQuery("from Warehouse wh where wh.name = :name").setString("name", name).uniqueResult();
    }

}
