package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.RegionType;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.WarehouseService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 8/27/13
 * Time: 12:17 PM
 */
public class CreateUpdateHKReachPricingEngineAction extends BaseAction {

  @Autowired
  CourierPricingEngineDao courierPricingEngineDao;
  @Autowired
  BaseDao baseDao;

  @Autowired
  WarehouseService warehouseService;

  @Autowired

  private HKReachPricingEngine hkReachPricingEngine;

  private List<Warehouse> onlineWarehouses;

  private Courier courier;
  private RegionType regionType;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  public Resolution save() {
    courierPricingEngineDao.save(hkReachPricingEngine);
    addRedirectAlertMessage(new SimpleMessage("HK Reach courier info saved"));
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  private void prepareEngineData() {
    onlineWarehouses = warehouseService.getAllWarehouses();

  }
}
