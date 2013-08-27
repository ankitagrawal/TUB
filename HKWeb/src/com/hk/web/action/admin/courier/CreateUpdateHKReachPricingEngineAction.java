package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.RegionType;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.WarehouseService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
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
  WarehouseService warehouseService;

  @Autowired
  HubService hubService;

  private HKReachPricingEngine hkReachPricingEngine;

  private List<Warehouse> onlineWarehouses;

  private  List<Hub>  hubs;

  @DefaultHandler
  public Resolution pre() {
    this.prepareEngineData();
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  public Resolution save() {
    hkReachPricingEngine.setUpdateTime(Calendar.getInstance().getTime());
    courierPricingEngineDao.save(hkReachPricingEngine);
    addRedirectAlertMessage(new SimpleMessage("HK Reach courier info saved"));
    this.prepareEngineData();
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  private void prepareEngineData() {
    onlineWarehouses = warehouseService.getServiceableWarehouses();
    hubs = hubService.getAllHubs();

  }

  public CourierPricingEngineDao getCourierPricingEngineDao() {
    return courierPricingEngineDao;
  }

  public void setCourierPricingEngineDao(CourierPricingEngineDao courierPricingEngineDao) {
    this.courierPricingEngineDao = courierPricingEngineDao;
  }

  public HKReachPricingEngine getHkReachPricingEngine() {
    return hkReachPricingEngine;
  }

  public void setHkReachPricingEngine(HKReachPricingEngine hkReachPricingEngine) {
    this.hkReachPricingEngine = hkReachPricingEngine;
  }

  public List<Warehouse> getOnlineWarehouses() {
    return onlineWarehouses;
  }

  public void setOnlineWarehouses(List<Warehouse> onlineWarehouses) {
    this.onlineWarehouses = onlineWarehouses;
  }

  public List<Hub> getHubs() {
    return hubs;
  }

  public void setHubs(List<Hub> hubs) {
    this.hubs = hubs;
  }
}
