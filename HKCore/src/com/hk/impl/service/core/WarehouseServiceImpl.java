package com.hk.impl.service.core;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.service.core.WarehouseService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseDaoImpl warehouseDao;

    public Warehouse getWarehouseById(Long warehouseId) {
        return getWarehouseDao().get(Warehouse.class, warehouseId);
    }

    public List<Warehouse> getAllWarehouses() {
        return getWarehouseDao().getAllWarehouses();
    }

    /**
     * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
     * two warehouses.
     * 
     * @param currentWarehouseForSO
     * @return
     */
    public Warehouse getWarehoueForFlipping(Warehouse currentWarehouseForSO) {
        return getWarehouseDao().getWarehoueForFlipping(currentWarehouseForSO);
    }

    public List<Warehouse> getServiceableWarehouses() {
        return getWarehouseDao().findByIds(Arrays.asList(DEFAULT_WAREHOUSE_ID, MUMBAI_WAREHOUSE_ID));
    }

		public List<Warehouse> getWarehousesToMarkOOS() {
			return getWarehouseDao().findByIds(Arrays.asList(DEFAULT_WAREHOUSE_ID, MUMBAI_WAREHOUSE_ID, CORPORATE_OFFICE_ID));
		}

    public Warehouse getCorporateOffice() {
        return getWarehouseDao().getWarehouseById(CORPORATE_OFFICE_ID);
    }

    /**
     * Default warehouse will be gurgaon
     * 
     * @return
     */
    public Warehouse getDefaultWarehouse() {
        return getWarehouseDao().getWarehouseById(DEFAULT_WAREHOUSE_ID);
    }

    public Warehouse getMumbaiWarehouse() {
        return getWarehouseDao().getWarehouseById(MUMBAI_WAREHOUSE_ID);
    }

    public Warehouse getWarehouseToBeAssignedByDefinedLogicForSplitting(Set<Warehouse> warehouses) {
        if (warehouses == null || warehouses.isEmpty()) {
            return getDefaultWarehouse();
        } else if (warehouses.size() == 1) {
            return warehouses.iterator().next();
        } else if (warehouses.size() == 2) {
            // TODO: avoid unnecessary db calls like this
            return getMumbaiWarehouse();
        }
        return null;
    }
    
    public Warehouse findByName(String name){
        return getWarehouseDao().findByName(name);
    }

    public WarehouseDaoImpl getWarehouseDao() {
        return warehouseDao;
    }

    public void setWarehouseDao(WarehouseDaoImpl warehouseDao) {
        this.warehouseDao = warehouseDao;
    }

}
