package com.hk.impl.service.core;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.Order;
import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.UserService;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.warehouse.EnumWarehouseType;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseDaoImpl warehouseDao;

  @Autowired
  private UserService userService;

    public Warehouse getWarehouseById(Long warehouseId) {
        return getWarehouseDao().get(Warehouse.class, warehouseId);
    }

    public List<Warehouse> getAllWarehouses() {
        return getWarehouseDao().getAllWarehouses();                                                            
    }

  public List<Warehouse> getAllActiveWarehouses(){
    return getWarehouseDao().getAllWarehouses(null, null, Boolean.TRUE);
  }

    /**
     * Currently since we only have two warehouses, so can assume it as a flip. This will change when we have more than
     * two warehouses.
     * 
     * @param currentWarehouseForSO
     * @return
     */
    public List<Warehouse> getWarehoueForFlipping(Warehouse currentWarehouseForSO) {
        return getWarehouseDao().getWarehoueForFlipping(currentWarehouseForSO);
    }

    // Default is B2C
    public List<Warehouse> getServiceableWarehouses() {
      User user = userService.getLoggedInUser();
      if (user != null && user.getRoleStrings() != null && user.getRoleStrings().contains(RoleConstants.B2B_USER.toString())) {
        return getWarehouseDao().getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), null, Boolean.TRUE);
      }
      return getWarehouseDao().getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), Boolean.TRUE, Boolean.TRUE);
    }

  public List<Warehouse> getServiceableWarehouses(Order order) {
      if (order != null && order.isB2bOrder() != null && order.isB2bOrder()) {
        return getWarehouseDao().getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), null, Boolean.TRUE);
      }
      return getWarehouseDao().getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), Boolean.TRUE, Boolean.TRUE);
    }

		public List<Warehouse> getWarehousesToMarkOOS() {
      //List<Warehouse> warehouseList = getWarehouseDao().getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), Boolean.TRUE, Boolean.TRUE);
      List<Warehouse> warehouseList = getServiceableWarehouses();
      warehouseList.add(getCorporateOffice()); // Adding Corportae on top of serviceable WHs
      return warehouseList;
		}

    public Warehouse getCorporateOffice() {
        return getWarehouseDao().getWarehouseById(GGN_CORPORATE_OFFICE_ID);
    }

    /**
     * Default warehouse will be gurgaon
     * 
     * @return
     */
    public Warehouse getDefaultWarehouse() {
        return getWarehouseDao().getWarehouseById(GGN_BRIGHT_WH_ID);
    }

    public Warehouse getMumbaiWarehouse() {
        return getWarehouseDao().getWarehouseById(MUM_BRIGHT_WH_ID);
    }

    // Since there are more than 2 wh that might fulfill so marking it Depricated.
    // Also, it is not being used anywhere
    @Deprecated
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
    
    public Warehouse findByIdentifier(String identifier){
        return getWarehouseDao().findByIdentifier(identifier);
    }

  @Override
  public Warehouse findShippingWarehouse(ShippingOrder shippingOrder) {
    if (shippingOrder.getBaseOrder().isB2bOrder() != null && shippingOrder.getBaseOrder().isB2bOrder()) {
      return shippingOrder.getWarehouse();
    } else {
      if (shippingOrder.getWarehouse().getId().equals(WarehouseService.MUM_BRIGHT_WH_ID)) {
        return getWarehouseById(WarehouseService.MUM_AQUA_WH_ID);
      } else if (shippingOrder.getWarehouse().getId().equals(WarehouseService.GGN_BRIGHT_WH_ID)) {
        return getWarehouseById(WarehouseService.GGN_AQUA_WH_ID);
      }
    }
    return shippingOrder.getWarehouse();
  }

  public WarehouseDaoImpl getWarehouseDao() {
        return warehouseDao;
    }

    public void setWarehouseDao(WarehouseDaoImpl warehouseDao) {
        this.warehouseDao = warehouseDao;
    }

}
