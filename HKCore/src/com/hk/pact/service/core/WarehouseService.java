package com.hk.pact.service.core;

import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.Order;

import java.util.List;
import java.util.Set;

/**
 * @author vaibhav.adlakha
 */
public interface WarehouseService {

  public static final Long GGN_BRIGHT_WH_ID = 1L;
  public static final Long GGN_AQUA_WH_ID = 10L;
  public static final Long MUM_BRIGHT_WH_ID = 2L;
  public static final Long MUM_AQUA_WH_ID = 20L;
  public static final Long GGN_BRIGHT_PHARMA_WH_ID = 101L;
  public static final Long CHANDIGARH_STORE_WH_ID = 201L;
  public static final Long DEL_PUNJABI_BAGH_STORE_WH_ID = 301L;
  public static final Long DEL_KAPASHERA_BRIGHT_WH_ID = 401L;
  public static final Long DEL_KAPASHERA_AQUA_WH_ID = 402L;
  public static final Long GGN_CORPORATE_OFFICE_ID = 999L;

  public Warehouse getWarehouseById(Long warehouseId);

  public List<Warehouse> getAllWarehouses();

  public List<Warehouse> getAllActiveWarehouses();

  /**
   * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
   * two warehouses.
   *
   * @param currentWarehouseForSO
   * @return
   */
  public List<Warehouse> getWarehoueForFlipping(Warehouse currentWarehouseForSO);

  public List<Warehouse> getServiceableWarehouses();

  public List<Warehouse> getServiceableWarehouses(Order order);

  public List<Warehouse> getWarehousesToMarkOOS();

  public Warehouse getCorporateOffice();

  /**
   * Default warehouse will be gurgaon
   *
   * @return
   */
  public Warehouse getDefaultWarehouse();


  public Warehouse getMumbaiWarehouse();

  public Warehouse getWarehouseToBeAssignedByDefinedLogicForSplitting(Set<Warehouse> warehouses);

  public Warehouse findByIdentifier(String identifier);

  public Warehouse findShippingWarehouse(ShippingOrder shippingOrder);

  public List<Warehouse> findWarehouses(String tinPrefix);

  public List<Warehouse> findWarehousesByPrefix(String tinPrefix);

  public Warehouse getAquaDefaultWarehouse();

}
