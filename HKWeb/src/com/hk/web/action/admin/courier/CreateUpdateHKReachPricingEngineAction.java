package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
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

  private Warehouse warehouseParam;

  private Hub hubParam;

  private List<HKReachPricingEngine> hkReachEngines;

  @DefaultHandler
  public Resolution pre() {
    this.prepareEngineData();
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  public Resolution saveOrUpdate() {
    if (hkReachPricingEngine.getId() != null) {
      HKReachPricingEngine localReachEngine = courierPricingEngineDao.get(HKReachPricingEngine.class, hkReachPricingEngine.getId());
      if (localReachEngine != null) {
        localReachEngine.setHub(hkReachPricingEngine.getHub());
        localReachEngine.setWarehouse(hkReachPricingEngine.getWarehouse());
        localReachEngine.setFixedCost(hkReachPricingEngine.getFixedCost());
        localReachEngine.setInterCityCost(hkReachPricingEngine.getInterCityCost());
        localReachEngine.setUpdateTime(Calendar.getInstance().getTime());
        courierPricingEngineDao.save(localReachEngine);
      } else {
        addRedirectAlertMessage(new SimpleError("Invalid data supplied."));
        this.prepareEngineData();
        return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
      }
    } else {
      List<HKReachPricingEngine> localEngines = courierPricingEngineDao.getHkReachPricingEngineList(hkReachPricingEngine.getWarehouse(),
              hkReachPricingEngine.getHub(), Boolean.FALSE);
      if (localEngines != null && !localEngines.isEmpty()) {
        addRedirectAlertMessage(new SimpleError("Duplicate data supplied, entry already exists."));
        return new RedirectResolution(CreateUpdateHKReachPricingEngineAction.class, "search")
          .addParameter("warehouse",hkReachPricingEngine.getWarehouse()).addParameter("hub", hkReachPricingEngine.getHub());
      }
      hkReachPricingEngine.setUpdateTime(Calendar.getInstance().getTime());
      courierPricingEngineDao.save(hkReachPricingEngine);
    }
    addRedirectAlertMessage(new SimpleMessage("HK Reach courier info saved"));
    this.prepareEngineData();
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  public Resolution search() {
    hkReachEngines = courierPricingEngineDao.getHkReachPricingEngineList(warehouseParam, hubParam, true);
    if (hkReachEngines == null || hkReachEngines.isEmpty()) {
      addRedirectAlertMessage(new SimpleMessage("No results match your search."));
    }
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

  public Warehouse getWarehouseParam() {
    return warehouseParam;
  }

  public void setWarehouseParam(Warehouse warehouseParam) {
    this.warehouseParam = warehouseParam;
  }

  public Hub getHubParam() {
    return hubParam;
  }

  public void setHubParam(Hub hubParam) {
    this.hubParam = hubParam;
  }

  public List<HKReachPricingEngine> getHkReachEngines() {
    return hkReachEngines;
  }

  public void setHkReachEngines(List<HKReachPricingEngine> hkReachEngines) {
    this.hkReachEngines = hkReachEngines;
  }
}
