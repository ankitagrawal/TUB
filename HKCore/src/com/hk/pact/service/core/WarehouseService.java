package com.hk.pact.service.core;

import com.hk.domain.warehouse.Warehouse;

import java.util.List;
import java.util.Set;

/**
 * @author vaibhav.adlakha
 */
public interface WarehouseService {

    public static final Long DEFAULT_WAREHOUSE_ID = 1L;
    public static final Long MUMBAI_WAREHOUSE_ID  = 2L;
    public static final Long GURGAON_PHARMA_WAREHOUSE_ID  = 101L;
    public static final Long CORPORATE_OFFICE_ID  = 999L;

    public Warehouse getWarehouseById(Long warehouseId);
    
    public List<Warehouse> getAllWarehouses();

    /**
     * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
     * two warehouses.
     * 
     * @param currentWarehouseForSO
     * @return
     */
    public Warehouse getWarehoueForFlipping(Warehouse currentWarehouseForSO);

    public List<Warehouse> getServiceableWarehouses();

    public Warehouse getCorporateOffice();

    /**
     * Default warehouse will be gurgaon
     * 
     * @return
     */
    public Warehouse getDefaultWarehouse();

    
    public Warehouse getMumbaiWarehouse();

    public Warehouse getWarehouseToBeAssignedByDefinedLogicForSplitting(Set<Warehouse> warehouses);

    public Warehouse findByName(String name);
}
